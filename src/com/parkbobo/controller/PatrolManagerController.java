package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolEmergency;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolEmergencyService;
import com.parkbobo.service.PatrolLocationInfoService;
import com.parkbobo.service.PatrolUserRegionService;
import com.parkbobo.service.PatrolUserService;
import com.parkbobo.utils.PageBean;

/**
 * 安防管理端接口
 * @author zj
 *@version 1.0
 */
@Controller
public class PatrolManagerController {

	@Resource
	private PatrolUserService patrolUserService;

	@Resource
	private PatrolConfigService patrolConfigService;

	@Resource
	private PatrolUserRegionService patrolUserRegionService;

	@Resource
	private PatrolLocationInfoService patrolLocationInfoService;

	@Resource
	private PatrolEmergencyService patrolEmergencyService;

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
			List<PatrolUser> allUser = this.patrolUserService.getAll();
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
	public void addUser(String jobNum,String password,String username,Integer campusNum,HttpServletResponse response) throws IOException{PrintWriter out = null;
	try {
		response.setCharacterEncoding("UTF-8");
		out = response.getWriter();
		Date date = new Date(); 
		PatrolUser patrolUser = new PatrolUser();
		patrolUser.setCreatetime(date);
		patrolUser.setCampusNum(campusNum);
		patrolUser.setLastUpdateTime(date);
		patrolUser.setJobNum(jobNum);
		patrolUser.setPassword(password);
		patrolUser.setIsDel((short)0);
		if (username != null) {
			patrolUser.setUsername(username);
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
			patrolUser.setLastUpdateTime(new Date());
			patrolUser.setPassword(password);
			patrolUser.setIsDel((short)0);
			PatrolUser user = this.patrolUserService.getById(id);
			if(user!=null){
				patrolUser.setCreatetime(user.getCreatetime());
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未查询到用户\"}");
				return;
			}
			if(username!=null){
				patrolUser.setUsername(username);
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
			//e.printStackTrace();
			out.print("{\"status\":\"false\",\"Code\":0,\"errorMsg\":\"未知异常\"}");
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * 删除用户
	 * @param id 巡查员id
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
				patrolUser.setIsDel((short)1);
				patrolUser.setLastUpdateTime(new Date());
				this.patrolUserService.update(patrolUser);
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
	 * 开启紧急状态
	 * @param configId  配置id
	 * @param username 用户名
	 * @param jobNum 工号
	 * @param campusNum 校区id 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("startEmergency")
	public void startEmergency(Integer configId,String username,String jobNum,Integer campusNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PatrolEmergency patrolEmergency = new PatrolEmergency();
			patrolEmergency.setJobNum(jobNum);
			patrolEmergency.setStartTime(new Date());
			if(username!=null){
				patrolEmergency.setUsername(URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8"));
			}
			patrolEmergency.setCampusNum(campusNum);
			PatrolEmergency emergency = this.patrolEmergencyService.add(patrolEmergency);
			PatrolConfig config = this.patrolConfigService.getById(configId);
			config.setIsEmergency(1);
			this.patrolConfigService.updateConfig(config);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"patrolEmergency\":"+JSONObject.toJSONString(emergency,features)+",\"patrolConfig\":"+JSONObject.toJSONString(config,features)+"}}");
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
	 * @param configId 配置id
	 * @param campusNum 校区id
	 * @param response 
	 * @throws IOException
	 */
	@RequestMapping("endEmergency")
	public void endEmergency(Integer configId,Integer campusNum,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			PatrolConfig config = this.patrolConfigService.getById(configId);
			PatrolEmergency patrolEmergency = this.patrolEmergencyService.getNewest(campusNum);
			if(patrolEmergency!=null){
				patrolEmergency.setEndTime(new Date());
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"无开始信息,请联系管理员\"}");
				return;
			}
			config.setIsEmergency(0);
			this.patrolConfigService.updateConfig(config);
			this.patrolEmergencyService.update(patrolEmergency);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"patrolEmergency\":"+JSONObject.toJSONString(patrolEmergency,features)+",\"patrolConfig\":"+JSONObject.toJSONString(config,features)+"}}");
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
	 * @param usregId 用户区域id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getLocation")
	public void getLocation(Integer campusNum,String jobNum,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out =  response.getWriter();
			PatrolUserRegion byJobNum = this.patrolUserRegionService.getByJobNum(jobNum);
			if(byJobNum!=null){
				PatrolLocationInfo location = this.patrolLocationInfoService.getLocation(jobNum, byJobNum.getId(), campusNum);
				if(location!=null){
					out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(location,features)+"}");
				}else{
					out.print("{\"status\":\"false\",\"Code\":-2,\"Msg\":\"此人暂无巡逻信息\"}");
				}
			}else{
				out.println("{\"status\":\"false\",\"Code\":-2,\"Msg\":\"此人未开始巡逻\"}");
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

	/**
	 * 根据区域查询相关人员
	 * @param regionId 区域id
	 * @param response 
	 * @throws IOException
	 */
	@RequestMapping("regionCrew")
	public void regionCrew(Integer regionId,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		List<PatrolUserRegion> patrolUserRegions = patrolUserRegionService.getByProperty("regionId", regionId);
		if (patrolUserRegions != null && patrolUserRegions.size()>0) {
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUserRegions,features)+"}");
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无该区域巡查信息\"}");
		}
		out.flush();
		out.close();
	}
	/**
	 * 查看所有用户的最新定位点
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("regionCurrentCrew")
	public void regionCurrentCrew(HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		List<PatrolUser> allUsers = patrolUserService.getAll();
		List<PatrolUserRegion> patrolUsersRe = new ArrayList<PatrolUserRegion>();
		List<PatrolLocationInfo> patrolLocationInfos = new ArrayList<PatrolLocationInfo>();
		for (PatrolUser patrolUser : allUsers) {
			String jobNum = patrolUser.getJobNum();
			List<PatrolUserRegion> patrolUserRegions = patrolUserRegionService.getByProperty("jobNum", jobNum,"startTime",false);
			if (patrolUserRegions!=null && patrolUserRegions.size()>0) {
				PatrolUserRegion patrolUserRegion = patrolUserRegions.get(0);
				patrolUsersRe.add(patrolUserRegion);
			}
		}
		if (patrolUsersRe!=null && patrolUsersRe.size()>0) {
			for (PatrolUserRegion patrolUserRegion : patrolUsersRe) {
				List<PatrolLocationInfo> patrolLocationInfo = patrolLocationInfoService.getByProperty("usregId", patrolUserRegion.getId(), "timestamp", false);
				if (patrolLocationInfo!=null && patrolLocationInfo.size()>0) {
					patrolLocationInfos.add(patrolLocationInfo.get(0));
				}
			}

			//获取刷新时间



			if (patrolLocationInfos != null && patrolLocationInfos.size()>0) {
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolLocationInfos,features)+"}");
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无信息\"}");
			}
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无信息\"}");
		}
		out.flush();
		out.close();
	}

	/**
	 * 获取用户区域表
	 * @param usregId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getUserRegion")
	public void getUserRegion(Integer usregId,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		PatrolUserRegion patrolUserRegion = patrolUserRegionService.getById(usregId);
		if (patrolUserRegion != null) {
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUserRegion,features)+"}");
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无该区域巡查信息\"}");
		}
		out.flush();
		out.close();
	}
	/**
	 * 异常报警
	 * @throws IOException 
	 */
	@RequestMapping("abnormalAlarm")
	public void abnormalAlarm(Integer page,Integer pageSize,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PageBean<PatrolLocationInfo> list = this.patrolLocationInfoService.getAbnormal(pageSize,page);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list,features)+"}");
		} catch (IOException e) {
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}
	}

}
