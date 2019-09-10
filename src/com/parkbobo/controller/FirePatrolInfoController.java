package com.parkbobo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.FirePatrolException;
import com.parkbobo.model.FirePatrolImg;
import com.parkbobo.model.FirePatrolInfo;
import com.parkbobo.service.FirePatrolExceptionService;
import com.parkbobo.service.FirePatrolImgService;
import com.parkbobo.service.FirePatrolInfoService;
import com.parkbobo.utils.PageBean;

/**
 * 巡查记录
 * @author zj
 * @version 1.0
 */
@Controller
public class FirePatrolInfoController {

	@Resource
	private FirePatrolInfoService firePatrolInfoService;
	@Resource
	private FirePatrolExceptionService firePatrolExceptionService;
	@Resource
	private FirePatrolImgService firePatrolImgService;

	@RequestMapping("firePatrolInfo_list")
	public ModelAndView list(String equipmentName,String username,Integer patrolStatus,String startTime,String endTime,Integer page,Integer pageSize) throws UnsupportedEncodingException
	{
		ModelAndView mv = new ModelAndView();

		String hql = "from  FirePatrolInfo f where 1=1";
		if(StringUtils.isNotBlank(equipmentName)){
			hql +=" and f.fireFightEquipment.name like '%"+equipmentName+"%'";
		}
		if(StringUtils.isNotBlank(username)){
			hql += " and f.firePatrolUser.username like '%" + username +"%'";
		}
		if(patrolStatus != null&&patrolStatus!=-1){
			hql += " and patrolStatus ="+patrolStatus;
		}
		if(StringUtils.isNotBlank(startTime)){
			hql += " and timestamp > '"+startTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql += " and timestamp < '"+endTime+"'";
		}
		hql += " order by timestamp desc";
		PageBean<FirePatrolInfo> firePatrolInfoPage = this.firePatrolInfoService.getByHql(hql,pageSize==null?12:pageSize, page==null?1:page);

		//返回结果显示
		mv.addObject("equipmentName",equipmentName);
		mv.addObject("patrolStatus",patrolStatus);
		mv.addObject("startTime",startTime);
		mv.addObject("endTime",endTime);
		mv.addObject("patrolUserName",username);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("firePatrolInfoPage", firePatrolInfoPage);
		mv.setViewName("manager/system/firePatrolInfo/firePatrolInfo-list");
		return mv;
	}
	/**
	 * 删除异常信息
	 * @return
	 */
	@RequestMapping("firePatrolInfo_delete")
	public ModelAndView delete(String ids,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		this.firePatrolInfoService.bulkDelete(ids);
		mv.setViewName("redirect:/firePatrolInfo_list?method=deleteSuccess");
		return mv;
	}

