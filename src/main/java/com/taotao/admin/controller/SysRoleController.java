package com.taotao.admin.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.admin.annotation.Log;
import com.taotao.admin.common.Constant;
import com.taotao.admin.common.P;
import com.taotao.admin.common.R;
import com.taotao.admin.entity.SysRole;
import com.taotao.admin.service.SysRoleMenuService;
import com.taotao.admin.service.SysRoleService;

/**
 * 管理系统角色控制器
 * @author eden
 * @time 2022年7月22日 上午11:42:04
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysRoleMenuService sysRoleMenuServcie;

	/**
	 * 角色列表
	 * (当管理员为admin时默认获得所有角色)
	 * @param pageNum
	 * @param pageSize
	 * @param roleName
	 * @return
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, 
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
			@RequestParam(required = false) String roleName) {
		P<SysRole> p = sysRoleService.getSysRoleList(pageNum, pageSize, roleName, getUserId() == Constant.Sys.SUPER_ADMIN ? null :  getUserId());
		return R.ok(p);
	}
	
	/**
	 * 获取当前登陆用户创建的所有角色
	 * (当前登陆用户为Admin时获取所有角色)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select() {
		List<SysRole> result = null;
		if(getUserId() == Constant.Sys.SUPER_ADMIN) {
			result = sysRoleService.list();
		}else {
			result = sysRoleService.listByMap(Map.of("adder", getUserId()));
		}
		return R.ok(result);
	}
	
	/**
	 * 获取当个角色信息
	 * @param roleId
	 * @return
	 */
	@GetMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId) {
		SysRole sysRole = sysRoleService.getById(roleId);
		List<Long> menuIdList = sysRoleMenuServcie.getMenuIdList(roleId);
		sysRole.setMenuIdList(menuIdList);
		return R.ok(sysRole);
	}
	
	@Log("保存或修改角色")
	@PostMapping("/saveOrUpdate")
	@RequiresPermissions(value = {"sys:role:update", "sys:role:save"})
	public R saveOrUpdate(@RequestBody SysRole role){
		role.setAdder(getUserId());
		if(role.getRoleId() == null || role.getRoleId() == 0) {
			role.setCreated(new Date());
		}
		sysRoleService.saveOrUpdate(role);
		return R.ok();
	}
	
	@Log("删除角色")
	@PostMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		return R.ok();
	}
}
