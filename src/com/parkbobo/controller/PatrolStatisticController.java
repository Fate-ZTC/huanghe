package com.parkbobo.controller;

import com.parkbobo.VO.PatrolsignStatisticVO;
import com.parkbobo.model.PatrolConfig;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.service.*;
import com.parkbobo.utils.PageBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
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
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台巡更统计
 */
@Controller
public class PatrolStatisticController {

    @Resource
    private PatrolConfigService patrolConfigService;
    @Resource
    private PatrolUserRegionService patrolUserRegionService;
    @Resource
    private PatrolSignPointInfoService patrolSignPointInfoService;
    @Resource
    private PatrolBeaconInfoService patrolBeaconInfoService;
    @Resource
    private PatrolSignRecordService patrolSignRecordService;
    @Resource
    private PatrolSignUserDateViewService patrolSignUserDateViewService;
    @Resource
    private PatrolPauseService patrolPauseService;
    @Resource
    private PatrolUserService patrolUserService;

    @RequestMapping("patrolStatistic_list")
    public ModelAndView statisticController(String startTime,String endTime,String name,String jobNum,Integer page,Integer pageSize){
        ModelAndView mv = new ModelAndView();
        Map<String, PatrolsignStatisticVO> statisticVOMap = new HashMap<String, PatrolsignStatisticVO>();
        Map<Integer, Integer> regionPointCountMap = new HashMap<Integer, Integer>();

        PatrolConfig patrolConfig = patrolConfigService.getConfig(1);

        String hql = "from PatrolUserRegion where 1=1 ";
        if(StringUtils.isNotBlank(name)){
            hql += " and username like '%" + name + "%'";
        }
        if(StringUtils.isNotBlank(jobNum)){
            hql += " and jobNum like '%" + jobNum + "%'";
        }
        if(StringUtils.isNotBlank(startTime)){
            hql += " and startTime > '" + startTime + "'";
        }
        if(StringUtils.isNotBlank(endTime)){
            hql += " and endTime < '" + endTime + "'";
        }
        List<PatrolUserRegion> userRegionList = patrolUserRegionService .getByHQL(hql);
        for(PatrolUserRegion userRegion : userRegionList){
            if(!regionPointCountMap.containsKey(userRegion.getRegionId())){
                regionPointCountMap.put(userRegion.getRegionId(), patrolSignPointInfoService.countWithRegionId(userRegion.getRegionId()));
            }

            Long endMillis = System.currentTimeMillis();
            if(userRegion.getEndTime() != null){
                endMillis = userRegion.getEndTime().getTime();
            }

            Long expectedCount = calculateNeedSignCount(userRegion.getStartTime().getTime(), endMillis, patrolConfig.getSignRange(), patrolConfig.getOvertimeDeal());

            if(!statisticVOMap.containsKey(userRegion.getJobNum())){
                PatrolsignStatisticVO statisticVO = new PatrolsignStatisticVO();
                statisticVO.setJobNum(userRegion.getJobNum());
                statisticVO.setUsername(userRegion.getUsername());
                statisticVO.setExpectedCount(Integer.parseInt(String.valueOf(expectedCount)));
                statisticVO.setSignedCount(0);

                statisticVOMap.put(userRegion.getJobNum(), statisticVO);
            } else{
                PatrolsignStatisticVO statisticVO = statisticVOMap.get(userRegion.getJobNum());
                statisticVO.setExpectedCount(statisticVO.getExpectedCount() + Integer.parseInt(String.valueOf(expectedCount)));
                statisticVOMap.put(userRegion.getJobNum(), statisticVO);
            }
        }
        String userHql = "from PatrolUser where isDel = 0";
        if(StringUtils.isNotBlank(name)){
            userHql += " and username like '%" + name + "%'";
        }
        if(StringUtils.isNotBlank(jobNum)){
            userHql += " and jobNum like '%" + jobNum + "%'";
        }
        userHql += " order by jobNum";
        if(pageSize==null){
            pageSize = 10;
        }
        if(page==null){
            page = 1;
        }
        PageBean<PatrolUser> userPageBean = patrolUserService.getUsers(userHql,pageSize,page);
        List<PatrolUser> patrolUserList = null;
        if(userPageBean!=null){
            patrolUserList = userPageBean.getList();
        }
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(patrolUserList!=null){
            for(PatrolUser patrolUser : patrolUserList) {
                if (statisticVOMap.containsKey(patrolUser.getJobNum())) {
                    Map<String,Object> map = new HashMap<>();
                    Integer effectiveSign = patrolSignRecordService.countEffective(patrolUser.getJobNum(), startTime, endTime);
                    PatrolsignStatisticVO statisticVO = statisticVOMap.get(patrolUser.getJobNum());
                    map.put("jobNum",statisticVO.getJobNum());
                    map.put("username",statisticVO.getUsername());
                    map.put("expectedCount",statisticVO.getExpectedCount());
                    map.put("signedCount",effectiveSign);
                    map.put("noSignCount",statisticVO.getExpectedCount() - effectiveSign);
                    mapList.add(map);
                }else{
                    Map<String,Object> map = new HashMap<>();
                    map.put("jobNum",patrolUser.getJobNum());
                    map.put("username",patrolUser.getUsername());
                    map.put("expectedCount",0);
                    map.put("signedCount",0);
                    map.put("noSignCount",0);
                    mapList.add(map);
                }
            }
        }
        PageBean.setTotalPage(userPageBean.getAllRow()%pageSize==0?userPageBean.getAllRow()/pageSize:userPageBean.getAllRow()/pageSize+1);
        mv.addObject("startTime",startTime);
        mv.addObject("endTime",endTime);
        mv.addObject("name",name);
        mv.addObject("jobNum",jobNum);
        mv.addObject("page",page);
        mv.addObject("pageSize",pageSize);
        mv.addObject("data",mapList);
        mv.addObject("userPageBean",userPageBean);
        mv.setViewName("manager/system/patrolStatistic/patrolStatistic-list");
        return mv;
    }

