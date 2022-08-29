package com.taotao.admin.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "sys_menu")
public class SysMenu implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long menuId;

    private Long parentId;
    
    @TableField(exist = false)
    private String parentName;

    private String name;

    private String url;

    private String perms;

    private Integer type;

    private String icon;

    private Integer sorted;
    
    @TableField(exist = false)
    private List<SysMenu> list;

}