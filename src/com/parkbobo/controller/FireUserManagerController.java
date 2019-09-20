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
import com.parkbobo.model.FirePatrolUser;
import com.parkbobo.service.FirePatrolExceptionService;
import com.parkbobo.service.FirePatrolUserService;
import com.parkbobo.utils.PageBean;
import com.system.utils.StringUtil;

/**
 * 消防巡查人员管理
 * @author zj
 *@version 1.0
 */
@Controller
public class FireUserManagerController {

	@Resource
	private FirePatrolUserService firePatrolUserService;

	@Resource
	private FirePatrolExceptionService firePatrolExceptionService;

	/**
	 * 巡查人员
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("firePatrolUserList")
	public ModelAndView list(FirePatrolUser firePatrolUser,Integer page,Integer pageSize) throws UnsupportedEncodingException
	{
		ModelAndView mv = new ModelAndView();

		String hql = "from  FirePatrolUser f where isDel = 0";

		if(firePatrolUser != null && StringUtil.isNotEmpty(firePatrolUser.getUsername()))
		{
			hql+=" and f.username like '%" + firePatrolUser.getUsername() + "%'";
		}
		if(firePatrolUser!= null && StringUtil.isNotEmpty(firePatrolUser.getJobNum())){
			hql +=" and f.jobNum like '%"+firePatrolUser.getJobNum()+"%'";
		}
		hql += " order by f.id";
		PageBean<FirePatrolUser> firePatrolUserPage = this.firePatrolUserService.getUsers(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("firePatrolUserPage", firePatrolUserPage);
		mv.addObject("firePatrolUser",firePatrolUser);
		mv.setViewName("manager/system/firePatrol/fireUser-list");
		return mv;
	}

	/**
	 * 添加消防巡查用户信息
	 * @param method
	 * @param firePatrolUser
	 * @param session
	 * @return
	 */
	@RequestMapping("firePatrolUser_add")
	public ModelAndView add(String method,FirePatrolUser firePatrolUser,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		//添加
		if(StringUtil.isNotEmpty(method) && method.equals("add"))
		{
			FirePatrolUser byJobNum = this.firePatrolUserService.getByJobNum(firePatrolUser.getJobNum());
			if(byJobNum!=null){
				mv.setViewName("manager/system/firePatrol/fireUser-add");
				mv.addObject("msg","工号已存在,请更换");
				return mv;
			}
			Date date = new Date();
			firePatrolUser.setCreateTime(date);
			firePatrolUser.setCampusNum(0);
			firePatrolUser.setIsDel((short)0);
			firePatrolUser.setLastUpdateTime(date);
			firePatrolUserService.addUser(firePatrolUser);
			mv.setViewName("redirect:/firePatrolUserList?method=addSuccess");
		}
		//跳转到添加页面
		else
		{
			mv.setViewName("manager/system/firePatrol/fireUser-add");
		}
		return mv;
	}
	@RequestMapping("firePatrolUser_edit")
	public ModelAndView edit(String method,FirePatrolUser firePatrolUser,HttpSession session,Integer id)
	{
		ModelAndView mv = new ModelAndView();
		//编辑
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			FirePatrolUser user = this.firePatrolUserService.getById(firePatrolUser.getId());
			firePatrolUser.setCampusNum(user.getCampusNum());
			firePatrolUser.setClientId(user.getClientId());
			firePatrolUser.setCreateTime(user.getCreateTime());
			firePatrolUser.setIsDel((short)0);
			firePatrolUser.setLastUpdateTime(new Date());
			firePatrolUserService.update(firePatrolUser);
			mv.setViewName("redirect:/firePatrolUserList?method=editSuccess");
		}
		//跳转到编辑页面
		else
		{
			firePatrolUser = firePatrolUserService.getById(id);
			mv.addObject("firePatrolUser", firePatrolUser);
			mv.setViewName("manager/system/firePatrol/fireUser-edit");
		}
		return mv;
	}
	/**
	 * 删除巡查人员
	 * @return
	 */
	@RequestMapping("firePatrolUser_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		if(ids==null){
			mv.setViewName("redirect:/firePatrolUserList");
			mv.addObject("msg","请勾选信息");
			return mv;
		}
		String[] idArr = ids.split(",");
		for(int i = 0;i < idArr.length;i++){
			int id = Integer.parseInt(idArr[i]);
			FirePatrolUser patrolUser = this.firePatrolUserService.getById(id);
			patrolUser.setIsDel((short)1);
			this.firePatrolUserService.update(patrolUser);
		}
		mv.setViewName("redirect:/firePatrolUserList?method=deleteSuccess");
		return mv;
	}

	//下面市消防巡查术语管理相关内容

	/**
	 * 消防术语记录查询
	 * @param keyWords	关键字
	 * @return
	 */
	@RequestMapping("/firePatrolExceptionType_list")
	public ModelAndView toFireExceptionTypeList(String keyWords,int page,int pageSize) {

		ModelAndView mv = new ModelAndView();
		//这里进行查询
		page = (page <= 0 ? 1 : page);
		pageSize = (pageSize <= 0 ? 10 : pageSize);

		StringBuffer sb = new StringBuffer();
		sb.append("FROM FirePatrolException ");
		if(keyWords != null && !"".equals(keyWords)) {
			sb.append("  WHERE exceptionName LIKE '%").append(keyWords).append("%'");
		}
		sb.append(" ORDER BY sort DESC");
		System.out.println(sb.toString());
		PageBean<FirePatrolException> patrolExceptionPageBean = firePatrolExceptionService.getByHql(sb.toString(),pageSize,page);
		//这里进行查询
		mv.addObject("patrolExceptionPageBean",patrolExceptionPageBean);
		mv.setViewName("manager/system/firePatrolExceptionType/firePatrol-exception-desc");
		return mv;
	}


	/**
	 * 单个删除和批量删除异常分类
	 * @return
	 */
	@RequestMapping("firePatrolExceptionType_delete")
	public ModelAndView deleteExceptionType(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		if(ids==null){
			mv.setViewName("redirect:/firePatrolExceptionType_list");
			mv.addObject("msg","请勾选信息");
			return mv;
		}
		String[] idArr = ids.split(",");
		for(int i = 0;i < idArr.length;i++) {
			int id = Integer.parseInt(idArr[i]);
			//这里是根据id进行删除
			this.firePatrolExceptionService.deleteById(id);
		}
		mv.setViewName("redirect:/firePatrolExceptionType_list?method=deleteSuccess");
		return mv;
	}


	//这里是进行添加
	@RequestMapping("/addExceptionType")
	public ModelAndView addFirePatrolExceptionType() {

		return null;
	}





	/**
	 * 导出excel
	 */
	@RequestMapping("firePatrolUser_excelOut")
	public ResponseEntity<byte[]> excelOut(FirePatrolUser firePatrolUser,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "消防巡查人员记录"+df.format(today)+".xls";
		List<FirePatrolUser> list = null;
		try {
			list = this.firePatrolUserService.getBySth(firePatrolUser.getUsername(),firePatrolUser.getJobNum());
		} catch (Exception e1) {
			e1.printStackTrace();
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
		} catch (Exception e) {
			return null;
		}
	}
}
