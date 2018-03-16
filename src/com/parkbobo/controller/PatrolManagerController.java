package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolUserService;

@Controller
public class PatrolManagerController {

	@Resource
	private PatrolUserService patrolUserService;

	@Resource
	private PatrolConfigService patrolConfigService;

	@RequestMapping("addUser")
	public void addUser(String jobNum,String password,String username,Integer campusNum,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			PatrolUser patrolUser = new PatrolUser();
			patrolUser.setCreateTime(new Date());
			patrolUser.setCampusNum(campusNum);
			patrolUser.setJobNum(jobNum);
			patrolUser.setPassword(password);
			//patrolUser.setUsername(new String(username.getBytes("ISO-8859-1"),"utf-8"));
			if (username != null) {
				patrolUser.setUsername(URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8"));
			} else {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户名不能为空\"}");
				return;
			}
			int flag = this.patrolUserService.addUser(patrolUser);
			if (flag == 2) {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"新增出错\"}");
			}
			if (flag == 0) {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账号已存在\"}");
			}
			if (flag == 1) {
				out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"新增成功\"}");
			} 
		} catch (Exception e) {
			out.print("{\"status\":\"false\",\"Code\":0,\"errorMsg\":\"未知异常\"}");
		}
	}

	@RequestMapping("updateUser")
	public void updateUser(Integer id,String username,String password,String jobNum,Integer campusNum,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			PatrolUser patrolUser = new PatrolUser();
			patrolUser.setId(id);
			patrolUser.setCampusNum(campusNum);
			patrolUser.setJobNum(jobNum);
			patrolUser.setPassword(password);
			patrolUser.setCreateTime(this.patrolUserService.getById(id).getCreateTime());
			if(username!=null){
				patrolUser.setUsername(URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8"));
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户名不能为空\"}");
				return;
			}
			int flag = this.patrolUserService.updateUser(patrolUser);
			if (flag == 2) {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"修改出错\"}");
			}
			if (flag == 0) {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账号已存在\"}");
			}
			if (flag == 1) {
				out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"修改成功\"}");
			} 
		} catch (Exception e) {
			out.print("{\"status\":\"false\",\"Code\":0,\"errorMsg\":\"未知异常\"}");
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * 启动紧急状态
	 * @throws IOException 
	 */
	@RequestMapping("startEmergency")
	public void startEmergency(Integer configId,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		PatrolConfig config = this.patrolConfigService.getById(configId);
		config.setIsEmergency(1);
		out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(config)+"}");
	}
	/**
	 * 取消紧急状态
	 * @throws IOException 
	 */
	@RequestMapping("endEmergency")
	public void endEmergency(Integer configId,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		PatrolConfig config = this.patrolConfigService.getById(configId);
		config.setIsEmergency(0);
		out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(config)+"}");
	}
	/**
	 * 实时获取安防人员轨迹
	 * 
	 */
	public void getLocation(Integer campusNum,Integer jobNum){
		
	}

}
