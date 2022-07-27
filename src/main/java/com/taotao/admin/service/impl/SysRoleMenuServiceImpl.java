package com.taotao.admin.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.entity.SysRoleMenu;
import com.taotao.admin.mapper.SysRoleMenuMapper;
import com.taotao.admin.service.SysRoleMenuService;

@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
	
	@Override
	@Transactional
	public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
		deleteBatch(roleId);
		
		if(CollectionUtils.isEmpty(menuIdList)) return;
		
		menuIdList.forEach(menuId -> {
			SysRoleMenu sysRoleMenu = new SysRoleMenu();
			sysRoleMenu.setMenuId(menuId);
			sysRoleMenu.setRoleId(roleId);
			save(sysRoleMenu);
		});
	}

	@Override
	public List<Long> getMenuIdList(Long roleId) {
		return baseMapper.selectMenuIdList(roleId);
	}

	@Override
	@Transactional
	public int deleteBatch(Long... roleIds) {
		return baseMapper.deleteBatch(roleIds);
	}

}
