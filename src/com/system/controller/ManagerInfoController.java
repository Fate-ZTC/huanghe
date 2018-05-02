package com.system.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.system.model.Manager;
import com.system.service.ManagerService;
import com.system.utils.StringUtil;

/**
 * 个人资料
 * @author LH
 * @since 1.0
 */
@Controller
public class ManagerInfoController{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8535267012382362540L;
	@Resource
	private ManagerService managerService;
	/**
	 * 个人资料编辑
	 * @return
	 */
	@RequestMapping("managerInfo_edit")
	public ModelAndView edit(String method,HttpSession session,Manager manager)
	{
		ModelAndView mv = new ModelAndView();
		//编辑
		Manager user = (Manager) session.getAttribute("loginUser");
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			manager.setUserId(user.getUserId());
			Manager u = managerService.updateUserInfo(manager);
			session.setAttribute("loginUser", u);
			mv.setViewName("redirect:/managerInfo_edit?method=editSuccess");
			return mv;
		}
		//跳转到编辑页面
		else
		{
			manager = managerService.getById(user.getUserId());
			mv.addObject("manager", manager);
			mv.setViewName("manager/system/managerInfo/managerInfo-edit");
		}
		return mv;
	}
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping("managerInfo_password")
	public ModelAndView password(String method,Manager manager,HttpSession session)
	{
		//编辑
		ModelAndView mv = new ModelAndView();
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			Manager user = (Manager) session.getAttribute("loginUser");
			managerService.updatePassword(user,manager.getPassword());
			mv.setViewName("redirect:/managerInfo_password?method=passwordSuccess");
		}
		//跳转到编辑页面
		else
		{
			mv.setViewName("manager/system/managerInfo/managerInfo-password");
		}
		return mv;
	}
	/**
	 * 验证邮箱是否存在
	 * @throws IOException 
	 */
	@RequestMapping("managerInfo_checkEmail")
	public void checkEmail(Manager manager,HttpServletRequest request,HttpServletResponse response,Integer id) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			if(!StringUtil.isNotEmpty(manager.getEmail())) {
				out.print("{\"ok\":\"\"} ");
			}
			if(managerService.checkProperty("email",manager.getEmail(),id))
			{
				out.print("{\"error\":\"该邮箱已被注册\"}");
			}
			else
			{
				out.print("{\"ok\":\"\"} ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"error\":\"\"} ");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 验证用户名是否存在
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value ="/managerInfo_checkUsername",method=RequestMethod.POST)
	public void checkUsername(HttpServletRequest request,HttpServletResponse response,Manager manager,Integer id) throws IOException
	{
		manager.setUsername(request.getParameter("username"));
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			if(StringUtil.isNotEmpty(manager.getUsername()) && managerService.checkProperty("username",manager.getUsername(),id))
			{
				out.print("{\"error\":\"该用户名已被注册\"}");
			}else if(!StringUtil.isNotEmpty(manager.getUsername())){
				out.print("{\"error\":\"\"} ");
			}
			else
			{
				out.print("{\"ok\":\"\"} ");;
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"error\":\"\"} ");
		}finally{
			out.flush();
			out.close();
		}
	}
	@RequestMapping("managerInfo_checkMobile")
	public void checkMobile(Manager manager,HttpServletRequest request,HttpServletResponse response,Integer id) throws IOException{
		manager.setMobile(request.getParameter("mobile"));
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {

			if(StringUtil.isNotEmpty(manager.getMobile()) && managerService.checkProperty("mobile",manager.getMobile(),id))
			{
				out.print("{\"error\":\"该手机号已被注册\"}");
			}else if(!StringUtil.isNotEmpty(manager.getMobile())){
				out.print("{\"error\":\"\"} ");
			}else
			{
				out.print("{\"ok\":\"\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"error\":\"\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
}
