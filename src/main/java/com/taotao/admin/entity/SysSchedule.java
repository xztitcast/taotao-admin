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
@TableName("sys_schedule")
public class SysSchedule implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

	@TableId(type = IdType.AUTO)
	private Long jobId;

	@NotBlank(message = "bean名称不能为空!")
    private String beanName;

	@NotBlank(message="方法名称不能为空")
    private String methodName;

    private String params;

	@NotBlank(message="cron表达式不能为空")
    private String cron;

    private Integer status;

    private String remark;

    private Date created;
}
