package com.taotao.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.admin.entity.SysRole;

public interface SysRoleMapper extends BaseMapper<SysRole> {

	List<Long> selectRoleIdList(Long adder);
}