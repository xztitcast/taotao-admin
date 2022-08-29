package com.taotao.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.common.Constant;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysRole;
import com.taotao.admin.mapper.SysRoleMapper;
import com.taotao.admin.service.SysRoleMenuService;
import com.taotao.admin.service.SysRoleService;
import com.taotao.admin.service.SysUserRoleService;
import com.taotao.admin.service.SysUserService;

@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Override
	@Transactional
	public boolean saveOrUpdate(SysRole entity) {
		boolean saveOrUpdate = super.saveOrUpdate(entity);
		if(saveOrUpdate) {
			//检查权限
			checkPrems(entity);
			sysRoleMenuService.saveOrUpdate(entity.getRoleId(), entity.getMenuIdList());
		}
		return saveOrUpdate;
	}

	@Override
	@Transactional
	public void deleteBatch(Long... roleIds) {
		this.removeByIds(Arrays.asList(roleIds));
		sysRoleMenuService.deleteBatch(roleIds);
		sysUserRoleService.deleteBatch(roleIds);
	}

	@Override
	public List<Long> getRoleIdList(Long adder) {
		List<SysRole> list = this.listByMap(Map.of("adder", adder));
		return list.stream().map(SysRole::getRoleId).collect(Collectors.toList());
	}

	@Override
	public P<SysRole> getSysRoleList(int pageNum, int pageSize, String roleName, Long adder) {
		IPage<SysRole> page = new Page<>(pageNum, pageSize);
		QueryWrapper<SysRole> warpper = new QueryWrapper<>();
		warpper.eq(roleName != null && !roleName.isBlank() , "role_name", roleName);
		warpper.eq(adder != null, "adder", adder);
		page(page, warpper);
		return new P<>(page.getTotal(), page.getRecords());
	}
	
	private void checkPrems(SysRole role) {
		if(role.getAdder() == Constant.Sys.SUPER_ADMIN) {
			return;
		}
		List<Long> menuIdList = sysUserService.getAllMenuId(role.getAdder());
		if(!menuIdList.containsAll(role.getMenuIdList())) {
			throw new RuntimeException("新增角色的权限，已超出你的权限范围");
		}
	}
}
