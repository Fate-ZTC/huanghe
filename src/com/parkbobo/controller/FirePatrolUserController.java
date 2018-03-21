package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.FireFightEquipment;
import com.parkbobo.model.FireFightEquipmentHistory;
import com.parkbobo.model.FirePatrolImg;
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.model.PatrolException;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.service.FireFightEquipmentHistoryService;
import com.parkbobo.service.FireFightEquipmentService;
import com.parkbobo.service.FirePatrolImgService;
import com.parkbobo.service.FirePatrolInfoService;
import com.parkbobo.service.PatrolExceptionService;
import com.parkbobo.service.PatrolUserService;

/**
 * 消防使用端接口
 * @author zj
 *@version 1.0
 */
@Controller
public class FirePatrolUserController {

	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

	@Resource
	private FireFightEquipmentService fireFightEquipmentService;
	@Resource
	private FirePatrolImgService firePatrolImgService;
	@Resource
	private FirePatrolInfoService firePatrolInfoService;
	@Resource
	private PatrolExceptionService patrolExceptionService;
	@Resource
	private PatrolUserService patrolUserService;
	@Resource
	private FireFightEquipmentHistoryService fireFightEquipmentHistoryService;
	/**
	 * 获取所有消防异常信息
	 * @throws IOException 
	 */
	@RequestMapping("getAllFireExceptions")
	public void getAllFireExceptions(HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			List<PatrolException> list = this.patrolExceptionService.getAllFireExceptions();
			if(list!=null&&list.size()>0){
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list,features)+"}");
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"无数据\"}");
			}
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
	 * 正常上报
	 * @param userId  用户id
	 * @param equipmentId 设备id
	 * @param imgUrl 图片地址
	 * @throws IOException 
	 */
	@RequestMapping("normalUpload")
	public void normalUpload(Integer userId,Integer equipmentId,String[] imgUrls,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			Date date = new Date();
			FirePatrolInfo newest = this.firePatrolInfoService.getNewest(equipmentId);
			if(newest!=null&&isEquals(date, newest.getTimestamp())){
				newest.setIsNewest((short)0);
				this.firePatrolInfoService.update(newest);
			}
			PatrolUser patrolUser = this.patrolUserService.getById(userId);
			FireFightEquipment fireFightEquipment = this.fireFightEquipmentService.getById(equipmentId);
			if(patrolUser!=null){
				if(fireFightEquipment!=null){
					FirePatrolInfo firePatrolInfo = new FirePatrolInfo();
					firePatrolInfo.setCampusNum(patrolUser.getCampusNum());
					firePatrolInfo.setPatrolUser(patrolUser);
					firePatrolInfo.setDescription("正常");
					firePatrolInfo.setFireFightEquipment(fireFightEquipment);
					firePatrolInfo.setPatrolStatus(1);
					firePatrolInfo.setIsNewest((short)1);
					firePatrolInfo.setTimestamp(date);
					firePatrolInfo.setUserName(patrolUser.getUsername());
					fireFightEquipment.setCheckStatus((short)1);
					fireFightEquipment.setStatus((short)1);
					fireFightEquipment.setLastUpdateTime(date);
					List<FireFightEquipmentHistory> fireFightEquiHistory = fireFightEquipmentHistoryService.getByProperty("oldId", fireFightEquipment.getId(), "lastUpdateTime", false);
					if (fireFightEquiHistory!=null && fireFightEquiHistory.size()>0) {
						FireFightEquipmentHistory fireFightEquipmentHistory = fireFightEquiHistory.get(0);
						fireFightEquipmentHistory.setCheckStatus((short)1);
						fireFightEquipmentHistory.setStatus((short)1);
						fireFightEquipmentHistory.setLastUpdateTime(date);
						fireFightEquipmentHistoryService.update(fireFightEquipmentHistory);
					}
					List<FirePatrolImg> list = new ArrayList<FirePatrolImg>();
					this.firePatrolInfoService.add(firePatrolInfo);
					this.fireFightEquipmentService.update(fireFightEquipment);
					if(imgUrls!=null&&imgUrls.length>0){
						for(int i = 0;i<imgUrls.length;i++){
							FirePatrolImg firePatrolImg = new FirePatrolImg();
							firePatrolImg.setFireFightEquipment(fireFightEquipment);
							firePatrolImg.setImgUrl(imgUrls[i]);
							firePatrolImg.setPatrolUser(patrolUser);
							firePatrolImg.setUploadTIme(date);
							list.add(this.firePatrolImgService.add(firePatrolImg));
						}
						out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"imgList\":"+JSONObject.toJSONString(list,features)+",\"firePatrolInfo\":"+JSONObject.toJSONString(firePatrolInfo,features)+"}}");
						return;
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"必须拍摄图片!!\"}");
						return;
					}
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"设备不存在\"}");
					return;
				}
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"用户不存在\"}");
				return;
			}
		}catch(Exception e){
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}

	/**
	 * 异常上报
	 * @param userId 用户id
	 * @param equipmentId 设备id
	 * @param imgUrls 图片地址数组
	 * @param exceptionTypes 错误代码 格式         "3,4,5,"
	 * @param description 描述
	 * @param response 
	 * @throws IOException
	 */
	@RequestMapping("abnormalUpload")
	public void abnormalUpload(Integer userId,Integer equipmentId,String[] imgUrls,String exceptionTypes,String description,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			Date date = new Date();
			FirePatrolInfo newest = this.firePatrolInfoService.getNewest(equipmentId);
			if(newest!=null&&isEquals(date, newest.getTimestamp())){
				newest.setIsNewest((short)0);
				this.firePatrolInfoService.update(newest);
			}
			PatrolUser patrolUser = this.patrolUserService.getById(userId);
			FireFightEquipment fireFightEquipment = this.fireFightEquipmentService.getById(equipmentId);
			if(patrolUser!=null){
				if(fireFightEquipment!=null){
					FirePatrolInfo firePatrolInfo = new FirePatrolInfo();
					firePatrolInfo.setCampusNum(patrolUser.getCampusNum());
					firePatrolInfo.setPatrolUser(patrolUser);
					firePatrolInfo.setFireFightEquipment(fireFightEquipment);
					if (description != null) {
						firePatrolInfo.setDescription(URLDecoder.decode(URLEncoder.encode(description, "ISO8859_1"), "UTF-8"));
					}else{
						firePatrolInfo.setDescription(description);
					}
					firePatrolInfo.setExceptionTypes(exceptionTypes);
					firePatrolInfo.setPatrolStatus(0);
					firePatrolInfo.setIsNewest((short)1);
					firePatrolInfo.setTimestamp(date);
					firePatrolInfo.setUserName(patrolUser.getUsername());
					fireFightEquipment.setCheckStatus((short)1);
					fireFightEquipment.setStatus((short)0);
					fireFightEquipment.setLastUpdateTime(date);
					List<FireFightEquipmentHistory> fireFightEquiHistory = fireFightEquipmentHistoryService.getByProperty("oldId", fireFightEquipment.getId(), "lastUpdateTime", false);
					if (fireFightEquiHistory!=null && fireFightEquiHistory.size()>0) {
						FireFightEquipmentHistory fireFightEquipmentHistory = fireFightEquiHistory.get(0);
						fireFightEquipmentHistory.setCheckStatus((short)1);
						fireFightEquipmentHistory.setStatus((short)0);
						fireFightEquipmentHistory.setLastUpdateTime(date);
						fireFightEquipmentHistoryService.update(fireFightEquipmentHistory);
					}
					List<FirePatrolImg> list = new ArrayList<FirePatrolImg>();
					this.firePatrolInfoService.add(firePatrolInfo);
					this.fireFightEquipmentService.update(fireFightEquipment);
					if(imgUrls!=null&&imgUrls.length>0){
						for(int i = 0;i<imgUrls.length;i++){
							FirePatrolImg firePatrolImg = new FirePatrolImg();
							firePatrolImg.setFireFightEquipment(fireFightEquipment);
							firePatrolImg.setImgUrl(imgUrls[i]);
							firePatrolImg.setPatrolUser(patrolUser);
							firePatrolImg.setUploadTIme(date);
							list.add(this.firePatrolImgService.add(firePatrolImg));
						}
						out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"imgList\":"+JSONObject.toJSONString(list,features)+",\"firePatrolInfo\":"+JSONObject.toJSONString(firePatrolInfo,features)+"}}");
						return;
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"必须拍摄图片!!\"}");
						return;
					}
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"设备不存在\"}");
					return;
				}
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"用户不存在\"}");
				return;
			}
		}catch(Exception e){
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}

	/**
	 * 初始化所有设备为未检查状态
	 * @param response
	 */
	@RequestMapping("setUnchecked")
	public void setUnchecked(HttpServletResponse response){
		List<FireFightEquipment> fireFightEquipments = fireFightEquipmentService.getAll();
		for (FireFightEquipment fireFightEquipment: fireFightEquipments) {
			FireFightEquipmentHistory history = new FireFightEquipmentHistory();
			history.setCampusNum(fireFightEquipment.getCampusNum());
			history.setCheckStatus(fireFightEquipment.getCheckStatus());
			history.setOldId(fireFightEquipment.getId());
			history.setLastUpdateTime(fireFightEquipment.getLastUpdateTime());
			history.setLat(fireFightEquipment.getLat());
			history.setLon(fireFightEquipment.getLon());
			history.setStatus(fireFightEquipment.getStatus());
			history.setName(fireFightEquipment.getName());
			this.fireFightEquipmentHistoryService.add(history);
		}
	}
	/**
	 * 判断两个日期是否在同一个月
	 * @param date1
	 * @param date2
	 * @return
	 */
	public boolean isEquals(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
	} 
}
