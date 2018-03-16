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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolLocationInfoService;
import com.parkbobo.service.PatrolUserRegionService;
import com.parkbobo.service.PatrolUserService;

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
	
	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};
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
	
}
