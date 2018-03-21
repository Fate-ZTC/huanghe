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

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolException;
import com.parkbobo.model.PatrolLocationInfo;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolExceptionService;
import com.parkbobo.service.PatrolLocationInfoService;
import com.parkbobo.service.PatrolRegionService;
import com.parkbobo.service.PatrolUserRegionService;
import com.parkbobo.service.PatrolUserService;
import com.parkbobo.utils.GisUtils;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

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
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUser)+"}");
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
	 * @param regionId   区域id
	 * @param jobNum   工号
	 * @param campusNum 校区编号
	 * @throws IOException 
	 */
	@RequestMapping("startPatrol")
	public void startPatrol(String  username,Integer regionId ,String jobNum,Integer campusNum,
			Integer configId,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
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
			patrolUserRegion.setStatus(1);
			this.patrolUserRegionService.addRecord(patrolUserRegion);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUserRegion,features)+"}");
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 结束巡逻
	 * @param patrolRegion
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("endPatrol")
	public void endPatrol(Integer userRegionId,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Date date = new Date();
			PatrolUserRegion patrolUserRegion = this.patrolUserRegionService.getById(userRegionId);
			patrolUserRegion.setLastUpdateTime(date);
			patrolUserRegion.setEndTime(date);
			this.patrolUserRegionService.updateRecord(patrolUserRegion);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":\""+JSONObject.toJSONString(patrolUserRegion)+"}");
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
	public void uploadLocation(Integer regionId,Integer configId,String jobNum,Double lon,Double lat,Integer campusNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if(lon==null||lat==null){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"此人已关闭定位\"}");
			return;
		}
		PatrolException patrolE1 = patrolExceptionService.get(1);
		PatrolException patrolE2 = patrolExceptionService.get(2);
		Date date = new Date();
		PatrolConfig patrolConfig = this.patrolConfigService.getById(configId);
		PatrolLocationInfo patrolLocationInfo = new PatrolLocationInfo();
		patrolLocationInfo.setCampusNum(campusNum);
		patrolLocationInfo.setLon(lon);//经度
		patrolLocationInfo.setLat(lat);//纬度
		patrolLocationInfo.setJobNum(jobNum);
		PatrolUserRegion patrolUserRegion = null;
		List<PatrolUserRegion> list = this.patrolUserRegionService.getByHQL("from PatrolUserRegion where jobNum ='"+jobNum+"' and endTime is null order by startTime desc limit 1");
		if(list!=null&&list.size()>0){
			patrolUserRegion = list.get(0);
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未获取到巡逻信息\"}");
			return;
		}
		patrolLocationInfo.setUsregId(regionId);
		patrolLocationInfo.setTimestamp(date);
		patrolLocationInfo.setUsername(patrolUserRegion.getUsername());
		if(patrolConfig.getIsEmergency()==1){
			patrolLocationInfo = patrolLocationInfoService.add(patrolLocationInfo);
			//紧急状态
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolLocationInfo)+"}");
			out.flush();
			out.close();
			return;
		}else{
			if(date.getTime()-patrolUserRegion.getStartTime().getTime()>=patrolConfig.getStartPatrolTime()*60*1000){
				//非紧急状态
				MultiPolygon regionLocation = patrolRegionService.getById(regionId).getRegionLocation();
				Polygon polygon  = GisUtils.createCircle(lon, lat,patrolConfig.getLeaveRegionDistance(), 50);
				if(regionLocation.contains(polygon)){
					//在巡逻区域内
					if(this.patrolUserRegionService.isLazy(patrolUserRegion)){
						//偷懒超过5分钟
						if(patrolUserRegion.getStatus()==1){
							//当前为正常状态
							patrolUserRegion.setStatus(2);
						}if(patrolUserRegion.getStatus()==2){
							//当前为异常状态
						}
						patrolLocationInfo.setPatrolException(patrolE2);
						patrolUserRegion.setPatrolException(patrolE1);
						patrolLocationInfo.setStatus(2);
					}else{
						//原地不超过五分钟
						if(patrolUserRegion.getStatus()==1){
							//当前为正常状态
						}if(patrolUserRegion.getStatus()==2){
							//当前为异常状态
							patrolUserRegion.setStatus(1);
						}
					}
				}else{
					//不在巡逻区域内
					//当前为正常状态
					if(patrolUserRegion.getStatus()==1){
						patrolUserRegion.setStatus(2);
					}
					patrolUserRegion.setPatrolException(patrolE1);
					patrolLocationInfo.setPatrolException(patrolE1);
					patrolLocationInfo.setStatus(2);
				}
			}
			patrolUserRegion.setLastUpdateTime(date);
			this.patrolUserRegionService.merge(patrolUserRegion);
			patrolLocationInfo = patrolLocationInfoService.add(patrolLocationInfo);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolLocationInfo)+"}");
			out.flush();
			out.close();
		}
	}
	/**
	 * 初始化区域信息
	 * @param 校区id
	 * @throws IOException 
	 */
	@RequestMapping("initRegion")
	public void getRegions(Integer campusNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			List<PatrolRegion> list = this.patrolRegionService.getByCampusNum(campusNum);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list,features)+"}");
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
			long countTime = this.patrolUserRegionService.getCountTime(jobNum);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+countTime+"}");
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
			if (patrolUserRegions!=null && patrolUserRegions.size()>0) {
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
