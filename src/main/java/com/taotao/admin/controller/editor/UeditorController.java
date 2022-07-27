package com.taotao.admin.controller.editor;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.taotao.admin.service.CloudStorageService;
import com.taotao.admin.ueditor.ActionEnter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ueditor")
public class UeditorController {
	
	@Autowired
	private CloudStorageService cloudStorageService;

	@RequestMapping(value = "/upload", method = {RequestMethod.POST, RequestMethod.GET})
	public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam("action") String action) {
		log.info("请求ueditor成功 action : {}", action);
		try(PrintWriter writer = response.getWriter()){
			switch(action) {
				case "config":
					request.setCharacterEncoding("utf-8");
					response.setContentType("application/json");
					String rootPath = request.getSession().getServletContext().getRealPath("/");
					String exec = new ActionEnter(request, rootPath).exec();
					writer.write(exec);
					break;
				case "uploadimage":
				case "uploadscrawl":
				case "catchimage":
				case "uploadvideo":
				case "uploadfile":
					MultipartHttpServletRequest mr = (MultipartHttpServletRequest)request;
					Map<String, MultipartFile> mvm = mr.getFileMap();
					mvm.values().forEach(f -> {
						JSONObject result = new JSONObject();
						String originalFilename = f.getOriginalFilename();
						String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
						try {
							String url = cloudStorageService.uploadFile(f);
							result.put("state", "SUCCESS");
							result.put("url", url);
							result.put("title", title);
							result.put("original", originalFilename);
						} catch (Exception e) {
							result.put("state", e.getMessage());
						}
						writer.write(result.toJSONString());
					});
					break;
				default :
					writer.write("ueditor action parameter is error");
			}
		}catch(Exception e) {
			log.debug("ueditor upload error", e);
		}
	}

}
