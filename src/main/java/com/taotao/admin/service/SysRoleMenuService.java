package com.taotao.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.entity.SysRoleMenu;

public interface SysRoleMenuService extends IService<SysRoleMenu> {

	void saveOrUpdate(Long roleId, List<Long> menuIdList);
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Long> getMenuIdList(Long roleId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long... roleIds);
}
