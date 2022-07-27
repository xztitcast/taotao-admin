package com.taotao.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysRole;

public interface SysRoleService extends IService<SysRole> {

	void deleteBatch(Long... roleIds);
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> getRoleIdList(Long adder);
	
	P<SysRole> getSysRoleList(int pageNum, int pageSize, String roleName, Long adder);
}
