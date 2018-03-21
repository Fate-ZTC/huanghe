package com.parkbobo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.service.PatrolUserService;
import com.parkbobo.utils.PageBean;

/**
 * 安防后台管理
 * @author zj
 *@version 1.0
 */
@Controller
public class PatrolBackstageController {

	@Resource
	private PatrolUserService patrolUserService;

	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

	/**
	 * 条件分页查询用户
	 * @param username (用户名)
	 * @param jobNum  工号(账号)
	 * @param page   页码
	 * @param pageSize 每页条数
	 * @throws IOException 
	 */
	@RequestMapping("pageQueryUsers")
	public void pageQueryUsers(String username,String jobNum,Integer page,Integer pageSize,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			PageBean<PatrolUser> users = this.patrolUserService.getUsers(username,jobNum,page, pageSize);
			out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(users,features)+"}");
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
	@RequestMapping("countUserBySth")
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
	 */
	public void bulkDelete(String idStr,HttpServletResponse response){
		String[] idArr = idStr.split(",");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
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
	@RequestMapping("excelOut")
	public ResponseEntity<byte[]> excelOut(String username,String jobNum,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "员工记录"+df.format(today)+".xls";
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


}