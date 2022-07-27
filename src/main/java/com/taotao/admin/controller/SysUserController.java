package com.taotao.admin.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.admin.annotation.Log;
import com.taotao.admin.common.P;
import com.taotao.admin.common.R;
import com.taotao.admin.entity.SysUser;
import com.taotao.admin.entity.form.UpPwdForm;
import com.taotao.admin.service.SysUserRoleService;
import com.taotao.admin.service.SysUserService;

/**
 * 管理系统用户控制器
 * @author eden
 * @time 2022年7月22日 下午12:16:09
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	/**
	 * 获取当前登陆用户创建的所有用户列表
	 * @param pageNum
	 * @param pageSize
	 * @param username
	 * @return
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, 
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
			@RequestParam(required = false) String username) {
		P<SysUser> list = sysUserService.getSysUserList(pageNum, pageSize, username, getUserId());
		return R.ok(list);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}
	
	/**
	 * 获取单个用户信息
	 * @param userId
	 * @return
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId) {
		SysUser user = sysUserService.getById(userId);
		if(user == null) {
			return R.error("用户不存在");
		}
		List<Long> roleIdList = sysUserRoleService.getRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		return R.ok(user);
	}
	
	@Log("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUser user)throws Exception {
		user.setCreated(new Date());
		user.setAdder(getUserId());
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		sysUserService.saveOrUpdate(user);
		return R.ok();
	}
	
	@PostMapping("/password")
	public R password(@Valid @RequestBody UpPwdForm from) throws Exception{
		SysUser sysUser = sysUserService.getById(getUserId());
		if(sysUser == null) {
			return R.error("用户不存在");
		}
		String oldPwd = new Sha256Hash(from.getPassword(), sysUser.getSalt()).toHex();
		if(!sysUser.getPassword().equals(oldPwd)) {
			return R.error("原密码错误");
		}
		String salt = RandomStringUtils.randomAlphanumeric(20);
		sysUser.setPassword(new Sha256Hash(from.getNewPassword(), salt).toHex());
		sysUser.setSalt(salt);
		sysUserService.updateById(sysUser);
		return R.ok();
	}
	
	@Log("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUser user) throws Exception{
		String password = user.getPassword();
		if(password != null && !password.isBlank()) {
			String salt = RandomStringUtils.randomAlphanumeric(20);
			user.setPassword(new Sha256Hash(password, salt).toHex());
			user.setSalt(salt);
		}
		user.setAdder(getUserId());
		sysUserService.saveOrUpdate(user);
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@Log("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
	
}
