package com.taotao.admin.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 云存储接口
 * @author eden
 * @time 2022年7月22日 下午3:38:34
 */
public interface CloudStorageService {
	
	/**
	 * 上传
	 * @param file
	 * @return
	 * @throws Exception
	 */
	String uploadFile(MultipartFile file) throws Exception;
	
	/**
	 * 上传
	 * @param file
	 * @param name 具体的存储商
	 * @return
	 * @throws Exception
	 */
	String uploadFile(MultipartFile file, Integer name) throws Exception;
	
	/**
	 * 上传(指定前缀路径)
	 * @param fileContent
	 * @param path
	 * @param name
	 * @return
	 * @throws Exception
	 */
	String uploadFile(byte[] fileContent, String path, Integer name)throws Exception;
	
	/**
	 * 远程抓取图片
	 * @param remoteURL
	 * @param name 具体的存储商
	 * @return
	 * @throws Exception
	 */
	String uploadFile(String remoteURL, Integer name)throws Exception;
	
	/**
	 * 删除图片
	 * @param urls
	 */
	void deleteFile(Integer name, String... urls);
}
