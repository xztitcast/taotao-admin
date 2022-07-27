package com.taotao.admin.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysLog;

public interface SysLogService extends IService<SysLog> {

	P<SysLog> getSysLogList(int pageNum, int pageSize, String username);
	
	/**
	 * 获取导出数据列表
	 * @param start
	 * @param end
	 * @return
	 */
	List<SysLog> getLogExcelList(Date start, Date end);
}
