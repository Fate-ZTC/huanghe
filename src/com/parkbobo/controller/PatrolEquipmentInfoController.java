/*
package com.parkbobo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolEquipmentInfo;
import com.parkbobo.service.PatrolEquipmentInfoService;
import com.parkbobo.utils.PageBean;
import com.parkbobo.utils.message.MessageListBean;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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

*/
/**
 * 巡更定位设备信息管理（待定，等回复做不做）
 * @version 1.0
 * @author ZQ
 * @since 2018-12-28 14:50:37
 *//*

@Controller
public class PatrolEquipmentInfoController {

    @Resource
    private PatrolEquipmentInfoService patrolEquipmentInfoService;


    private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

    */
/**
     * 后台获取定位设备列表
     * @param patrolEquipmentInfo 检索信息
     * @param pageSize
     * @param page
     * @param method
     * @return
     *//*

    @RequestMapping("patrolEquipmentInfo_list")
    public ModelAndView list(PatrolEquipmentInfo patrolEquipmentInfo, Integer pageSize, Integer page, String method)
    {
        ModelAndView mv = new ModelAndView();
        String hql = "from PatrolEquipmentInfo where 1 = 1 ";
        if(StringUtil.isNotEmpty(patrolEquipmentInfo.getPatrolEquipmentManufacturer().getManufactuName())){
            hql += "and manufacturer_id like '%" + patrolEquipmentInfo.getPatrolEquipmentManufacturer().getManufactuName() + "%' ";
        }

        if(patrolEquipmentInfo.getNumber() != null){
            hql += "and number = " + patrolEquipmentInfo.getNumber() + " ";
        }

        if(patrolEquipmentInfo.getName() != null){
            hql += "and name = " + patrolEquipmentInfo.getName() + " ";
        }

        hql += "order by updateTime desc";

        if(pageSize == null || pageSize == 0){
            pageSize = 12;
        }

        if(page == null || page == 0){
            page = 1;
        }

        PageBean<PatrolEquipmentInfo> pageBean = patrolEquipmentInfoService.pageQuery(hql, pageSize, page);

        mv.addObject("pageBean", pageBean);
        mv.addObject("pageSize", pageSize);
        mv.addObject("page", page);
        mv.addObject("method", method);
        mv.addObject("patrolEquipmentInfo", patrolEquipmentInfo);
        mv.setViewName("manager/system/patrolEquipmentInfo/patrolEquipmentInfo-list");
        return mv;
    }

    */
/**
     * 添加
     * @param method
     * @param patrolEquipmentInfo
     * @return
     *//*

    @RequestMapping("patrolEquipmentInfo_add")
    public ModelAndView add(String method,PatrolEquipmentInfo patrolEquipmentInfo)
    {
        ModelAndView mv = new ModelAndView();
        //添加
        if(StringUtil.isNotEmpty(method) && method.equals("add"))
        {
            String[] propertyNames = {"number", "name"};
            Object[] values = {patrolEquipmentInfo.getNumber(), patrolEquipmentInfo.getName()};
            PatrolEquipmentInfo byNumberName = patrolEquipmentInfoService.getUniqueByPropertys(propertyNames,values);
            if(byNumberName!=null){
                mv.setViewName("manager/system/patrolEquipmentInfo/patrolEquipmentInfo-add");
                mv.addObject("msg","number, name已存在,请检查");
                return mv;
            }
            Date date = new Date();
            patrolEquipmentInfo.setUpdateTime(date);
            patrolEquipmentInfoService.add(patrolEquipmentInfo);
            mv.setViewName("redirect:/patrolEquipmentInfo_list?method=addSuccess");
        }
        //跳转到添加页面
        else
        {
            mv.setViewName("manager/system/patrolEquipmentInfo/patrolEquipmentInfo-add");
        }
        return mv;
    }

    */
/**
     * 编辑
     * @param method
     * @param patrolEquipmentInfo
     * @param id
     * @param msg
     * @return
     *//*

    @RequestMapping("patrolEquipmentInfo_edit")
    public ModelAndView edit(String method,PatrolEquipmentInfo patrolEquipmentInfo, Integer id, String msg)
    {
        ModelAndView mv = new ModelAndView();
        //编辑
        if(StringUtil.isNotEmpty(method) && method.equals("edit"))
        {

            String hql = "from PatrolEquipmentInfo where number = " + patrolEquipmentInfo.getNumber()
                    + " and name = " + patrolEquipmentInfo.getName()
                    + " and equipmenId != " + patrolEquipmentInfo.getEquipmenId();
            List<PatrolEquipmentInfo> list = patrolEquipmentInfoService.getByHql(hql);
            if(list.size() > 0){
                mv.setViewName("redirect:/patrolEquipmentInfo_edit?id=" + patrolEquipmentInfo.getEquipmenId());
                mv.addObject("msg","number, name已存在,请检查");
            } else{
                Date date = new Date();
                patrolEquipmentInfo.setUpdateTime(date);
                patrolEquipmentInfoService.update(patrolEquipmentInfo);
                mv.setViewName("redirect:/patrolEquipmentInfo_list?method=editSuccess");
            }
        }
        //跳转到编辑页面
        else
        {
            patrolEquipmentInfo = patrolEquipmentInfoService.get(id);
            mv.addObject("patrolEquipmentInfo", patrolEquipmentInfo);
            mv.setViewName("manager/system/patrolEquipmentInfo/patrolEquipmentInfo-edit");
            if(StringUtil.isNotEmpty(msg)){
                mv.addObject("msg", msg);
            }
        }
        return mv;
    }

    */
