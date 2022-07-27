package com.taotao.admin.common;

/**
 * 状态枚举
 * @author eden
 * 2022年5月11日
 */
public enum S {

	SUCCESS(0, "成功"),
	
	ERROR(-1, "系统异常稍后重试"),
	
	CODE_EXPIRE(1000, "验证码已失效!"),
	
	CODE_ERROR(1001, "验证码错误!"),
	
	USER_PWD_ERROR(1002, "账号或密码错误!"),
	
	USER_INACTIVE(1003, "账号已被禁用!");
	
	private int value;
	
	private String message;
	
	S(int value, String message){
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}
}
