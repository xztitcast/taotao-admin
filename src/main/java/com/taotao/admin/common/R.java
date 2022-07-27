package com.taotao.admin.common;

import com.alibaba.fastjson.JSONObject;

public class R extends JSONObject {

	private static final long serialVersionUID = 1L;

	private static final String CODE = "code";
	
	private static final String MESSAGE = "msg";
	
	private static final String RESULT = "result";
	
	public R() {
		this(S.SUCCESS.getValue(), S.SUCCESS.getMessage());
	}
	
	public R(int code, String message) {
		put(CODE, code);
		put(MESSAGE, message);
	}

	public static R error() {
		return error(S.ERROR);
	}
	
	public static R error(String message) {
		return error(S.ERROR.getValue(), message);
	}
	
	public static R error(S s) {
		return error(s.getValue(), s.getMessage());
	}
	
	public static R error(int code, String message) {
		R r = new R(code, message);
		return r;
	}
	
	public static R ok() {
		return new R();
	}
	
	public static R ok(Object value) {
		return ok().put(RESULT, value);
	}
	
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
