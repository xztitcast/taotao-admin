package com.taotao.admin.controller;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.taotao.admin.entity.SysSchedule;
import com.taotao.admin.service.SysScheduleService;

/**
 * 定时任务控制器
 * @author eden
 * @time 2022年7月22日 上午11:34:38
 */
@Validated
@RestController
@RequestMapping("/sys/schedule")
public class SysScheduleController {

	@Autowired
	private SysScheduleService sysScheduleService;
	
	/**
	 * 获取定时任务列表
	 * @param pageNum
	 * @param pageSize
	 * @param beanName
	 * @return
	 */
	@GetMapping("list")
	@RequiresPermissions("sys:schedule:list")
	public R list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
			@RequestParam(required = false) String beanName) {
		P<SysSchedule> p = sysScheduleService.queryJobList(pageNum, pageSize, beanName);
		return R.ok(p);
	}
	
	/**
	 * 单个定时任务信息
	 * @param jobId
	 * @return
	 */
	@GetMapping("/info/{jobId}")
	@RequiresPermissions("sys:schedule:info")
	public R info(@PathVariable("jobId") Long jobId) {
		SysSchedule job = sysScheduleService.queryScheduleJob(jobId);
		return R.ok(job);
	}
	
	@Log("保存定时任务")
	@PostMapping("/save")
	@RequiresPermissions("sys:schedule:save")
	public R save(@RequestBody SysSchedule sysSchedule) {
		sysSchedule.setCreated(new Date());
		sysScheduleService.saveOrUpdate(sysSchedule);
		return R.ok();
	}
	
	@Log("修改定时任务")
	@PostMapping("/update")
	@RequiresPermissions("sys:schedule:save")
	public R update(@RequestBody SysSchedule sysSchedule) {
		SysSchedule job = sysScheduleService.queryScheduleJob(sysSchedule.getJobId());
		if (job == null) {
			return R.error("jobId = " + sysSchedule.getJobId() +"不存在");
		}
		sysSchedule.setCreated(job.getCreated());
		sysScheduleService.saveOrUpdate(sysSchedule);
		return R.ok();
	}
	
	@Log("删除定时任务")
	@PostMapping("/delete")
	@RequiresPermissions("sys:schedule:delete")
	public R delete(@RequestBody Long[] jobIds){
		sysScheduleService.deleteBatch(jobIds);
		
		return R.ok();
	}
	
	@Log("手动运行定时任务")
	@PostMapping("/run")
	@RequiresPermissions("sys:schedule:run")
	public R run(@RequestBody Long[] jobIds){
		sysScheduleService.run(jobIds);
		
		return R.ok();
	}
	
	@Log("手动暂停定时任务")
	@PostMapping("/pause")
	@RequiresPermissions("sys:schedule:pause")
	public R pause(@RequestBody Long[] jobIds){
		sysScheduleService.pause(jobIds);
		
		return R.ok();
	}
	
	@Log("手动恢复定时任务")
	@PostMapping("/resume")
	@RequiresPermissions("sys:schedule:resume")
	public R resume(@RequestBody Long[] jobIds){
		sysScheduleService.resume(jobIds);
		
		return R.ok();
	}
}
