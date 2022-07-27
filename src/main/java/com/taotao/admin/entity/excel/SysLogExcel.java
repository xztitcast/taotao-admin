package com.taotao.admin.entity.excel;

import com.taotao.admin.export.ExcelField;

import lombok.Getter;
import lombok.Setter;

/**
 * 导出功能demo
 * excel 属性字段统一全部用String类型
 * @author eden
 * @time 2022年7月26日 下午5:31:34
 */
@Getter
@Setter
public class SysLogExcel {
	
	@ExcelField(title = "用户", order = 1)
	private String username;

	@ExcelField(title = "操作", order = 2)
	private String operation;

	@ExcelField(title = "方法", order = 3)
	private String method;
	
	@ExcelField(title = "参数", order = 4)
	private String params;

	@ExcelField(title = "执行时长(毫秒)", order = 5)
	private String time;
	
	@ExcelField(title = "IP地址", order = 6)
	private String ip;

	@ExcelField(title = "创建日期", order = 7)
	private String created;
	
	
}
