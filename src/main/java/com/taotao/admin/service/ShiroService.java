package com.taotao.admin.service;

import java.util.Set;

import com.taotao.admin.entity.SysUser;

public interface ShiroService {

	/**
	 * 生成token
	 * @return
	 */
	String createToken(SysUser adminUser);
	
	/**
	 * 获取用户信息
	 * @param token
	 * @return
	 */
	SysUser getByToken(String token);
	
	/**
	 * 删除
	 * @param token
	 * @return
	 */
	boolean remove(String token);
	
	Set<String> getUserPermissions(long userId);
	
}
