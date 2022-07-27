package com.taotao.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysSchedule;

public interface SysScheduleService extends IService<SysSchedule> {

	 P<SysSchedule> queryJobList(int pageNum, int pageSize, String beanName);
	 
	 /**
	 * 根据jobId获取定时任务
	 * @param jobId
	 * @return
	 */
	 SysSchedule queryScheduleJob(Long jobId);
	
	/**
	 * 批量删除定时任务
	 */
	void deleteBatch(Long[] jobIds);
	
	/**
	 * 批量更新定时任务状态
	 */
	void updateBatch(Long[] jobIds, int status);
	
	/**
	 * 立即执行
	 */
	void run(Long[] jobIds);
	
	/**
	 * 暂停运行
	 */
	void pause(Long[] jobIds);
	
	/**
	 * 恢复运行
	 */
	void resume(Long[] jobIds);
}
