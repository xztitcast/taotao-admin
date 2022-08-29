package com.taotao.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
		List<SysRoleMenu> list = this.listByMap(Map.of("role_id", roleId));
		return list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public int deleteBatch(Long... roleIds) {
		LambdaQueryWrapper<SysRoleMenu> wrapper = Wrappers.lambdaQuery(SysRoleMenu.class).in(SysRoleMenu::getRoleId, Arrays.asList(roleIds));
		return this.baseMapper.delete(wrapper);
	}

}
