package com.taotao.admin.controller;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.admin.common.P;
import com.taotao.admin.common.R;
import com.taotao.admin.entity.SysLog;
import com.taotao.admin.entity.excel.SysLogExcel;
import com.taotao.admin.export.SimpleWriteExcel;
import com.taotao.admin.service.SysLogService;

/**
 * 管理系统日志控制器
 * @author eden
 * @time 2022年7月22日 上午11:36:22
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends BaseController {

	@Autowired
	private SysLogService sysLogService;
	
	@GetMapping("/list")
	@RequiresPermissions("sys:log:list")
	public R list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, 
			@RequestParam(required = false) String username) {
		P<SysLog> p = sysLogService.getSysLogList(pageNum, pageSize, username);
		return R.ok(p);
	}
	
	/**
	 * 导出功能demo
	 * @param response
	 * @param start
	 * @param end
	 * @throws Exception 
	 */
	@GetMapping("/excel")
	public void excel(HttpServletResponse response,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws Exception {
		if(start == null && end == null) {
			end = new Date();
			start = DateUtils.addDays(end, -30);
		}
		List<SysLog> list = sysLogService.getLogExcelList(start, end);
		SimpleWriteExcel<SysLogExcel> excel = new SimpleWriteExcel<>("日志数据统计");
		List<SysLogExcel> excelList = list.stream().map(s -> {
			SysLogExcel sle = new SysLogExcel();
			sle.setUsername(s.getUsername());
			sle.setOperation(s.getOperation());
			sle.setMethod(s.getMethod());
			sle.setParams(s.getParams());
			sle.setTime(s.getTime().toString());
			sle.setIp(s.getIp());
			sle.setCreated(DateFormatUtils.format(s.getCreated(), "yyyy-MM-dd HH:mm:ss"));
			return sle;
		}).collect(Collectors.toList());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);

		try(OutputStream out = response.getOutputStream();){
			excel.addRows(excelList).write(out);
		}catch(Exception e){
			throw e;
		}
	}
}
