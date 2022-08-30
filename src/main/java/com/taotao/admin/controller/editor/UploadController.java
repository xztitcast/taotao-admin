package com.taotao.admin.controller.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.admin.common.R;
import com.taotao.admin.service.CloudStorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 图片上传类
 * @author francis
 *
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	private CloudStorageService cloudStorageService;
	
	@PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R image(@RequestParam MultipartFile file, @RequestParam(required = false, defaultValue = "1") Integer name) {
		String imgUrl = "";
		try {
			imgUrl = cloudStorageService.uploadFile(file, name);
		} catch (Exception e) {
			log.error("图片上传失败", e);
			return R.error("图片上传失败！"); 
		}
		return R.ok().put("url", imgUrl);
	}
	
	@PostMapping(value = "/apk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R apk(@RequestParam MultipartFile file, @RequestParam(required = false) Integer name) {
		String originalFilename = file.getOriginalFilename();
		try {
			String url = cloudStorageService.uploadFile(file.getBytes(), "apk/" + originalFilename, name);
			return R.ok(url);
		} catch (Exception e) {
			log.error("apk包上传失败!", e);
			return R.error(e.getMessage());
		}
	}
	
	@PostMapping(value = "/remove", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public R remove(@RequestParam String url, @RequestParam(required = false) Integer name) {
		cloudStorageService.deleteFile(name, url);
		return R.ok();
	}
	
}
