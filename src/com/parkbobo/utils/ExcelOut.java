package com.parkbobo.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

public class ExcelOut {
	private static ExcelOut instance = null;
	private ExcelOut() {}

	public static ExcelOut getInstance() {
	    if (instance == null)
	        instance = new ExcelOut();
	    return instance;
	}
	public ResponseEntity<byte[]> excelOut(String title,String[] headers,List<Object[]> dataList,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
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
	}
	
}
