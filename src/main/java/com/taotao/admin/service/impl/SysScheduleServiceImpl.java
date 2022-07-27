package com.taotao.admin.service.impl;

import java.util.List;
import java.util.Optional;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.common.Constant;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysSchedule;
import com.taotao.admin.mapper.SysScheduleMapper;
import com.taotao.admin.schedule.ScheduleManager;
import com.taotao.admin.service.SysScheduleService;

@Service("sysScheduleService")
public class SysScheduleServiceImpl extends ServiceImpl<SysScheduleMapper, SysSchedule> implements SysScheduleService, InitializingBean {
	
	@Autowired
    private Scheduler scheduler;

	@Override
	public P<SysSchedule> queryJobList(int pageNum, int pageSize, String beanName) {
		IPage<SysSchedule> page = new Page<>(pageNum, pageSize);
		QueryWrapper<SysSchedule> query = new QueryWrapper<>();
		query.eq(beanName !=null && !beanName.isBlank(), "bean_name", beanName);
		page(page, query);
		return new P<>(page.getTotal(), page.getRecords());
	}

	@Override
	public SysSchedule queryScheduleJob(Long jobId) {
		return getById(jobId);
	}

	@Override
	@Transactional
	public boolean saveOrUpdate(SysSchedule sysSchedule) {
		if(sysSchedule.getJobId() == null || sysSchedule.getJobId() <= 0) {
			ScheduleManager.create(scheduler, sysSchedule);
		}else {
			ScheduleManager.update(scheduler, sysSchedule);
		}
		return super.saveOrUpdate(sysSchedule);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] jobIds) {
		for(Long jobId : jobIds) {
			removeById(jobId);
			ScheduleManager.deleteJob(scheduler, jobId);
		}
	}

	@Override
	@Transactional
	public void updateBatch(Long[] jobIds, int status) {
		for(Long jobId : jobIds) {
			Optional<SysSchedule> optional = Optional.of(getById(jobId));
			optional.ifPresent(job -> {
				job.setStatus(status);
				updateById(job);
			});
		}
	}

	@Override
	public void run(Long[] jobIds) {
		for(Long jobId : jobIds) {
			Optional<SysSchedule> optional = Optional.of(getById(jobId));
			optional.ifPresent(job -> ScheduleManager.run(scheduler, job));
		}
	}

	@Override  
	@Transactional
	public void pause(Long[] jobIds) {
		for(Long jobId : jobIds) {
			ScheduleManager.pauseJob(scheduler, jobId);
		}
		updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
	}

	@Override
	@Transactional
	public void resume(Long[] jobIds) {
		for(Long jobId : jobIds) {
			ScheduleManager.resumeJob(scheduler, jobId);
		}
		updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<SysSchedule> jobList = list();
		jobList.forEach(job -> {
			CronTrigger cronTrigger = ScheduleManager.getCronTrigger(scheduler, job.getJobId());
			if(cronTrigger == null) {
				ScheduleManager.create(scheduler, job);
			}else {
				ScheduleManager.update(scheduler, job);
			}
		});
	}
    
}
