package com.parkbobo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.FireFightEquipment;
import com.parkbobo.model.FireFightEquipmentHistory;
import com.parkbobo.model.FirePatrolConfig;
import com.parkbobo.model.FirePatrolException;
import com.parkbobo.model.FirePatrolImg;
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.model.FirePatrolUser;
import com.parkbobo.service.FireFightEquipmentHistoryService;
import com.parkbobo.service.FireFightEquipmentService;
import com.parkbobo.service.FirePatrolConfigService;
import com.parkbobo.service.FirePatrolExceptionService;
import com.parkbobo.service.FirePatrolImgService;
import com.parkbobo.service.FirePatrolInfoService;
import com.parkbobo.service.FirePatrolUserService;
import com.parkbobo.utils.GisUtil;

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
	private FirePatrolExceptionService firePatrolExceptionService;
	@Resource
	private FirePatrolUserService firePatrolUserService;
	@Resource
	private FireFightEquipmentHistoryService fireFightEquipmentHistoryService;
	@Resource
	private FirePatrolConfigService firePatrolConfigService;
	/**
	 * 登录
	 * @param jobNum 工号
	 * @param password 密码
	 * @return json
	 * @throws IOException 
	 */
	@RequestMapping("firePatrolUserLogin")
	public void userLogin(HttpServletResponse response,String jobNum,String password) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			FirePatrolUser patrolUser = this.firePatrolUserService.userLogin(jobNum, password);
			if(patrolUser != null){
				if(patrolUser.getIsDel()==1){
					out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账户已删除\"}");
					return;
				}
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUser,features)+"}");
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
	 * 获取所有消防异常信息
	 * @throws IOException 
	 */
	@RequestMapping("getAllFireExceptions")
	public void getAllFireExceptions(HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			List<FirePatrolException> list = this.firePatrolExceptionService.getAllFireExceptions();
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
	 * 获取当前登录用户上传的信息 
	 * @param jobNum 工号
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getByJobNum")
	public void getInfoByJobNum(String jobNum,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			List<FirePatrolInfo> list = this.firePatrolInfoService.getByHql(jobNum);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list,features)+"}");
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

	public String upload(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) throws Exception {
		//如果文件不为空，写入上传路径
		if(!file.isEmpty()) {
			//上传文件路径
			String path = request.getServletContext().getRealPath("/images/");
			//上传文件名
			String filename = file.getOriginalFilename();
			File filepath = new File(path,filename);
			//判断路径是否存在，如果不存在就创建一个
			if (!filepath.getParentFile().exists()) { 
				filepath.getParentFile().mkdirs();
			}
			//将上传文件保存到一个目标文件当中
			file.transferTo(new File(path + File.separator + filename));
			return path + File.separator + filename;
		} else {
			return "error";
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
	public void normalUpload(MultipartFile[] file,Double lon,Double lat,Integer userId,Integer equipmentId,HttpServletResponse response,HttpServletRequest request) throws IOException{
		PrintWriter out = null;
		try {

			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			FirePatrolConfig patrolConfig = this.firePatrolConfigService.getById(1);
			FireFightEquipment fireFightEquipment = this.fireFightEquipmentService.getById(equipmentId);
			GisUtil g = GisUtil.getInstance();
			if(g.distanceByLngLat(lon, lat, (double)fireFightEquipment.getLon(), (double)fireFightEquipment.getLat())>patrolConfig.getDistance()){
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"请在指定区域内上传数据\"}");
				return;
			}
			Date date = new Date();
			FirePatrolInfo newest = this.firePatrolInfoService.getNewest(equipmentId);
			if(newest!=null&&isEquals(date, newest.getTimestamp())){
				newest.setIsNewest((short)0);
				this.firePatrolInfoService.update(newest);
			}
			FirePatrolUser patrolUser = this.firePatrolUserService.getById(userId);
			if(patrolUser!=null){
				FirePatrolInfo firePatrolInfo = new FirePatrolInfo();
				firePatrolInfo.setCampusNum(patrolUser.getCampusNum());
				firePatrolInfo.setFirePatrolUser(patrolUser);
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
				FirePatrolInfo add = this.firePatrolInfoService.add(firePatrolInfo);
				this.fireFightEquipmentService.update(fireFightEquipment);
				if(file!=null&&file.length>0){
					for(int i = 0;i<file.length;i++){
						FirePatrolImg firePatrolImg = new FirePatrolImg();
						firePatrolImg.setFireFightEquipment(fireFightEquipment);
						firePatrolImg.setImgUrl(upload(request,file[i]));
						firePatrolImg.setFirePatrolUser(patrolUser);
						firePatrolImg.setUploadTIme(date);
						firePatrolImg.setInfoId(add.getId());
						list.add(this.firePatrolImgService.add(firePatrolImg));
					}
					out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"imgList\":"+JSONObject.toJSONString(list,features)+",\"firePatrolInfo\":"+JSONObject.toJSONString(firePatrolInfo,features)+"}}");
					return;
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"必须拍摄图片!!\"}");
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
	public void abnormalUpload(MultipartFile[] file,Double lon,Double lat,Integer userId,Integer equipmentId,String exceptionTypes,String description,HttpServletResponse response,HttpServletRequest request) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			Date date = new Date();
			FireFightEquipment fireFightEquipment = this.fireFightEquipmentService.getById(equipmentId);
			FirePatrolConfig patrolConfig = this.firePatrolConfigService.getById(1);
			GisUtil g = GisUtil.getInstance();
			if(g.distanceByLngLat(lon, lat, (double)fireFightEquipment.getLon(), (double)fireFightEquipment.getLat())>patrolConfig.getDistance()){
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"请在指定区域内上传数据\"}");
				return;
			}
			FirePatrolInfo newest = this.firePatrolInfoService.getNewest(equipmentId);
			if(newest!=null&&isEquals(date, newest.getTimestamp())){
				newest.setIsNewest((short)0);
				this.firePatrolInfoService.update(newest);
			}
			FirePatrolUser patrolUser = this.firePatrolUserService.getById(userId);
			if(patrolUser!=null){
				FirePatrolInfo firePatrolInfo = new FirePatrolInfo();
				firePatrolInfo.setCampusNum(patrolUser.getCampusNum());
				firePatrolInfo.setFirePatrolUser(patrolUser);
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
				FirePatrolInfo add = this.firePatrolInfoService.add(firePatrolInfo);
				this.fireFightEquipmentService.update(fireFightEquipment);
				if(file!=null&&file.length>0){
					for(int i = 0;i<file.length;i++){
						FirePatrolImg firePatrolImg = new FirePatrolImg();
						firePatrolImg.setFireFightEquipment(fireFightEquipment);
						firePatrolImg.setImgUrl(upload(request,file[i]));
						firePatrolImg.setFirePatrolUser(patrolUser);
						firePatrolImg.setUploadTIme(date);
						firePatrolImg.setInfoId(add.getId());
						list.add(this.firePatrolImgService.add(firePatrolImg));
					}
					out.print("{\"status\":\"true\",\"Code\":1,\"data\":{\"imgList\":"+JSONObject.toJSONString(list,features)+",\"firePatrolInfo\":"+JSONObject.toJSONString(firePatrolInfo,features)+"}}");
					return;
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"必须拍摄图片!!\"}");
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
