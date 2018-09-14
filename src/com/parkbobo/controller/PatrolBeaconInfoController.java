package com.parkbobo.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.*;
import com.parkbobo.service.*;
import com.parkbobo.utils.PageBean;
import com.system.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 安防巡更-标签信息管理
 * @author RY
 * @version 1.0
 * @since 2018-7-6 18:22:20
 */
@Controller
public class PatrolBeaconInfoController {

	@Resource
	private PatrolBeaconInfoService patrolBeaconInfoService;


	private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

	/**
	 * 后台获取标签列表
	 * @param patrolBeaconInfo 检索信息
	 * @param pageSize
	 * @param page
	 * @param method
	 * @return
	 */
	@RequestMapping("patrolBeaconInfo_list")
	public ModelAndView list(PatrolBeaconInfo patrolBeaconInfo, Integer pageSize, Integer page, String method)
	{
		ModelAndView mv = new ModelAndView();
		String hql = "from PatrolBeaconInfo where 1 = 1 ";
		if(StringUtil.isNotEmpty(patrolBeaconInfo.getUuid())){
			hql += "and uuid like '%" + patrolBeaconInfo.getUuid() + "%' ";
		}

		if(patrolBeaconInfo.getMajor() != null){
			hql += "and major = " + patrolBeaconInfo.getMajor() + " ";
		}

		if(patrolBeaconInfo.getMinor() != null){
			hql += "and minor = " + patrolBeaconInfo.getMinor() + " ";
		}

		hql += "order by updateTime desc";

		if(pageSize == null || pageSize == 0){
			pageSize = 12;
		}

		if(page == null || page == 0){
			page = 1;
		}

		PageBean<PatrolBeaconInfo> pageBean = patrolBeaconInfoService.pageQuery(hql, pageSize, page);

		mv.addObject("pageBean", pageBean);
		mv.addObject("pageSize", pageSize);
		mv.addObject("page", page);
		mv.addObject("method", method);
		mv.addObject("patrolBeaconInfo", patrolBeaconInfo);
		mv.setViewName("manager/system/patrolBeaconInfo/patrolBeaconInfo-list");
		return mv;
	}

	/**
	 * 添加
	 * @param method
	 * @param patrolBeaconInfo
	 * @return
	 */
	@RequestMapping("patrolBeaconInfo_add")
	public ModelAndView add(String method,PatrolBeaconInfo patrolBeaconInfo)
	{
		ModelAndView mv = new ModelAndView();
		//添加
		if(StringUtil.isNotEmpty(method) && method.equals("add"))
		{
			String[] propertyNames = {"major", "minor"};
			Object[] values = {patrolBeaconInfo.getMajor(), patrolBeaconInfo.getMinor()};
			PatrolBeaconInfo byMajorMinor = patrolBeaconInfoService.getUniqueByPropertys(propertyNames, values);
			if(byMajorMinor!=null){
				mv.setViewName("manager/system/patrolBeaconInfo/patrolBeaconInfo-add");
				mv.addObject("msg","major, minor已存在,请检查");
				return mv;
			}
			Date date = new Date();
			patrolBeaconInfo.setUpdateTime(date);
			patrolBeaconInfoService.add(patrolBeaconInfo);
			mv.setViewName("redirect:/patrolBeaconInfo_list?method=addSuccess");
		}
		//跳转到添加页面
		else
		{
			mv.setViewName("manager/system/patrolBeaconInfo/patrolBeaconInfo-add");
		}
		return mv;
	}

