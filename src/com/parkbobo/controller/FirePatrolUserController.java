package com.parkbobo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.parkbobo.dao.FirePatrolInfoDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.VO.FirePatrolEquipmentStatusVO;
import com.parkbobo.VO.FirePatrolUseBarListVO;
import com.parkbobo.VO.FirePatrolUseEquNumVO;
import com.parkbobo.VO.FirePatrolUseStatisticsVO;
import com.parkbobo.dao.FireFightEquipmentHistoryDao;
import com.parkbobo.dao.FirePatrolBuildingInfoDao;
import com.parkbobo.model.*;
import com.parkbobo.service.*;
import com.parkbobo.utils.DateMUtils;
import com.parkbobo.utils.GisUtil;
import com.parkbobo.utils.message.MessageBean;
import com.parkbobo.utils.message.MessageListBean;

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
	@Resource
	private FirePatrolEquipmentSychService firePatrolEquipmentSychService;
	@Resource
	private ExceptionPushService exceptionPushService;
	@Resource(name = "firePatrolBuildingTypeService")
	private FirePatrolBuildingTypeService buildingTypeService;
	@Resource
	private FirePatrolTimeQuantumService firePatrolTimeQuantumService;
	@Resource
	private FirePatrolBuildingInfoService firePatrolBuildingInfoService;
	@Resource
	private FireFightEquipmentHistoryDao fireFightEquipmentHistoryDao;
	@Resource
	private FirePatrolInfoDao firePatrolInfoDao;
	@Resource
	private FirePatrolBuildingInfoDao firePatrolBuildingInfoDao;

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
			FirePatrolUser firePatrolUser = this.firePatrolUserService.getByJobNumAll(jobNum);
			if(firePatrolUser==null){
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户不存在!\"}");
				return;
			}
			if(patrolUser != null){
				if(patrolUser.getIsDel()==1){
					out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户已删除!\"}");
					return;
				}
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUser,features)+"}");
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户名或密码错误\"}");
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


	//TODO 这里上传的图片保存的地址是有问题的,是项目的相关地址
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
			String basePath =
					request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
							request.getContextPath() + File.separator + "images" + File.separator + filename ;
			return basePath;
		} else {
			return "error";
		}
	}


	/**
	 * 正常上报
	 * @param userId  用户id
	 * @param equipmentId 设备id
	 * @param file 文件
	 * @param lon 经纬度
	 * @param lat 纬度
	 * @throws IOException
	 */
	@RequestMapping("normalUpload")
	public void normalUpload(@RequestParam("file") MultipartFile[] file,Double lon,Double lat,
							 Integer userId,Integer equipmentId,String locationName,
							 HttpServletResponse response,HttpServletRequest request) throws IOException {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			FirePatrolConfig patrolConfig = this.firePatrolConfigService.getById(1);
			FireFightEquipment fireFightEquipment = this.fireFightEquipmentService.getById(equipmentId);
			if(fireFightEquipment == null) {
                out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"没有查找到设备信息\"}");
                return;
            }
			GisUtil g = GisUtil.getInstance();
			double flon = fireFightEquipment.getLon();
			double flat = fireFightEquipment.getLat();
			double distance = g.distanceByLngLat(lon, lat, flon,flat);
			if( distance > patrolConfig.getDistance()) {
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"请在指定区域内上传数据\"}");
				return;
			}
			Date date = new Date();
			FirePatrolInfo newest = this.firePatrolInfoService.getNewest(equipmentId);
			if(newest != null && isEquals(date, newest.getTimestamp())) {
				newest.setIsNewest((short)0);
				this.firePatrolInfoService.update(newest);
			}

			FirePatrolUser patrolUser = this.firePatrolUserService.getById(userId);
            String jobNum = patrolUser.getJobNum();
            int campusNum = patrolUser.getCampusNum();
            boolean isStart = firePatrolTimeQuantumService.isStartTime(jobNum,campusNum);

            if(isStart) {
                isStart(jobNum,campusNum,firePatrolTimeQuantumService);
            }
			if(patrolUser!=null) {
				FirePatrolInfo firePatrolInfo = new FirePatrolInfo();
				firePatrolInfo.setCampusNum(patrolUser.getCampusNum());
				firePatrolInfo.setFirePatrolUser(patrolUser);
				firePatrolInfo.setDescription("正常");
				firePatrolInfo.setFireFightEquipment(fireFightEquipment);
				firePatrolInfo.setPatrolStatus(1);
				firePatrolInfo.setIsNewest((short)1);
				firePatrolInfo.setTimestamp(date);
				firePatrolInfo.setUserName(patrolUser.getUsername());

				Integer id = fireFightEquipment.getId();
				List<FireFightEquipmentHistory> fireFightEquiHistory = fireFightEquipmentHistoryService.getByProperty("oldId", fireFightEquipment.getId(), "lastUpdateTime", false);
				fireFightEquipment.setCheckStatus((short)1);
				fireFightEquipment.setStatus((short)1);
				fireFightEquipment.setLastUpdateTime(date);

				//进行和消防专题图进行设备检测数据同步
				FirePatrolEquipmentStatusVO statusVO = new FirePatrolEquipmentStatusVO();
				statusVO.setPatrolUser(patrolUser.getUsername());						//巡查员姓名
				statusVO.setDeviceStatus("1");											//巡查状态
				statusVO.setDeviceId(fireFightEquipment.getPointid().toString());		//设备消防专题图id
				firePatrolEquipmentSychService.updateFirePatrolEquipmentVOStatus(statusVO);


				System.out.println(fireFightEquiHistory);

				List<FirePatrolImg> list = new ArrayList<FirePatrolImg>();

				//这里进行补充
				firePatrolInfo.setFloorid(fireFightEquipment.getFloorid());
				firePatrolInfo.setLon(fireFightEquipment.getLon());
				firePatrolInfo.setLat(fireFightEquipment.getLat());
				firePatrolInfo.setJobNum(patrolUser.getJobNum());

				FirePatrolInfo add = this.firePatrolInfoService.add(firePatrolInfo);

				//设备历史记录添加
				if (fireFightEquiHistory!=null && fireFightEquiHistory.size()>0) {
					FireFightEquipmentHistory fireFightEquipmentHistory = fireFightEquiHistory.get(0);
					fireFightEquipmentHistory.setCheckStatus((short)1);
					fireFightEquipmentHistory.setStatus((short)1);
					fireFightEquipmentHistory.setLastUpdateTime(date);
					fireFightEquipmentHistory.setUserName(patrolUser.getUsername());
					fireFightEquipmentHistory.setJobNum(patrolUser.getJobNum());
					if(locationName != null) {
						fireFightEquipmentHistory.setLocationName(locationName);
					}
					//这里进行巡查次数设置
					int count = fireFightEquipmentHistory.getCheckCount();
					fireFightEquipmentHistory.setCheckCount(count + 1);
					fireFightEquipmentHistoryService.update(fireFightEquipmentHistory);
				}else {
					//没有查询到设备表中数据,进行添加新的数据
					FireFightEquipmentHistory fireFightEquipmentHistory = new FireFightEquipmentHistory();
					fireFightEquipmentHistory.setName(fireFightEquipment.getName());
					fireFightEquipmentHistory.setCampusNum(fireFightEquipment.getCampusNum());
					fireFightEquipmentHistory.setCheckStatus((short)1);
					fireFightEquipmentHistory.setStatus((short)1);
					fireFightEquipmentHistory.setFloorid(fireFightEquipment.getFloorid());
					fireFightEquipmentHistory.setBuildingCode(fireFightEquipment.getBuildingCode());
					fireFightEquipmentHistory.setLon(lon);
					fireFightEquipmentHistory.setLat(lat);
					fireFightEquipmentHistory.setLastUpdateTime(new Date());
					fireFightEquipmentHistory.setOldId(add.getId());
					fireFightEquipmentHistory.setJobNum(patrolUser.getJobNum());
					fireFightEquipmentHistory.setUserName(patrolUser.getUsername());
					//设置巡查次数
					fireFightEquipmentHistory.setCheckCount(1);
					if(locationName != null) {
						fireFightEquipmentHistory.setLocationName(locationName);
					}
					fireFightEquipmentHistoryService.update(fireFightEquipmentHistory);
				}


				this.fireFightEquipmentService.update(fireFightEquipment);
				if(file!=null&&file.length > 0) {
					for(int i = 0;i<file.length;i++) {
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
		}catch(Exception e) {
			if(out==null) {
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"未知异常,请技术人员\"}");
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
	}




	/**
	 * 异常上报
	 * @param userId 用户id
	 * @param equipmentId 设备id
	 * @param file 图片地址数组
	 * @param lat 纬度
	 * @param lon 经度
	 * @param exceptionTypes 错误代码 格式         "3,4,5,"
	 * @param description 描述
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("abnormalUpload")
	public void abnormalUpload(@RequestParam("file") MultipartFile[] file,Double lon,Double lat,
							   Integer userId,Integer equipmentId,String exceptionTypes,String description,
							   String locationName,HttpServletResponse response,HttpServletRequest request) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out=response.getWriter();
			Date date = new Date();
			FireFightEquipment fireFightEquipment = this.fireFightEquipmentService.getById(equipmentId);
			FirePatrolConfig patrolConfig = this.firePatrolConfigService.getById(1);
			GisUtil g = GisUtil.getInstance();
			if(fireFightEquipment == null) {
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"没有查找到设备信息\"}");
				return;
			}
			if(g.distanceByLngLat(lon, lat, (double)fireFightEquipment.getLon(), (double)fireFightEquipment.getLat())>patrolConfig.getDistance()){
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"请在指定区域内上传数据\"}");
				return;
			}
			FirePatrolInfo newest = this.firePatrolInfoService.getNewest(equipmentId);
			if(newest!=null&&isEquals(date, newest.getTimestamp())) {
				newest.setIsNewest((short)0);
				this.firePatrolInfoService.update(newest);
			}
			FirePatrolUser patrolUser = this.firePatrolUserService.getById(userId);
			if(patrolUser!=null) {
				FirePatrolInfo firePatrolInfo = new FirePatrolInfo();
				firePatrolInfo.setCampusNum(patrolUser.getCampusNum());
				firePatrolInfo.setFirePatrolUser(patrolUser);
				firePatrolInfo.setFireFightEquipment(fireFightEquipment);
				if (description != null) {
//					firePatrolInfo.setDescription(URLDecoder.decode(URLEncoder.encode(description, "ISO8859_1"), "UTF-8"));
					firePatrolInfo.setDescription(description);
				}else{
					firePatrolInfo.setDescription(description);
				}
				firePatrolInfo.setExceptionTypes(exceptionTypes);
				firePatrolInfo.setPatrolStatus(0);
				firePatrolInfo.setIsNewest((short)1);
				firePatrolInfo.setTimestamp(date);
				firePatrolInfo.setUserName(patrolUser.getUsername());
				List<FireFightEquipmentHistory> fireFightEquiHistory = fireFightEquipmentHistoryService.getByProperty("oldId", fireFightEquipment.getId(), "lastUpdateTime", false);
				fireFightEquipment.setCheckStatus((short)1);
				fireFightEquipment.setStatus((short)0);
				fireFightEquipment.setLastUpdateTime(date);

				//进行和消防专题图进行设备检测数据同步
				FirePatrolEquipmentStatusVO statusVO = new FirePatrolEquipmentStatusVO();
				statusVO.setPatrolUser(patrolUser.getUsername());						//巡查员姓名
				statusVO.setDeviceStatus("0");											//巡查状态
				statusVO.setDeviceId(fireFightEquipment.getPointid().toString());					//设备消防专题图id
				firePatrolEquipmentSychService.updateFirePatrolEquipmentVOStatus(statusVO);
				String jobNum = patrolUser.getJobNum();
				int campusNum = patrolUser.getCampusNum();
				boolean isStart = firePatrolTimeQuantumService.isStartTime(jobNum,campusNum);

				if(isStart) {
					isStart(jobNum,campusNum,firePatrolTimeQuantumService);
				}

				List<FirePatrolImg> list = new ArrayList<FirePatrolImg>();

				//进行异常设备推送
				String adminUserIdsStr = exceptionPushService.getPartrolAdminUserId("1");
				//这里进行推送
				if(description == null) {
					description = "设备出现故障,请注意,及时进行处理!";
				}
				exceptionPushService.pushSend("1","消防设备异常",description,adminUserIdsStr);

				//这里进行补充
				firePatrolInfo.setFloorid(fireFightEquipment.getFloorid());
				firePatrolInfo.setLon(fireFightEquipment.getLon());
				firePatrolInfo.setLat(fireFightEquipment.getLat());
				firePatrolInfo.setJobNum(patrolUser.getJobNum());


				FirePatrolInfo add = this.firePatrolInfoService.add(firePatrolInfo);


				if (fireFightEquiHistory!=null && fireFightEquiHistory.size() > 0) {
					//查询到设备表中有数据
					FireFightEquipmentHistory fireFightEquipmentHistory = fireFightEquiHistory.get(0);
					fireFightEquipmentHistory.setCheckStatus((short)1);
					fireFightEquipmentHistory.setStatus((short)0);
					fireFightEquipmentHistory.setLastUpdateTime(date);
					fireFightEquipmentHistory.setUserName(patrolUser.getUsername());
					fireFightEquipmentHistory.setJobNum(patrolUser.getJobNum());
					if(locationName != null) {
						fireFightEquipmentHistory.setLocationName(locationName);
					}
					int count = fireFightEquipmentHistory.getCheckCount();
					fireFightEquipmentHistory.setCheckCount(count + 1);
					fireFightEquipmentHistoryService.update(fireFightEquipmentHistory);
				}else {
					//没有查询到设备表中数据,进行添加新的数据
					FireFightEquipmentHistory fireFightEquipmentHistory = new FireFightEquipmentHistory();
					fireFightEquipmentHistory.setName(fireFightEquipment.getName());
					fireFightEquipmentHistory.setFloorid(fireFightEquipment.getFloorid());
					fireFightEquipmentHistory.setBuildingCode(fireFightEquipment.getBuildingCode());
					fireFightEquipmentHistory.setCampusNum(fireFightEquipment.getCampusNum());
					fireFightEquipmentHistory.setCheckStatus((short)1);
					fireFightEquipmentHistory.setStatus((short)0);
					fireFightEquipmentHistory.setLon(lon);
					fireFightEquipmentHistory.setLat(lat);
					fireFightEquipmentHistory.setLastUpdateTime(new Date());
					fireFightEquipmentHistory.setOldId(fireFightEquipment.getId());
					fireFightEquipmentHistory.setUserName(patrolUser.getUsername());
					fireFightEquipmentHistory.setJobNum(patrolUser.getJobNum());
					if(locationName != null) {
						fireFightEquipmentHistory.setLocationName(locationName);
					}
					fireFightEquipmentHistory.setCheckCount(1);
					fireFightEquipmentHistoryService.update(fireFightEquipmentHistory);
				}


				this.fireFightEquipmentService.update(fireFightEquipment);
				if(file!=null&&file.length>0) {
					for(int i = 0;i<file.length;i++) {
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
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
	}

	/**
	 * 上传所有消防设备正常信息（主要包含两种情况，1、在）
	 * @param file			 图片列表
	 * @param uploadType	上传类型3
	 * @param userId		用户工号
	 * @param lon 			经度
	 * @param lat 			纬度
	 * @param buildingCode	大楼id
	 * @param floorid 		楼层id
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/allNormalUpload")
	public void allNormalUpload(@RequestParam("file") MultipartFile[] file,int uploadType,String buildingCode,
								String floorid,Double lon,Double lat,Integer userId,String locationName,
								HttpServletResponse response,HttpServletRequest request) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type","application/json;charset=utf-8");
		PrintWriter out = null;
		try {
		    out = response.getWriter();
			MessageBean messageBean = new MessageBean();
			messageBean.setCode(200);
			if(uploadType != 3) {
				messageBean.setStatus(false);
				messageBean.setMessage("上传类型错误，该类型不是上传所有正常设备参数！");
				out.write(JSON.toJSONString(messageBean));
				return;
			}

			//下面进行判断设备是在室内还是在室外
            List<FireFightEquipment> fireFightEquipments = new ArrayList<>();
			//室内设备
			if(buildingCode != null && floorid != null) {
                fireFightEquipments = this.fireFightEquipmentService.getIndoorEquipment(buildingCode,floorid);
			}

			//室外设备(室外设备没有大楼id)
			if(buildingCode == null && floorid != null) {
                fireFightEquipments = this.fireFightEquipmentService.getOutdoorEquipment(null,floorid);
			}


			if(userId == null || "".equals(userId)) {
			    messageBean.setMessage("用户账号为空！");
			    messageBean.setStatus(false);
			    messageBean.setCode(200);
			    out.write(JSON.toJSONString(messageBean));
			    return;
            }
            FirePatrolUser patrolUser = this.firePatrolUserService.getById(userId);

			//保存图片
			List<FirePatrolImg> list = new ArrayList<FirePatrolImg>();



			//进行数据保存
            if(fireFightEquipments != null && fireFightEquipments.size() > 0) {
			    //这里进行批量添加新的检查设备
                List<FirePatrolInfo> firePatrolInfos = new ArrayList<>();
                for(FireFightEquipment fireFightEquipment : fireFightEquipments) {
                    //获取最新的一条数据
                    FirePatrolInfo newest = this.firePatrolInfoService.getNewest(fireFightEquipment.getId());
                    if(newest != null && isEquals(new Date(), newest.getTimestamp())) {
                        newest.setIsNewest((short)0);
                        //跟新状态为最新巡查
                        this.firePatrolInfoService.update(newest);
                    }

                    if(patrolUser != null) {
                        FirePatrolInfo firePatrolInfo = new FirePatrolInfo();
                        firePatrolInfo.setCampusNum(patrolUser.getCampusNum());
                        firePatrolInfo.setFirePatrolUser(patrolUser);
                        firePatrolInfo.setFireFightEquipment(fireFightEquipment);
                        String description = "正常";
                        firePatrolInfo.setDescription(description);
                        firePatrolInfo.setPatrolStatus(1);
                        firePatrolInfo.setIsNewest((short)1);
                        firePatrolInfo.setTimestamp(new Date());
                        firePatrolInfo.setUserName(patrolUser.getUsername());
                        List<FireFightEquipmentHistory> fireFightEquiHistory = fireFightEquipmentHistoryService.getByProperty("oldId", fireFightEquipment.getId(), "lastUpdateTime", false);
                        fireFightEquipment.setCheckStatus((short)1);
                        fireFightEquipment.setStatus((short)1);
                        fireFightEquipment.setLastUpdateTime(new Date());

                        //进行和消防专题图进行设备检测数据同步
                        FirePatrolEquipmentStatusVO statusVO = new FirePatrolEquipmentStatusVO();
                        statusVO.setPatrolUser(patrolUser.getUsername());						//巡查员姓名
                        statusVO.setDeviceStatus("0");											//巡查状态
                        statusVO.setDeviceId(fireFightEquipment.getPointid().toString());					//设备消防专题图id
                        firePatrolEquipmentSychService.updateFirePatrolEquipmentVOStatus(statusVO);




                        //这里进行补充
                        firePatrolInfo.setFloorid(fireFightEquipment.getFloorid());
                        firePatrolInfo.setLon(fireFightEquipment.getLon());
                        firePatrolInfo.setLat(fireFightEquipment.getLat());
                        firePatrolInfo.setJobNum(patrolUser.getJobNum());


                        FirePatrolInfo add = this.firePatrolInfoService.add(firePatrolInfo);


                        if (fireFightEquiHistory!=null && fireFightEquiHistory.size() > 0) {
                            //查询到设备表中有数据
                            FireFightEquipmentHistory fireFightEquipmentHistory = fireFightEquiHistory.get(0);
                            fireFightEquipmentHistory.setCheckStatus((short)1);
                            fireFightEquipmentHistory.setStatus((short)1);
                            fireFightEquipmentHistory.setLastUpdateTime(new Date());
                            fireFightEquipmentHistory.setUserName(patrolUser.getUsername());
                            fireFightEquipmentHistory.setJobNum(patrolUser.getJobNum());
							if(locationName != null) {
								fireFightEquipmentHistory.setLocationName(locationName);
							}
                            fireFightEquipmentHistoryService.update(fireFightEquipmentHistory);
                        }else {
                            //没有查询到设备表中数据,进行添加新的数据
                            FireFightEquipmentHistory fireFightEquipmentHistory = new FireFightEquipmentHistory();
                            fireFightEquipmentHistory.setName(fireFightEquipment.getName());
                            fireFightEquipmentHistory.setCampusNum(fireFightEquipment.getCampusNum());
                            fireFightEquipmentHistory.setCheckStatus((short)1);
                            fireFightEquipmentHistory.setStatus((short)1);
                            fireFightEquipmentHistory.setLon(lon);
                            fireFightEquipmentHistory.setLat(lat);
                            fireFightEquipmentHistory.setLastUpdateTime(new Date());
                            fireFightEquipmentHistory.setOldId(fireFightEquipment.getId());
                            fireFightEquipmentHistory.setUserName(patrolUser.getUsername());
                            fireFightEquipmentHistory.setJobNum(patrolUser.getJobNum());
							if(locationName != null) {
								fireFightEquipmentHistory.setLocationName(locationName);
							}
                            fireFightEquipmentHistoryService.update(fireFightEquipmentHistory);
                        }


                        this.fireFightEquipmentService.update(fireFightEquipment);
                        if(list.size() == 0) {
							if (file != null && file.length > 0) {
								for (int i = 0; i < file.length; i++) {
									FirePatrolImg firePatrolImg = new FirePatrolImg();
									firePatrolImg.setFireFightEquipment(fireFightEquipment);
									firePatrolImg.setImgUrl(upload(request, file[i]));
									firePatrolImg.setFirePatrolUser(patrolUser);
									firePatrolImg.setUploadTIme(new Date());
									firePatrolImg.setInfoId(add.getId());
									list.add(this.firePatrolImgService.add(firePatrolImg));
								}
							} else {
								messageBean.setMessage("必须拍摄图片");
								messageBean.setCode(200);
								messageBean.setStatus(false);
								out.print(JSON.toJSONString(messageBean));
								return;
							}
						}

                    }else {
                        messageBean.setStatus(false);
                        messageBean.setMessage("该用户不存在");
                        out.write(JSON.toJSONString(messageBean));
                        return;
                    }

                }
                //在这里进行上传成功操作
                messageBean.setCode(200);
                messageBean.setStatus(true);
                messageBean.setMessage("上传成功");
                out.write(JSON.toJSONString(messageBean));
                return;
            }else {
            	messageBean.setMessage("没有查找到相关设备");
            	messageBean.setStatus(false);
            	messageBean.setCode(200);
            	out.write(JSON.toJSONString(messageBean));
            	return;
			}

		}catch(Exception e){
			if(out==null){
				out=response.getWriter();
			}
			MessageBean messageBean = new MessageBean();
			messageBean.setStatus(false);
			messageBean.setCode(200);
			messageBean.setMessage("未知异常，请联系技术人员！");
			out.print(JSON.toJSONString(messageBean));
			e.printStackTrace();
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
	public void setUnchecked(HttpServletResponse response) {
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
	 * 获取type类型信息
	 * @param response
	 */
	@RequestMapping("/getBuildingType")
	public void getBuildingType(HttpServletResponse response,String campusId) {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String hql = "FROM FirePatrolBuildingType WHERE campusId="+campusId+" ORDER BY sort";
			List<FirePatrolBuildingType> firePatrolBuildingTypes = buildingTypeService.getBuildingType(hql);
			MessageListBean<FirePatrolBuildingType> message = new MessageListBean<>();
			message.setCode(200);
			if(firePatrolBuildingTypes != null && firePatrolBuildingTypes.size() > 0) {
				message.setData(firePatrolBuildingTypes);
				message.setMessage("success");
				message.setStatus(true);
			}
			out.write(JSON.toJSONString(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * 这里进行分类统计
	 * @param type
	 */
	public void fireAdminTypeStatistical(int type) {
		//获取每月异常设备统计
	}


	/**
	 * 跳转到消防巡查使用端统计
	 * @param jobNum	工号
	 * @return
	 */
	@RequestMapping("/toFirePatrolUseStatisticsPage")
	public ModelAndView toFirePatrolUseStatisticsPage(String jobNum) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("jobNum",jobNum);
		//这里默认进去是本月
		mv.addObject("isThisMonth",true);
		mv.setViewName("fire_patrol_app_page/fire-patrol-statistical");
		return mv;
	}


	/**
	 * 进行获取消防巡查使用端巡查记录统计，这里是针对各人的巡查记录进行统计
	 * @param jobNum		工号
	 * @param date			时间（这里上传的时间类型为：2018-05）
	 * @param buildingType  大楼类型
	 * @param page			当前页数
	 * @param pageSize 		页数条数
	 * @param response
	 */
	@RequestMapping("/getFirePatrolUseStatisticsData")
	public void getFirePatrolUseStatisticsData(String jobNum,String date,int buildingType,
                                               int page,int pageSize,HttpServletResponse response) {
//		response.setContentType("text/xml; charset=utf-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
//		response.setCharacterEncoding("utf-8");
	    PrintWriter out = null;
	    MessageBean messageBean = new MessageBean();
        try {
            out = response.getWriter();
			messageBean.setCode(200);
			if(jobNum == null || "".equals(jobNum) || buildingType < 0) {
                messageBean.setStatus(false);
                messageBean.setMessage("请求参数不能为空");
                out.write(JSON.toJSONString(messageBean));
                return;
            }

            page = (page <=0 ? 1 : page);
            pageSize = (pageSize <= 0 ? 20 : pageSize);
			buildingType = (buildingType <=0 ? 0 : buildingType);

            //判断这个人是否存在
            FirePatrolUser patrolUser = firePatrolUserService.getByJobNum(jobNum);
            if(patrolUser == null) {
                messageBean.setMessage("该用户不存在");
                messageBean.setStatus(false);
                messageBean.setCode(200);
                out.write(JSON.toJSONString(messageBean));
                return;
            }

            List<FirePatrolUseStatisticsVO> useStatisticsVOList = new ArrayList<>();
			FirePatrolUseStatisticsVO useStatisticsVO = new FirePatrolUseStatisticsVO();

            //这里进行统计列表信息
            String hql = "FROM FirePatrolBuildingType WHERE campusId=1";
            List<FirePatrolBuildingType> firePatrolBuildingTypes = buildingTypeService.getBuildingType(hql);

            //设置列表头部信息
            if(firePatrolBuildingTypes != null && firePatrolBuildingTypes.size() > 0) {
                FirePatrolUseBarListVO firePatrolUseBarListVO = new FirePatrolUseBarListVO();
                firePatrolUseBarListVO.setFirePatrolBuildingTypes(firePatrolBuildingTypes);
                useStatisticsVO.setFirePatrolUseBarListVO(firePatrolUseBarListVO);
            }

			//这里进行计算开始结束时间
			String startStr = "";
            String endStr = "";
			Map<String,Date> monthStartEnd = null;
			Date tempDate = null;
			if(date == null) {
				//下面是真实的
				monthStartEnd = DateMUtils.getNowMonth(new Date());

			}

			if(date != null && date.contains("-")) {
				date = date + "-01 00:00:00";
				tempDate = DateMUtils.stringFormatDate(date);
				monthStartEnd = DateMUtils.getNowMonth(tempDate);
			}

			//这里进行时间格式化
			if(tempDate == null) {
				tempDate = new Date();
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M");
			String month = simpleDateFormat.format(tempDate);
			useStatisticsVO.setDateStr(month);

			if(monthStartEnd != null) {
				startStr = DateMUtils.dateFormatStr(monthStartEnd.get(DateMUtils.START_DATE),DateMUtils.FORMAT_DATE);
				endStr = DateMUtils.dateFormatStr(monthStartEnd.get(DateMUtils.END_DATE),DateMUtils.FORMAT_DATE);
			}

            //查询该人本月巡查数据个数
			String sql = "SELECT count (*) AS totalEqui," +
					"SUM (CASE WHEN fpi.status = 1 THEN 1 ELSE 0 END) AS normalEqui," +
					"SUM (CASE WHEN fpi.status = 0 THEN 1 ELSE 0 END) AS exceptionEqui," +
					"to_char(fpi.last_update_time,'YYYY-MM') AS month," +
					"SUM(fpi.check_count) AS totalcheckcount " +
					"FROM fire_fight_equipment_history AS fpi WHERE job_num = '"+ jobNum +"' ";

			if(startStr != null && endStr != null) {
				sql += " AND fpi.last_update_time BETWEEN '"+startStr+"' AND '"+endStr+"' GROUP BY month,fpi.check_count";
			}


			//这里进行type判断，0为全部
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT COUNT (*) AS totalEqui,");
			sb.append("SUM (CASE WHEN ffqh.status = 1 THEN 1 ELSE 0 END) AS normalEqui,");
			sb.append("SUM (CASE WHEN ffqh.status = 0 THEN 1 ELSE 0 END) AS exceptionEqui,");
			sb.append("to_char(ffqh.last_update_time,'YYYY-MM') AS MONTH,");
			sb.append("SUM (ffqh.check_count) AS totalcheckcount ");
			sb.append("FROM fire_fight_equipment_history ffqh ");
			sb.append("LEFT JOIN fire_patrol_building_info fpbi ON ffqh.building_code = fpbi.building_id ");
			sb.append("LEFT JOIN fire_patrol_building_type fpbt ON fpbt.\"type\" = fpbi.\"type\" ");
			sb.append("WHERE ");
			//不是全部的情况
			if(buildingType > 0) {
				sb.append(" fpbt.\"type\" = '" + buildingType + "' AND");
			}
			sb.append(" ffqh.job_num = '"+ jobNum + "' ");
			sb.append(" AND ffqh.last_update_time BETWEEN '" + startStr + "' AND '" + endStr + "' GROUP BY MONTH");

			System.out.println(sb.toString());
			//保存巡查数量
			List<Object[]> statisticsNum = fireFightEquipmentHistoryService.getBySql(sb.toString());
			if(statisticsNum != null && statisticsNum.size() > 0) {
					Object[] objects = statisticsNum.get(0);
					FirePatrolUseEquNumVO firePatrolUseEquNumVO = new FirePatrolUseEquNumVO();
					//这里进行设置
					if (objects[0] != null) {
						//总设备数
						firePatrolUseEquNumVO.setAllCount(Integer.parseInt(objects[0].toString()));
					}
					if (objects[1] != null) {
						//正常设备
						firePatrolUseEquNumVO.setNormalCount(Integer.parseInt(objects[1].toString()));
					}
					if (objects[2] != null) {
						//异常设备
						firePatrolUseEquNumVO.setExceptionCount(Integer.parseInt(objects[2].toString()));
					}
					if (objects[3] != null) {
						//时间
						firePatrolUseEquNumVO.setMonth(objects[3].toString());
					}
					if (objects[4] != null) {
						//巡查次数
						firePatrolUseEquNumVO.setTotalcheckcount(Integer.parseInt(objects[4].toString()));
					}
					//设置统计数量
					useStatisticsVO.setFirePatrolUseEquNumVO(firePatrolUseEquNumVO);
				}

			//--------------------------------

            //TODO 统计
			StringBuffer entitySb = new StringBuffer();
			entitySb.append("SELECT fpi.*,fpi.id as ido,ffe.NAME,ffeh.location_name,fpe.* ");
			entitySb.append("FROM fire_patrol_info as fpi ");
			entitySb.append("LEFT Join fire_fight_equipment_history as ffeh on ffeh.old_id=fpi.equipment_id and ffeh.last_update_time BETWEEN '"+startStr+"'"+
					"AND '"+endStr+"'" +
					"LEFT JOIN fire_fight_equipment as ffe on ffe.id=fpi.equipment_id");
            entitySb.append(" LEFT JOIN fire_patrol_exception AS fpe ON CAST(fpi.exception_types as int) = fpe.id\t");
			entitySb.append(" WHERE");
			entitySb.append(" fpi.job_num = '"+ jobNum + "' ");
			entitySb.append(" AND fpi.timestamp BETWEEN '" + startStr + "' AND '" + endStr + "'");
			entitySb.append(" ORDER BY fpi.timestamp DESC ").append(" LIMIT ").append(pageSize).append(" OFFSET ").append((page-1)*pageSize);
			System.out.println(entitySb.toString());
			//这里进行查询数据
			List<Map<String,Object>> entitys = firePatrolInfoDao.findForJdbc(entitySb.toString());
			List<FirePatrolInfo> histories = FirePatrolInfo.toObjectList(entitys);
			//这里进行组装数据

			for(Map<String,Object> entity:entitys){
				Integer ido = Integer.valueOf(entity.get("ido").toString());

				sql="SELECT * FROM fire_patrol_img where info_id='"+ido+"'";
				List<Map<String,Object>> list = firePatrolBuildingInfoDao.findForJdbc(sql);
				List<String> strings = new ArrayList<String>();
				for (Map<String,Object> one:list){
					Object imgUrl = one.get("img_url");
					String s = String.valueOf(imgUrl);
					strings.add(s);
				}
				entity.put("img",strings);
//				entity.put("description",)
				String s="";
				if(entity.get("exception_types")!=null){
					Integer fpid = Integer.valueOf(entity.get("exception_types").toString());
					sql="SELECT * FROM fire_patrol_exception where id='"+fpid+"'";
					List<Map<String,Object>> list1 = firePatrolBuildingInfoDao.findForJdbc(sql);
					Object exceptionName = list1.get(0).get("exception_name");
					s = String.valueOf(exceptionName);
				}
				entity.put("exception_name",s);

			}


			//查询记录总条数
			StringBuffer countSb = new StringBuffer();
			countSb.append("SELECT count(*) ");
			countSb.append("FROM fire_fight_equipment_history ffqh ");
			countSb.append("LEFT JOIN fire_patrol_building_info fpbi ON ffqh.building_code = fpbi.building_id ");
			countSb.append("LEFT JOIN fire_patrol_building_type fpbt ON fpbt.\"type\" = fpbi.\"type\" ");
			countSb.append("WHERE ");
			//不是全部的情况
			if(buildingType > 0) {
				entitySb.append(" fpbt.\"type\" = '" + buildingType + "' AND");
			}
			countSb.append(" ffqh.job_num = '"+ jobNum + "' ");
			countSb.append(" AND ffqh.last_update_time BETWEEN '" + startStr + "' AND '" + endStr + "'");

			long count = fireFightEquipmentHistoryDao.getCountForJdbc(countSb.toString());

			//--------------------------------

			useStatisticsVO.setNextPage((page * pageSize >= count) ? false : true);
			useStatisticsVO.setPage(page);
			useStatisticsVO.setPageSize(pageSize);
//			useStatisticsVO.setFirePatrolInfoList(histories);

			useStatisticsVO.setFirePatrolInfoListNew(entitys);


			//设置选中类型0 为全部 其他的是大楼type
			useStatisticsVO.setBuildingType(buildingType);
			messageBean.setMessage("success");
			messageBean.setStatus(true);
			messageBean.setData(useStatisticsVO);
			out.write(JSON.toJSONString(messageBean));
        } catch (IOException e) {
            e.printStackTrace();
            messageBean.setMessage("服务出现错误，请联系技术人员");
            messageBean.setCode(200);
            messageBean.setStatus(false);
            out.write(JSON.toJSONString(messageBean));
        }finally {
            out.flush();
            out.close();
        }
	}

	/**
	 * 进行判断是否在区域内(这里不光进行判断距离，同时还需要进行判断是否为开始，如果是则进行设置开始)
	 * @param lon			经度
	 * @param lat			纬度
	 * @param equipmentId	设备id
	 * @param jobNum		工号
	 * @param campusNum		校区id
	 * @param buildingId	大楼id
	 */
	@RequestMapping("/judgeFirePatrolSweep")
	public void judgeFirePatrolSweep(String jobNum,Integer campusNum,Integer equipmentId,String buildingId,double lon,double lat,HttpServletResponse response) {

		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type","application/json;charset=utf-8");
			out = response.getWriter();
			FirePatrolConfig firePatrolConfig = firePatrolConfigService.getById(1);
			MessageBean messageBean = new MessageBean();
			messageBean.setCode(200);

			if(jobNum == null && campusNum == null) {
				messageBean.setStatus(false);
				messageBean.setMessage("上传参数为空");
				out.write(JSON.toJSONString(messageBean));
				return;
			}

            //进行判断是否是第一次扫码
            boolean isStart = firePatrolTimeQuantumService.isStartTime(jobNum,campusNum);

            if(isStart) {
                isStart(jobNum,campusNum,firePatrolTimeQuantumService);
            }

			//计算距离
			GisUtil g = GisUtil.getInstance();
			boolean isExceedDistance = false;

			//1、有设备id，（室内和室外单个二维码）
			if(equipmentId != null && equipmentId > 0) {
				//进行查询设备经纬度
				FireFightEquipment fireFightEquipment = this.fireFightEquipmentService.getById(equipmentId);
				double flon = fireFightEquipment.getLon();
				double flat = fireFightEquipment.getLat();
				double distance = g.distanceByLngLat(lon, lat, flon,flat);
				if( distance > firePatrolConfig.getDistance()) {
					isExceedDistance = true;
				}
			}

			//2、无设备id，有大楼id（室内所有设备二维码）
			if(equipmentId == null && buildingId != null) {
				//进行查询大楼
				String sql = "SELECT st_distance((SELECT geometry FROM fire_patrol_building_info WHERE buildingId="+buildingId+"), st_geometryfromtext('POINT("+lon+" "+lat+")')) AS distance";
				double distance = firePatrolBuildingInfoService.getDistance(sql);
				if(distance > firePatrolConfig.getDistance()) {
					isExceedDistance = true;
				}

			}
			//3、无大楼id，无设备id（室外所有消防设备二维码）
			if(equipmentId == null && buildingId == null) {
				//查询配置
				double outLon = firePatrolConfig.getOutAllNormalSweepLon();
				double outLat = firePatrolConfig.getOutAllNormalSweepLat();
				double distance = g.distanceByLngLat(lon,lat,outLon,outLat);
				if(distance > firePatrolConfig.getDistance()) {
					isExceedDistance = true;
				}
			}

			//超过了指定距离
			if(isExceedDistance) {
				messageBean.setMessage("请到达设备范围附近" + firePatrolConfig.getDistance() + "米，进行扫码！");
				messageBean.setStatus(false);
				out.write(JSON.toJSONString(messageBean));
				return;
			}else {
				messageBean.setMessage("success");
				messageBean.setStatus(true);
				out.write(JSON.toJSONString(messageBean));
				return;
			}

		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			out.flush();
			out.close();
		}
	}


	/**
	 * 获取fpid
	 * @param equpmentId
	 */
	@RequestMapping("/getFpidByOldId")
	public void getFirePatrolInfoByOldId(int equpmentId,HttpServletResponse response) {

		PrintWriter out = null;
		response.setCharacterEncoding("UTF-8");
		try {
			out = response.getWriter();
			MessageBean messageBean = new MessageBean();
			String sql = "SELECT id AS fpid FROM fire_patrol_info WHERE equipment_id='" +equpmentId+ "'" + " AND is_newest=1";
			List<Map<String,Object>> list = firePatrolBuildingInfoDao.findForJdbc(sql);
			messageBean.setCode(200);
			if(list != null && list.size() > 0) {
				Map<String,Object> map = list.get(0);
				int fpid = (Integer)map.get("fpid");
				messageBean.setMessage("success");
				messageBean.setStatus(true);
				messageBean.addPropertie("fpid",fpid);
			}else {
				messageBean.setMessage("没有查询到异常详情");
				messageBean.setStatus(false);
			}
			System.out.println(JSON.toJSONString(messageBean));
			out.write(JSON.toJSONString(messageBean));
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.flush();
			out.close();
		}


	}


	/**
	 * 返回状态，当前是否在巡查(这里根据开始)
	 * @param jobNum		工号
	 * @param campusNum		校区id
	 */
	@RequestMapping("/getFirePatrolUseStatus")
	public void getFirePatrolUseStatus(String jobNum,int campusNum,HttpServletResponse response) {
		PrintWriter out = null;
		MessageBean messageBean = new MessageBean();
		try {

			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			messageBean.setCode(200);

			if(jobNum == null || campusNum <= 0) {
				messageBean.setMessage("请求参数不能为空");
				messageBean.setStatus(false);
				out.write(JSON.toJSONString(messageBean));
			}

			//查看当前账号是否在巡查状态
			messageBean.setStatus(true);
			messageBean.setMessage("success");
			String hql = "FROM FirePatrolTimeQuantum WHERE jobNum='" + jobNum + "' AND " + "campusNum=" + campusNum + " AND isNew=1";
			List<FirePatrolTimeQuantum> firePatrolTimeQuantums = firePatrolTimeQuantumService.getByHql(hql);
			if(firePatrolTimeQuantums != null && firePatrolTimeQuantums.size() > 0) {
				FirePatrolTimeQuantum firePatrolTimeQuantum = firePatrolTimeQuantums.get(0);
				if(firePatrolTimeQuantum != null) {
					if(firePatrolTimeQuantum.getEndTime() == null) {
						//这里在巡查状态
						messageBean.setData(firePatrolTimeQuantum);
						messageBean.addPropertie("isFirePatrolStatus",true);
						out.write(JSON.toJSONString(messageBean));
						return;
					}
				}
			}

			messageBean.addPropertie("isFirePatrolStatus",false);
			out.write(JSON.toJSONString(messageBean));
			return;
		}catch (Exception e) {
			e.printStackTrace();
			messageBean.setStatus(false);
			messageBean.setMessage("出现错误，请联系技术人员");
			messageBean.setCode(200);
			out.write(JSON.toJSONString(messageBean));
		}finally {
			out.flush();
			out.close();
		}

	}


	/**
	 * 结束消防巡查状态
	 * @param jobNum		工号
	 * @param campusNum		校区id
	 * @param response
	 */
	@RequestMapping("/endFirePatrolUse")
	public void endFirePatrolUse(String jobNum,int campusNum,HttpServletResponse response) {
		PrintWriter out = null;
		try {

			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			MessageBean messageBean = new MessageBean();
			if(jobNum == null || campusNum < 0) {
				messageBean.setStatus(false);
				messageBean.setMessage("参数不能为空");
				out.write(JSON.toJSONString(messageBean));
				return;
			}

			//查询出最新的一条内容
			String hql = "FROM FirePatrolTimeQuantum WHERE jobNum='" + jobNum + "' AND " + "campusNum=" + campusNum + " AND isNew=1";
			List<FirePatrolTimeQuantum> firePatrolTimeQuantums = firePatrolTimeQuantumService.getByHql(hql);
			if(firePatrolTimeQuantums != null && firePatrolTimeQuantums.size() > 0) {
				FirePatrolTimeQuantum firePatrolTimeQuantum = firePatrolTimeQuantums.get(0);
				firePatrolTimeQuantum.setEndTime(new Date());
				firePatrolTimeQuantum.setIsNew(0);
				firePatrolTimeQuantumService.updateFirePatrolTimeQuantum(firePatrolTimeQuantum);
				messageBean.setMessage("结束巡查成功");
				messageBean.setStatus(true);
				out.write(JSON.toJSONString(messageBean));
				return;
			}else {
				messageBean.setStatus(false);
				messageBean.setMessage("结束巡查失败，请联系技术人员");
				out.write(JSON.toJSONString(messageBean));
				return;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			out.flush();
			out.close();
		}
	}

    /**
     * 开始巡查状态
     * @param jobNum 工号
     * @param campusNum 校区id
     * @param firePatrolTimeQuantumService
     */
    void isStart(String jobNum,int campusNum,FirePatrolTimeQuantumService firePatrolTimeQuantumService){
        //需要将之前最新的一条数据状态进行更改
        String hql = "FROM FirePatrolTimeQuantum WHERE jobNum='" + jobNum + "' AND " + "campusNum=" + campusNum + " AND isNew=1";
        List<FirePatrolTimeQuantum> firePatrolTimeQuantums = firePatrolTimeQuantumService.getByHql(hql);
        if(firePatrolTimeQuantums != null && firePatrolTimeQuantums.size() > 0) {
            FirePatrolTimeQuantum firePatrolTimeQuantum = firePatrolTimeQuantums.get(0);
            //这里进行更新状态
            firePatrolTimeQuantum.setIsNew(0);
            firePatrolTimeQuantumService.updateFirePatrolTimeQuantum(firePatrolTimeQuantum);
        }
        //是的话进行保存用户的相关信息
        FirePatrolTimeQuantum firePatrolTimeQuantum = new FirePatrolTimeQuantum();
        firePatrolTimeQuantum.setCampusNum(campusNum);
        firePatrolTimeQuantum.setIsNew(1);
        firePatrolTimeQuantum.setStartTime(new Date());
        firePatrolTimeQuantum.setJobNum(jobNum);
        firePatrolTimeQuantumService.addFirePatrolTimeQuantum(firePatrolTimeQuantum);
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
