package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolLocationInfoService;
import com.parkbobo.service.PatrolUserService;

@Controller
public class PatrolManagerController {

	@Resource
	private PatrolUserService patrolUserService;

	@Resource
	private PatrolConfigService patrolConfigService;

	@Resource 
	private PatrolLocationInfoService patrolLocationInfoService;

	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

	/**
	 * 获取所有巡查员
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("getAllPatrolUser")
	public void getAllUser(HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			List<PatrolUser> allUser = this.patrolUserService.getAllUser();
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(allUser,features)+"}");
		} catch (IOException e) {
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");

		}finally{
			out.flush();
			out.close();
		}


	}
	/**
	 * 新增巡查员
	 * @param jobNum 工号
	 * @param password 密码
	 * @param username 用户名
	 * @param campusNum 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("addPatrolUser")
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

	/**
	 *返显巡查员信息 
	 *@param id 巡查员id
	 * @throws IOException 
	 */
	@RequestMapping("getPatrolUserById")
	public void getUserById(Integer id,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			PatrolUser patrolUser = this.patrolUserService.getById(id);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUser,features)+"}");
		}catch(Exception e){
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 更新巡查员
	 * @param id 巡查员id
	 * @param username 用户名
	 * @param password 密码
	 * @param jobNum 工号
	 * @param campusNum 校区id
	 * @param response 
	 * @throws IOException
	 */
	@RequestMapping("updatePatrolUser")
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
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"Code\":0,\"errorMsg\":\"未知异常\"}");
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * 删除用户
	 * @param 巡查员id
	 * @throws IOException 
	 */
	@RequestMapping("deletePatrolUser")
	public void deleteUser(HttpServletResponse response,Integer id) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PatrolUser patrolUser = this.patrolUserService.getById(id);
			if(patrolUser == null){
				out.print("{\"status\":\"false\",\"Code\":-1,\"Msg\":\"用户不存在\"}");
				return;
			}else{
				this.patrolUserService.deleteById(id);
			}
			out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"删除成功\"}");
		} catch (IOException e) {
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * 启动紧急状态
	 * @param configId 配置信息id
	 * @throws IOException 
	 */
	@RequestMapping("startEmergency")
	public void startEmergency(Integer configId,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PatrolConfig config = this.patrolConfigService.getById(configId);
			config.setIsEmergency(1);
			this.patrolConfigService.updateConfig(config);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(config)+"}");
		} catch (Exception e) {
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 取消紧急状态
	 * @param 配置信息id
	 * @throws IOException 
	 */
	@RequestMapping("endEmergency")
	public void endEmergency(Integer configId,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			PatrolConfig config = this.patrolConfigService.getById(configId);
			config.setIsEmergency(0);
			this.patrolConfigService.updateConfig(config);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(config)+"}");
		} catch (Exception e) {
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 实时获取位置
	 * @param campusNum 校区id
	 * @param jobNum 工号
	 * @param regionId 区域id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getLocation")
	public void getLocation(Integer campusNum,String jobNum,Integer regionId,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out =  response.getWriter();
			PatrolLocationInfo location = this.patrolLocationInfoService.getLocation(jobNum, regionId, campusNum);
			if(location!=null){
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(location)+"}");
			}else{
				out.print("{\"status\":\"false\",\"Code\":-2,\"Msg\":\"此人暂无巡逻信息\"}");
			}
		} catch (Exception e) {
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}

}
