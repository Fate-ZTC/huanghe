package com.parkbobo.controller;

import com.parkbobo.model.PatrolPause;
import com.parkbobo.service.PatrolPauseService;
import com.parkbobo.utils.PageBean;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PatrolPauseController {
    @Resource
    private PatrolPauseService patrolPauseService;

    @RequestMapping("patrolPause_list")
    public ModelAndView list(String method, String usercode, String name,String startTime, String endTime, Integer pageSize, Integer page){
        ModelAndView mv = new ModelAndView();
        String hql = "from  PatrolPause  where 1=1";
        if(StringUtils.isNotBlank(usercode)){
            hql +=" and usercode like '%"+usercode+"%'";
        }
        if(StringUtils.isNotBlank(name)){
            hql += " and username like '%" + name +"%'";
        }
        if(StringUtils.isNotBlank(startTime)){
            hql += " and pauseStart > '" + startTime+"'";
        }
        if(StringUtils.isNotBlank(endTime)){
            hql += " and pauseEnd < '" + endTime+"'";
        }
        hql += " order by pauseEnd desc";
        PageBean<PatrolPause> patrolPausePage =  patrolPauseService.pageQuery(hql,pageSize==null?10:pageSize,page==null?1:page);
        mv.addObject("usercode",usercode);
        mv.addObject("startTime",startTime);
        mv.addObject("endTime",endTime);
        mv.addObject("name",name);
        mv.addObject("page",page);
        mv.addObject("pageSize",pageSize);
        mv.addObject("method",method);
        mv.addObject("patrolPausePage", patrolPausePage);
        mv.setViewName("manager/system/patrolPause/patrolPause-list");
        return mv;
    }
    /**
     * 删除暂停巡更记录信息
     * @return
     */
    @RequestMapping("patrolPause_delete")
    public ModelAndView delete(String ids,HttpSession session) {
        ModelAndView mv = new ModelAndView();
        this.patrolPauseService.bulkDelete(ids);
        mv.setViewName("redirect:/patrolPause_list?method=deleteSuccess");
        return mv;
    }

    /**
     * 暂停巡更记录导出
     * @param response
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("patrolPause_excelOut")
    public ResponseEntity<byte[]> excelOut(String ids,String name, String usercode,String pauseStart, String pauseEnd, HttpServletResponse response, HttpServletRequest request) throws IOException{
        response.setCharacterEncoding("UTF-8");
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //导出文件的标题
        String title = "暂停巡更记录"+df.format(today)+".xls";
        List<PatrolPause> list = new ArrayList<>();
        if(ids!=null && ids.length() > 0) {
            String[] strs = ids.split(",");
            Integer[] idArr = new Integer[strs.length];
            for (int i=0; i< strs.length; i++) {
                idArr[i] = Integer.parseInt(strs[i]);
                PatrolPause patrolPause = this.patrolPauseService.get(idArr[i]);
                list.add(patrolPause);
            }
        }else{
            String hql = "from  PatrolPause  where 1=1";
            if(StringUtils.isNotBlank(usercode)){
                hql +=" and usercode like '%"+usercode+"%'";
            }
            if(StringUtils.isNotBlank(name)){
                hql += " and username like '%" + name +"%'";
            }
            if(StringUtils.isNotBlank(pauseStart)){
                hql += " and pauseStart > '"+pauseStart+"'";
            }
            if(StringUtils.isNotBlank(pauseEnd)){
                hql += " and pauseEnd < '"+pauseEnd+"'";
            }
            hql += " order by pauseEnd desc";
            try {
                list = this.patrolPauseService.getByHql(hql);
            } catch (Exception e1) {
            }
        }
        //设置表格标题行
        String[] headers = new String[] {"姓名","账号", "暂停开始时间","暂停结束时间","暂停时长"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        if (list!=null && list.size()>0) {
            for (PatrolPause entryOutRecord : list) {//循环每一条数据
                objs = new Object[headers.length];
                objs[0] = entryOutRecord.getUsername();
                objs[1] = entryOutRecord.getUsercode();
                objs[2] = entryOutRecord.getPauseStart();
                objs[3] = entryOutRecord.getPauseEnd();
                objs[4] = entryOutRecord.getCheckDuration();
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
        style.setAlignment(HorizontalAlignment.CENTER);HSSFCell cell = null;   //设置单元格的数据类型
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i,10000);
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