/**
     * 删除
     * @param ids
     * @return
     *//*

    @RequestMapping("patrolEquipmentInfo_delete")
    public ModelAndView delete(String ids)
    {
        ModelAndView mv = new ModelAndView();
        String[] idArray = ids.split(",");
        for(String str : idArray){
            patrolEquipmentInfoService.delete(Integer.parseInt(str));
        }
        mv.setViewName("redirect:/patrolBeaconInfo_list?method=deleteSuccess");
        return mv;
    }

    */
/**
     * 导出excel
     *//*

    @RequestMapping("patrolEquipmentInfo_excelOut")
    public ResponseEntity<byte[]> excelOut(PatrolEquipmentInfo patrolEquipmentInfo, HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setCharacterEncoding("UTF-8");
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //导出文件的标题
        String title = "定位设备信息"+df.format(today)+".xls";
        List<PatrolEquipmentInfo> list = null;
        try {
            String hql = "from PatrolEquipmentInfo where 1 = 1 ";
            if(StringUtil.isNotEmpty(patrolEquipmentInfo.getPatrolEquipmentManufacturer().getManufactuName())){
                hql += "and manufacturer_id like '%" + patrolEquipmentInfo.getPatrolEquipmentManufacturer().getManufactuName() + "%' ";
            }

            if(patrolEquipmentInfo.getMajor() != null){
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

    */
/**
     * 下载导入模板
     *//*

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

    */
/**
     * 进入导入页面
     * @return
     *//*

    @RequestMapping(value = "/patrolBeaconInfo_toImport")
    public ModelAndView toImport(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("manager/system/patrolBeaconInfo/patrolBeaconInfo-import");
        return mv;
    }

    */
/**
     * 导入蓝牙标签
     * @param attached
     * @return
     *//*

    @RequestMapping(value = "/patrolBeaconInfo_import")
    public ModelAndView importBeaconInfo(@RequestParam("attached") MultipartFile attached) throws IOException {
        ModelAndView mv = new ModelAndView();

        HSSFWorkbook work = new HSSFWorkbook(attached.getInputStream());// 得到这个excel表格对象
        HSSFSheet sheet = work.getSheetAt(0); //得到第一个sheet
        int rowNo = sheet.getLastRowNum(); //得到行数
        System.out.println("rowNo:" + rowNo);
        for (int i = 1; i <= rowNo; i++) {
            HSSFRow row = sheet.getRow(i);
            HSSFCell cell1 = row.getCell((short) 0);
            HSSFCell cell2 = row.getCell((short) 1);
            HSSFCell cell3 = row.getCell((short) 2);
            if(cell1!=null){
                cell1.setCellType(CellType.STRING);
            }
            if(cell2!=null){
                cell2.setCellType(CellType.STRING);
            }
            if(cell3!=null){
                cell3.setCellType(CellType.STRING);
            }
            String ce1 = cell1 == null?"空":cell1.getStringCellValue();
            String ce2 = cell2 == null?"空":cell2.getStringCellValue();
            String ce3 = cell3 == null?"空":cell3.getStringCellValue();

            System.out.println(ce1 + "\t" + ce2 + "\t" + ce3);

            if(StringUtil.isNotEmpty(ce1) && StringUtil.isNotEmpty(ce2) && StringUtil.isNotEmpty(ce3)){
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

    */
/**
     * 配置标签-点位绑定时
     * 获取未绑定标签
     * 如果是修改配置，则需要根据点位把已绑定标签获取到
     * @param pointId
     * @return
     *//*

    @ResponseBody
    @RequestMapping("/patrolBeaconInfo_loadUnbindBeacons")
    public String loadUnbindBeacons(Integer pointId, String uuid, Integer major, Integer minor){
        MessageListBean<PatrolBeaconInfo> messageListBean = new MessageListBean<PatrolBeaconInfo>();

        try {
            String hql = "from PatrolBeaconInfo where 1 = 1 ";

            String queryHql = "";
            if(StringUtil.isNotEmpty(uuid)){
                queryHql += "and uuid like '%" + uuid + "%' ";
            }
            if(major != null){
                queryHql += "and major = " + major + " ";
            }
            if(minor != null){
                queryHql += "and minor = " + minor + " ";
            }

            if(pointId != null){
                hql += "and (patrolSignPointInfo.pointId = " + pointId + " or (patrolSignPointInfo is null " + queryHql + ")) ";
            } else{
                hql += "and patrolSignPointInfo is null " + queryHql;
            }

            hql += "order by patrolSignPointInfo, beaconId";
            System.out.println(hql);
            List<PatrolBeaconInfo> beaconInfoList = patrolBeaconInfoService.getByHql(hql);
            if(beaconInfoList.size() > 0){
                messageListBean.setStatus(true);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
                messageListBean.setData(beaconInfoList);
            } else{
                messageListBean.setStatus(false);
                messageListBean.setCode(-1);
                messageListBean.setMessage("没有未绑定标签");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageListBean.setStatus(false);
            messageListBean.setCode(-1);
            messageListBean.setMessage("接口错误");
        }
        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

}
*/
