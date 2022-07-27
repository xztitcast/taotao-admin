package com.taotao.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.entity.SysUserRole;

public interface SysUserRoleService extends IService<SysUserRole> {

    void saveOrUpdate(Long userId, List<Long> roleIdList);
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> getRoleIdList(Long userId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}
