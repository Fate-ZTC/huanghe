package com.parkbobo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.FirePatrolConfig;
import com.parkbobo.model.FirePatrolException;
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.model.FirePatrolUser;
import com.parkbobo.service.FirePatrolConfigService;
import com.parkbobo.service.FirePatrolExceptionService;
import com.parkbobo.service.FirePatrolInfoService;
import com.parkbobo.service.FirePatrolUserService;
import com.parkbobo.utils.PageBean;

@Controller
public class FirePatrolBackstageController {

	@Resource
	private FirePatrolExceptionService firePatrolExceptionService;
	@Resource
	private FirePatrolConfigService firePatrolConfigService;
	@Resource
	private FirePatrolUserService firePatrolUserService;

	private FirePatrolInfoService firePatrolInfoService;
	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

	/**
	 * 分页获取异常信息
	 * @param excName 关键词
	 * @param response
	 */
	@RequestMapping("fireExcQuery")
	public void pageQuery(String excName,Integer page,Integer pageSize,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			List<FirePatrolException> list = this.firePatrolExceptionService.getBySth(excName, page, pageSize);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(list,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 统计根据关键词查出的数据量
	 * @param excName
	 */
	@RequestMapping("countFireExc")
	public void countBySth(String excName,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			int count = this.firePatrolExceptionService.countBySth(excName);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+count+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 添加异常信息
	 * @param excName 异常名称
	 * @param sort 排序
	 * @param response
	 */
	@RequestMapping("addFireExc")
	public void addRecord(String excName,Integer sort,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			FirePatrolException firePatrolException = new FirePatrolException();
			if(excName!=null){
				firePatrolException.setExceptionName(URLDecoder.decode(URLEncoder.encode(excName, "ISO8859_1"), "UTF-8"));
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"异常名不能为空\"}");
				return;
			}
			firePatrolException.setSort(sort);
			firePatrolException.setUpdateTime(new Date());
			FirePatrolException record = this.firePatrolExceptionService.addRecord(firePatrolException);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(record,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 更新异常类别信息
	 * @param id 异常id
	 * @param sort 排序
	 * @param excName 名称
	 * @param response
	 */
	@RequestMapping("updateFireExc")
	public void updateExc(Integer id,Integer sort,String excName,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			FirePatrolException firePatrolException = this.firePatrolExceptionService.getById(id);
			if(excName!=null){
				firePatrolException.setExceptionName(URLDecoder.decode(URLEncoder.encode(excName, "ISO8859_1"), "UTF-8"));
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"异常名不能为空\"}");
				return;
			}
			firePatrolException.setSort(sort);
			firePatrolException.setUpdateTime(new Date());
			this.firePatrolExceptionService.updateRecord(firePatrolException);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(firePatrolException,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 返显异常类别信息
	 * @param id 异常id
	 * @param response
	 */
	@RequestMapping("reshowFireExc")
	public void reshowException(Integer id,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			FirePatrolException firePatrolException = this.firePatrolExceptionService.getById(id);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(firePatrolException,features)+"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 根据id删除信息
	 * @param id 异常id
	 * @param response
	 */
	@RequestMapping("deleteFireExc")
	public void deleteById(Integer id,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			this.firePatrolExceptionService.deleteById(id);
			out.print("{\"status\":\"true\",\"Code\":1,\"msg\":\"删除成功\"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 批量删除异常信息
	 * @param idStr
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("bulkDeleteFireExc")
	public void bulkDelete(String idStr,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		if(idStr!=null){
			String[] idArr = idStr.split(",");
			try {
				out = response.getWriter();
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
	 * 修改配置信息
	 * @param distance 距离 m
	 * @param response
	 */
	@RequestMapping("updateFireConfig")
	public void updateConfig(Integer configId,Integer distance,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			FirePatrolConfig config = this.firePatrolConfigService.getById(configId);
			config.setDistance(distance);
			this.firePatrolConfigService.updateConfig(config);
			out.print("{\"status\":\"true\",\"Code\":1,\"Msg\":\"修改成功\"}");
		}catch(Exception e){
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"未知异常,请技术人员\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 返显消防巡查配置信息
	 * @param campusNum 校区id
	 * @param response
	 */
	@RequestMapping("reshowFireConfig")
	public void reshowConfig(Integer campusNum,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			FirePatrolConfig config = this.firePatrolConfigService.getConfigBy(campusNum);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(config,features)+"}");
		}catch(Exception e){
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
	@RequestMapping("countFireUser")
	public void countUserBySth(String username,String jobNum,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			int countUsers = this.firePatrolUserService.countUsers(username, jobNum);
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
	@RequestMapping("bulkDeleteFireUser")
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
						FirePatrolUser patrolUser = this.firePatrolUserService.getById(id);
						if(patrolUser == null){
							out.print("{\"status\":\"false\",\"Code\":-1,\"Msg\":\"用户不存在\"}");
							return;
						}else{
							patrolUser.setIsDel((short)1);
							this.firePatrolUserService.update(patrolUser);
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
	@RequestMapping("excelOutFireUser")
	public ResponseEntity<byte[]> excelOut(String username,String jobNum,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "消防巡查人员记录"+df.format(today)+".xls";
		List<FirePatrolUser> list = null;
		try {
			list = this.firePatrolUserService.getBySth(username,jobNum);
		} catch (Exception e1) {
			out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"获取数据错误\"}");
		}
		//设置表格标题行
		String[] headers = new String[] {"巡更人员姓名","巡更人员账号", "更新时间"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		if (list!=null && list.size()>0) {
			for (FirePatrolUser entryOutRecord : list) {//循环每一条数据
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
	 * 分页获取消防巡查记录
	 * @param equipName 设备名称
	 * @param username 用户名
	 * @param status 巡查结果
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 * @param page 页码
	 * @param pageSize 每页条数
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("fireInfoQuery")
	public void getFirePatrolInfoBy(String equipName,String username,Integer status,Date startTime,Date endTime,Integer page,Integer pageSize,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PageBean<FirePatrolInfo> firePatrolInfo = this.firePatrolInfoService.getUsers(equipName,username,status,startTime,endTime,page, pageSize);
			List<FirePatrolInfo> list = null;
			if(firePatrolInfo!=null){
				list = firePatrolInfo.getList();
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
	 * 统计复核条件的巡查记录数量
	 * @param equipName 设备名称
	 * @param username 用户名
	 * @param status  是否异常  1正常 0异常
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 */
	@RequestMapping("countFireInfo")
	public void countInfo(String equipName,String username,Integer status,Date startTime,Date endTime){
		try {
			int count = this.firePatrolInfoService.countInfo(equipName,username,status,startTime,endTime);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据id删除巡查记录
	 * @param id 记录id
	 */
	@RequestMapping("deleteFireInfo")
	public void deleteById(Integer id){
		this.firePatrolInfoService.deleteById(id);
	}
	/**
	 * 查看详细异常信息
	 * @param excStr 异常信息id串
	 */
	@RequestMapping("showExc")
	public void showExcpetions(String excStr){
		String[] idArr = excStr.split(",");
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i < idArr.length;i++){
			int id = Integer.parseInt(idArr[i]);
			FirePatrolException patrolException = this.firePatrolExceptionService.getById(id);
			sb.append(patrolException.getExceptionName()+",");
		}
		String excInfo = sb.substring(0, sb.length()-1).toString();
		System.out.println(excInfo);
	}


}