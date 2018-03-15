package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolRegionService;
import com.parkbobo.service.PatrolUserRegionService;
import com.parkbobo.service.PatrolUserService;

/**
 * 安防使用端接口
 * @author zj
 *@version 1.0
 */
@Controller
public class PatrolUserController {

	@Resource
	private PatrolUserService patrolUserService;

	@Resource
	private PatrolRegionService patrolRegionService;

	@Resource
	private PatrolConfigService patrolConfigService;

	@Resource
	private PatrolUserRegionService patrolUserRegionService;
	/**
	 * 登录
	 * @param jobNum 工号
	 * @param password 密码
	 * @return json
	 * @throws IOException 
	 */
	@RequestMapping("userLogin")
	public void userLogin(HttpServletResponse response,String jobNum,String password) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		PatrolUser patrolUser = this.patrolUserService.userLogin(jobNum, password);
		if(patrolUser != null){
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUser)+"}");
		}
		out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账号密码错误\"}");
	}
	/**
	 * 开始巡逻
	 * @param username  员工姓名
	 * @param regionId   区域id
	 * @param jobNum   工号
	 * @param campusNum 校区编号
	 * @return json
	 * @throws IOException 
	 */
	@RequestMapping("startPatrol")
	public void startPatrol(String  username,Integer regionId ,String jobNum,Integer campusNum,
			Integer uploadTime,Integer leaveRegionDistance,Integer startPatrolTime,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		PatrolUserRegion patrolUserRegion = new PatrolUserRegion();
		patrolUserRegion.setUsername(username);
		patrolUserRegion.setRegionId(regionId);
		patrolUserRegion.setJobNum(jobNum);
		Date date = new Date();
		patrolUserRegion.setStartTime(date);
		patrolUserRegion.setLastUpdateTime(date);
		PatrolConfig patrolConfig = new PatrolConfig();
		patrolConfig.setCampusNum(campusNum);
		patrolConfig.setLeaveRegionDistance(leaveRegionDistance);
		patrolConfig.setStartPatrolTime(startPatrolTime);
		patrolConfig.setUploadTime(uploadTime);
		PatrolConfig config = this.patrolConfigService.getConfig(patrolConfig);
		this.patrolUserRegionService.addRecord(patrolUserRegion);
		out.print("{\"status\":\"false\",\"Code\":1,\"data\":{\"patrolUserRegion\":"+JSONObject.toJSONString(patrolUserRegion)+",\"configId\":"+config.getId()+"}}");
	}
	/**
	 * 结束巡逻
	 * @param patrolRegion
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("endPatrol")
	public void endPatrol(Integer regionId,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Date date = new Date();
		PatrolUserRegion patrolUserRegion = this.patrolUserRegionService.getById(regionId);
		patrolUserRegion.setLastUpdateTime(date);
		patrolUserRegion.setEndTime(date);
		out.print("{\"status\":\"false\",\"Code\":1,\"data\":\""+JSONObject.toJSONString(patrolUserRegion)+"}");
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
	 * 上传位置信息
	 * @param patrolLocationInfo
	 * @return
	 */
	@RequestMapping("uploadLocation")
	public String uploadLocation(Integer usregId,Integer configId,Integer userId,String jobNum,String username,Integer patrolUserRegionId,double lon,double lat,Integer campusNum,HttpServletResponse response){
		PatrolConfig patrolConfig = this.patrolConfigService.getById(configId);
		PatrolLocationInfo patrolLocationInfo = new PatrolLocationInfo();
		patrolLocationInfo.setCampusNum(campusNum);
		patrolLocationInfo.setLat(lat);
		patrolLocationInfo.setLon(lon);
		patrolLocationInfo.setUserId(userId);
		patrolLocationInfo.setUsername(username);
		patrolLocationInfo.setUsregId(usregId);
		PatrolUserRegion patrolUserRegion = this.patrolUserRegionService.getById(patrolUserRegionId);
		if(patrolConfig.getIsEmergency()==1){
			
		}else{
			
		}

		return null;
	}
}
