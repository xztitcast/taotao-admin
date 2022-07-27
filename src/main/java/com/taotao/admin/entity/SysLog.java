package com.taotao.admin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_log")
public class SysLog implements Serializable{

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;
	//用户名
	private String username;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	
	//方法参数
	private String params;

	//执行时长(毫秒)
	private Long time;
	//IP地址
	private String ip;
	//创建时间
	private Date created;
}
