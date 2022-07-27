package com.taotao.admin.controller.app;

import java.util.Arrays;
import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.admin.common.P;
import com.taotao.admin.common.R;
import com.taotao.admin.entity.Version;
import com.taotao.admin.service.VersionService;

/**
 * app版本控制器
 * @author eden
 * @time 2022年7月22日 上午11:35:27
 */
@RestController
@RequestMapping("/sys/version")
public class SysVersionController {
	
	@Autowired
	private VersionService versionService;

	@GetMapping("/list")
	@RequiresPermissions("sys:version:list")
	public R list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, 
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
		P<Version> p = versionService.getVersionList(pageNum, pageSize);
		return R.ok(p);
	}
	
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:version:info")
	public R info(@PathVariable Integer id) {
		Version version = versionService.getById(id);
		return R.ok(version);
	}
	
	@PostMapping("/saveOrUpdate")
	@RequiresPermissions({"sys:version:save","sys:version:update"})
	public R saveOrUpdate(@RequestBody Version version) {
		if(version.getId() == null) {
			version.setCreated(new Date());
		}
		versionService.saveOrUpdate(version);
		return R.ok();
	}
	
	@PostMapping("/delete")
	@RequiresPermissions("sys:version:delete")
	public R delete(@RequestBody Integer[] ids) {
		versionService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}
}
