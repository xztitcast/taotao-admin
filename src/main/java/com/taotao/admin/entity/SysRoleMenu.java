package com.taotao.admin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "sys_role_menu")
public class SysRoleMenu implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;

    private Long roleId;

    private Long menuId;

}