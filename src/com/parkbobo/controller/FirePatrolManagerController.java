package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.FireFightEquipment;
import com.parkbobo.model.FireFightEquipmentHistory;
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.model.PatrolException;
import com.parkbobo.service.FireFightEquipmentHistoryService;
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
	private FireFightEquipmentHistoryService fireFightEquipmentHistoryService;
	
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
		String sql = "SELECT count (*) AS totalEqui, SUM (CASE WHEN fpi.status = 1 THEN 1 ELSE 0 END ) AS normalEqui,"+
				"SUM (CASE WHEN fpi.status = 0 THEN 1 ELSE 0 END ) AS abnormalEqui,"+
				"SUM (CASE WHEN fpi.check_status = 1 THEN 1 ELSE 0 END ) AS patrolEqui,"+
				"SUM (CASE WHEN fpi.check_status = 0 THEN 1 ELSE 0 END ) AS unPatrolEqui,"+
				"to_char(fpi.last_update_time,'YYYY-MM') AS month  FROM fire_fight_equipment_history AS fpi GROUP BY month";
		List<Object[]> objects = fireFightEquipmentHistoryService.getBySql(sql);
		StringBuilder sb =new StringBuilder();
		sb.append("{\"status\":\"true\",\"errorCode\":0,\"list\":[");
		if(objects!=null && objects.size()>0){
			int i = 0;
			for (Object[] object : objects) {
				sb.append("{");
				sb.append("\"normalEqui\":\""+object[0]+"\",");
				sb.append("\"abnormalEqui\":\""+object[1]+"\",");
				sb.append("\"patrolEqui\":\""+object[2]+"\",");
				sb.append("\"unPatrolEqui\":\""+object[3]+"\",");
				sb.append("\"totalEqui\":\""+object[4]+"\",");
				sb.append("\"month\":\""+object[5]+"\"");
				if(i==objects.size()-1){
					sb.append("}");
				}else{
					sb.append("},");
				}
				i++;
			}
		}
		sb.append("]}");
		out.print(sb.toString());
		out.flush();
		out.close();
	}
	/**
	 * 查看异常和未巡查设备
	 * @param startDate 开始时间
	 * @param exceEqui 异常设备
	 * @param checkEqui 巡查设备
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("checkEqui")
	public void checkEqui(String startDate,String exceEqui,String checkEqui,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (StringUtils.isNotBlank(startDate)) {
				startDate = startDate + "-01 00:00:00";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(startDate));
				calendar.add(Calendar.MONTH, 1);
				String endDate = sdf.format(calendar.getTime());
				String hql = "from FireFightEquipmentHistory where lastUpdateTime >= '"+startDate+"' and lastUpdateTime < '"+endDate+"'";
				if (StringUtils.equals("exceEqui", exceEqui)) {
					hql += " and status = 0";
				}
				if (StringUtils.equals("checkEqui", checkEqui)) {
					hql += " and checkStatus = 0";
				}
				List<FireFightEquipmentHistory> fireFightEquis = fireFightEquipmentHistoryService.getByHQL(hql);
				out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(fireFightEquis,features)+"}");
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"参数不完整\"}");
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"网络错误\"}");
		}finally {
			out.flush();
			out.close();
		}
	}
}
