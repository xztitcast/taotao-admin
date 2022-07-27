package com.taotao.admin.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class OAuth2Token implements AuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	private String token;
	
	public OAuth2Token(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

}