	/**
	 * 编辑
	 * @param method
	 * @param patrolBeaconInfo
	 * @param id
	 * @return
	 */
	@RequestMapping("patrolBeaconInfo_edit")
	public ModelAndView edit(String method,PatrolBeaconInfo patrolBeaconInfo, Integer id)
	{
		ModelAndView mv = new ModelAndView();
		//编辑
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			String[] propertyNames = {"major", "minor"};
			Object[] values = {patrolBeaconInfo.getMajor(), patrolBeaconInfo.getMinor()};
			PatrolBeaconInfo byMajorMinor = patrolBeaconInfoService.getUniqueByPropertys(propertyNames, values);
			if(byMajorMinor!=null){
				mv.setViewName("redirect:/patrolBeaconInfo_edit?id=" + patrolBeaconInfo.getBeaconId());
				mv.addObject("msg","major, minor已存在,请检查");
				return mv;
			}
			Date date = new Date();
			patrolBeaconInfo.setUpdateTime(date);
			patrolBeaconInfoService.update(patrolBeaconInfo);
			mv.setViewName("redirect:/patrolBeaconInfo_list?method=editSuccess");
		}
		//跳转到编辑页面
		else
		{
			patrolBeaconInfo = patrolBeaconInfoService.get(id);
			mv.addObject("patrolBeaconInfo", patrolBeaconInfo);
			mv.setViewName("manager/system/patrolBeaconInfo/patrolBeaconInfo-edit");
		}
		return mv;
	}

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("patrolBeaconInfo_edit")
	public ModelAndView delete(String ids)
	{
		ModelAndView mv = new ModelAndView();
		String[] idArray = ids.split(",");
		for(String str : idArray){
			patrolBeaconInfoService.delete(Integer.parseInt(str));
		}
		mv.setViewName("redirect:/patrolBeaconInfo_list?method=deleteSuccess");
		return mv;
	}

	/**
	 * 导出excel
	 */
	@RequestMapping("patrolBeaconInfo_excelOut")
	public ResponseEntity<byte[]> excelOut(PatrolBeaconInfo patrolBeaconInfo,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "蓝牙标签信息"+df.format(today)+".xls";
		List<PatrolBeaconInfo> list = null;
		try {
			String hql = "from PatrolBeaconInfo where 1 = 1 ";
			if(StringUtil.isNotEmpty(patrolBeaconInfo.getUuid())){
				hql += "and uuid like '%" + patrolBeaconInfo.getUuid() + "%' ";
			}

			if(patrolBeaconInfo.getMajor() != null){
				hql += "and major = " + patrolBeaconInfo.getMajor() + " ";
			}

			if(patrolBeaconInfo.getMinor() != null){
				hql += "and minor = " + patrolBeaconInfo.getMinor() + " ";
			}

			hql += "order by updateTime desc";


			list = patrolBeaconInfoService.getByHql(hql);
		} catch (Exception e1) {
		}
		//设置表格标题行
		String[] headers = new String[] {"UUID","major", "minor", "更新时间"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		if (list!=null && list.size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (PatrolBeaconInfo patrolBeaconInfo1 : list) {//循环每一条数据
				objs = new Object[headers.length];
				objs[0] = patrolBeaconInfo1.getUuid();
				objs[1] = patrolBeaconInfo1.getMajor();
				objs[2] = patrolBeaconInfo1.getMinor();
				objs[3] = sdf.format(patrolBeaconInfo1.getUpdateTime());
				//数据添加到excel表格
				dataList.add(objs);
			}
		}
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
		HSSFCell cell = null;   //设置单元格的数据类型
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

	/**
	 * 下载导入模板
	 */
	@RequestMapping("patrolBeaconInfo_downloadTemplate")
	public ResponseEntity<byte[]> downloadTemplate(HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "蓝牙标签信息导入模板.xls";
		try {
			String hql = "from PatrolBeaconInfo where 1 = 1 ";
		} catch (Exception e1) {
		}
		//设置表格标题行
		String[] headers = new String[] {"UUID","major", "minor"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;


		objs = new Object[headers.length];
		objs[0] = "5191916";
		objs[1] = "10054";
		objs[2] = "2319";
		//数据添加到excel表格
		dataList.add(objs);


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
		HSSFCell cell = null;   //设置单元格的数据类型
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

	/**
	 * 进入导入页面
	 * @return
	 */
	@RequestMapping(value = "/patrolBeaconInfo_toImport")
	public ModelAndView toImport(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("manager/system/patrolBeaconInfo/patrolBeaconInfo-import");
		return mv;
	}

	/**
	 * 导入蓝牙标签
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/patrolBeaconInfo_import", method = RequestMethod.POST)
	public ModelAndView importBeaconInfo(@RequestParam("file") CommonsMultipartFile file) throws IOException {
		ModelAndView mv = new ModelAndView();

		HSSFWorkbook work = new HSSFWorkbook(file.getInputStream());// 得到这个excel表格对象
		HSSFSheet sheet = work.getSheetAt(0); //得到第一个sheet
		int rowNo = sheet.getLastRowNum(); //得到行数
		System.out.println("rowNo:" + rowNo);
		for (int i = 2; i <= rowNo; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell1 = row.getCell((short) 0);
			HSSFCell cell2 = row.getCell((short) 1);
			HSSFCell cell3 = row.getCell((short) 2);

			cell1.setCellType(CellType.STRING);
			cell2.setCellType(CellType.STRING);
			cell3.setCellType(CellType.STRING);

			String ce1 = cell1 == null?"空":cell1.getStringCellValue();
			String ce2 = cell2 == null?"空":cell2.getStringCellValue();
			String ce3 = cell3 == null?"空":cell3.getStringCellValue();

			System.out.println(ce1 + "\t" + ce2 + "\t" + ce3);

			if(StringUtil.isNotEmpty(ce1) && StringUtil.isNotEmpty(ce2) && !StringUtil.isNotEmpty(ce3)){
				try {
					PatrolBeaconInfo patrolBeaconInfo = new PatrolBeaconInfo();
					patrolBeaconInfo.setUuid(ce1);
					patrolBeaconInfo.setMajor(Integer.parseInt(ce2));
					patrolBeaconInfo.setMinor(Integer.parseInt(ce3));
					patrolBeaconInfo.setUpdateTime(new Date());

					String[] propertyNames = {"major", "minor"};
					Object[] values = {patrolBeaconInfo.getMajor(), patrolBeaconInfo.getMinor()};
					PatrolBeaconInfo byMajorMinor = patrolBeaconInfoService.getUniqueByPropertys(propertyNames, values);
					if(byMajorMinor == null){
						patrolBeaconInfoService.add(patrolBeaconInfo);
					}

				} catch (NumberFormatException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		mv.setViewName("redirect:/patrolBeaconInfo_list?method=importSuccess");

		return mv;
	}
}
