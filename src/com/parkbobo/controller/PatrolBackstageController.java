package com.parkbobo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolEmergency;
import com.parkbobo.model.PatrolException;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.model.PatrolUserRegionShow;
import com.parkbobo.service.PatrolConfigService;
import com.parkbobo.service.PatrolEmergencyService;
import com.parkbobo.service.PatrolExceptionService;
import com.parkbobo.service.PatrolLocationInfoService;
import com.parkbobo.service.PatrolRegionService;
import com.parkbobo.service.PatrolUserRegionService;
import com.parkbobo.service.PatrolUserService;
import com.parkbobo.utils.PageBean;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * 安防后台管理
 * @author zj
 *@version 1.0
 */
@Controller
public class PatrolBackstageController {

	@Resource
	private PatrolUserService patrolUserService;
	@Resource
	private PatrolUserRegionService patrolUserRegionService;
	@Resource
	private PatrolLocationInfoService patrolLocationInfoService;
	@Resource
	private PatrolRegionService patrolRegionService;
	@Resource
	private PatrolExceptionService patrolExceptionService;
	@Resource
	private PatrolEmergencyService patrolEmergencyService;
	@Resource
	private PatrolConfigService patrolConfigService;

	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

	/**
	 * 条件分页查询用户
	 * @param username (用户名)
	 * @param jobNum  工号(账号)
	 * @param page   页码
	 * @param pageSize 每页条数
	 * @throws IOException 
	 */
	@RequestMapping("pageQueryPatrolUsers")
	public void pageQueryUsers(String username,String jobNum,Integer page,Integer pageSize,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PageBean<PatrolUser> users = this.patrolUserService.getUsers(username,jobNum,page, pageSize);
			List<PatrolUser> list = null;
			if(users!=null){
				list = users.getList();
			}
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list,features)+"}");
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
	 * 按条件统计用户数量
	 * @param username
	 * @param jobNum
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("countPatrolUser")
	public void countUserBySth(String username,String jobNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			int countUsers = this.patrolUserService.countUsers(username, jobNum);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+countUsers+"}");
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
	 * 批量删除 格式(1,2,3,)   传入用户id
	 * @param idStr
	 * @throws IOException 
	 */
	@RequestMapping("bulkDeletePatrolUser")
	public void bulkDeleteUser(String idStr,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		if(idStr!=null){
			String[] idArr = idStr.split(",");
			try {
				out = response.getWriter();
				for(int i = 0;i < idArr.length;i++){
					try {
						int id = Integer.parseInt(idArr[i]);
						PatrolUser patrolUser = this.patrolUserService.getById(id);
						if(patrolUser == null){
							out.print("{\"status\":\"false\",\"Code\":-1,\"Msg\":\"用户不存在\"}");
							return;
						}else{
							patrolUser.setIsDel((short)1);
							this.patrolUserService.update(patrolUser);
						}
					} catch (NumberFormatException e) {
						out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"参数格式不对\"}");
					}
				}
				out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"删除成功\"}");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				out.flush();
				out.close();
			}
		}else{
			out= response.getWriter();
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"请勾选要删除的用户\"}");
			out.flush();
			out.close();
		}
	}
	/**
	 * 导出excel
	 * @param username 用户名
	 * @param jobNum 工号
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("patrolUserExcelOut")
	public ResponseEntity<byte[]> excelOut(String username,String jobNum,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "安防巡查人员记录"+df.format(today)+".xls";
		List<PatrolUser> list = null;
		try {
			list = this.patrolUserService.getBySth(username,jobNum);
		} catch (Exception e1) {
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"获取数据错误\"}");
		}
		//设置表格标题行
		String[] headers = new String[] {"巡更人员姓名","巡更人员账号", "更新时间"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		if (list!=null && list.size()>0) {
			for (PatrolUser entryOutRecord : list) {//循环每一条数据
				objs = new Object[headers.length];
				objs[0] = entryOutRecord.getUsername();
				objs[1] = entryOutRecord.getJobNum();
				objs[2] = entryOutRecord.getLastUpdateTime();
				//数据添加到excel表格
				dataList.add(objs);
			}
		}
		try {
			//防止中文乱码
			// 第一步，创建一个webbook，对应一个Excel文件    
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet    
			HSSFSheet sheet = wb.createSheet(title);
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short    
			HSSFRow row = sheet.createRow((int) 0);
			// 第四步，创建单元格，并设置值表头 设置表头居中    
			HSSFCellStyle style = wb.createCellStyle(); 
			style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式    
			HSSFCell  cell = null;   //设置单元格的数据类型
			for (int i = 0; i < headers.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，    
			for(int i=0;i<dataList.size();i++){
				if (i<5) {
					sheet.autoSizeColumn(i, true);
				}
				Object[] obj = dataList.get(i);//遍历每个对象
				row = sheet.createRow(i+1);//创建所需的行数（从第二行开始写数据）
				for(int j=0; j<obj.length; j++){
					cell = row.createCell(j);
					if (obj[j]!=null && !obj[j].equals("null") ) {
						cell.setCellValue(obj[j].toString());
					}else{
						cell.setCellValue("");
					}
					cell.setCellStyle(style);			//设置单元格样式
				}
			}
			//下载文件路径
			String path = request.getServletContext().getRealPath("/download/");
			File file = new File(path);
			if (!file.exists()){
				file.setWritable(true, false); //设置文件夹权限，避免在Linux下不能写入文件
				file.mkdirs();
			}
			file = new File(path+"temp.xls");
			HttpHeaders httpHeaders = new HttpHeaders();  
			//下载显示的文件名，解决中文名称乱码问题  
			String downloadFielName = new String(title.getBytes("UTF-8"),"iso-8859-1");
			//通知浏览器以attachment（下载方式）打开图片
			httpHeaders.setContentDispositionFormData("attachment", downloadFielName); 
			//application/octet-stream ： 二进制流数据（最常见的文件下载）。
			httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			wb.write(file);
			wb.close();
			out.print("{\"status\":\"false\",\"errorCode\":1,\"Msg\":\"导出成功\"}");
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),httpHeaders, HttpStatus.CREATED);  
		} catch (Exception e) {
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
			return null;
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 分页获取巡查信息列表
	 * @param username 用户名
	 * @param regionId 区域id
	 * @param exceptionType 是否异常  1正常
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 * @param page 页码
	 * @param pageSize 每页条数
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("patrolUserRegionList")
	public ModelAndView getPatrolUserRegionsBySth(String username,Integer regionId,Integer exceptionType,String startTime,String endTime,Integer page,Integer pageSize,HttpServletResponse response) throws IOException{
		ModelAndView mv = new ModelAndView();
		List<PatrolRegion> patrolRegions = patrolRegionService.getAll();
		
		PageBean<PatrolUserRegion> patrolUserRegions = this.patrolUserRegionService.getPatrolUserBySth(username, regionId, exceptionType, startTime, endTime, page, pageSize);
		for (PatrolUserRegion patrolUserRegion : patrolUserRegions.getList()) {
			PatrolRegion patrolRegion = patrolRegionService.getById(patrolUserRegion.getRegionId());
			patrolUserRegion.setRegionName(patrolRegion.getRegionName());
		}
		mv.addObject("patrolUserRegions", patrolUserRegions);
		mv.addObject("patrolRegions", patrolRegions);
		mv.setViewName("manager/system/patrol/patrolUserRegionList");
//		List<PatrolUserRegionShow> list1 = new ArrayList<PatrolUserRegionShow>();
//		Map<Integer,String> map = new HashMap<Integer, String>();
//		List<PatrolException> exceptionList = this.patrolExceptionService.getAll();
//		for(int k=0; k < exceptionList.size();k++){
//			PatrolException p = exceptionList.get(k);
//			map.put(p.getId(),p.getExceptionName());
//		}
//		if(list!=null&&list.size()>0){
//			for(int i = 0; i < list.size();i++){
//				PatrolUserRegion p = list.get(i);
//				PatrolUserRegionShow p1 = new PatrolUserRegionShow();
//				p1.setUsername(p.getUsername());
//				p1.setJobNum(p.getJobNum());
//				p1.setStartTime(p.getStartTime());
//				p1.setEndTime(p.getEndTime());
//				if(p.getEndTime()!=null){
//					p1.setPatrolTime(p.getEndTime().getTime()-p.getStartTime().getTime());
//				}else{
//					p1.setPatrolTime(null);
//				}
//				PatrolRegion patrolRegion = this.patrolRegionService.getById(p.getRegionId());
//				p1.setRegionName(patrolRegion.getRegionName());
//				List<Integer> list2 = this.patrolLocationInfoService.getExceptionTypes(p.getJobNum(), startTime, endTime);
//				if(list2!=null&&list2.size()>0){
//					p1.setStatus(2);
//					String exceptionNames = "";
//					for(int j =0;j<list2.size();j++){
//						exceptionNames += map.get(list2.get(j))+",";
//					}
//					String exp = exceptionNames.substring(0, exceptionNames.length()-1);
//					p1.setExceptionName(exp);
//				}else{
//					p1.setStatus(1);
//				}
//				list1.add(p1);
//			}
//		}
			return mv;
	}
	/**
	 * 安防巡更区域查询
	 * @param regionName 区域名
	 * @param campusNum 校区id
	 * @param page 页码
	 * @param pageSize 每页条数
	 * @param response
	 */
	@RequestMapping("regionQuery")
	public void getRegionBySth(String regionName,Integer campusNum,Integer page,Integer pageSize,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			List<PatrolRegion> list = this.patrolRegionService.getBySth(regionName, campusNum, page, pageSize);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 返显
	 * @param regionId 区域id
	 * @param response
	 */
	@RequestMapping("reshowRegion")
	public void reshowRegion(Integer regionId,HttpServletResponse response){
		PatrolRegion patrolRegion = this.patrolRegionService.getById(regionId);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+patrolRegion+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}

	}
	/**
	 * 修改区域名称
	 * @param regionId 区域id
	 * @param regionName 区域名字
	 * @param response
	 */
	@RequestMapping("updateRegionName")
	public void updateRegionName(Integer regionId,String regionName,HttpServletResponse response){
		PatrolRegion patrolRegion = this.patrolRegionService.getById(regionId);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(regionName==null||"".equals(regionName)){
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"区域名不能为空\"}");
			}
			patrolRegion.setRegionName(URLDecoder.decode(URLEncoder.encode(regionName, "ISO8859_1"), "UTF-8"));
			patrolRegion.setLastUpdateTime(new Date());
			this.patrolRegionService.update(patrolRegion);
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 批量删除区域
	 * @param idStr 区域id 格式(3,4,5,)
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("bulkDeleteRegion")
	public void  bulkDeleteRegion(String idStr,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		if(idStr!=null){
			String[] idArr = idStr.split(",");
			try {
				out = response.getWriter();
				this.patrolRegionService.bulkDelete(idArr);
				out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"删除成功\"}");
			}catch(Exception e){
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
			}finally{
				out.flush();
				out.close();
			}
		}else{
			out= response.getWriter();
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"请勾选要删除的区域\"}");
			out.flush();
			out.close();
		}
	}
	/**
	 * 根据id删除区域
	 * @param regionId 区域id
	 * @param response
	 */
	@RequestMapping("deleteRegion")
	public void deleteById(Integer regionId,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			this.patrolRegionService.deleteById(regionId);
			out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"删除成功\"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 新增区域信息
	 * @param campusNum 校区id
	 * @param regionName 区域名字
	 * @param response
	 */
	@RequestMapping("addRegion")
	public void addRegion(Integer campusNum,String regionName,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		Date date = new Date();
		try {
			out = response.getWriter();
			MultiPolygon multiPolygon = null;
			PatrolRegion patrolRegion = new PatrolRegion();
			if(regionName!=null){
				patrolRegion.setRegionName(URLDecoder.decode(URLEncoder.encode(regionName, "ISO8859_1"), "UTF-8"));
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"区域名不能为空\"}");
				return;
			}
			patrolRegion.setCampusNum(campusNum);
			patrolRegion.setRegionLocation(multiPolygon);
			patrolRegion.setCreatetime(date);
			patrolRegion.setLastUpdateTime(date);
			this.patrolRegionService.addRecord(patrolRegion);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolRegion,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 更新区域信息
	 * @param regionId 区域id
	 * @param response
	 */
	@RequestMapping("updateRegionLocation")
	public void updateRegionLocation(Integer regionId,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		Date date = new Date();
		try {
			out = response.getWriter();
			MultiPolygon multiPolygon = null;
			PatrolRegion patrolRegion = this.patrolRegionService.getById(regionId);
			patrolRegion.setRegionLocation(multiPolygon);
			patrolRegion.setLastUpdateTime(date);
			this.patrolRegionService.update(patrolRegion);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolRegion,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 分页查询突发事件信息
	 * @param campusNum 校区id
	 * @param page 页码
	 * @param pageSize 每页条数
	 * @param response
	 */
	@RequestMapping("emergencyQuery")
	public void getEmergencies(Integer campusNum,Integer page,Integer pageSize,Date startTime,Date endTime,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			List<PatrolEmergency> list = this.patrolEmergencyService.getByPage(campusNum,page, pageSize, startTime,endTime);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 统计符合要求的信息数量
	 * @param campusNum 校区id
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 */
	@RequestMapping("countEmergency")
	public void countEmergencies(Integer campusNum,Date startTime,Date endTime,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			int count = this.patrolEmergencyService.countEmergencies(campusNum, startTime,endTime);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+count+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 根据id删除突发事件记录
	 * @param id 突发事件id 
	 * @param response
	 */
	@RequestMapping("deleteEmergency")
	public void deleteEmergency(Integer id,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			this.patrolEmergencyService.deleteById(id);
			out.print("{\"status\":\"true\",\"Code\":1,\"msg\":"+"删除成功"+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 根据id批量删除突发事件信息
	 * @param idStr 突发事件id  格式(3,4,5,)
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("bulkDeleteEmergency")
	public void  bulkDeleteEmergencies(String idStr,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		if(idStr!=null){
			String[] idArr = idStr.split(",");
			try {
				out = response.getWriter();
				this.patrolEmergencyService.bulkDelete(idArr);
				out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"删除成功\"}");
			}catch(Exception e){
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
			}finally{
				out.flush();
				out.close();
			}
		}else{
			out= response.getWriter();
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"请勾选要删除的区域\"}");
			out.flush();
			out.close();
		}
	}
	@RequestMapping("excelOutEmergency")
	public ResponseEntity<byte[]> excelOutEmergency(Integer campusNum,Date startTime,Date endTime,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "突发事件记录"+df.format(today)+".xls";
		List<PatrolEmergency> list = null;
		try {
			list = this.patrolEmergencyService.getBySth(campusNum,startTime,endTime);
		} catch (Exception e1) {
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"获取数据错误\"}");
		}
		//设置表格标题行
		String[] headers = new String[] {"突发事件发时间","突发事件结束时间", "事件时长","突发事件发起人姓名","突发事件发起人账号"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		if (list!=null && list.size()>0) {
			for (PatrolEmergency entryOutRecord : list) {//循环每一条数据
				objs = new Object[headers.length];
				objs[0] = entryOutRecord.getStartTime();
				objs[1] = entryOutRecord.getEndTime();
				if(entryOutRecord.getEndTime()!=null){
					long mills = entryOutRecord.getStartTime().getTime()-entryOutRecord.getEndTime().getTime();
					long day = 0;    
					long hour = 0;    
					long min = 0;    
					long sec = 0;    
					day = mills / (24 * 60 * 60 * 1000);    
					hour = (mills / (60 * 60 * 1000) - day * 24);    
					min = ((mills / (60 * 1000)) - day * 24 * 60 - hour * 60);    
					sec = (mills/1000-day*24*60*60-hour*60*60-min*60); 
					objs[2] = hour+":"+min+":"+sec;
				}else{
					objs[2] = null;
				}
				objs[3] = entryOutRecord.getUsername();
				objs[4] = entryOutRecord.getJobNum();
				//数据添加到excel表格
				dataList.add(objs);
			}
		}
		try {
			//防止中文乱码
			// 第一步，创建一个webbook，对应一个Excel文件    
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet    
			HSSFSheet sheet = wb.createSheet(title);
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short    
			HSSFRow row = sheet.createRow((int) 0);
			// 第四步，创建单元格，并设置值表头 设置表头居中    
			HSSFCellStyle style = wb.createCellStyle(); 
			style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式    
			HSSFCell  cell = null;   //设置单元格的数据类型
			for (int i = 0; i < headers.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，    
			for(int i=0;i<dataList.size();i++){
				if (i<5) {
					sheet.autoSizeColumn(i, true);
				}
				Object[] obj = dataList.get(i);//遍历每个对象
				row = sheet.createRow(i+1);//创建所需的行数（从第二行开始写数据）
				for(int j=0; j<obj.length; j++){
					cell = row.createCell(j);
					if (obj[j]!=null && !obj[j].equals("null") ) {
						cell.setCellValue(obj[j].toString());
					}else{
						cell.setCellValue("");
					}
					cell.setCellStyle(style);			//设置单元格样式
				}
			}
			//下载文件路径
			String path = request.getServletContext().getRealPath("/download/");
			File file = new File(path);
			if (!file.exists()){
				file.setWritable(true, false); //设置文件夹权限，避免在Linux下不能写入文件
				file.mkdirs();
			}
			file = new File(path+"temp.xls");
			HttpHeaders httpHeaders = new HttpHeaders();  
			//下载显示的文件名，解决中文名称乱码问题  
			String downloadFielName = new String(title.getBytes("UTF-8"),"iso-8859-1");
			//通知浏览器以attachment（下载方式）打开图片
			httpHeaders.setContentDispositionFormData("attachment", downloadFielName); 
			//application/octet-stream ： 二进制流数据（最常见的文件下载）。
			httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			wb.write(file);
			wb.close();
			out.print("{\"status\":\"false\",\"errorCode\":1,\"Msg\":\"导出成功\"}");
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),httpHeaders, HttpStatus.CREATED);  
		} catch (Exception e) {
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"流程错误,请联系技术人员\"}");
			return null;
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 返显配置信息
	 * @param response
	 */
	@RequestMapping("reshowPatrolConfig")
	public void reshowPatrolConfig(Integer campusNum,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PatrolConfig config = this.patrolConfigService.getByP(campusNum);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(config,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	@RequestMapping("updatePatrolConfig")
	public void updatePatrolConfig(Integer configId,Integer uploadTime,Integer refreshTime,Integer leaveRegionDistance,Integer leaveRegionTime,Integer startPatrolTime,Integer lazyTime,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PatrolConfig config = this.patrolConfigService.getById(configId);
			if(lazyTime==null){
				config.setLazyTime(0);
			}else{
				config.setLazyTime(lazyTime);
			}
			if(leaveRegionDistance==null){
				config.setLeaveRegionDistance(0);
			}else{
				config.setLeaveRegionDistance(leaveRegionDistance);
			}
			if(leaveRegionTime == null){
				config.setLeaveRegionTime(0);
			}else{
				config.setLeaveRegionTime(leaveRegionTime);
			}
			if(refreshTime==null){
				config.setRefreshTime(0);
			}else{
				config.setRefreshTime(refreshTime);
			}
			if(startPatrolTime==null){
				config.setStartPatrolTime(0);
			}else{
				config.setStartPatrolTime(startPatrolTime);
			}
			if(uploadTime==null){
				config.setUploadTime(0);
			}else{
				config.setUploadTime(uploadTime);
			}
			this.patrolConfigService.updateConfig(config);
			out.print("{\"status\":\"false\",\"errorCode\":1,\"Msg\":\"修改成功\"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}

}

