package com.taotao.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.admin.entity.SysRoleMenu;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

	List<Long> selectMenuIdList(Long roleId);

	int deleteBatch(Long[] roleIds);

}