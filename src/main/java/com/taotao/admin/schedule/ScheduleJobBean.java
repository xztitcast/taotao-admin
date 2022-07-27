package com.taotao.admin.schedule;

import java.lang.reflect.Method;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.taotao.admin.common.utils.SpringContextUtil;
import com.taotao.admin.entity.SysSchedule;

import lombok.extern.slf4j.Slf4j;


/**
 * 定时任务执行业务逻辑核心类
 * job执行成功或者失败最好将日志打印出来
 * @author xiangzuotao
 * @2019年11月7日
 */
@Slf4j
public class ScheduleJobBean extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		SysSchedule sysSchedule = (SysSchedule)context.getMergedJobDataMap().get(SysSchedule.JOB_PARAM_KEY);
		try {
			Object target = SpringContextUtil.getBean(sysSchedule.getBeanName());
			Method method = target.getClass().getDeclaredMethod("run", String.class);
			method.invoke(target, sysSchedule.getParams());
		} catch (Exception e) {
			log.error("方法executeInternal执行异常", e);
		} 
	}

}
