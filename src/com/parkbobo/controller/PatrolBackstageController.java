package com.parkbobo.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.service.*;
import com.parkbobo.utils.PageBean;
import com.system.model.Manager;
import com.system.service.OptLogsService;

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
	@Resource
	private OptLogsService optLogsService;



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
		page = (page != null && page > 0 ? page : 1);
		pageSize = (pageSize != null && pageSize > 0 ? pageSize : 10);
		String hql = "from PatrolRegion where isDel != 1 order by id asc";
		List<PatrolRegion> patrolRegions = patrolRegionService.getByHQL(hql);
		PageBean<PatrolUserRegion> patrolUserRegions = this.patrolUserRegionService.getPatrolUserBySth(username, regionId, exceptionType, startTime, endTime, page, pageSize);

		for (PatrolUserRegion patrolUserRegion : patrolUserRegions.getList()) {
			PatrolRegion patrolRegion = patrolRegionService.getById(patrolUserRegion.getRegionId());
			if( patrolRegion != null && null != patrolRegion.getRegionName()) {
				patrolUserRegion.setRegionName(patrolRegion.getRegionName());
			}
		}
		//将数据添加到进
		mv.addObject("patrolUserRegions", patrolUserRegions);
		mv.addObject("patrolRegions", patrolRegions);
		mv.addObject("name",username);
		mv.addObject("regionId",regionId);
		mv.addObject("exceptionType",exceptionType);
		mv.addObject("startTime",startTime);
		mv.addObject("endTime",endTime);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.setViewName("manager/system/patrol/patrolUserRegionList");
		return mv;
	}
	/**
	 * 巡查信息删除
	 * @return
	 */
	@RequestMapping("patUserReg_delete")
	public ModelAndView delete(String ids,HttpSession session){
		ModelAndView mv = new ModelAndView();
		Manager user = (Manager) session.getAttribute("loginUser");
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			Integer[] idArr = new Integer[strs.length];
			for (int i=0; i< strs.length; i++) {
				idArr[i] = Integer.parseInt(strs[i]);
			}
			patrolUserRegionService.bulkDelete(idArr);
		}
		optLogsService.addLogo("巡查管理", user, "删除巡查信息,信息ID：" +ids);
		mv.setViewName("redirect:/patrolUserRegionList?method=deleteSuccess");
		return mv;
	}

	//TODO 巡更记录导出有问题
	/**
	 * 巡查信息列表导出
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("paUserRegExOut")
	public ResponseEntity<byte[]> paUserRegExOut(String username,Integer regionId,Integer exceptionType,String startTime,String endTime,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "巡查信息列表"+df.format(today)+".xls";
		//
		List<PatrolUserRegion> list = patrolUserRegionService.getPatrolUserBySth(username,regionId,exceptionType,startTime,endTime);
		//设置表格标题行
		String[] headers = new String[] {"巡更人员姓名","巡更人员账号", "开始巡查时间", "结束巡查时间", "巡更时长", "巡更区域", "是否异常", "异常原因"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		if (list!=null && list.size()>0) {
			for (PatrolUserRegion record : list) {//循环每一条数据
				PatrolRegion patrolRegion = patrolRegionService.getById(record.getRegionId());
				record.setRegionName(patrolRegion.getRegionName());
				objs = new Object[headers.length];
				objs[0] = record.getUsername();
				objs[1] = record.getJobNum();
				objs[2] = record.formatStartTime();
				objs[3] = record.formatEndTime();
				objs[4] = record.getCheckDuration();
				objs[5] = record.getRegionName();
				if (record.getPatrolException()!=null) {
					objs[6] = "是";
					objs[7] = record.getPatrolException().getExceptionName();
				}else{
					objs[6] = "否";
					objs[7] = "无";
				}
				//数据添加到excel表格
				dataList.add(objs);
			}
		}

//		ExcelOut.getInstance().excelOut(title, headers, dataList, request, response);
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
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 安防巡更区域查询
	 * @param regionId 区域id
	 * @param campusNum 校区id
	 * @param page 页码
	 * @param pageSize 每页条数
	 * @param response
	 */
	@RequestMapping("patrolRegList")
	public ModelAndView getRegionBySth(Integer regionId,Integer campusNum,Integer page,Integer pageSize,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		String hql = "from PatrolRegion where isDel != 1 order by id asc";
		List<PatrolRegion> patrolRegions = patrolRegionService.getByHQL(hql);
		PageBean<PatrolRegion> patrolRegionPage = new PageBean<>();
		patrolRegionPage = this.patrolRegionService.getBySth(regionId, campusNum, pageSize, page);
		mv.addObject("patrolRegionPage",patrolRegionPage);
//		mv.addObject("patrolReg\"patrolRegionPage\"ionPage",patrolRegionPage);
		mv.addObject("patrolRegions",patrolRegions);
		mv.setViewName("manager/system/patrol/patrolRegList");
		return mv;
	}
	/**
	 * 安防巡更区域删除
	 * @param ids 区域id
	 */
	@RequestMapping("patrolReg_delete")
	public ModelAndView patrolRegDelete(String ids){
		ModelAndView mv = new ModelAndView();
		if(ids.length() > 0){
			String[] strs = ids.split(",");
			Integer[] idArr = new Integer[strs.length];
			for (int i=0; i< strs.length; i++) {
				idArr[i] = Integer.parseInt(strs[i]);
				PatrolRegion patrolRegion = patrolRegionService.getById(idArr[i]);
				patrolRegion.setIsDel((short)1);
				patrolRegionService.update(patrolRegion);
			}
		}
		mv.setViewName("redirect:/patrolRegList?method=deleteSuccess");
		return mv;
	}


	/**
	 * 安防巡更区域增加
	 * @param regionName 区域名
	 */
	@RequestMapping("patrolReg_add")
	public ModelAndView patrolRegAdd(String regionName,String color) {
		ModelAndView mv = new ModelAndView();
		PatrolRegion patrolRegion = new PatrolRegion();
		Date date = new Date();
		//TODO 这里进行默认校区设置(默认设置为校区一)
		patrolRegion.setCampusNum(1);
		patrolRegion.setCreatetime(date);
		patrolRegion.setIsDel((short)0);
		patrolRegion.setLastUpdateTime(date);
		patrolRegion.setRegionName(regionName);

		//进行判断color
		if("#".equals(color.substring(0,1))) {
			patrolRegion.setColor(color.trim());
		}else {
			patrolRegion.setColor("#"+color.trim());
		}

		patrolRegionService.addRecord(patrolRegion);
		mv.setViewName("redirect:/patrolRegList?method=addSuccess");
		return mv;
	}
	/**
	 * 安防巡更区域修改
	 * @param regionId 区域id
	 * @param regionName 区域名
	 */
	@RequestMapping("patrolReg_update")
	public ModelAndView patrolRegUpdate(Integer regionId,String regionName,String color) {
		ModelAndView mv = new ModelAndView();
		PatrolRegion patrolRegion = patrolRegionService.getById(regionId);
		patrolRegion.setRegionName(regionName);
		if(color != null) {
			if("#".equals(color.substring(0,1))) {
				patrolRegion.setColor(color.trim());
			}else {
				patrolRegion.setColor("#"+color.trim());
			}
		}
		patrolRegion.setLastUpdateTime(new Date());
		patrolRegionService.update(patrolRegion);
		mv.setViewName("redirect:/patrolRegList?method=editSuccess");
		return mv;
	}


	/**
	 * 跳转到绘制地图页面
	 * @param id	区域id
	 * @return
     */
	@RequestMapping("/firePatrolMap")
	public ModelAndView patrolMap(Integer id,HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		if(id != null && id > 0) {
			mv.addObject("id",id);
		}
		//查询区域相关信息
		if(id == null && id <= 0) {
			return null;
		}
		String hql = "FROM PatrolRegion WHERE isDel=0 AND id=" + id;
		List<PatrolRegion> patrolRegions = patrolRegionService.getByHQL(hql);
		if(patrolRegions != null && patrolRegions.size() > 0) {
			mv.addObject("patrolRegion", JSON.toJSONString(patrolRegions.get(0)));
			mv.addObject("color",patrolRegions.get(0).getColor());
		}
		mv.addObject("id",id);
		mv.setViewName("manager/system/firePatrolMap/map");
		return mv;
	}

	/**
	 * 添加绘制的巡更区域
	 * @param id			区域id
	 * @param polygon		区域信息
     */
	@RequestMapping("/addDrawRegion")
	public ModelAndView addFrawRegion(Integer id,String polygon) {
		ModelAndView mv = new ModelAndView();
		if(polygon != null && !"".equals(polygon) && id != null && !"".equals(id)) {
			String sql = "UPDATE patrol_region SET region_location = st_geomfromtext('"+polygon+"')  WHERE id=" + id;
			int result = patrolRegionService.updateBySql(sql);
			if(result > 0) {
				mv.addObject("message","数据上传成功");
			}
		}
		String hql = "FROM PatrolRegion WHERE isDel=0 AND id=" + id;
		List<PatrolRegion> patrolRegions = patrolRegionService.getByHQL(hql);
		if(patrolRegions != null && patrolRegions.size() > 0) {
			mv.addObject("patrolRegion", JSON.toJSONString(patrolRegions.get(0)));
			mv.addObject("color",patrolRegions.get(0).getColor());
		}
		mv.addObject("id",id);
		mv.setViewName("manager/system/firePatrolMap/map");
		return mv;
	}


	/**
	 * 将该区域内的区域范围进行清空
	 * @param id
     */
	@RequestMapping("/deleteRegionLocation")
	public ModelAndView deleteRegionLocation(Integer id) {
		ModelAndView mv = new ModelAndView();
		if(id != null && !"".equals(id)) {
			String sql = "UPDATE patrol_region SET region_location=null WHERE id=" + id;
			int result = patrolRegionService.updateBySql(sql);
		}
		String hql = "FROM PatrolRegion WHERE isDel=0 AND id=" + id;
		List<PatrolRegion> patrolRegions = patrolRegionService.getByHQL(hql);
		if(patrolRegions != null && patrolRegions.size() > 0) {
			mv.addObject("patrolRegion", JSON.toJSONString(patrolRegions.get(0)));
			mv.addObject("color",patrolRegions.get(0).getColor());
		}
		mv.addObject("id",id);
		mv.setViewName("manager/system/firePatrolMap/map");
		return mv;
	}


    /**
	 * 根据校区id进行显示所有区域内容
	 * @param campusNum		校区id
	 * @return
     */
	@RequestMapping("/toShowAllDrawPage")
	public ModelAndView toShowAllDrawPage(Integer campusNum) {
		ModelAndView mv = new ModelAndView();
		//通过校区id进行查询所有区域信息
		String hql = "FROM PatrolRegion WHERE isDel=0 AND campusNum=" + campusNum;
		List<PatrolRegion> patrolRegions = patrolRegionService.getByHQL(hql);
		mv.addObject("patrolRegions",JSON.toJSONString(patrolRegions));
		mv.setViewName("manager/system/firePatrolMap/showAllRegionMap");
		return mv;
	}


	//这里进行跳转页面

	/**
	 * 跳转到设置颜色页面
	 * @return
	 */
	@RequestMapping("/toSelectColorPage")
	public ModelAndView toSelectColorPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("manager/system/patrol/color-selection");
		return mv;
	}

	/**
	 * 跳转到更新页面
	 * @param regionId
	 * @param regionName
	 * @param color
	 * @return
	 */
	@RequestMapping("/toSelectColorPageUpdate")
	public ModelAndView toSelectColorUpdatePage(Integer regionId,String regionName,String color) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("regionName",regionName);
		mv.addObject("regionId",regionId);
		mv.addObject("color",color);
		mv.setViewName("manager/system/patrol/color-selection-update");
		return mv;
	}

}

