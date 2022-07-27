package com.taotao.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.common.Constant;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysUser;
import com.taotao.admin.mapper.SysUserMapper;
import com.taotao.admin.service.SysUserRoleService;
import com.taotao.admin.service.SysUserService;

@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
	
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Override
	public P<SysUser> getSysUserList(int pageNum, int pageSize, String username, Long userId) {
		IPage<SysUser> page = new Page<>(pageNum, pageSize);
		QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
		wrapper.eq(username != null && !username.isBlank(), "`username`", username)
			   .eq(Constant.Sys.SUPER_ADMIN != userId ,"adder", userId);
		page(page,wrapper);
		return new P<>(page.getTotal(), page.getRecords());
	}

	@Override
	public List<String> getAllPerms(Long userId) {
		return baseMapper.selectAllPerms(userId);
	}

	@Override
	public List<Long> getAllMenuId(Long userId) {
		return baseMapper.selectAllMenuId(userId);
	}

	@Override
	public SysUser getByUserName(String username) {
		return baseMapper.selectByUserName(username);
	}

	@Override
	@Transactional
	public boolean saveOrUpdate(SysUser entity) {
		boolean saveOrUpdate = super.saveOrUpdate(entity);
		if(saveOrUpdate) {
			//检查角色权限
			checkRole(entity);
			
			sysUserRoleService.saveOrUpdate(entity.getUserId(), entity.getRoleIdList());
		}
		
		return saveOrUpdate;
	}

	@Override
	@Transactional
	public void deleteBatch(Long... userIds) {
		baseMapper.deleteByIds(userIds);
	}
	
	private void checkRole(SysUser user) {
		if (CollectionUtils.isEmpty(user.getRoleIdList())) {
			return ;
		}
		
		//if you not a super admin, you need to check if the user's role is self-created
		if(user.getAdder() == Constant.Sys.SUPER_ADMIN) {
			return;
		}
		//get the list of roles created by the user
		List<Long> idList = sysUserRoleService.getRoleIdList(user.getAdder());
		
		//check permission
		if(!idList.containsAll(user.getRoleIdList())) {
			throw new RuntimeException("新增用户所选角色，不是本人创建");
		}
		
	}

}
