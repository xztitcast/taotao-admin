package com.taotao.admin.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "sys_role")
public class SysRole implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long roleId;

    private String roleName;

    private String remark;

    private Long adder;

    private Date created;
    
    @TableField(exist = false)
    private List<Long> menuIdList;

}