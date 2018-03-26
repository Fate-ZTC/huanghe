package com.parkbobo.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.parkbobo.model.PatrolEmergency;
import com.parkbobo.service.PatrolEmergencyService;
import com.parkbobo.utils.PageBean;

/**
 * 突发时间记录
 * @author zj
 *@version 1.0
 */
@Controller
public class PatrolEmergencyController {
	@Resource
	private PatrolEmergencyService patrolEmergencyService;
	
	@RequestMapping("patrolEmergency_list")
	public ModelAndView list(String startTime,String endTime,Integer page,Integer pageSize) throws UnsupportedEncodingException
	{
		ModelAndView mv = new ModelAndView();
		
		String hql = "from  PatrolEmergency f where campusNum = 1";
		if(StringUtils.isNotBlank(startTime)){
			hql += " and f.startTime > '"+startTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql += " and f.startTime < '"+endTime+"'";
		}
		hql += " order by startTime desc";
		PageBean<PatrolEmergency> patrolEmergencyPage = this.patrolEmergencyService.getByHql(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("patrolEmergencyPage", patrolEmergencyPage);
		mv.addObject("startTime",startTime);
		mv.addObject("endTime",endTime);
		mv.setViewName("manager/system/patrolEmergency/patrolEmergency-list");
		return mv;
	}
	/**
	 * 删除异常信息
	 * @return
	 */
	@RequestMapping("patrolEmergency_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		this.patrolEmergencyService.bulkDelete(ids);
		mv.setViewName("redirect:/patrolEmergency_list?method=deleteSuccess");
		return mv;
	}
	
	/**
	 * 消防巡查记录导出
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("patrolEmergency_excelOut")
	public ResponseEntity<byte[]> excelOut(Integer campusNum,Date startTime1,Date endTime1,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "突发事件记录"+df.format(today)+".xls";
		List<PatrolEmergency> list = null;
		try {
			list = this.patrolEmergencyService.getBySth(campusNum==null?1:campusNum,startTime1,endTime1);
		} catch (Exception e1) {
		}
		//设置表格标题行
		String[] headers = new String[] {"突发事件发生时间","突发事件接收时间", "事件时长","突发事件发起人姓名","突发事件发起人账号"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		if (list!=null && list.size()>0) {
			for (PatrolEmergency entryOutRecord : list) {//循环每一条数据
				objs = new Object[headers.length];
				objs[0] = entryOutRecord.getStartTime();
				objs[1] = entryOutRecord.getEndTime();
				objs[2] = entryOutRecord.getCheckDuration();
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
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),httpHeaders, HttpStatus.CREATED);  
		} catch (Exception e) {
			return null;
		}
	}
}
