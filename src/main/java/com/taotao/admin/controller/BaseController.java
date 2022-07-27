package com.taotao.admin.controller;

import org.apache.shiro.SecurityUtils;

import com.taotao.admin.entity.SysUser;

/**
 * 权限基础控制器
 * @author eden
 * @time 2022年7月22日 上午11:36:07
 */
public class BaseController {

	protected SysUser getUser() {
		return (SysUser) SecurityUtils.getSubject().getPrincipal();
	}
	
	protected Long getUserId() {
		return getUser().getUserId();
	}
}
