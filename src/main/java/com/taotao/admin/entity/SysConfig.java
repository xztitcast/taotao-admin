package com.taotao.admin.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_config")
public class SysConfig implements Serializable{

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;
	
	@NotBlank(message="参数主键不能为空")
	private String paramKey;
	
	@NotBlank(message="参数名不能为空")
	private String paramName;
	
	@NotBlank(message="参数值不能为空")
	private String paramVal;
	
	private String remark;
	
	private Date created;
	
}
