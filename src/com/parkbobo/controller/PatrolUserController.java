package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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

	@Resource
	private PatrolPauseService patrolPauseService;

	@Resource
	private PatrolSignRecordService patrolSignRecordService;

    @Resource
    private PatrolHelpMessageService patrolHelpMessageService;

    @Resource
    private PatrolSignPointInfoService patrolSignPointInfoService;



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
		byte[] b= new byte[0];
		if(jobNum!=null){
			b=jobNum.getBytes("ISO_8859-1");
		}
		String jobNum1=new String(b,"UTF-8");
		if(password!=null){
			b=password.getBytes("ISO_8859-1");
		}
		String password1=new String(b,"UTF-8");
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			PatrolUser patrolUser = this.patrolUserService.userLogin(jobNum1, password1);
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
        byte[] b= new byte[0];
        if(jobNum!=null){
            b=jobNum.getBytes("ISO_8859-1");
        }
        String jobNum1=new String(b,"UTF-8");
        if(username!=null){
            b=username.getBytes("ISO_8859-1");
        }
        String username1=new String(b,"UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			PatrolUserRegion patrolUserRegion = new PatrolUserRegion();
			if(username1!=null){
				patrolUserRegion.setUsername(username1);
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户名不能为空\"}");
				return;
			}
			patrolUserRegion.setRegionId(regionId);
			patrolUserRegion.setJobNum(jobNum1);
			Date date = new Date();
			patrolUserRegion.setStartTime(date);
			patrolUserRegion.setLastUpdateTime(date);
			patrolUserRegion.setStatus(1);
			patrolUserRegion.setAbnormalCount(0);
			//设置区域id
			patrolUserRegion.setCampusNum(campusNum);
			PatrolUserRegion byJobNum = this.patrolUserRegionService.getByJobNum(jobNum1);
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
        byte[] b= new byte[0];
        if(jobNum!=null){
            b=jobNum.getBytes("ISO_8859-1");
        }
        String jobNum1=new String(b,"UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();
			Date date = new Date();
			PatrolSignRecord patrolSignRecord=new PatrolSignRecord();
			List<PatrolUserRegion> list = this.patrolUserRegionService.getsByJobNum(jobNum1);
			//看是否有未结束的巡逻
			if(list!=null&&list.size()>0) {
				for(int i = 0; i < list.size();i++) {
					PatrolUserRegion patrolUserRegion = list.get(i);
					patrolUserRegion.setLastUpdateTime(date);
					patrolUserRegion.setEndTime(date);
					this.patrolUserRegionService.updateRecord(patrolUserRegion);
					//向巡更记录中添加一条巡更记录
					patrolSignRecord.setJobNum(patrolUserRegion.getJobNum());
					patrolSignRecord.setUsername(patrolUserRegion.getUsername());
					patrolSignRecord.setSignTime(date);
					patrolSignRecord.setSignType(2);
					PatrolSignPointInfo patrolSignPointInfo=new PatrolSignPointInfo();
					patrolSignPointInfo.setPointId(patrolUserRegion.getRegionId());

					patrolSignRecord.setPatrolSignPointInfo(patrolSignPointInfo);
                    //定位版是直接用签到版的调过来的，所以下面有点瑕疵，用merge只是为了不报错
                    patrolSignPointInfo.setUpdateTime(new Date());
                    PatrolRegion patrolRegion=new PatrolRegion();
                    patrolRegion.setId(patrolUserRegion.getRegionId());
                    patrolSignPointInfo.setPatrolRegion(patrolRegion);
                    if(patrolSignPointInfoService.get(patrolUserRegion.getRegionId())==null){
                        patrolSignPointInfoService.add(patrolSignPointInfo);
                        String sql="update patrol_sign_point_info set point_id = "+patrolUserRegion.getRegionId()+"where id = "+patrolUserRegion.getRegionId();
                        patrolSignPointInfoService.updateBySql(sql);
                    }
                    patrolSignPointInfo=patrolSignPointInfoService.get(patrolUserRegion.getRegionId());
                    patrolSignRecord.setPatrolSignPointInfo(patrolSignPointInfo);
					patrolSignRecordService.add(patrolSignRecord);
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
	@Deprecated
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
	/*定位版*/
	@RequestMapping("/uploadLocation")
	public void uploadLocation_new(Integer regionId,Integer configId, String jobNum,Double lon, Double lat,
								   Integer campusNum, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		PatrolUserRegion patrolUserRegion = null;
		byte[] b= new byte[0];
		if(jobNum!=null){
			b=jobNum.getBytes("ISO_8859-1");
		}
		String jobNum1=new String(b,"UTF-8");
		//进行获取配置信息
		if(StringUtil.isEmpty(jobNum1) || configId == null || configId <= 0) {
			//设置默认配置
			configId = 1;
		}

		PatrolConfig patrolConfig = this.patrolConfigService.getById(configId);
		//查询当前巡查人员
		List<PatrolUserRegion> list = this.patrolUserRegionService.getByHQL("from PatrolUserRegion where jobNum ='"+jobNum1+"' and endTime is null order by startTime desc limit 1");
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
		//是否在指定时间内到达
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
		// 是否暂停
		boolean isPause = patrolPauseService.checkPauseStatus() == null ? false : true;


		if(patrolConfig.getLazyTime()==null){
			isNotChange=false;
		}
		if(patrolConfig.getLeaveRegionDistance()==null){
			isLeaveDistance=false;
		}
		if(patrolConfig.getLeaveRegionTime()==null){
			isLeaveTime=false;
		}



		//创建实时经纬度信息
		PatrolLocationInfo patrolLocationInfo = new PatrolLocationInfo();
		Date date = new Date();
		patrolLocationInfo.setCampusNum(campusNum);
		patrolLocationInfo.setLon(lon);//经度
		patrolLocationInfo.setLat(lat);//纬度
		patrolLocationInfo.setJobNum(jobNum1);
		patrolLocationInfo.setUsregId(patrolUserRegion.getId());
		patrolLocationInfo.setTimestamp(date);
		patrolLocationInfo.setUsername(patrolUserRegion.getUsername());

		//设置上传时间
		patrolUserRegion.setLastUpdateTime(new Date());

//		String adminUserIdsStr = exceptionPushService.getPartrolAdminUserId("2");
		String adminUserIdsStr = "all";

		//信息推送对象
		PatrolExceptionPushInfo pushInfo = new PatrolExceptionPushInfo();
		String title = "异常推送";
		String content = "";

		if(isEmergency || isPause) {			//紧急状态以及暂停

			//查询最新的一条定位数据信息
			PatrolLocationInfo locationInfo= patrolLocationInfoService.getLocation(patrolLocationInfo.getJobNum(),patrolLocationInfo.getUsregId(),patrolLocationInfo.getCampusNum());
			//最新的一条数据与现在上传的时间差
			Long xcDate=null;
			if(locationInfo!=null){
				xcDate=patrolLocationInfo.getTimestamp().getTime()-locationInfo.getTimestamp().getTime();
			}
			//如果配置的定位数据上传周期为空，则直接上传，不为空则需要判断
			if(patrolConfig.getSignRange()==null){
				patrolLocationInfo =patrolLocationInfoService.add(patrolLocationInfo);
			}
			else{
				if(locationInfo==null){
					patrolLocationInfo =patrolLocationInfoService.add(patrolLocationInfo);
				}
				else if(xcDate>=patrolConfig.getSignRange()*1000){
					patrolLocationInfo =patrolLocationInfoService.add(patrolLocationInfo);
				}
			}
			out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
					+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
					toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
			out.flush();
			out.close();
			return;
		} else{						//非紧急状态,进行相关判定
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

						exceptionPushService.pushUsePatrolSend("3", "提醒",
								"开始巡更，请在" + startPatrolTime + "分钟内，到达指定区域！", jobNum1);
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
						pushInfo.setJobNum(jobNum1);
						pushInfo.setLon(lon);
						pushInfo.setLat(lat);
						pushInfo.setPushDate(new Date());
						pushInfo.setUserName(patrolUserRegion.getUsername());
						pushInfo.setUsregId(patrolUserRegion.getId());
						pushInfo.setUsregName(patrolRegionService.getById(regionId).getRegionName());
						pushInfo.setExceptionType(exception.getType());
						pushInfo.setExceptionName(content);
						pushInfo.setStatus(2);
						pushInfo.setExceptionId(exception.getId());
						pushInfoService.addExceptionPushInfo(pushInfo);
						exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
						//需要账号 再处理一下
//						exceptionPushService.pushUsePatrolSend("3","异常推送",
//								"请在" + startPatrolTime +"分钟内，到达指定位置",jobNum1);
						exceptionPushService.pushUsePatrolSend("3","异常推送",
								"请在" + startPatrolTime +"分钟内，到达指定位置",jobNum1);
						patrolUserRegion.setExceptionPushTime(new Date());
						//进行保存异常信息到异常信息表中
						patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum1);

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


				//这里进行重新计算是否能进行推送
				isCanPush = this.patrolConfigService.isArrivePushTime(patrolUserRegion,exceptionTime,startPatrolTime);
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
					pushInfo.setJobNum(jobNum1);
					pushInfo.setLon(lon);
					pushInfo.setLat(lat);
					pushInfo.setUserName(patrolUserRegion.getUsername());
					pushInfo.setUsregId(patrolUserRegion.getId());
					pushInfo.setUsregName(patrolRegionService.getById(regionId).getRegionName());
					pushInfo.setPushDate(new Date());
					pushInfo.setExceptionType(exception.getType());
					pushInfo.setExceptionName(content);
					pushInfo.setStatus(2);
					pushInfo.setExceptionId(exception.getId());
					pushInfoService.addExceptionPushInfo(pushInfo);
					exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
					exceptionPushService.pushUsePatrolSend("3","异常推送：",
							"您离开巡逻区域超过指定距离" + leaveDistance + "米,请尽快回到指定区域！",jobNum1);
					patrolUserRegion.setExceptionPushTime(new Date());
					//将异常信息记录在ExceptionInfo
					patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum1);
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
					pushInfo.setJobNum(jobNum1);
					pushInfo.setLon(lon);
					pushInfo.setLat(lat);
					pushInfo.setUserName(patrolUserRegion.getUsername());
					pushInfo.setUsregId(patrolUserRegion.getId());
					pushInfo.setUsregName(patrolRegionService.getById(regionId).getRegionName());
					pushInfo.setPushDate(new Date());
					pushInfo.setExceptionType(exception.getType());
					pushInfo.setExceptionName(content);
					pushInfo.setStatus(2);
					pushInfo.setExceptionId(exception.getId());
					pushInfoService.addExceptionPushInfo(pushInfo);
					exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
					exceptionPushService.pushUsePatrolSend("3","异常推送：",
							"您离开指定区域超过规定时间" + leaveTime +"分钟，请尽快回到指定区域",jobNum1);
					patrolUserRegion.setExceptionPushTime(new Date());
					//将异常信息记录在ExceptionInfo
					patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum1);
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
					pushInfo.setJobNum(jobNum1);
					pushInfo.setLon(lon);
					pushInfo.setLat(lat);
					pushInfo.setUserName(patrolUserRegion.getUsername());
					pushInfo.setUsregId(patrolUserRegion.getId());
					pushInfo.setUsregName(patrolRegionService.getById(regionId).getRegionName());
					pushInfo.setPushDate(new Date());
					pushInfo.setExceptionType(exception.getType());
					pushInfo.setExceptionName(content);
					pushInfo.setStatus(2);
					pushInfo.setExceptionId(exception.getId());
					pushInfoService.addExceptionPushInfo(pushInfo);
					exceptionPushService.pushSend("2",title,content,adminUserIdsStr);
					exceptionPushService.pushUsePatrolSend("3","异常推送：","已检测不到您的位置",jobNum1);
					patrolUserRegion.setExceptionPushTime(new Date());
					//将异常信息记录在ExceptionInfo
					patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum1);
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
					pushInfo.setJobNum(jobNum1);
					pushInfo.setLon(lon);
					pushInfo.setLat(lat);
					pushInfo.setUserName(patrolUserRegion.getUsername());
					pushInfo.setUsregId(patrolUserRegion.getId());
					pushInfo.setUsregName(patrolRegionService.getById(regionId).getRegionName());
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
					patrolExceptionService.addExceptionInfo(exception.getType(),patrolUserRegion.getId(),patrolUserRegion.getUsername(),jobNum1);
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


    /*签到版*/
	@RequestMapping("/signUploadLocation")
	public void signUploadLocation_new(Integer regionId,Integer configId, String jobNum,Double lon, Double lat,
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
		// 是否暂停
		boolean isPause = patrolPauseService.checkPauseStatus() == null ? false : true;


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

		if(isEmergency || isPause) {			//紧急状态以及暂停
			patrolLocationInfo = patrolLocationInfoService.add(patrolLocationInfo);
			out.print("{\"status\":\"true\",\"Code\":1,\"uploadTime\":"
					+ patrolConfig.getUploadTime() +",\"data\":{\"patrolLocationInfo\":"+
					toJSONString(patrolLocationInfo,features)+",\"patrolConfig\":"+ toJSONString(patrolConfig,features)+"}}");
			out.flush();
			out.close();
			return;
		} else{						//非紧急状态,进行相关判定
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


				//这里进行重新计算是否能进行推送
				isCanPush = this.patrolConfigService.isArrivePushTime(patrolUserRegion,exceptionTime,startPatrolTime);
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
        byte[] b= new byte[0];
        if(jobNum!=null){
            b=jobNum.getBytes("ISO_8859-1");
        }
        String jobNum1=new String(b,"UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PatrolUserRegion patrolUserRegion = this.patrolUserRegionService.getCountTime(jobNum1);
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
		byte[] b = new byte[0];
		if (jobNum != null)
			b = jobNum.getBytes("ISO-8859-1");
		String jobNum1 = new String(b, "utf-8");

		PrintWriter out = response.getWriter();
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) && StringUtils.isNotBlank(jobNum1)) {
			String sql = "SELECT job_num, to_char(start_time, 'yyyy-MM-dd') FROM patrol_user_region WHERE "+
					"job_num = '"+jobNum1+"' AND start_time >= '"+startDate+"' AND end_time < '"+endDate+"'"+
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
			if(campusNum !=null ) {
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






	/**
	 * 安防求救
	 * @param lon 经度
	 * @param lat 纬度
	 * @param userCode
	 * @param userName
	 */
	@RequestMapping("/pushInfoHelp")
	public void pushInfoHelp(Double lon, Double lat,Integer campusNum,String userCode,String userName,HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		PatrolExceptionPushInfo info = new PatrolExceptionPushInfo();

        PatrolHelpMessage patrolHelpMessage=new PatrolHelpMessage();
        PrintWriter out = null;
		try{
            out = response.getWriter();
            String title="求救信号";
            String content="有学生报警，请尽快赶往事发地点！";
//		String adminUserIdsStr = exceptionPushService.getPartrolAdminUserId("2");
            //到时候需要获取一下管理端的账号，目前传的是all，广播
            String hql="from PatrolUser where 1=1";
            hql+="and isDel = 0";
            hql+="and isJpush = 0";
            List<PatrolUser> list= patrolUserService.getByHQL(hql);
            String ids="";
            if(list.size()>0&list!=null){
                for(PatrolUser patrolUser : list){
                    ids+=patrolUser.getJobNum();
                    ids+=",";

                }
            }

            String adminUserIdsStr =ids;
            //因为这个接口是求救专用接口，所以这个type没啥特殊意义
            String type="1";

            patrolHelpMessage.setUserCode(userCode);
            patrolHelpMessage.setUserName(userName);
            patrolHelpMessage.setLat(lat);
            patrolHelpMessage.setLon(lon);
            patrolHelpMessage.setHelpTime(new Date());
            patrolHelpMessage.setIsRead(0);
            patrolHelpMessage.setCampusNum(campusNum);
            patrolHelpMessageService.add(patrolHelpMessage);
            exceptionPushService.pushSendHelp(patrolHelpMessage.getId(),title,content,userCode,userName,lon,lat,type,adminUserIdsStr);
            out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolHelpMessage,features)+"}");
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
     * 列表
     * @return
     */
    @RequestMapping("/patrolJpushList")
    public void patrolJpushList(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        MessageBean messageBean = new MessageBean();
        try {
            out = response.getWriter();
            String hql = "from  PatrolUser  where 1=1";
            hql += "and isDel = 0";
            hql += " order by lastUpdateTime desc";
            List<PatrolUser> patrolUsers=patrolUserService.getByHQL(hql);
            messageBean.setData(patrolUsers);
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

    /**
     * 修改
     * @param response
     * @throws IOException
     */
    @RequestMapping("/updateIsJpush")
    public void updateIsJpush(String ids,HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            List<PatrolUser> list=patrolUserService.getAllUser();
            for(PatrolUser patrolUser :list){
                patrolUser.setIsJpush(0);
                patrolUserService.update(patrolUser);
            }
            if(ids!=null){
                String[] idArray = ids.split(",");
                for(String str : idArray){
                    PatrolUser patrolUser=patrolUserService.getById(Integer.parseInt(str));
                    patrolUser.setIsJpush(1);
                    patrolUserService.update(patrolUser);
                }
                out.print("{\"status\":\"true\",\"Code\":1,\"msg\":\"修改成功!\"}");
            }else{
                out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"修改失败！\"}");
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
     * 获取求救信息列表
     * @param pageSize
     * @param page
     * @param response
     */
    @RequestMapping("/getPatrolHelpMessageList")
    public void getPatrolHelpMessageList(int pageSize,int page,HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        MessageBean messageBean = new MessageBean();
        try {
            out = response.getWriter();
            String hql = "FROM PatrolHelpMessage";
//            if(campusNum !=null ) {
//                hql += " WHERE campusNum=" + campusNum + " ORDER BY pushDate DESC";
//            }else {
//                messageBean.setCode(200);
//                messageBean.setStatus(false);
//                messageBean.setMessage("参数不能为空");
//                out.write(JSON.toJSONString(messageBean));
//                return;
//            }
            hql += " order by isRead,helpTime desc";
            PageBean<PatrolHelpMessage> patrolHelpMessagePageBean = patrolHelpMessageService.getPatrolHelpMessage(hql,pageSize,page);
            messageBean.setData(patrolHelpMessagePageBean);
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


    /**
     * 通过id把是否已读改成1
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping("updateIsReade")
    public void updateIsReade(Integer id,HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("UTF-8");
        byte[] b= new byte[0];
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Date date = new Date();
            PatrolHelpMessage patrolHelpMessage=patrolHelpMessageService.getById(id);
            if(patrolHelpMessage!=null){
                patrolHelpMessage.setIsRead(1);
                patrolHelpMessageService.update(patrolHelpMessage);
                out.print("{\"status\":\"true\",\"Code\":1,\"msg\":\"修改成功!\"}");
            }else{
                out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"当前id没有求救信息！\"}");
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



//    /**
//     * 安防求救
//     * @param lon 经度
//     * @param lat 纬度
//     * @param userCode
//     * @param userName
//     */
//    @RequestMapping("/pushInfoHelp")
//    public void pushInfoHelp(Double lon, Double lat,String userCode,String userName,HttpServletResponse response) {
//        response.setCharacterEncoding("UTF-8");
////		PatrolExceptionPushInfo info = new PatrolExceptionPushInfo();
//
////        PrintWriter out = null;
////		try{
////
////
////        }catch(Exception e){
////        if(out==null){
////            out=response.getWriter();
////        }
////        out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
////    }finally{
////        out.flush();
////        out.close();
////    }
//        String title="求救信号";
//        String content="有学生报警，请尽快赶往事发地点！";
////		String adminUserIdsStr = exceptionPushService.getPartrolAdminUserId("2");
//        //到时候需要获取一下管理端的账号，目前传的是all，广播
//        String adminUserIdsStr ="all";
//        //因为这个接口是求救专用接口，所以这个type没啥特殊意义
//        String type="1";
//
//        exceptionPushService.pushSendHelp(title,content,userCode,userName,lon,lat,type,adminUserIdsStr);
//    }














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
