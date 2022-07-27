package com.taotao.admin.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.common.Constant;
import com.taotao.admin.entity.SysMenu;
import com.taotao.admin.mapper.SysMenuMapper;
import com.taotao.admin.service.SysMenuService;
import com.taotao.admin.service.SysRoleMenuService;
import com.taotao.admin.service.SysUserService;

@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@Override
	public List<SysMenu> getListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenu> menuList = getListParentId(parentId);
		if(menuIdList == null) {
			return menuList;
		}
		return menuList.stream().filter(m -> menuIdList.contains(m.getMenuId())).collect(Collectors.toList());
		
	}

	@Override
	public List<SysMenu> getListParentId(Long parentId) {
		return baseMapper.selectListParentId(parentId);
	}

	@Override
	public List<SysMenu> getNotButtonList() {
		return baseMapper.selectNotButtonList();
	}

	@Override
	public List<SysMenu> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.Sys.SUPER_ADMIN){
			return getAllMenuList(null);
		}
		
		//用户菜单列表
		List<Long> menuIdList = sysUserService.getAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	@Transactional
	public void delete(Long menuId) {
		this.removeById(menuId);
		sysRoleMenuService.removeByMap(Map.of("munu_id", menuId));
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenu> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenu> menuList = getListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Long> menuIdList){
		return menuList.stream().map(m -> {
			if(m.getType() == Constant.MenuType.CATALOG.getValue()) {
				m.setList(getMenuTreeList(getListParentId(m.getMenuId(), menuIdList), menuIdList));
			}
			return m;
		}).collect(Collectors.toList());
		
	}
}