    /**
     * 根据开始毫秒数、结束毫秒数、签到周期、超出时间处理方式
     * 计算应签次数
     * @param startMillis
     * @param endMillis
     * @param signRange
     * @param overtimeDeal
     * @return
     */
    public Long calculateNeedSignCount(Long startMillis, Long endMillis, Integer signRange, Integer overtimeDeal){
        if(overtimeDeal == 1){
            return (endMillis - startMillis) / (signRange * 60 * 1000) + ((endMillis - startMillis) % (signRange * 60 * 1000) == 0 ? 0 : 1);
        } else{
            return (endMillis - startMillis) / (signRange * 60 * 1000);
        }
    }

    @RequestMapping("patrolStatistic_excelOut")
    public ResponseEntity<byte[]> excelOut(String name, String jobNum, String startTime, String endTime, HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setCharacterEncoding("UTF-8");
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //导出文件的标题
        String title = "巡更统计"+df.format(today)+".xls";
        Map<String, PatrolsignStatisticVO> statisticVOMap = new HashMap<String, PatrolsignStatisticVO>();
        Map<Integer, Integer> regionPointCountMap = new HashMap<Integer, Integer>();

        PatrolConfig patrolConfig = patrolConfigService.getConfig(1);

        String hql = "from PatrolUserRegion where 1=1 ";
        if(StringUtils.isNotBlank(name)){
            hql += " and username like '%" + name + "%'";
        }
        if(StringUtils.isNotBlank(jobNum)){
            hql += " and jobNum like '%" + jobNum + "%'";
        }
        if(StringUtils.isNotBlank(startTime)){
            hql += " and startTime > '" + startTime + "'";
        }
        if(StringUtils.isNotBlank(endTime)){
            hql += " and endTime < '" + endTime + "'";
        }
        List<PatrolUserRegion> userRegionList = patrolUserRegionService .getByHQL(hql);
        for(PatrolUserRegion userRegion : userRegionList){
            if(!regionPointCountMap.containsKey(userRegion.getRegionId())){
                regionPointCountMap.put(userRegion.getRegionId(), patrolSignPointInfoService.countWithRegionId(userRegion.getRegionId()));
            }

            Long endMillis = System.currentTimeMillis();
            if(userRegion.getEndTime() != null){
                endMillis = userRegion.getEndTime().getTime();
            }

            Long expectedCount = calculateNeedSignCount(userRegion.getStartTime().getTime(), endMillis, patrolConfig.getSignRange(), patrolConfig.getOvertimeDeal());

            if(!statisticVOMap.containsKey(userRegion.getJobNum())){
                PatrolsignStatisticVO statisticVO = new PatrolsignStatisticVO();
                statisticVO.setJobNum(userRegion.getJobNum());
                statisticVO.setUsername(userRegion.getUsername());
                statisticVO.setExpectedCount(Integer.parseInt(String.valueOf(expectedCount)));
                statisticVO.setSignedCount(0);

                statisticVOMap.put(userRegion.getJobNum(), statisticVO);
            } else{
                PatrolsignStatisticVO statisticVO = statisticVOMap.get(userRegion.getJobNum());
                statisticVO.setExpectedCount(statisticVO.getExpectedCount() + Integer.parseInt(String.valueOf(expectedCount)));
                statisticVOMap.put(userRegion.getJobNum(), statisticVO);
            }
        }
        String userHql = "from PatrolUser where isDel = 0";
        if(StringUtils.isNotBlank(name)){
            userHql += " and username like '%" + name + "%'";
        }
        if(StringUtils.isNotBlank(jobNum)){
            userHql += " and jobNum like '%" + jobNum + "%'";
        }
        userHql += " order by jobNum";
        List<PatrolUser> patrolUserList = this.patrolUserService.getBySth(name,jobNum);
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(patrolUserList!=null){
            for(PatrolUser patrolUser : patrolUserList) {
                if (statisticVOMap.containsKey(patrolUser.getJobNum())) {
                    Map<String,Object> map = new HashMap<>();
                    Integer effectiveSign = patrolSignRecordService.countEffective(patrolUser.getJobNum(), startTime, endTime);
                    PatrolsignStatisticVO statisticVO = statisticVOMap.get(patrolUser.getJobNum());
                    map.put("jobNum",statisticVO.getJobNum());
                    map.put("username",statisticVO.getUsername());
                    map.put("expectedCount",statisticVO.getExpectedCount());
                    map.put("signedCount",effectiveSign);
                    map.put("noSignCount",statisticVO.getExpectedCount() - effectiveSign);
                    mapList.add(map);
                }
            }
        }

        //设置表格标题行
        String[] headers = new String[] {"姓名","工号", "应巡查","实巡查","未巡查"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        if (mapList!=null && mapList.size()>0) {
            for (Map<String,Object> entryOutRecord : mapList) {//循环每一条数据
                objs = new Object[headers.length];
                objs[0] = entryOutRecord.get("username");
                objs[1] = entryOutRecord.get("jobNum");
                objs[2] = entryOutRecord.get("expectedCount");
                objs[3] = entryOutRecord.get("signedCount");
                objs[4] = entryOutRecord.get("noSignCount");
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

}
