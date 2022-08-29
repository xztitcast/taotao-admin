package com.taotao.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysLog;
import com.taotao.admin.mapper.SysLogMapper;
import com.taotao.admin.service.SysLogService;

@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

	@Override
	public P<SysLog> getSysLogList(int pageNum, int pageSize, String username) {
		IPage<SysLog> page = new Page<>(pageNum, pageSize);
		QueryWrapper<SysLog> query = new QueryWrapper<>();
		query.eq(username != null && !username.isBlank(), "`username`", username);
		page(page, query);
		return new P<>(page.getTotal(), page.getRecords());
	}

	@Override
	public List<SysLog> getLogExcelList(Date start, Date end) {
		QueryWrapper<SysLog> query = new QueryWrapper<>();
		query.ge(start != null, "created", start).le(end != null, "created", end);
		return list(query);
	}

}
