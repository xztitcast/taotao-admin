package com.taotao.admin.shiro;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.admin.common.R;
import com.taotao.admin.common.utils.HttpContextUtils;

public class OAuth2Filter extends AuthenticatingFilter {

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		//获取请求token
        String token = getRequestToken((HttpServletRequest) request);

        if(token == null || token.isBlank()){
            return null;
        }

        return new OAuth2Token(token);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }

        return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//获取请求token，如果token不存在，直接返回401
        String token = getRequestToken((HttpServletRequest) request);
        if(token ==null || token.isBlank() || "null".equals(token.trim())){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
            R r = R.error(401, "invalid token");
            httpResponse.getWriter().print(r.toJSONString());
            return false;
        }

        return executeLogin(request, response);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json; charset=UTF-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        R r = R.error(401, e.getMessage());
        try {
			httpResponse.getWriter().print(r.toJSONString());
		} catch (IOException e1) {
		}
        
		return super.onLoginFailure(token, e, request, response);
	}
	
	/**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if(token == null || token.isBlank()){
            token = httpRequest.getParameter("token");
        }
        
        if(token == null || token.isBlank()) {
        	Cookie[] cookies = httpRequest.getCookies();
        	if(Objects.nonNull(cookies)) {
            	for(Cookie c : httpRequest.getCookies()) {
            		if("token".equals(c.getName())) {
            			token = c.getValue();
            			break;
            		}
            	}
        	}
        }

        return token;
    }
    
    

}
