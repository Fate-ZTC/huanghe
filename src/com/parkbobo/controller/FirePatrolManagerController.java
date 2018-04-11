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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.*;
import com.parkbobo.service.*;
import com.parkbobo.utils.PageBean;

@Controller
public class FirePatrolManagerController {

	@Resource
	private FirePatrolUserService firePatrolUserService;

	@Resource
	private FirePatrolExceptionService firePatrolExceptionService;
	
	@Resource
	private FireFightEquipmentService fireFightEquipmentService;

	@Resource
	private FireFightEquipmentHistoryService fireFightEquipmentHistoryService;

	@Resource
	private FirePatrolInfoService firePatrolInfoService;

	@Resource
	private FirePatrolImgService firePatrolImgService;

	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};
	/**
	 * 获取所有巡查员
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("getAllFirePatrolUser")
	public void getAllUser(Integer page,Integer pageSize,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String hql = "FROM FirePatrolUser WHERE isDel=0";
			List<FirePatrolUser> allUser = this.firePatrolUserService.getAllUser(hql);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(allUser,features)+"}");
		} catch (IOException e) {
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
	 * 新增消防巡查员
	 * @param jobNum 工号
	 * @param password 密码
	 * @param username 用户名
	 * @param campusNum 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("addFirePatrolUser")
	public void addUser(String jobNum,String password,String username,Integer campusNum,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			FirePatrolUser patrolUser = new FirePatrolUser();
			Date date = new Date();
			patrolUser.setCreateTime(date);
			patrolUser.setCampusNum(campusNum);
			patrolUser.setJobNum(jobNum);
			patrolUser.setLastUpdateTime(date);
			patrolUser.setPassword(password);
			patrolUser.setIsDel((short)0);
			//patrolUser.setUsername(new String(username.getBytes("ISO-8859-1"),"utf-8"));
			if (username != null) {
//				patrolUser.setUsername(URLDecoder.decode(URLEncoder.encode(username, "ISO8859_1"), "UTF-8"));
				patrolUser.setUsername(username);
			} else {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户名不能为空\"}");
				return;
			}
			int flag = this.firePatrolUserService.addUser(patrolUser);
			if (flag == 2) {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"新增出错\"}");
			}
			if (flag == 0) {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账号已存在\"}");
			}
			if (flag == 1) {
				out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"新增成功\"}");
			} 
		} catch (Exception e) {
			out.print("{\"status\":\"false\",\"Code\":0,\"errorMsg\":\"未知异常\"}");
		}
	}

	/**
	 *返显巡查员信息 
	 *@param id 巡查员id
	 * @throws IOException 
	 */
	@RequestMapping("getFirePatrolUserById")
	public void getUserById(Integer id,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			FirePatrolUser patrolUser = this.firePatrolUserService.getById(id);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUser,features)+"}");
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
	 * 更新巡查员
	 * @param id 巡查员id
	 * @param username 用户名
	 * @param password 密码
	 * @param jobNum 工号
	 * @param campusNum 校区id
	 * @param response 
	 * @throws IOException
	 */
	@RequestMapping("updateFirePatrolUser")
	public void updateUser(Integer id,String username,String password,String jobNum,Integer campusNum,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			FirePatrolUser patrolUser = new FirePatrolUser();
			patrolUser.setId(id);
			patrolUser.setCampusNum(campusNum);
			patrolUser.setJobNum(jobNum);
			patrolUser.setLastUpdateTime(new Date());
			patrolUser.setPassword(password);
			patrolUser.setCreateTime(this.firePatrolUserService.getById(id).getCreateTime());
			patrolUser.setIsDel((short)0);
			if(username!=null){
				patrolUser.setUsername(username);
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"用户名不能为空\"}");
				return;
			}
			int flag = this.firePatrolUserService.updateUser(patrolUser);
			if (flag == 2) {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"修改出错\"}");
			}
			if (flag == 0) {
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"账号已存在\"}");
			}
			if (flag == 1) {
				out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"修改成功\"}");
			} 
		} catch (Exception e) {
			out.print("{\"status\":\"false\",\"Code\":0,\"errorMsg\":\"未知异常\"}");
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * 删除用户
	 * @param  id 巡查员id
	 * @throws IOException 
	 */
	@RequestMapping("deleteFirePatrolUser")
	public void deleteUser(HttpServletResponse response,Integer id) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			FirePatrolUser patrolUser = this.firePatrolUserService.getById(id);
			if(patrolUser == null){
				out.print("{\"status\":\"false\",\"Code\":-1,\"Msg\":\"用户不存在\"}");
				return;
			}else{
				patrolUser.setIsDel((short)1);
				patrolUser.setLastUpdateTime(new Date());
				this.firePatrolUserService.update(patrolUser);
			}
			out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"删除成功\"}");
		} catch (IOException e) {
			if(out==null){
				out=response.getWriter();
			}
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * 查看设备
	 * @param checkExp 是否查看异常设备   0 否  1 是
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("checkEquipment")
	public void checkEquipment(Integer checkExp,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		List<FirePatrolInfo> firePatrolInfos = new ArrayList<FirePatrolInfo>();
		List<FireFightEquipment> fireFightEquipments = null;
		if (checkExp!=null && checkExp.equals(1)) {
			fireFightEquipments = fireFightEquipmentService.getByProperty("status", (short)0);
		}else{
			fireFightEquipments = fireFightEquipmentService.getAll();
		}
		if (fireFightEquipments!=null && fireFightEquipments.size()>0) {
			for (FireFightEquipment fireFightEquipment : fireFightEquipments) {
				List<FirePatrolInfo> firePatrolInfo = firePatrolInfoService.getByProperty("fireFightEquipment.id", fireFightEquipment.getId(), "timestamp", false);
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

		if(fpId == null) {
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"参数不能为空\"}");
			return;
		}

		FirePatrolInfo firePatrolInfo = firePatrolInfoService.get(fpId);
		if (firePatrolInfo != null) {
			String excptionTypes = firePatrolInfo.getExceptionTypes();
//			excptionTypes = excptionTypes.substring(0, excptionTypes.length()-1);
			String hql = "FROM FirePatrolException WHERE id IN ("+ excptionTypes +")";
			List<FirePatrolException> patrolExceptions = firePatrolExceptionService.getByHQL(hql);
			//进行图片查询
			String imgHql = "FROM FirePatrolImg WHERE infoId=" + fpId;
			List<FirePatrolImg> firePatrolImgs = firePatrolImgService.getByHql(imgHql);
			List<String> imgPaths = null;
			if(firePatrolImgs != null && firePatrolImgs.size() > 0) {
				imgPaths = new ArrayList<>();
				for(FirePatrolImg firePatrolImg : firePatrolImgs) {
					imgPaths.add(firePatrolImg.getImgUrl());
				}
			}
			out.print("{\"status\":\"true\",\"Code\":1,\"imgPaths\":"+ JSON.toJSONString(imgPaths)+",\"data\":"+JSONObject.toJSONString(firePatrolInfo,features)+",\"excptionData\":"+JSONObject.toJSONString(patrolExceptions,features)+"}");
		}else{
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无该消防设备信息\"}");
		}
		out.flush();
		out.close();
	}
	/**
	 * 巡查统计
	 * @param response
	 * @param page			当前页数
	 * @param pageSize		当前页数量
	 * @throws IOException
	 */
	@RequestMapping("statistical")
	public void statistical(String startDate,Integer page,Integer pageSize,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");

		if(page == null || page == 0) {
			page = 1;
		}

		PrintWriter out = response.getWriter();
		String sql = "SELECT count (*) AS totalEqui, SUM (CASE WHEN fpi.status = 1 THEN 1 ELSE 0 END ) AS normalEqui,"+
				"SUM (CASE WHEN fpi.status = 0 THEN 1 ELSE 0 END ) AS abnormalEqui,"+
				"SUM (CASE WHEN fpi.check_status = 1 THEN 1 ELSE 0 END ) AS patrolEqui,"+
				"SUM (CASE WHEN fpi.check_status = 0 THEN 1 ELSE 0 END ) AS unPatrolEqui,"+
				"to_char(fpi.last_update_time,'YYYY-MM') AS month  FROM fire_fight_equipment_history AS fpi ";
		int count = 0;
		String countHql = "SELECT to_char(last_update_time, 'YYYY-MM') AS MONTH FROM fire_fight_equipment_history";
		if (StringUtils.isNotBlank(startDate)) {
			try {
				startDate = startDate + "-01 00:00:00";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(startDate));
				calendar.add(Calendar.MONTH, 1);
				String endDate = sdf.format(calendar.getTime());
				sql += " WHERE fpi.last_update_time BETWEEN '"+startDate+"' AND '"+endDate+"'";
				countHql += " WHERE last_update_time BETWEEN '" + startDate + "' AND '" + endDate + "'";
			} catch (ParseException e) {
				e.printStackTrace();
				out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"网络错误\"}");
				return;
			}
		}
		//这里进行计算offset
		int start = (page -1) * pageSize;
		sql += " GROUP BY month ORDER BY month DESC LIMIT "+pageSize+" OFFSET " + start;
		countHql += " GROUP BY month";
		List<Object[]> objectCount = fireFightEquipmentHistoryService.getBySql(countHql);
		List<Object[]> objects = fireFightEquipmentHistoryService.getBySql(sql);
		//是否还有下一页
		boolean isNextPage = false;
		count = objectCount.size();
		if((page * pageSize) < count) {
			isNextPage = true;
		}

		StringBuilder sb =new StringBuilder();
		sb.append("{\"status\":\"true\",\"page\":"+page+",\"pageSize\":"+pageSize+",\"count\":"+count+",\"isNextPage\":"+isNextPage+",\"errorCode\":0,\"list\":[");
		if(objects!=null && objects.size()>0){
			int i = 0;
			for (Object[] object : objects) {
//				sb.append("{");
//				sb.append("\"normalEqui\":\""+object[0]+"\",");
//				sb.append("\"abnormalEqui\":\""+object[1]+"\",");
//				sb.append("\"patrolEqui\":\""+object[2]+"\",");
//				sb.append("\"unPatrolEqui\":\""+object[3]+"\",");
//				sb.append("\"totalEqui\":\""+object[4]+"\",");
//				sb.append("\"month\":\""+object[5]+"\"");
//				if(i==objects.size()-1){
//					sb.append("}");
//				}else{
//					sb.append("},");
//				}
//				i++;
				sb.append("{");
				sb.append("\"totalEqui\":\""+object[0]+"\",");
				sb.append("\"normalEqui\":\""+object[1]+"\",");
				sb.append("\"abnormalEqui\":\""+object[2]+"\",");
				sb.append("\"patrolEqui\":\""+object[3]+"\",");
				sb.append("\"unPatrolEqui\":\""+object[4]+"\",");
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
	public void checkEqui(String startDate,String exceEqui,String checkEqui,int pageSize,int page,HttpServletResponse response) throws IOException{
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
				PageBean<FireFightEquipmentHistory> fireFightEquis = fireFightEquipmentHistoryService.pageQuery(hql, pageSize, page);

				if (fireFightEquis.getList()!=null && fireFightEquis.getList().size()>0) {
					for (FireFightEquipmentHistory fireHisFightEquipment : fireFightEquis.getList()) {
						String fpinfoHql = "from FirePatrolInfo where timestamp >= '"+startDate+"' and timestamp < '"+endDate+"'"+
								" and fireFightEquipment.id = " + fireHisFightEquipment.getOldId()+"order by timestamp desc";
						List<FirePatrolInfo> firePatrolInfo = firePatrolInfoService.getByHqlPatrolInfo(fpinfoHql);
						if (firePatrolInfo!=null && firePatrolInfo.size()>0) {
							fireHisFightEquipment.setFpid(firePatrolInfo.get(0).getId());
						}
					}
				}


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
