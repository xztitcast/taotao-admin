package com.taotao.admin.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
@TableName(value = "sys_user")
public class SysUser implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long userId;

    private String username;

    @JSONField(serialize = false)
    private String password;

    @JSONField(serialize = false)
    private String salt;

    @JsonFormat(shape = Shape.NUMBER)
    private Boolean status;
    
    private String avatar;
    
    private Long adder;

    private Date created;
    
    @TableField(exist = false)
    private List<Long> roleIdList;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Long getAdder() {
		return adder;
	}

	public void setAdder(Long adder) {
		this.adder = adder;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public List<Long> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}

}