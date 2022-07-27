package com.taotao.admin.controller.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

import com.taotao.admin.common.P;
import com.taotao.admin.common.R;
import com.taotao.admin.entity.Storage;
import com.taotao.admin.service.StorageService;

/**
 * 后台管理oss控制器
 * @author eden
 * @time 2022年7月23日 下午4:23:47
 */
@RestController
@RequestMapping("/sys/oss")
public class SysOssController {

	@Autowired
	private StorageService storageService;
	
	/**
	 * 列表
	 * @param pageNum
	 * @param pageSize
	 * @param name 实际是传的是ID
	 * @return
	 */
	@GetMapping("/list")
	public R list(@RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
				@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
				@RequestParam(required = false) Integer name) {
		P<Storage> p = storageService.getStorageList(pageNum, pageSize, name);
		return R.ok(p);
	}
	
	/**
	 * 单个oss信息
	 * @param id
	 * @return
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		Storage storage = storageService.getById(id);
		return R.ok(storage);
	}
	
	/**
	 * 保存或者更新
	 * @param storage
	 * @return
	 */
	@PostMapping("/saveOrUpdate")
	public R saveOrUpdate(@RequestBody Storage storage) {
		if(storage.getId() == null || storage.getId() <= 0) {
			storage.setCreated(new Date());
		}
		storageService.saveOrUpdate(storage);
		return R.ok();
	}

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@PostMapping("/delete")
	public R delete(@RequestBody Integer ids) {
		storageService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}
	
	/**
	 * 获取所有的运营商
	 * @return
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:oss:select")
	public R select() {
		ArrayList<Map<String, Object>> list = storageService.list().stream().map(s -> {
			Map<String, Object> map = new HashMap<>();
			map.put("value", s.getId());
			map.put("label", s.getName());
			return map;
		}).collect(ArrayList::new, List::add, List::addAll);
		return R.ok(list);
	}
}
