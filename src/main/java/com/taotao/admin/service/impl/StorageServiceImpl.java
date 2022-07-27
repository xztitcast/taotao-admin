package com.taotao.admin.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.taotao.admin.common.Constant.Cloud;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.Storage;
import com.taotao.admin.mapper.CloudStorageMapper;
import com.taotao.admin.service.CloudStorageService;
import com.taotao.admin.service.StorageService;

@Service("storageService")
public class StorageServiceImpl extends ServiceImpl<CloudStorageMapper, Storage> implements StorageService, CloudStorageService, InitializingBean {
	
	private RestTemplate restTemplate;
	
	@Override
	public Storage getByName(String name) {
		LambdaQueryWrapper<Storage> query = Wrappers.lambdaQuery(Storage.class).eq(Storage::getName, name);
		return getOne(query);
	}
	
	@Override
	public P<Storage> getStorageList(int pageNum, int pageSize, Integer name) {
		IPage<Storage> page = new Page<>(pageNum, pageSize);
		QueryWrapper<Storage> query = new QueryWrapper<>();
		query.eq(name != null && name > 0 ,"id", name);
		page(page, query);
		return new P<>(page.getTotal(), page.getRecords());
	}
	
	@Override
	public String uploadFile(MultipartFile file) throws Exception {
		return uploadFile(file, Cloud.ALIYUN); //默认存储
	}
	
	@Override
	public String uploadFile(MultipartFile file, Integer name) throws Exception {
		String originalFilename = file.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);//扩展名
		String path = UUID.randomUUID().toString().replace("-", "")+ "."+ extName;
		return uploadFile(file.getBytes(), path, name);
	}

	@Override
	public String uploadFile(byte[] fileContent, String path, Integer name) throws Exception {
		Storage storage = getById(name); //不对存储为空判断,没有配置直接空指针
		int index = path.indexOf('/');
		if(index == -1) {
			storage.setPrefix(storage.getPrefix() + path);
		}else {
			storage.setPrefix(path);
		}
		return CloudServiceProvider.upload(fileContent, storage);
		
	}

	@Override
	public String uploadFile(String remoteURL, Integer name) throws Exception {
		String extName = remoteURL.substring(remoteURL.lastIndexOf(".")+1);
		byte[] bs = restTemplate.getForObject(remoteURL, byte[].class);
		String path = UUID.randomUUID().toString().replace("-", "")+ "."+ extName;
		return uploadFile(bs, path, name);
	}

	@Override
	public void deleteFile(Integer name, String... urls) {
		CloudServiceProvider.delete(Arrays.asList(urls), getById(name));//不对存储为空判断,没有配置直接空指针
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(5000)).setReadTimeout(Duration.ofMillis(5000)).build();
	}

	private static abstract class CloudServiceProvider {
		
		private CloudServiceProvider () {}
		
		public static String upload(byte[] fileContent, Storage s) throws Exception {
			switch(s.getId()) {
			case Cloud.ALIYUN:
				OSSClient ossClient = new OSSClient(s.getPoint(), s.getAccessKey(), s.getSecretKey());
				ossClient.putObject(s.getBucket(), s.getPrefix(), new ByteArrayInputStream(fileContent));
				ossClient.shutdown();
				break;
			case Cloud.QCLOUD:
				COSClient client = new COSClient(new BasicCOSCredentials(s.getAccessKey(), s.getSecretKey()), new ClientConfig(new Region(s.getPoint())));
				InputStream input = new ByteArrayInputStream(fileContent);
				ObjectMetadata metadata = new ObjectMetadata();
	            metadata.setContentLength(input.available());
	            PutObjectRequest request = new PutObjectRequest(s.getBucket(), s.getPrefix(), input, metadata);
	            client.putObject(request);
	            client.shutdown();
				break;
			case Cloud.QINIU:
				UploadManager uploadManager = new UploadManager(new Configuration(com.qiniu.storage.Region.autoRegion()));
				String token = Auth.create(s.getAccessKey(), s.getSecretKey()).uploadToken(s.getBucket());
				uploadManager.put(fileContent, token, token);
				break;
			case Cloud.HUAWEI:
				break;
			case Cloud.BAIDU:
				break;
			default:
				throw new RuntimeException("没有对应的云存储提供商");
			}
			return s.getDomain() + s.getPrefix();
		}
		
		public static void delete(List<String> urlList, Storage s) {
			switch(s.getId()) {
			case Cloud.ALIYUN:
				final OSSClient ossClient = new OSSClient(s.getPoint(), s.getAccessKey(), s.getSecretKey());
				urlList.stream().filter((@NotNull var  url) -> !url.isBlank()).forEach(url -> ossClient.deleteObject(s.getBucket(), url.replace(s.getDomain(), "")));
				ossClient.shutdown();
				break;
			case Cloud.QCLOUD:
				COSClient client = new COSClient(new BasicCOSCredentials(s.getAccessKey(), s.getSecretKey()), new ClientConfig(new Region(s.getPoint())));
				urlList.stream().filter((@NotNull var  url) -> !url.isBlank()).forEach(url -> client.deleteObject(s.getBucket(), url.replace(s.getDomain(), "")));
				break;
			case Cloud.QINIU:
				BucketManager bucketMapper = new BucketManager( Auth.create(s.getAccessKey(), s.getSecretKey()), new Configuration(com.qiniu.storage.Region.autoRegion()));
				urlList.stream().filter((@NotNull var  url) -> !url.isBlank()).forEach(url -> {
					try {
						bucketMapper.delete(s.getBucket(), url.replace(s.getDomain(), ""));
					} catch (QiniuException e) {
					}
				});
				break;
			case Cloud.HUAWEI:
				break;
			case Cloud.BAIDU:
				break;
			default:
				throw new RuntimeException("没有对应的云存储提供商");
			}
		}
	}

}
