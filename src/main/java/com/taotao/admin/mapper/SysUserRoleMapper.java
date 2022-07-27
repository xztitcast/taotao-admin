package com.taotao.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.admin.entity.SysUserRole;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

	List<Long> selectRoleIdList(Long userId);

	int deleteBatch(Long[] roleIds);
}