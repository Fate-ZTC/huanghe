package com.parkbobo.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.service.*;
import com.parkbobo.utils.ExcelOut;
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
		String hql = "from PatrolRegion where isDel != 1 order by id asc";
		List<PatrolRegion> patrolRegions = patrolRegionService.getByHQL(hql);
		PageBean<PatrolUserRegion> patrolUserRegions = this.patrolUserRegionService.getPatrolUserBySth(username, regionId, exceptionType, startTime, endTime, page, pageSize);
		for (PatrolUserRegion patrolUserRegion : patrolUserRegions.getList()) {
			PatrolRegion patrolRegion = patrolRegionService.getById(patrolUserRegion.getRegionId());
			patrolUserRegion.setRegionName(patrolRegion.getRegionName());
		}
		mv.addObject("patrolUserRegions", patrolUserRegions);
		mv.addObject("patrolRegions", patrolRegions);
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
	/**
	 * 巡查信息列表导出
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("paUserRegExOut")
	public void paUserRegExOut(HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "巡查信息列表"+df.format(today)+".xls";
		List<PatrolUserRegion> list = patrolUserRegionService.getAll();
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
		ExcelOut.getInstance().excelOut(title, headers, dataList, request, response);
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
		if(regionId == null) {
			patrolRegionPage.setList(patrolRegions);
		}else {
			patrolRegionPage = this.patrolRegionService.getBySth(regionId, campusNum, pageSize, page);
		}
		mv.addObject("patrolRegionPage",patrolRegionPage);
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
	public ModelAndView patrolRegAdd(String regionName,String color){
		ModelAndView mv = new ModelAndView();
		PatrolRegion patrolRegion = new PatrolRegion();
		Date date = new Date();
		//TODO 这里进行默认校区设置(默认设置为校区一)
		patrolRegion.setCampusNum(1);
		patrolRegion.setCreatetime(date);
		patrolRegion.setIsDel((short)0);
		patrolRegion.setLastUpdateTime(date);
		patrolRegion.setRegionName(regionName);
		patrolRegion.setColor(color);
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
	public ModelAndView patrolRegUpdate(Integer regionId,String regionName){
		ModelAndView mv = new ModelAndView();
		PatrolRegion patrolRegion = patrolRegionService.getById(regionId);
		patrolRegion.setRegionName(regionName);
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
	public ModelAndView patrolMap(Integer id) {
		ModelAndView mv = new ModelAndView();
		if(id != null && id > 0) {
			mv.addObject("id",id);
		}
		mv.setViewName("manager/system/firePatrolMap/map");
		return mv;
	}
	

}

