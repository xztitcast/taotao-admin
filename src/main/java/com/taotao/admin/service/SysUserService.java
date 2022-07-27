package com.taotao.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
	
	/**
	 * 查询用户列表
	 * @param pageNum
	 * @param pageSize
	 * @param username
	 * @param userId
	 * @return
	 */
	P<SysUser> getSysUserList(int pageNum, int pageSize, String username, Long userId);

	/**
	 * 查询所有用户权限
	 * @param userId
	 * @return
	 */
	List<String> getAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> getAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUser getByUserName(String username);

	/**
	 * 删除用户
	 */
	void deleteBatch(Long... userIds);

}
