package com.taotao.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.entity.SysMenu;

public interface SysMenuService extends IService<SysMenu> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenu> getListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenu> getListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenu> getNotButtonList();
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenu> getUserMenuList(Long userId);

	/**
	 * 删除
	 */
	void delete(Long menuId) ;
}
