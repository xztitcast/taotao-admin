package com.taotao.admin.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.entity.SysUserRole;
import com.taotao.admin.mapper.SysUserRoleMapper;
import com.taotao.admin.service.SysUserRoleService;

@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
	
	@Override
	@Transactional
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		this.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
		
		if(CollectionUtils.isEmpty(roleIdList)) return;
		
		roleIdList.forEach(id -> {
			SysUserRole sysUserRole = new SysUserRole();
			sysUserRole.setRoleId(id);
			sysUserRole.setUserId(userId);
			this.save(sysUserRole);
		});
	}

	@Override
	public List<Long> getRoleIdList(Long userId) {
		return baseMapper.selectRoleIdList(userId);
	}

	@Override
	public int deleteBatch(Long[] roleIds) {
		return baseMapper.deleteBatch(roleIds);
	}
}
