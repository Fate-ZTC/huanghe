package com.system.security;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 在这个类中，最重要的是decide方法，如果不存在对该资源的定义，直接放行；否则，如果找到正确的角色，即认为拥有权限，并放行，
 * 否则throw new AccessDeniedException("no right")。所有的异常建议平台统一进行封装并管理。
 * @author LH
 *
 */
public class MyAccessDecisionManager implements AccessDecisionManager {
	private static final Logger logger  = Logger.getLogger(MyAccessDecisionManager.class);
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
			InsufficientAuthenticationException {
		if(logger.isDebugEnabled()){
			logger.debug("decide(Authentication, Object, Collection<ConfigAttribute>) - start");//$NON-NLS-1$ 
		}
		if(configAttributes == null){
			if (logger.isDebugEnabled()) {  
				logger.debug("decide(Authentication, Object, Collection<ConfigAttribute>) - end"); //$NON-NLS-1$  
			}
			return ;
		}
		if (logger.isDebugEnabled()){
			logger.debug("正在访问的url是："+object.toString());
		}
		Iterator<ConfigAttribute> iterator = configAttributes.iterator();
		while(iterator.hasNext()){
			ConfigAttribute ca = iterator.next();
			logger.debug("needRole is："+ca.getAttribute());
			String needRole=((SecurityConfig)ca).getAttribute();
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(needRole.equals(ga.getAuthority())){
					if (logger.isDebugEnabled()) {
						logger.debug("判断到，needRole 是"+needRole+",用户的角色是:"+ga.getAuthority()+"，授权数据相匹配");
						logger.debug("decide(Authentication, Object, Collection<ConfigAttribute>) - end"); //$NON-NLS-1$
					}
					return;
				}
			}
		}
		throw new AccessDeniedException("没有权限");
	}

	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

}
