package com.system.controller;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.UserAuth;
import com.parkbobo.model.Users;
import com.parkbobo.model.UsersCars;
import com.parkbobo.service.UsersCarsService;
import com.parkbobo.service.UsersService;
import com.parkbobo.utils.PageBean;
import com.system.model.Department;
import com.system.model.Manager;
import com.system.service.DepartmentService;
import com.system.utils.StringUtil;
/**
 * 部门管理
 * @author LH
 * @since 1.0
 */
@Controller
public class UserAuthManagerController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1620751832599338243L;
	@Resource
	private UsersCarsService usersCarsService;
	@Resource
	private UsersService usersService;
	/**
	 * 部门列表
	 * @return
	 */
	@RequestMapping("userAuthManager_list")
	public ModelAndView list(String startTime,String endTime,String mobile,Integer page,Integer pageSize)
	{
		ModelAndView mv = new ModelAndView();
		PageBean<UserAuth> userAuthPage = usersCarsService.pageQuerySql(startTime, endTime, mobile,pageSize==null?12:pageSize, page==null?1:page);	
		mv.addObject("userAuthPage", userAuthPage);
		mv.addObject("mobile", mobile);
		mv.addObject("startTime", startTime);
		mv.addObject("endTime", endTime);
		mv.setViewName("manager/system/userAuthManager/userAuthManager-list");
		return mv;
	}
	/**
	 * 驾驶证审核
	 * */
	@RequestMapping("userAuthManager_auth")
	public ModelAndView driverAuth(String id,String method,Integer authStatus,String authReason,String mobile)
	{
		ModelAndView mv = new ModelAndView();
		if(StringUtil.isNotEmpty(method) && method.equals("edit")){
			Users users = usersService.get(mobile);
			users.setAuthStatus(authStatus);
			users.setAuthReason(authReason);
			usersService.update(users);
			mv.setViewName("redirect:/userAuthManager_list?method=driverAuthSuccess");
		}else{
			Users users = usersService.get(id);
			mv.addObject("users", users);
			mv.setViewName("manager/system/userAuthManager/userAuthManager-driverAuth");
		}
		return mv;
	}
	/**
	 * 行驶证审核
	 * */
	@RequestMapping("userAuthManager_drivingAuth")
	public ModelAndView drivingAuth(String id,String method,String authReason,String authStatus,String kid)
	{
		ModelAndView mv = new ModelAndView();
		authReason += ",,";
		if(StringUtil.isNotEmpty(method) && method.equals("edit")){
			if(StringUtil.isNotEmpty(kid)){
				String[] kidArr = kid.split(",");
				String[] authStatusArr = authStatus.split(",");
				String[] authReasonArr = authReason.split(",");
				for (int i = 0; i < kidArr.length; i++) {
					UsersCars usersCars = usersCarsService.get(Integer.valueOf(kidArr[i].trim()));
					usersCars.setAuthStatus(Integer.valueOf(authStatusArr[i].trim()));
					if(i>=authReasonArr.length){
						usersCars.setAuthReason(null);
					}else{
						usersCars.setAuthReason(authReasonArr[i]);
					}
					usersCarsService.update(usersCars);						
				}
			}
			mv.setViewName("redirect:/userAuthManager_list?method=drivingAuthSuccess");
		}else{
			String hql = "from UsersCars where mobile = '" + id + "'";
			List<UsersCars> usersCars = usersCarsService.getByHql(hql);
			mv.addObject("usersCars", usersCars);
			mv.addObject("id", id);
			mv.setViewName("manager/system/userAuthManager/userAuthManager-drivingAuth");
		}
		return mv;
	}
	
}
