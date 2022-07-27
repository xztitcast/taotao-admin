package com.taotao.admin.entity.form;

import java.io.Serializable;

public class SysForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String username;
	
	protected String password;
	
	protected String captcha;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}
