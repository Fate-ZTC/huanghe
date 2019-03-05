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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.PatrolUser;
import com.parkbobo.service.PatrolUserService;
import com.parkbobo.utils.PageBean;
import com.system.utils.StringUtil;

/**
 * 安防巡更人员管理后台
 * @author zj
 *
 */
@Controller
public class PatrolUserManagerController {

	@Resource
	private PatrolUserService patrolUserService;
	/**
	 * 巡查人员
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("patrolUser_list")
	public ModelAndView list(PatrolUser patrolUser,Integer page,Integer pageSize) throws UnsupportedEncodingException
	{
		ModelAndView mv = new ModelAndView();

		String hql = "from  PatrolUser f where isDel = 0";
		if(patrolUser != null && StringUtil.isNotEmpty(patrolUser.getUsername()))
		{
			hql+=" and f.username like '%" + patrolUser.getUsername() + "%'";
		}
		if(patrolUser != null && StringUtil.isNotEmpty(patrolUser.getJobNum())){
			hql +=" and f.jobNum like '%"+patrolUser.getJobNum()+"%'";
		}
		hql += " order by f.id";
		PageBean<PatrolUser> patrolUserPage = this.patrolUserService.getUsers(hql,pageSize==null?12:pageSize, page==null?1:page);
		mv.addObject("patrolUserPage", patrolUserPage);
		mv.addObject("patrolUser",patrolUser);
		mv.setViewName("manager/system/patrolUser/patrolUser-list");
		return mv;
	}
	@RequestMapping("patrolUser_add")
	public ModelAndView add(String method,PatrolUser patrolUser,HttpSession session )
	{
		ModelAndView mv = new ModelAndView();
		//添加
		if(StringUtil.isNotEmpty(method) && method.equals("add"))
		{
			PatrolUser byJobNum = this.patrolUserService.getByJobNum(patrolUser.getJobNum());
			if(byJobNum!=null){
				mv.setViewName("manager/system/patrolUser/patrolUser-add");
				mv.addObject("msg","工号已存在,请更换");
				return mv;
			}
			Date date = new Date();
			patrolUser.setCreatetime(date);
			patrolUser.setCampusNum(1);
			patrolUser.setIsDel((short)0);
			patrolUser.setLastUpdateTime(date);
			patrolUserService.addUser(patrolUser);
			mv.setViewName("redirect:/patrolUser_list?method=addSuccess");
		}
		//跳转到添加页面
		else
		{
			mv.setViewName("manager/system/patrolUser/patrolUser-add");
		}
		return mv;
	}



	@RequestMapping("patrolUser_edit")
	public ModelAndView edit(String method,PatrolUser patrolUser,HttpSession session,Integer id)
	{
		ModelAndView mv = new ModelAndView();
		//编辑
		if(StringUtil.isNotEmpty(method) && method.equals("edit"))
		{
			PatrolUser user = patrolUserService.getById(patrolUser.getId());
			patrolUser.setLastUpdateTime(new Date());
			patrolUser.setCampusNum(user.getCampusNum());
			patrolUser.setClientId(user.getClientId());
			patrolUser.setCreatetime(user.getCreatetime());
			patrolUser.setIsDel((short)0);
			patrolUserService.merge(patrolUser);
			mv.setViewName("redirect:/patrolUser_list?method=editSuccess");
		}
		//跳转到编辑页面
		else
		{
			patrolUser = patrolUserService.getById(id);
			mv.addObject("patrolUser",patrolUser);
			mv.setViewName("manager/system/patrolUser/patrolUser-edit");
		}
		return mv;
	}
	/**
	 * 删除巡查人员
	 * @return
	 */
	@RequestMapping("patrolUser_delete")
	public ModelAndView delete(String ids,HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		if(ids==null){
			mv.setViewName("redirect:/patrolUser_list");
			mv.addObject("msg","请勾选信息");
			return mv;
		}
		String[] idArr = ids.split(",");
		for(int i = 0;i < idArr.length;i++){
			int id = Integer.parseInt(idArr[i]);
			PatrolUser patrolUser = this.patrolUserService.getById(id);
			patrolUser.setIsDel((short)1);
			this.patrolUserService.update(patrolUser);
		}
		mv.setViewName("redirect:/patrolUser_list?method=deleteSuccess");
		return mv;
	}
	/**
	 * 导出excel
	 */
	@RequestMapping("patrolUser_excelOut")
	public ResponseEntity<byte[]> excelOut(String ids,String jobNum,String username,HttpServletResponse response,HttpServletRequest request) throws IOException{
		response.setCharacterEncoding("UTF-8");
		System.out.println(ids+":::::"+username+jobNum);
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//导出文件的标题
		String title = "安防巡更人员记录"+df.format(today)+".xls";
		List<PatrolUser> list = new ArrayList<>();
		if(ids!=null && ids.length()>0){
            String[] strs = ids.split(",");
            Integer[] idArr = new Integer[strs.length];
            for (int i=0; i< strs.length; i++) {
                idArr[i] = Integer.parseInt(strs[i]);
                PatrolUser user = this.patrolUserService.getById(idArr[i]);
                list.add(user);
            }
		}else{
            try {
                list = this.patrolUserService.getBySth(username,jobNum);
            } catch (Exception e1) {
            }
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
