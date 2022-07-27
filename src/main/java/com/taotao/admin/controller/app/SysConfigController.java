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

import com.taotao.admin.annotation.Log;
import com.taotao.admin.common.P;
import com.taotao.admin.common.R;
import com.taotao.admin.entity.SysConfig;
import com.taotao.admin.service.ConfigService;

/**
 * 字典配置控制器
 * @author eden
 * @time 2022年7月22日 上午11:33:57
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

	@Autowired
	private ConfigService configService;
	
	/**
	 * 配置列表
	 * @param pageSize
	 * @param pageNum
	 * @param paramKey
	 * @return
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:config:list")
	public R list(@RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
				@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, 
		        @RequestParam(required = false) String paramKey){
		P<SysConfig> p = configService.getConfigList(pageNum, pageSize, paramKey);
		return R.ok(p);
	}
	
	
	/**
	 * 单配置信息
	 * @param id
	 * @return
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:config:info")
	public R info(@PathVariable("id") Long id){
		SysConfig config = configService.getById(id);
		
		return R.ok(config);
	}
	
	/**
	 * 保存配置
	 */
	@Log("保存配置")
	@PostMapping("/save")
	@RequiresPermissions("sys:config:save")
	public R save(@RequestBody SysConfig config){
		config.setCreated(new Date());
		configService.save(config);
		
		return R.ok();
	}
	
	/**
	 * 修改配置
	 */
	@Log("修改配置")
	@PostMapping("/update")
	@RequiresPermissions("sys:config:update")
	public R update(@RequestBody SysConfig config){
		
		configService.updateById(config);
		
		return R.ok();
	}
	
	/**
	 * 删除配置
	 */
	@Log("删除配置")
	@PostMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public R delete(@RequestBody Long[] ids){
		configService.removeByIds(Arrays.asList(ids));
		
		return R.ok();
	}
}
