package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolLocationInfoService;
import com.parkbobo.service.PatrolRegionService;
import com.parkbobo.service.PatrolUserRegionService;
import com.parkbobo.service.PatrolUserService;
import com.parkbobo.utils.GisUtils;
import com.vividsolutions.jts.geom.MultiPolygon;

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

	@Resource
	private PatrolLocationInfoService patrolLocationInfoService;
	/**
	 * 格式化json
	 */
	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};
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
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账号密码错误\"}");
		}
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
		if(username!=null){
			patrolUserRegion.setUsername(URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8"));
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户名不能为空\"}");
			return;
		}
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
		out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"patrolUserRegion\":"+JSONObject.toJSONString(patrolUserRegion)+",\"configId\":"+config.getId()+"}}");
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
		out.print("{\"status\":\"true\",\"Code\":1,\"data\":\""+JSONObject.toJSONString(patrolUserRegion)+"}");
	}
	
	/**
	 * 上传位置信息
	 * @param usregId  区域id
	 * @param configId 配置id
	 * @param userId 用户id
	 * @param jobNum 工号
	 * @param username 姓名
	 * @param patrolUserRegionId  用户区域id
	 * @param lon 经度
	 * @param lat 纬度
	 * @param campusNum  校区编号
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("uploadLocation")
	public void uploadLocation(Integer usregId,Integer configId,Integer userId,String jobNum,String username,Integer patrolUserRegionId,double lon,double lat,Integer campusNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Date date = new Date();
		PatrolConfig patrolConfig = this.patrolConfigService.getById(configId);
		PatrolLocationInfo patrolLocationInfo = new PatrolLocationInfo();
		patrolLocationInfo.setCampusNum(campusNum);
		patrolLocationInfo.setLon(lon);//经度
		patrolLocationInfo.setLat(lat);//纬度
		patrolLocationInfo.setUserId(userId);
		patrolLocationInfo.setUsername(username);
		patrolLocationInfo.setUsregId(usregId);
		patrolLocationInfo.setTimestamp(date);
		PatrolUserRegion patrolUserRegion = this.patrolUserRegionService.getById(patrolUserRegionId);
		if(patrolConfig.getIsEmergency()==1){
			//紧急状态
			if(this.patrolLocationInfoService.addRecord(patrolLocationInfo)){
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolLocationInfo)+"}");
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"添加巡查轨迹失败,请联系管理员\"}");
			}
		}else{
			//非紧急状态
			MultiPolygon regionLocation = this.patrolRegionService.getById(usregId).getRegionLocation();
			if(regionLocation.contains(GisUtils.createPoint(lon, lat))){
				//在巡逻区域内
				if(this.patrolLocationInfoService.isLazy(patrolConfig.getUploadTime(), userId, lon, lat, usregId)){
					//原地超过5分钟
					if(patrolUserRegion.getStatus()==1){
						//当前为正常状态
						this.patrolUserRegionService.switchUpload(patrolUserRegionId);
					}if(patrolUserRegion.getStatus()==2){
						//当前为异常状态
					}
					patrolLocationInfo.setExceptionType(1);
					patrolLocationInfo.setStatus(2);
				}else{
					//原地不超过五分钟
					if(patrolUserRegion.getStatus()==1){
						//当前为正常状态
					}if(patrolUserRegion.getStatus()==2){
						//当前为异常状态
						this.patrolUserRegionService.switchUpload(patrolUserRegionId);
					}
				}
			}else{
				//不在巡逻区域内
				//当前为正常状态
				if(patrolUserRegion.getStatus()==1){
					this.patrolUserRegionService.switchUpload(patrolUserRegionId);
				}
				patrolLocationInfo.setExceptionType(2);
				patrolLocationInfo.setStatus(2);
			}
		}
		this.patrolLocationInfoService.addRecord(patrolLocationInfo);
		out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolLocationInfo)+"}");
		out.flush();
		out.close();
	}
	/**
	 * 初始化区域信息
	 * @throws IOException 
	 */
	@RequestMapping("initRegion")
	public void getRegions(Integer campusNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		List<PatrolRegion> list = this.patrolRegionService.getByCampusNum(campusNum);
		out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list)+"}"); 
	}
	/**
	 * 获取签到时间集合
	 * @param startDate 日历第一天
	 * @param endDate 日历最后天
	 * @param jobNum 工号
	 * @param response 
	 * @throws IOException 
	 */
	@RequestMapping("getCalendar")
	public void getCalendar(String startDate, String endDate, String jobNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) && StringUtils.isNotBlank(jobNum)) {
			String hql = "from PatrolUserRegion where jobNum = '"+ jobNum +"' and startTime >= '"+ startDate +" 00:00:00' and endTime < '"+ endDate +" 00:00:00'";
			List<PatrolUserRegion> patrolUserRegions = patrolUserRegionService.getByHQL(hql);
			if (patrolUserRegions!=null && patrolUserRegions.size()>0) {
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUserRegions,features)+"}"); 
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无数据\"}"); 
			}
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"参数不完整\"}"); 
		}
		out.flush();
		out.close();
	}
	
}
