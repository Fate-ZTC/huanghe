package com.system.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import com.system.model.*;
import com.system.service.TokenService;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.system.service.ManagerService;



/**
 * 在这个类中，你就可以从数据库中读入用户的密码，角色信息，是否锁定，账号是否过期等。建议通过我们封装的平台级持久层管理类获取和管理。
 * @author LH
 *
 */
@SuppressWarnings("deprecation")
public class MyUserDetailService implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(MyUserDetailService.class);
    private ManagerService managerService;
    @Resource(name = "tokenService")
    private TokenService tokenService;
    public ManagerService getManagerService() {
        return managerService;
    }
    @Resource(name="managerService")
    public void setManagerService(ManagerService managerService) {
        this.managerService = managerService;
    }
    //登录验证
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        if (logger.isDebugEnabled()) {
            logger.debug("loadUserByUsername(String) - start"); //$NON-NLS-1$
        }
        //调用中控接口得到用户信息
        Manager manager = tokenService.getManagerInfo(TokenService.getResponseResult());
        Collection<GrantedAuthority> auths = obtionGrantedAuthorities(manager);
        boolean enables = true;//账号是否激活
        boolean accountNonExpired = true;//用户账号过期
        boolean credentialsNonExpired = true;//用户凭证过期
        boolean accountNonLocked = true;//账号是否未锁定
        //封装成spring security的user
        User user = new User(manager.getUsername(),"123456", enables, accountNonExpired, credentialsNonExpired, accountNonLocked, auths);
        return user;
    }
    //取得用户的权限
    private Set<GrantedAuthority> obtionGrantedAuthorities(Manager manager) {
        Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
        //Set<ManagerResources> managerResourcees = manager.getManagerResourceses();
        Set<ManagerRole> managerRoles = manager.getManagerRoles();
        //用户权限
		/*for (ManagerResources managerResources : managerResourcees) {
			Resources resources = managerResources.getResources();
			if(resources.getEnable() == 1){
				authSet.add(new GrantedAuthorityImpl(resources.getEnname()));
			}
		}*/
        Role role = null;
        for (ManagerRole managerRole : managerRoles) {
            role = managerRole.getRole();
            if(role != null && role.getEnable() == 1)
            {
                Set<RoleResources> roleResourcees  = role.getRoleResourceses();
                for (RoleResources roleResources : roleResourcees) {
                    Resources resources = roleResources.getResources();
                    if(resources.getEnable() == 1) {
                        authSet.add(new GrantedAuthorityImpl(resources.getEnname()));
                    }
                }
            }
        }
        //用户角色权限

        if (logger.isDebugEnabled()) {
            logger.debug("真蛋疼,靠"); //$NON-NLS-1$
        }

        authSet.add(new GrantedAuthorityImpl("main_index"));
        return authSet;
    }
}