	/**
	 * 显示所有异常信息
	 * @throws IOException
	 */
	@RequestMapping("showExcptions")
	public void showExc(String exceptionTypes,Integer id,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		FirePatrolInfo firePatrolInfo = this.firePatrolInfoService.get(id);
		String[] exceptions = null;
		if(exceptionTypes.length() > 0){
			String[] strs = exceptionTypes.split(",");
			exceptions = new String[strs.length];
			for (int i=0; i< strs.length; i++) {
				exceptions[i]=this.firePatrolExceptionService.getById(Integer.parseInt(strs[i])).getExceptionName();
			}
		}
		String exception = "";
		for (int i = 0; i < exceptions.length; i++) {
			exception += ","+exceptions[i];
		}
		exception = exception.substring(1);
		Map<String,Object> map = new HashMap<>();
		map.put("status","true");
		map.put("Code",1);
		map.put("exceptions",exception);
		map.put("description",firePatrolInfo.getDescription());
		String  param= JSON.toJSONString(map);
		out.print(param);
//		out.print("{\"status\":\"true\",\"Code\":1,\"exceptions\":\""+exception+"\",\"description\":\""+firePatrolInfo.getDescription()+"\"}");
		out.flush();
		out.close();
	}
	/**
	 * 展示图片
	 * @param id
	 * @return
	 */
	@RequestMapping("/showExceptionImages")
	public ModelAndView showImgs(Integer id) {
		ModelAndView mv = new ModelAndView();
		//查询异常图片
		String hql = "FROM FirePatrolImg WHERE ";
		List<FirePatrolImg> firePatrolImgs = null;
		if(id != null && id > 0) {
			hql += " infoId=" + id;
			firePatrolImgs = firePatrolImgService.getByHql(hql);
			mv.addObject("firePatrolImgs",firePatrolImgs);
		}


		//下面进行获取异常类型并查询异常类型
		String infoHql = "FROM FirePatrolInfo WHERE";
		List<FirePatrolInfo> firePatrolInfos = null;
		String[] exceptionTypes = null;
		if(id != null && id > 0) {
			infoHql += " id=" + id;
			firePatrolInfos = firePatrolInfoService.getByHqlPatrolInfo(infoHql);
			if(firePatrolInfos != null && firePatrolInfos.size() > 0) {
				FirePatrolInfo firePatrolInfo = firePatrolInfos.get(0);
				if(firePatrolInfo != null) {
					String description = firePatrolInfo.getDescription();
					mv.addObject("description",description);
					String exception_types = firePatrolInfo.getExceptionTypes();
					if(exception_types != null) {
						exceptionTypes = exception_types.split(",");
					}
				}
			}
		}

		//进行查询
		if(exceptionTypes != null && exceptionTypes.length > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("FROM FirePatrolException WHERE id IN (");
			for (String v : exceptionTypes) {
				if (v != null && !"".equals(v)) {
					sb.append(v);
					sb.append(",");
				}
			}
			String regulation = sb.substring(sb.length()-1,sb.length());
			String exehql = "";
			if(regulation != null && ",".equals(regulation)) {
				exehql = sb.substring(0,sb.length()-1) + ")";
				List<FirePatrolException> exceptions = firePatrolExceptionService.getByHQL(exehql);
				mv.addObject("exceptions",exceptions);
			}

		}

		mv.setViewName("manager/system/firePatrolInfo/firepatrollinfo-image");
		return mv;
	}


//	public static void main(String[] args) {
//		String[] exceptionTypes = "123,123,123".split(",");
//		StringBuffer exceHql = new StringBuffer();
//		exceHql.append("FROM FirePatrolException WHERE id IN (");
//		for (String v : exceptionTypes) {
//			if (v != null && !"".equals(v)) {
//				exceHql.append(v);
//				exceHql.append(",");
//			}
//		}
//		String test = exceHql.substring(exceHql.length()-1,exceHql.length());
//		if(",".equals(test)) {
//			String value = exceHql.substring(0,exceHql.length()-1);
//			System.out.println(value);
//		}
//		System.out.println(test);
//	}

	/**
	 * 消防巡查记录导出
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("firePatrolInfo_excelOut")
	public ResponseEntity<byte[]> excelOut(String username,String equipmentName,Integer status,String startTime,String endTime,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "消防巡查记录"+df.format(today)+".xls";
		List<FirePatrolInfo> list = null;
		try {
			list = this.firePatrolInfoService.getBySth(username,equipmentName,status,startTime,endTime);
		} catch (Exception e1) {
		}
		//设置表格标题行
		String[] headers = new String[] {"设备名称","巡查人员姓名", "巡查人员账号","巡查时间","巡查结果"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		if (list!=null && list.size()>0) {
			for (FirePatrolInfo entryOutRecord : list) {//循环每一条数据
				objs = new Object[headers.length];
				objs[0] = entryOutRecord.getFireFightEquipment().getName();
				objs[1] = entryOutRecord.getFirePatrolUser().getUsername();
				objs[2] = entryOutRecord.getFirePatrolUser().getJobNum();
				objs[3] = entryOutRecord.getTimestamp();
				objs[4] = entryOutRecord.getPatrolStatus()==1?"设备正常":"设备异常";
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
		// 设置表格默认列宽度为20个字节

		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
			HSSFCell  cell = null;   //设置单元格的数据类型
			for (int i = 0; i < headers.length; i++) {
				sheet.setColumnWidth(i,10000);
				cell = row.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
			for(int i=0;i<dataList.size();i++){
				if (i<5) {
					//sheet.autoSizeColumn(i, true);
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
