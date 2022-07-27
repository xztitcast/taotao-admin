package com.taotao.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.admin.entity.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser> {

	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> selectAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> selectAllMenuId(Long userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUser selectByUserName(String username);

	void deleteByIds(Long[] userIds);
}
