package com.taotao.admin.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.taotao.admin.common.Constant;
import com.taotao.admin.entity.SysSchedule;

public abstract class ScheduleManager {

private final static String JOB_NAME = "TASK_";
	
	/**
	 * 获取触发器key
	 * @param jobId
	 * @return
	 */
	public static TriggerKey getTriggerKey(Long jobId) {
		return TriggerKey.triggerKey(JOB_NAME + jobId);
	}
	
	/**
	 * 获取jobKey
	 * @param jobId
	 * @return
	 */
	public static JobKey getJobKey(Long jobId) {
		return  JobKey.jobKey(JOB_NAME + jobId);
	}
	
	/**
	 * 获取cron表达式触发器
	 * @param scheduler
	 * @param jobId
	 * @return
	 */
	public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
		try {
			return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
		} catch (SchedulerException e) {
			throw new RuntimeException("获取定时任务CronTrigger出现异常", e);
		}
	}
	
	/**
	 * 创建定时任务
	 * @param scheduler
	 * @param sysSchedule
	 */
	public static void create(Scheduler scheduler, SysSchedule sysSchedule) {
		try {
			//构建job信息
			JobDetail jobDetail = JobBuilder.newJob(ScheduleJobBean.class).withIdentity(getJobKey(sysSchedule.getJobId())).build();
			
			//表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysSchedule.getCron())
            		.withMisfireHandlingInstructionDoNothing();
            
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(sysSchedule.getJobId())).withSchedule(scheduleBuilder).build();
            
            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(SysSchedule.JOB_PARAM_KEY, sysSchedule);
            
            scheduler.scheduleJob(jobDetail, trigger);
            //暂停任务
            if(sysSchedule.getStatus() == Constant.ScheduleStatus.PAUSE.getValue()){
            	pauseJob(scheduler, sysSchedule.getJobId());
            }
		} catch (SchedulerException e) {
			throw new RuntimeException("创建定时任务失败", e);
		}
	}
	
	/**
	 * 更新定时任务
	 * @param scheduler
	 * @param sysSchedule
	 */
	public static void update(Scheduler scheduler, SysSchedule sysSchedule) {
		try {
			TriggerKey triggerKey = getTriggerKey(sysSchedule.getJobId());

	         //表达式调度构建器
	         CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysSchedule.getCron())
	         		.withMisfireHandlingInstructionDoNothing();

	         CronTrigger trigger = getCronTrigger(scheduler, sysSchedule.getJobId());
	         
	         //按新的cronExpression表达式重新构建trigger
	         trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
	         
	         //参数
	         trigger.getJobDataMap().put(SysSchedule.JOB_PARAM_KEY, sysSchedule);
	         
	         scheduler.rescheduleJob(triggerKey, trigger);
	         
	         //暂停任务
	         if(sysSchedule.getStatus() == Constant.ScheduleStatus.PAUSE.getValue()){
	         	pauseJob(scheduler, sysSchedule.getJobId());
	         }
		} catch (SchedulerException e) {
			throw new RuntimeException("更新定时任务失败", e);
		}
	}
	
	/**
	 * 立即执行任务
	 * @param scheduler
	 * @param sysSchedule
	 */
	public static void run(Scheduler scheduler, SysSchedule sysSchedule) {
		try {
			JobDataMap dataMap = new JobDataMap();
        	dataMap.put(SysSchedule.JOB_PARAM_KEY, sysSchedule);
        	
            scheduler.triggerJob(getJobKey(sysSchedule.getJobId()), dataMap);
		} catch (SchedulerException e) {
			throw new RuntimeException("立即执行定时任务失败", e);
		}
	}
	
	/**
	 * 暂停定时任务
	 * @param scheduler
	 * @param jobId
	 */
	public static void pauseJob(Scheduler scheduler, Long jobId) {
		try {
			scheduler.pauseJob(getJobKey(jobId));
		} catch (SchedulerException e) {
			throw new RuntimeException("暂停定时任务失败", e);
		}
	}
	
	/**
	 * 恢复定时任务
	 * @param scheduler
	 * @param jobId
	 */
	public static void resumeJob(Scheduler scheduler, Long jobId) {
		try {
			scheduler.resumeJob(getJobKey(jobId));
		} catch (SchedulerException e) {
			throw new RuntimeException("暂停定时任务失败", e);
		}
	}
	
	/**
	 * 删除定时任务
	 * @param scheduler
	 * @param jobId
	 */
	public static void deleteJob(Scheduler scheduler, Long jobId) {
		try {
			scheduler.deleteJob(getJobKey(jobId));
		} catch (SchedulerException e) {
			throw new RuntimeException("删除定时任务失败", e);
		}
	}
}
