package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.*;
import com.parkbobo.service.*;
import com.parkbobo.utils.GisUtils;
import com.parkbobo.utils.JPushClientExample;
import com.parkbobo.utils.PageBean;
import com.parkbobo.utils.message.MessageBean;
import com.system.utils.StringUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import static com.alibaba.fastjson.JSON.toJSONString;

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

	@Resource
	private PatrolExceptionService patrolExceptionService;

	@Resource
	private PatrolExceptionPushInfoService pushInfoService;

	@Resource
	private ExceptionPushService exceptionPushService;



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
	@RequestMapping("patrolUserLogin")
	public void userLogin(HttpServletResponse response,String jobNum,String password) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			PatrolUser patrolUser = this.patrolUserService.userLogin(jobNum, password);
			if(patrolUser != null){
				if(patrolUser.getIsDel()==1){
					out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账户已删除\"}");
					return;
				}
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+ toJSONString(patrolUser,features)+"}");
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账号密码错误\"}");
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
	 * 开始巡逻
	 * @param username  员工姓名
	 * @param regionId  区域id
	 * @param jobNum    工号
	 * @param campusNum 校区编号
	 * @throws IOException 
	 */
	@RequestMapping("startPatrol")
	public void startPatrol(String username,Integer regionId ,String jobNum,Integer campusNum,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PatrolUserRegion patrolUserRegion = new PatrolUserRegion();
			if(username!=null){
				patrolUserRegion.setUsername(username);
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户名不能为空\"}");
				return;
			}
			patrolUserRegion.setRegionId(regionId);
			patrolUserRegion.setJobNum(jobNum);
			Date date = new Date();
			patrolUserRegion.setStartTime(date);
			patrolUserRegion.setLastUpdateTime(date);
			patrolUserRegion.setStatus(1);
			patrolUserRegion.setAbnormalCount(0);
			//设置区域id
			patrolUserRegion.setCampusNum(campusNum);
			PatrolUserRegion byJobNum = this.patrolUserRegionService.getByJobNum(jobNum);
			if(byJobNum!=null) {
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"当前有未结束巡更记录\"}");
				return;
			}

			//TODO 这里需要进行推送，判断是否在巡更范围内，在范围内推送开始巡查，不在范围内推送巡查需要在指定时间内到达

			this.patrolUserRegionService.addRecord(patrolUserRegion);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+ toJSONString(patrolUserRegion,features)+"}");
		} catch (Exception e) {
			e.printStackTrace();
			if(out==null) {
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 结束巡逻
	 * @param jobNum
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("endPatrol")
	public void endPatrol(String jobNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Date date = new Date();
			List<PatrolUserRegion> list = this.patrolUserRegionService.getsByJobNum(jobNum);
			if(list!=null&&list.size()>0) {
				for(int i = 0; i < list.size();i++) {
					PatrolUserRegion patrolUserRegion = list.get(i);
					patrolUserRegion.setLastUpdateTime(date);
					patrolUserRegion.setEndTime(date);
					this.patrolUserRegionService.updateRecord(patrolUserRegion);
				}
			}
			out.print("{\"status\":\"true\",\"Code\":1,\"msg\":\"结束巡更成功!\"}");
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



	//TODO 上传位置信息
	/**
	 * 上传位置信息
	 * @param regionId  区域id
	 * @param configId 配置id
	 * @param jobNum 工号
	 * @param lon 经度
	 * @param lat 纬度
	 * @param campusNum  校区编号
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("uploadLocation_old")
	public void uploadLocation(Integer regionId,Integer configId,String jobNum,Double lon,Double lat,Integer campusNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("[{\"key\":\"Content-Type\",\"value\":\"application/json\",\"description\":\"\"}];charset=UTF-8");
		PrintWriter out = response.getWriter();
		PatrolUserRegion patrolUserRegion = null;
		//获取配置信息
		PatrolConfig patrolConfig = this.patrolConfigService.getById(configId);
		Date date = new Date();
		//查询当前在巡查的人
		List<PatrolUserRegion> list = this.patrolUserRegionService.getByHQL("from PatrolUserRegion where jobNum ='"+jobNum+"' and endTime is null order by startTime desc limit 1");
		//判断是否存在巡查人
		if(list!=null&&list.size()>0) {
			patrolUserRegion = list.get(0);
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未获取到巡逻信息\"}");
			return;
		}
		//进行判断是否进行上传经纬度
		if(lon==null||lat==null) {
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"此人已关闭定位\"}");
			JPushClientExample push = new JPushClientExample("4636b7d218171e7cf4a89e5c", "b4d131ecf1c2438fb492feac");
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "1");
			map.put("content","异常报告");
			push.sendMsg("异常报警", "用户"+patrolUserRegion.getUsername()+",工号:"+jobNum+",于"+date+"未上传有效定位信息", map);
			return;
		}
		//不在巡逻区域异常信息获取
		PatrolException patrolE1 = patrolExceptionService.get(1);
		//五分钟没有移动异常信息获取
		PatrolException patrolE2 = patrolExceptionService.get(2);
		//创建试试经纬度信息
		PatrolLocationInfo patrolLocationInfo = new PatrolLocationInfo();
		patrolLocationInfo.setCampusNum(campusNum);
		patrolLocationInfo.setLon(lon);//经度
		patrolLocationInfo.setLat(lat);//纬度
		patrolLocationInfo.setJobNum(jobNum);
		patrolLocationInfo.setUsregId(patrolUserRegion.getId());
		patrolLocationInfo.setTimestamp(date);
		patrolLocationInfo.setUsername(patrolUserRegion.getUsername());

		if(patrolConfig.getIsEmergency() == 1) {
			patrolLocationInfo = patrolLocationInfoService.add(patrolLocationInfo);
			//紧急状态
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"patrolLocationInfo\":"+ toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
			out.flush();
			out.close();
			return;
		}else{
			//非紧急状态
			if(date.getTime() - patrolUserRegion.getStartTime().getTime() >= patrolConfig.getStartPatrolTime() * 60 * 1000) {
				//超过允许到达时间
				Geometry regionLocation = patrolRegionService.getById(regionId).getRegionLocation();
				//进行距离判断
				Polygon polygon  = GisUtils.createCircle(lon, lat,patrolConfig.getLeaveRegionDistance(), 50);
				if(regionLocation.contains(polygon)) {
					//在巡逻区域内
					if(this.patrolUserRegionService.isLazy(patrolUserRegion,patrolConfig.getLazyTime())){
						//偷懒超过规定时间
						if(patrolUserRegion.getStatus() == 1) {
							//当前为正常状态
							patrolUserRegion.setStatus(2);
						}if(patrolUserRegion.getStatus() == 2) {
							//当前为异常状态
						}
						patrolLocationInfo.setPatrolException(patrolE2);
						patrolUserRegion.setPatrolException(patrolE1);
						patrolLocationInfo.setStatus(2);
						JPushClientExample push = new JPushClientExample("4636b7d218171e7cf4a89e5c", "b4d131ecf1c2438fb492feac");
						Map<String, String> map = new HashMap<String, String>();
						map.put("type", "1");
						map.put("content","异常报告");
						push.sendMsg("异常报警", "用户"+patrolUserRegion.getUsername() + ",工号:"+jobNum+",于"+date+"起,已超过"+patrolConfig.getLazyTime()+"分钟未更新巡逻信息", map);
					}else{
						//原地不超过规定时间
						if(patrolUserRegion.getStatus()==1) {
							//当前为正常状态
						}if(patrolUserRegion.getStatus()==2) {
							//当前为异常状态
							patrolUserRegion.setStatus(1);
						}
					}
				}else {
					//不在巡逻区域内
					Integer abnormalCount = patrolUserRegion.getAbnormalCount();
					if(abnormalCount>patrolConfig.getLeaveRegionTime()*60/patrolConfig.getUploadTime()) {
						//异常状态  报警(持续时间超过规定)
						if(patrolUserRegion.getStatus() == 1) {
							patrolUserRegion.setStatus(2);
						}
						patrolUserRegion.setPatrolException(patrolE1);
						patrolLocationInfo.setPatrolException(patrolE1);
						patrolLocationInfo.setStatus(2);
						JPushClientExample push = new JPushClientExample("4636b7d218171e7cf4a89e5c", "b4d131ecf1c2438fb492feac");
						Map<String, String> map = new HashMap<String, String>();
						map.put("type", "1");
						map.put("content","异常报告");
						push.sendMsg("异常报警", "用户"+patrolUserRegion.getUsername()+",工号:"+jobNum+",于"+date+"起,已超过"+patrolConfig.getLeaveRegionTime()+"分钟位于巡逻区域外", map);
						patrolUserRegion.setAbnormalCount(0);
					}else{
						//异常状态  不报警
						patrolLocationInfo.setPatrolException(patrolE1);
						patrolLocationInfo.setStatus(1);
						patrolUserRegion.setAbnormalCount(abnormalCount+1);
					}
				}
			}
			patrolUserRegion.setLastUpdateTime(date);
			this.patrolUserRegionService.merge(patrolUserRegion);
			patrolLocationInfo = patrolLocationInfoService.add(patrolLocationInfo);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"patrolLocationInfo\":"+ toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
			out.flush();
			out.close();
		}
	}

	/**
	 * 安防巡查上传经纬度接口
	 * @param regionId		区域id
	 * @param configId		配置id
	 * @param jobNum		工号
	 * @param lon			经度
	 * @param lat			纬度
	 * @param campusNum		区域id
	 * @param response
     * @throws Exception
     */
	@RequestMapping("/uploadLocation")
	public void uploadLocation_new(Integer regionId,Integer configId, String jobNum,Double lon, Double lat,
								   Integer campusNum, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		PatrolUserRegion patrolUserRegion = null;
		//进行获取配置信息
		if(StringUtil.isEmpty(jobNum) || configId == null || configId <= 0) {
			//设置默认配置
			configId = 1;
		}

		PatrolConfig patrolConfig = this.patrolConfigService.getById(configId);
		//查询当前巡查人员
		List<PatrolUserRegion> list = this.patrolUserRegionService.getByHQL("from PatrolUserRegion where jobNum ='"+jobNum+"' and endTime is null order by startTime desc limit 1");
		if(list != null && list.size() > 0) {
			patrolUserRegion = list.get(0);
		}else {
			//TODO 没有人员相关信息
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未获取到巡逻信息\"}");
			return;
		}
		//获取相关配置信息
		Integer emergencyStatus = patrolConfig.getIsEmergency();			//紧急状态
		Integer startPatrolTime = patrolConfig.getStartPatrolTime();		//开始时间
		Integer leaveDistance = patrolConfig.getLeaveRegionDistance();		//离开指定区域距离
		Integer leaveTime = patrolConfig.getLeaveRegionTime();				//离开区域范围时间
		Integer lossConfigTime = patrolConfig.getPersonnelLossTime();		//人员丢失时间
		Integer locationNotChangeTime = patrolConfig.getLazyTime();			//人员位置不进行变化
		Integer exceptionTime = patrolConfig.getExceptionPushTime();		//异常频率

		//是否紧急
		boolean isEmergency = this.patrolConfigService.isEmergency(emergencyStatus);
		//是否在制定时间内到达
		boolean isArriveTimeOn = this.patrolConfigService.isArriveTimeOn(startPatrolTime,patrolUserRegion.getStartTime());
		//是否离开巡逻区域超过指定距离
		boolean isLeaveDistance = this.patrolConfigService.isLeaveDistance(lon,lat,leaveDistance,regionId);
		//离开巡逻区域是否超过指定时间(这里需要根据是否在指定区域内时间重置时间)
		boolean isLeaveTime = this.patrolConfigService.isLeaveTime(lon,lat,regionId,leaveTime,patrolUserRegion,true);
		//人员丢失位置判断
		boolean isLoss = this.patrolConfigService.isLoseTime(patrolUserRegion,lossConfigTime);
		//人员位置不变判断(这里需要进行重置时间)
		boolean isNotChange = this.patrolConfigService.isLocationNotChange(lon,lat,patrolUserRegion,locationNotChangeTime);
		//是否能进行推送（这里的推送判断是增加了巡查开始时间限制的）
		boolean isCanPush = this.patrolConfigService.isArrivePushTime(patrolUserRegion,exceptionTime,startPatrolTime);
		//是否进行推送，这里是给使用端进行推送时的判断的，这个没有加入前几分钟的限制
		boolean isCanPushUse = this.patrolConfigService.isArrivePushTimeNot(patrolUserRegion,exceptionTime);


		//创建试试经纬度信息
		PatrolLocationInfo patrolLocationInfo = new PatrolLocationInfo();
		Date date = new Date();
		patrolLocationInfo.setCampusNum(campusNum);
		patrolLocationInfo.setLon(lon);//经度
		patrolLocationInfo.setLat(lat);//纬度
		patrolLocationInfo.setJobNum(jobNum);
		patrolLocationInfo.setUsregId(patrolUserRegion.getId());
		patrolLocationInfo.setTimestamp(date);
		patrolLocationInfo.setUsername(patrolUserRegion.getUsername());

		//设置上传时间
		patrolUserRegion.setLastUpdateTime(new Date());

		String adminUserIdsStr = exceptionPushService.getPartrolAdminUserId("2");

		//信息推送对象
		PatrolExceptionPushInfo pushInfo = new PatrolExceptionPushInfo();
		String title = "异常推送";
		String content = "";

		if(isEmergency) {			//紧急状态
			patrolLocationInfo = patrolLocationInfoService.add(patrolLocationInfo);
			out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
					+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
					toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
			out.flush();
			out.close();
			return;
		}else {						//非紧急状态,进行相关判定
			PatrolException exception = new PatrolException();
			if(!patrolUserRegion.isArrive()) {
				if(isArriveTimeOn) {	//是否到达指定地点
					//到达
					System.out.println("到达时间判断");
					if(!isLeaveDistance) {
						//在巡逻区域内
						patrolUserRegion.setArrive(true);
						this.patrolUserRegionService.updateRecord(patrolUserRegion);
						System.out.println("到达距离判断");
					}
					if(isCanPushUse) {
						exceptionPushService.pushUsePatrolSend("3", "异常推送",
								"开始巡更，请在" + startPatrolTime + "分钟内，到达指定区域！", jobNum);
						patrolUserRegion.setExceptionPushTime(new Date());
						//这里跟新异常推送时间
						patrolUserRegionService.updateRecord(patrolUserRegion);
					}
				}else if(!isLeaveDistance) {
					//到达指定区域，进行修改状态(到达则进行修改状态)
					patrolUserRegion.setArrive(true);
					this.patrolConfigService.updateTimeAndStatus(patrolUserRegion,patrolLocationInfo);
					out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
							+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
							toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
					out.flush();
					out.close();
					return;
				}else {
					//未到达
					System.out.println("未到达");
					//这里进行保存
					exception = patrolConfigService.exceptionAssembly(2);
					patrolLocationInfo.setPatrolException(exception);
					patrolLocationInfo.setStatus(2);
					patrolLocationInfoService.add(patrolLocationInfo);
					//更新异常状态
					patrolUserRegion.setPatrolException(exception);
					patrolUserRegion.setStatus(2);
					//异常推送记录
					pushInfo.setExceptionDate(new Date());
					//进行推送消息
					if(isCanPush) {
						//TODO 进行消息推送
						content = "巡查人:" + patrolUserRegion.getUsername() + ",巡查开始未在指定时间" + startPatrolTime + "分钟内到达指定位置";
						pushInfo.setCampusNum(campusNum);
						pushInfo.setJobNum(jobNum);
						pushInfo.setLon(lon);
						pushInfo.setLat(lat);
						pushInfo.setPushDate(new Date());
						pushInfo.setUserName(patrolUserRegion.getUsername());
						pushInfo.setUsregId(patrolUserRegion.getId());
						pushInfo.setExceptionType(exception.getType());
						pushInfo.setExceptionName(content);
						pushInfo.setStatus(2);
						pushInfo.setExceptionId(exception.getId());
						pushInfoService.addExceptionPushInfo(pushInfo);
						exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
						exceptionPushService.pushUsePatrolSend("3","异常推送",
								"请在" + startPatrolTime +"分钟内，到达指定位置",jobNum);
						patrolUserRegion.setExceptionPushTime(new Date());
						//进行保存异常信息到异常信息表中
						patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum);

					}
					//进行
					patrolUserRegionService.updateRecord(patrolUserRegion);
					out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
							+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
							toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
					out.flush();
					out.close();
					return;
				}
			}

			if(isLeaveDistance) {		//是否离开巡逻区域超过指定距离
				//离开巡逻区域
				System.out.println("离开巡逻区域");
				exception = patrolConfigService.exceptionAssembly(1);
				patrolLocationInfo.setPatrolException(exception);
				patrolLocationInfo.setStatus(2);
				patrolLocationInfoService.add(patrolLocationInfo);
				//更新异常状态
				patrolUserRegion.setPatrolException(exception);
				patrolUserRegion.setStatus(2);
				//推送异常时间设置
				pushInfo.setExceptionDate(new Date());
				//进行推送消息
				if(isCanPush) {
					//TODO 进行消息推送
					content = "巡查人:"+patrolUserRegion.getUsername()+",离开巡逻区域超过指定距离"+leaveDistance+"米";
					pushInfo.setCampusNum(campusNum);
					pushInfo.setJobNum(jobNum);
					pushInfo.setLon(lon);
					pushInfo.setLat(lat);
					pushInfo.setUserName(patrolUserRegion.getUsername());
					pushInfo.setUsregId(patrolUserRegion.getId());
					pushInfo.setPushDate(new Date());
					pushInfo.setExceptionType(exception.getType());
					pushInfo.setExceptionName(content);
					pushInfo.setStatus(2);
					pushInfo.setExceptionId(exception.getId());
					pushInfoService.addExceptionPushInfo(pushInfo);
					exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
					exceptionPushService.pushUsePatrolSend("3","异常推送：",
							"您离开巡逻区域超过指定距离" + leaveDistance + "米,请尽快回到指定区域！",jobNum);
					patrolUserRegion.setExceptionPushTime(new Date());
					//将异常信息记录在ExceptionInfo
					patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum);
				}
				patrolUserRegionService.updateRecord(patrolUserRegion);
				out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
						+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
						toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
				out.flush();
				out.close();
				return;
			}

			if(isLeaveTime) {		//离开巡逻区域是否超过指定时间
				//超过指定时间
				System.out.println("超过指定时间");
				exception = patrolConfigService.exceptionAssembly(3);
				patrolLocationInfo.setPatrolException(exception);
				patrolLocationInfo.setStatus(2);
				patrolLocationInfoService.add(patrolLocationInfo);
				//更新异常状态
				patrolUserRegion.setPatrolException(exception);
				patrolUserRegion.setStatus(2);
				//消息推送异常时间设置
				pushInfo.setExceptionDate(new Date());
				//进行推送消息
				if(isCanPush) {
					//TODO 进行消息推送
					content = "巡查人:"+patrolUserRegion.getUsername()+",离开巡逻区域超过指定时间"+leaveTime+"分钟";
					pushInfo.setCampusNum(campusNum);
					pushInfo.setJobNum(jobNum);
					pushInfo.setLon(lon);
					pushInfo.setLat(lat);
					pushInfo.setUserName(patrolUserRegion.getUsername());
					pushInfo.setUsregId(patrolUserRegion.getId());
					pushInfo.setPushDate(new Date());
					pushInfo.setExceptionType(exception.getType());
					pushInfo.setExceptionName(content);
					pushInfo.setStatus(2);
					pushInfo.setExceptionId(exception.getId());
					pushInfoService.addExceptionPushInfo(pushInfo);
					exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
					exceptionPushService.pushUsePatrolSend("3","异常推送：",
							"您离开指定区域超过规定时间" + leaveTime +"分钟，请尽快回到指定区域",jobNum);
					patrolUserRegion.setExceptionPushTime(new Date());
					//将异常信息记录在ExceptionInfo
					patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum);
				}
				patrolUserRegionService.updateRecord(patrolUserRegion);
				out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
						+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
						toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
				out.flush();
				out.close();
				return;
			}


			if(isLoss) {			//人员丢失位置判断
				//人员丢失
				System.out.println("人员丢失");
				exception = patrolConfigService.exceptionAssembly(4);
				patrolLocationInfo.setPatrolException(exception);
				patrolLocationInfo.setStatus(2);
				patrolLocationInfoService.add(patrolLocationInfo);
				//更新异常状态
				patrolUserRegion.setPatrolException(exception);
				patrolUserRegion.setStatus(2);
				//消息推送异常时间设置
				//TODO 获取上次异常时间
				pushInfo.setExceptionDate(new Date());
				//进行推送消息
				if(isCanPush) {
					//TODO 进行消息推送
					content = "巡查人:"+patrolUserRegion.getUsername()+",人员位置丢失,超过指定时间未上传经纬度"+lossConfigTime+"分钟";
					pushInfo.setCampusNum(campusNum);
					pushInfo.setJobNum(jobNum);
					pushInfo.setLon(lon);
					pushInfo.setLat(lat);
					pushInfo.setUserName(patrolUserRegion.getUsername());
					pushInfo.setUsregId(patrolUserRegion.getId());
					pushInfo.setPushDate(new Date());
					pushInfo.setExceptionType(exception.getType());
					pushInfo.setExceptionName(content);
					pushInfo.setStatus(2);
					pushInfo.setExceptionId(exception.getId());
					pushInfoService.addExceptionPushInfo(pushInfo);
					exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
					exceptionPushService.pushUsePatrolSend("3","异常推送：","已检测不到您的位置",jobNum);
					patrolUserRegion.setExceptionPushTime(new Date());
					//将异常信息记录在ExceptionInfo
					patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum);
				}
				patrolUserRegionService.updateRecord(patrolUserRegion);
				out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
						+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
						toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
				out.flush();
				out.close();
				return;
			}

			if(isNotChange) {			//人员位置是否改变
				//未改变
				System.out.println("位置没有改变");
				exception = patrolConfigService.exceptionAssembly(5);
				patrolLocationInfo.setPatrolException(exception);
				patrolLocationInfo.setStatus(2);
				patrolLocationInfoService.add(patrolLocationInfo);
				//更新异常状态
				patrolUserRegion.setPatrolException(exception);
				patrolUserRegion.setStatus(2);
				//进行推送消息
				if(isCanPush) {
					//TODO 进行消息推送
					content = "巡查人:"+patrolUserRegion.getUsername()+",巡查人员经纬度信息没有发生变化,"+locationNotChangeTime+"分钟";
					pushInfo.setCampusNum(campusNum);
					pushInfo.setJobNum(jobNum);
					pushInfo.setLon(lon);
					pushInfo.setLat(lat);
					pushInfo.setUserName(patrolUserRegion.getUsername());
					pushInfo.setUsregId(patrolUserRegion.getId());
					pushInfo.setExceptionDate(new Date());
					pushInfo.setExceptionType(exception.getType());
					pushInfo.setExceptionName(content);
					pushInfo.setStatus(2);
					pushInfo.setPushDate(new Date());
					pushInfo.setExceptionId(exception.getId());
					pushInfoService.addExceptionPushInfo(pushInfo);
					exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
					patrolUserRegion.setExceptionPushTime(new Date());
					//将异常信息记录在ExceptionInfo
					patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum);
				}
				patrolUserRegionService.updateRecord(patrolUserRegion);
				out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
						+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
						toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
				out.flush();
				out.close();
				return;
			}

			//进行更新状态
			patrolConfigService.updateTimeAndStatus(patrolUserRegion,patrolLocationInfo);


			out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
					+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
					toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
			out.flush();
			out.close();
			return;
		}
	}





	/**
	 * 初始化区域信息
	 * @param campusNum 校区id
	 * @throws IOException 
	 */
	@RequestMapping("initRegion")
	public void getRegions(Integer campusNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String hql = "FROM PatrolRegion WHERE isDel=0 AND campusNum=" + campusNum;
			List<PatrolRegion> list = this.patrolRegionService.getByHQL(hql);
			PatrolConfig patrolConfig = this.patrolConfigService.getById(1);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"patrolRegion\":"+JSONObject.toJSONString(list,features)+",\"isEmergency\":"+JSONObject.toJSONString(patrolConfig.getIsEmergency(),features)+"}}");
		} catch (Exception e) {
			//e.printStackTrace();
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
	 * 获取巡逻时间
	 * @param jobNum 工号
	 * @throws IOException 
	 */
	@RequestMapping("countTime")
	public void getCountTime(String jobNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PatrolUserRegion patrolUserRegion = this.patrolUserRegionService.getCountTime(jobNum);
			long countTime = 0;
			if(patrolUserRegion!=null) {
				countTime = new Date().getTime() - patrolUserRegion.getStartTime().getTime();
			}
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"PatrolUserRegion\":"+ toJSONString(patrolUserRegion,features)+",\"countTime\":"+countTime+"}}");
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
			String sql = "SELECT job_num, to_char(start_time, 'yyyy-MM-dd') FROM patrol_user_region WHERE "+
					"job_num = '"+jobNum+"' AND start_time >= '"+startDate+"' AND end_time < '"+endDate+"'"+
					"GROUP BY job_num,to_char(start_time,'yyyy-MM-dd')";
			List<Object[]> patrolUserRegions = patrolUserRegionService.getBySql(sql);
			StringBuilder sb =new StringBuilder();
			sb.append("{\"status\":\"true\",\"Code\":1,\"list\":[");
			if(patrolUserRegions!=null && patrolUserRegions.size()>0){
				int i = 0;
				for (Object[] object : patrolUserRegions) {
					sb.append("{");
					sb.append("\"date\":\""+object[1]+"\"");
					if(i==patrolUserRegions.size()-1){
						sb.append("}");
					}else{
						sb.append("},");
					}
					i++;
				}
			}
			sb.append("]}");
			out.print(sb.toString()); 
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"参数不完整\"}"); 
		}
		out.flush();
		out.close();
	}


	/**
	 * 获取安防巡查异常推送列表信息
	 * @param campusNum		校区id
	 * @param pageSize		当前页大小
	 * @param page			当前页
     */
	@RequestMapping("/getPatrolExceptionList")
	public void getPatrolExceptionList(Integer campusNum,int pageSize,int page,HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		MessageBean messageBean = new MessageBean();
		try {
			out = response.getWriter();
			String hql = "FROM PatrolExceptionPushInfo";
			if(campusNum != null && campusNum > 0) {
				hql += " WHERE campusNum=" + campusNum + " ORDER BY pushDate DESC";
			}else {
				messageBean.setCode(200);
				messageBean.setStatus(false);
				messageBean.setMessage("参数不能为空");
				out.write(JSON.toJSONString(messageBean));
				return;
			}
			PageBean<PatrolExceptionPushInfo> pushInfoPageBean = pushInfoService.getExceptionPushInfo(hql,pageSize,page);
			messageBean.setData(pushInfoPageBean);
			messageBean.setCode(200);
			messageBean.setStatus(true);
			messageBean.setMessage("success");
			out.write(JSON.toJSONString(messageBean));
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}


	}



//	@RequestMapping("/pushInfo")
//	public void test() {
//		PatrolExceptionPushInfo info = new PatrolExceptionPushInfo();
//		info.setCampusNum(1);
//		info.setStatus(2);
//		info.setExceptionId(1);
//		info.setExceptionName("异常");
//		info.setExceptionType(1);
//		info.setJobNum("123");
//		info.setLat(34);
//		info.setLon(110);
//		info.setUserName("张三");
//		info.setUsregId(234);
//		pushInfoService.addExceptionPushInfo(info);
//
//	}


//	@RequestMapping("/requestTest")
//	public void test() {
//		FirePatrolEquipmentStatusVO equipmentStatusVO = new FirePatrolEquipmentStatusVO();
//		equipmentStatusVO.setDeviceId("10078");
//		equipmentStatusVO.setDeviceStatus("0");
//		equipmentStatusVO.setPatrolUser("张三");
//		firePatrolEquipmentSychService.updateFirePatrolEquipmentVOStatus(equipmentStatusVO);
//
//	}

	//进行测试
//	@RequestMapping("/testPush")
//	public void testPush() {
//		patrolConfigService.isLeaveDistance(113,34,40,5);
//	}

}
