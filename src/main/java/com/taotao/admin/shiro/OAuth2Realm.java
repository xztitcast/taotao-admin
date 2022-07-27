package com.taotao.admin.shiro;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.taotao.admin.entity.SysUser;
import com.taotao.admin.service.ShiroService;

@Component
public class OAuth2Realm extends AuthorizingRealm {
	
	@Lazy
	@Autowired
    private ShiroService shiroService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof OAuth2Token;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUser user = (SysUser)principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
        SysUser sysUser = shiroService.getByToken(accessToken);
        
        //token失效
        if(sysUser == null){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        if(sysUser.getStatus()){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        return new SimpleAuthenticationInfo(sysUser, accessToken, getName());
	}

}
