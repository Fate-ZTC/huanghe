package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.model.PatrolException;
import com.parkbobo.service.FireFightEquipmentService;
import com.parkbobo.service.FirePatrolInfoService;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolExceptionService;
import com.parkbobo.service.PatrolLocationInfoService;
import com.parkbobo.service.PatrolUserRegionService;
import com.parkbobo.service.PatrolUserService;

@Controller
public class FirePatrolManagerController {

	@Resource
	private PatrolUserService patrolUserService;

	@Resource
	private PatrolConfigService patrolConfigService;

	@Resource
	private PatrolUserRegionService patrolUserRegionService;

	@Resource
	private PatrolLocationInfoService patrolLocationInfoService;
	
	@Resource
	private FireFightEquipmentService fireFightEquipmentService;
	
	@Resource
	private FirePatrolInfoService firePatrolInfoService;
	
	@Resource
	private PatrolExceptionService patrolExceptionService;

	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

	/**
	 * 查看设备
	 * @param checkExp 是否查看异常设备
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("checkEquipment")
	public void checkEquipment(Integer checkExp,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		List<FirePatrolInfo> firePatrolInfos = new ArrayList<FirePatrolInfo>();
		List<FireFightEquipment> fireFightEquipments = null;
		if (checkExp.equals(1)) {
			fireFightEquipments = fireFightEquipmentService.getByProperty("status", (short)2);
		}else{
			fireFightEquipments = fireFightEquipmentService.getAll();
		}
		if (fireFightEquipments!=null && fireFightEquipments.size()>0) {
			for (FireFightEquipment fireFightEquipment : fireFightEquipments) {
				List<FirePatrolInfo> firePatrolInfo = firePatrolInfoService.getByProperty("equipmentId.id", fireFightEquipment.getId(), "timestamp", false);
				if (firePatrolInfo!=null && firePatrolInfo.size()>0) {
					firePatrolInfos.add(firePatrolInfo.get(0));
				}
			}
		}
		if (firePatrolInfos != null && firePatrolInfos.size()>0) {
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(firePatrolInfos,features)+"}");
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无该区域消防设备信息\"}");
		}
		out.flush();
		out.close();
	}
	/**
	 * 查看异常详情
	 * @param fpId 巡查信息id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("exceptionDetail")
	public void exceptionDetail(Integer fpId,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		FirePatrolInfo firePatrolInfo = firePatrolInfoService.get(fpId);
		if (firePatrolInfo != null) {
			String excptionTypes = firePatrolInfo.getExceptionTypes();
			excptionTypes = excptionTypes.substring(0, excptionTypes.length()-1);
			String hql = "FROM PatrolException WHERE id IN ("+ excptionTypes +")";
			List<PatrolException> patrolExceptions = patrolExceptionService.getByHQL(hql);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(firePatrolInfo,features)+",\"excptionData\":"+JSONObject.toJSONString(patrolExceptions,features)+"}");
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无该消防设备信息\"}");
		}
		out.flush();
		out.close();
	}
	/**
	 * 巡查统计
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("statistical")
	public void statistical(HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		List<FireFightEquipment> fireFightEquipments = fireFightEquipmentService.getAll();
		int excEqu=0;
		int patrolEqu=0;
		for (FireFightEquipment fireFightEquipment : fireFightEquipments) {
			if (fireFightEquipment.getStatus().equals(2)) {
				excEqu++;
			}
			if (fireFightEquipment.getCheckStatus().equals(1)) {
				patrolEqu++;
			}
		}
		List<FirePatrolInfo> firePatrolInfos = new ArrayList<FirePatrolInfo>();
		if (fireFightEquipments!=null && fireFightEquipments.size()>0) {
			for (FireFightEquipment fireFightEquipment : fireFightEquipments) {
				List<FirePatrolInfo> firePatrolInfo = firePatrolInfoService.getByProperty("equipmentId.id", fireFightEquipment.getId(), "timestamp", false);
				if (firePatrolInfo!=null && firePatrolInfo.size()>0) {
					firePatrolInfos.add(firePatrolInfo.get(0));
				}
			}
		}
		if (firePatrolInfos != null && firePatrolInfos.size()>0) {
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(firePatrolInfos,features)+"}");
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无该区域消防设备信息\"}");
		}
		out.flush();
		out.close();
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
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);
        System.out.println(year1 + "  " + month1);
        System.out.println(year2 + "  " + month2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
    } 
	
}
