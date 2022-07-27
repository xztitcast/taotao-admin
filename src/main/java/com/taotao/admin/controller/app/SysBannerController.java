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
import com.taotao.admin.entity.Banner;
import com.taotao.admin.service.BannerService;

/**
 * banner轮播图控制器
 * @author eden
 * @time 2022年7月22日 上午11:35:12
 */
@RestController
@RequestMapping("/sys/banner")
public class SysBannerController {
	
	@Autowired
	private BannerService bannerService;

	/**
	 * 获取banner轮播列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:banner:list")
	public R list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, 
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
		P<Banner> p = bannerService.getBannerlList(pageNum, pageSize);
		return R.ok(p);
	}
	
	/**
	 * 获取单个banner信息
	 * @param id
	 * @return
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:banner:info")
	public R info(@PathVariable Integer id) {
		Banner banner = bannerService.getById(id);
		return R.ok(banner);
	}
	
	/**
	 * 保存更新
	 * @param banner
	 * @return
	 */
	@PostMapping("/saveOrUpdate")
	@RequiresPermissions({"sys:banner:save", "sys:banner:update"})
	public R saveOrUpdate(@RequestBody Banner banner) {
		if(banner.getId() == null || banner.getId() <= 0) {
			banner.setCreated(new Date());
		}
		bannerService.saveOrUpdate(banner);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@PostMapping("/delete")
	@RequiresPermissions("sys:banner:delete")
	public R delete(@RequestBody Integer[] ids) {
		bannerService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}
}
