package com.mobile.port;

import com.alibaba.fastjson.JSON;


import com.mobile.util.Configuration;
import com.parkbobo.utils.message.MessageBean;





import com.mobile.model.Module;
import com.mobile.model.Users;
import com.mobile.service.EnumerateDetailService;
import com.mobile.service.FreshMenService;
import com.mobile.service.ModuleService;
import com.mobile.service.UsersService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * APP模块管理接口类
 * @author zq
 * @version 1.0
 * @since 2019-1-2 10:08:08
 * @version 1.0
 * 添加新生判断，新生在用户表的用户类型同老生一样为1
 * 需要添加用户判断
 * 面向新生模块类型为4
 *
 */
@Controller
public class ModuleInterface extends ActionSupport implements ServletResponseAware,ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7464880563906201017L;
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
		this.response.setContentType("text/html;charset=utf-8");
	}
	
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}
	
	@Resource(name = "moduleService")
	private ModuleService moduleService;
	@Resource(name = "usersService")
	private UsersService usersService;
	@Resource(name = "enumerateDetailService")
	private EnumerateDetailService enumerateDetailService;
	@Resource(name = "freshMenService")
	private FreshMenService freshMenService;
//	private String usercode;
	private Integer moduleId;// 模块ID
	
	/**
	 * 获取APP模块列表
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping("getModuleList")
	public String getModuleList(HttpServletResponse response,String usercode) throws IOException, SQLException, ClassNotFoundException{
		PrintWriter out;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		out = response.getWriter();
		StringBuffer s = new StringBuffer();
		StringBuffer tempStr = new StringBuffer();
		try {
			String accesPath = Configuration.getInstance().getValue("basepath");
			Users user = usersService.checkExistByUserId(usercode);
			int moduleType = 1;
			if(user.getUserType() == 2)
				moduleType = 2;
			else if(freshMenService.get(usercode) != null){
				moduleType = 4;
			}
			String status = enumerateDetailService.loadAppModuleStatus();
			String hql = "from Module where (channel = 3 or channel = 0) and parentModule is NULL " +
					"and (userType = 0 or userType = " + moduleType + " or (userType = 5 and privateUsers like '%" + usercode + "%')) " +
					"and display = 1 and enable = 1 order by orderid";
			List<Module> moduleList = moduleService.getByHql(hql);
			for(Module module : moduleList){
				tempStr.append("{");
				tempStr.append("\"id\":\"" + module.getModuleid() + "\",");
				tempStr.append("\"type\":\"" + module.getType() + "\",");
				tempStr.append("\"name\":\"" + module.getModulename() + "\",");
				tempStr.append("\"icon\":\"" + accesPath + module.getIcon() + "\",");
				tempStr.append("\"url\":\"" + (module.getType() == 2 ?  module.getTarget() : "") + "\",");
				tempStr.append("\"need_parm\":\"" + module.getNeedParam() + "\"");
				tempStr.append("},");
			}
			if(tempStr.length() > 0){
				tempStr.deleteCharAt(tempStr.length() - 1);
			}
			s.append("{");
			s.append("\"status\":\"success\",");
			s.append("\"update\":\"" + status + "\",");
			s.append("\"data\":[" + tempStr + "]");
			s.append("}");
			out.print(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			s = new StringBuffer();
			s.append("{");
			s.append("\"status\":\"error\",");
			s.append("\"update\":\"0\",");
			s.append("\"data\":[]");
			s.append("}");
			out.print(s);
		}
			
			out.close();
			
         return null;
	}

	/**
	 * 根据模块ID
	 * 获取被授权的指定用户账号
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public String getPrivateUsers() throws IOException, SQLException, ClassNotFoundException{
		PrintWriter out;
		this.request.setCharacterEncoding("utf-8");
		this.response.setContentType("text/html;charset=utf-8");
		out = response.getWriter();
		Module module = moduleService.loadById(moduleId);
		MessageBean<Module> messageBean = new MessageBean<Module>(module);
		if (module != null) {
			messageBean.setMessage("获取成功");
			messageBean.setStatus(true);
			messageBean.setCode(200);
		} else {
			messageBean.setMessage("获取失败");
			messageBean.setStatus(false);
			messageBean.setCode(-1);
		}
		String json = JSON.toJSONString(messageBean);
		out.print(json);
		out.close();
		return null;
	}

/*	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}*/

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
}
