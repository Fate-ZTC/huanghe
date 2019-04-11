package com.parkbobo.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.VO.PatrolLocationInfoVo;
import com.parkbobo.VO.PatrolLocationMassageVo;
import com.parkbobo.model.*;
import com.parkbobo.service.*;
import com.parkbobo.utils.filter.GeoTrackFilter;
import com.system.utils.StringUtil;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @vesion:2.1
 * @author:wys
 * @date:2019/3/26
 * 巡更轨迹相关接口
 */
@Controller
public class PatrolTrailInterfaceController {
    @Resource
    private PatrolUserService patrolUserService;
    @Resource
    private PatrolUserRegionService patrolUserRegionService;
    @Resource
    private PatrolLocationInfoService patrolLocationInfoService;
    @Resource
    private PatrolRegionService patrolRegionService;
    @Resource
    private PatrolConfigService patrolConfigService;
    @Resource
    private PatrolRegionHistoryService patrolRegionHistoryService;
    private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.DisableCircularReferenceDetect};


    /**
     * 获取所有巡更人员
     * @param response
     */
    @RequestMapping("getPatrolPerson")
    public void getPatrolPerson(String campusNum,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();
        StringBuffer child = new StringBuffer();

        try {
            out = response.getWriter();
            String hql = "FROM PatrolUser WHERE isDel=0 and campusNum = " + Integer.parseInt(StringUtil.isEmpty(campusNum)?"0":campusNum);
            List<PatrolUser> allUser = this.patrolUserService.getAll(hql);
            for (PatrolUser patrolUser:allUser){
                Integer result = patrolUserRegionService.exist(patrolUser.getJobNum());
                child.append("{")
                        .append("\"username\":\"" + patrolUser.getUsername() + "\",")
                        .append("\"jobNum\":\"" + patrolUser.getJobNum() +"\",")
                        .append("\"isClick\":" + result)
                        .append("},");
            }
            json.append("{\"status\":true,")
            .append("\"code\":1,")
            .append("\"data\":[")
            .append(StringUtil.deleteLastStr(child.toString()))
             .append("]")
             .append("}");
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            json = new StringBuffer();
            json.append("{\"status\":false,");
            json.append("\"code\":-2,");
            json.append("\"errorMsg\":\"接口错误\"}");
            out.print(json);
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 获取巡更历史记录
     * @param jobNum
     * @param starttime
     * @param endtime
     * @param response
     */
    @RequestMapping("patrolRecordHistoryList")
    public void patrolRecordHistoryList(String jobNum,String starttime,String endtime,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();
        StringBuffer child = new StringBuffer();
        StringBuffer data = new StringBuffer();

        try {
            out = response.getWriter();
            jobNum = new String(jobNum.getBytes("ISO_8859-1"),"UTF-8");
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            String sql = "SELECT region.id,patrol_region.region_name,region.starttime,region.endtime FROM (SELECT id,region_id, " +
                    "to_char(start_time, 'yyyy-MM-dd HH:MI') as starttime,to_char(end_time, 'yyyy-MM-dd HH:MI') as endtime FROM patrol_user_region " +
                    "WHERE job_num = '"+jobNum+"' " ;
            if(StringUtil.isNotEmpty(starttime)){
                Date time = sp.parse(starttime);
                starttime = sp.format(time);
                sql += " and to_char(start_time, 'yyyy-MM-dd HH:MI') >= '"+starttime+"'";
            }
            if(StringUtil.isNotEmpty(endtime)){
                Date time = sp.parse(endtime);
                endtime = sp.format(time);
                sql += " and to_char(end_time, 'yyyy-MM-dd HH:MI') <= '"+endtime+"'";
            }
            sql += " and end_time IS NOT NULL ORDER BY to_char(start_time, 'yyyy-MM-dd') desc," +
                    "to_char(start_time, 'yyyy-MM-dd HH:MI')) AS region LEFT JOIN patrol_region ON region.region_id = patrol_region.id";
            List<Object[]> patrolUserRegions = patrolUserRegionService.getBySql(sql);
            Map<String,List<Object[]>> map = new LinkedHashMap<>();
            for (Object[] obj:patrolUserRegions){
                List<Object[]> list = map.get(obj[2].toString().substring(0,10));
                if(list==null){
                    list = new ArrayList<>();
                    list.add(obj);
                    map.put(obj[2].toString().substring(0,10),list);
                }else{
                    list.add(obj);
                    map.put(obj[2].toString().substring(0,10),list);
                }
            }
            for (String in:map.keySet()){
                List<Object[]> list = map.get(in);
                data = new StringBuffer("");
                for (Object[] obj:list){
                    data.append("{")
                        .append("\"id\":\"" + obj[0] + "\",")
                        .append("\"regionName\":\"" + obj[1] +"\",")
                        .append("\"starttime\":\"" + obj[2].toString().substring(11,obj[2].toString().length()) +"\",")
                        .append("\"endtime\":\"" + obj[3].toString().substring(11,obj[3].toString().length()) +"\"")
                        .append("},");
                }
                child.append("{")
                        .append("\"time\":\"" + in + "\",")
                        .append("\"patrolUserRegions\":[")
                        .append(StringUtil.deleteLastStr(data.toString()))
                        .append("]")
                        .append("},");
            }
            json.append("{\"status\":true,")
                    .append("\"code\":1,")
                    .append("\"data\":[")
                    .append(StringUtil.deleteLastStr(child.toString()))
                    .append("]")
                    .append("}");
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            json = new StringBuffer();
            json.append("{\"status\":false,");
            json.append("\"code\":-2,");
            json.append("\"errorMsg\":\"接口错误\"}");
            out.print(json);
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 通过区域id获取轨迹线
     * @param id
     * @param response
     */
    @RequestMapping("getPatrolTrailById")
    public void getPatrolTrailById(String id,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();
        StringBuffer child = new StringBuffer();
        StringBuffer data = new StringBuffer();

        try {
            out = response.getWriter();
            PatrolUserRegion patrolUserRegion = patrolUserRegionService.getById(Integer.parseInt(id));
            PatrolRegionHistory patrolRegion = patrolRegionHistoryService.getById(patrolUserRegion.getHistoryRegionId());
            SimpleDateFormat sp = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            SimpleDateFormat sp1 = new SimpleDateFormat("HH:mm");
            String hql = "from PatrolLocationInfo where usregId = " + patrolUserRegion.getId() + " order by timestamp";
            List<PatrolLocationInfo> infoList = patrolLocationInfoService.getByHql(hql);

            List<PatrolLocationMassageVo> patrolLocationMassageVos = new ArrayList<>();
            PatrolLocationMassageVo patrolLocationMassageVo = null ;
            List<PatrolLocationInfoVo> patrolLocationInfoVo = null;
            PatrolLocationInfo patrolLocationInfo = new PatrolLocationInfo();

            Date location_time = null;
            GeoTrackFilter filter = new GeoTrackFilter(1.0D);
            for (int i=0;i<infoList.size();i++){
                PatrolLocationInfo uli = infoList.get(i);
                Double seconds_per_timestep = 0D;
                if(i != 0){
                    seconds_per_timestep = Double.valueOf(String.valueOf(
                            (uli.getTimestamp().getTime() -
                                    location_time.getTime())
                                    / 1000));
                }
                location_time = uli.getTimestamp();

                filter.update_velocity2d(uli.getLat(), uli.getLon(), seconds_per_timestep);
                double[] newLatLon = filter.get_lat_long();
                uli.setLat(newLatLon[0]);
                uli.setLon(newLatLon[1]);

                if(i==0||i==(infoList.size()-1)){
                    patrolLocationMassageVo = new PatrolLocationMassageVo();
                    patrolLocationInfoVo = new ArrayList<>();
                    patrolLocationInfo = infoList.get(i);
                    if(i==0){
                        patrolLocationMassageVo.setMassage("开始巡更");
                    }else{
                        patrolLocationMassageVo.setMassage("结束巡更");
                    }
                    if(patrolLocationInfo.getStatus()==1){
                        patrolLocationMassageVo.setIsAbnormal(0);
                        patrolLocationMassageVo.setType(0);
                    }else{
                        patrolLocationMassageVo.setIsAbnormal(1);
                        patrolLocationMassageVo.setType(patrolLocationInfo.getPatrolException().getId());
                    }
                    patrolLocationMassageVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                    PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                    String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                    locationInfoVo.setGeom(geom);
                    locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                    locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                    locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                    patrolLocationInfoVo.add(locationInfoVo);
                    patrolLocationMassageVo.setPatrolLocationInfoVos(patrolLocationInfoVo);
                    patrolLocationMassageVos.add(patrolLocationMassageVo);
                    patrolLocationMassageVo = null;
                }else{
                    if(patrolLocationMassageVo==null){
                        patrolLocationMassageVo = new PatrolLocationMassageVo();
                        patrolLocationInfoVo = new ArrayList<>();
                        patrolLocationInfo = infoList.get(i);
                        if(patrolLocationInfo.getStatus()==1){
                            patrolLocationMassageVo.setIsAbnormal(0);
                            patrolLocationMassageVo.setType(0);
                        }else{
                            if(patrolLocationInfo.getPatrolException().getId().equals(1)){
                                patrolLocationMassageVo.setMassage("离开巡更区域");
                            }else if(patrolLocationInfo.getPatrolException().getId().equals(2)){
                                patrolLocationMassageVo.setMassage("未按时到达巡更区域");
                            }else if(patrolLocationInfo.getPatrolException().getId().equals(3)){
                                patrolLocationMassageVo.setMassage("离开巡更区域超时");
                            }else if(patrolLocationInfo.getPatrolException().getId().equals(4)){
                                patrolLocationMassageVo.setMassage("人员位置丢失");
                            }else if(patrolLocationInfo.getPatrolException().getId().equals(5)){
                                patrolLocationMassageVo.setMassage("人员位置无变化");
                            }
                            patrolLocationMassageVo.setIsAbnormal(1);
                            patrolLocationMassageVo.setType(patrolLocationInfo.getPatrolException().getId());
                        }
                        patrolLocationMassageVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                        PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                        String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                        locationInfoVo.setGeom(geom);
                        locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                        locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                        locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                        patrolLocationInfoVo.add(locationInfoVo);
                    }else{
                        if(patrolLocationInfo.getStatus().equals(infoList.get(i).getStatus())){
                            if(infoList.get(i).getStatus().equals(2)){
                                if(!patrolLocationInfo.getPatrolException().getId().equals(infoList.get(i).getPatrolException().getId())){
                                    patrolLocationMassageVo.setPatrolLocationInfoVos(patrolLocationInfoVo);
                                    patrolLocationMassageVos.add(patrolLocationMassageVo);

                                    patrolLocationMassageVo = new PatrolLocationMassageVo();
                                    patrolLocationInfoVo = new ArrayList<>();
                                    patrolLocationInfo = infoList.get(i);
                                    if(patrolLocationInfo.getStatus()==1){
                                        patrolLocationMassageVo.setMassage("回到巡更区域");
                                        patrolLocationMassageVo.setIsAbnormal(0);
                                        patrolLocationMassageVo.setType(0);
                                    }else{
                                        if(patrolLocationInfo.getPatrolException().getId().equals(1)){
                                            patrolLocationMassageVo.setMassage("离开巡更区域");
                                        }else if(patrolLocationInfo.getPatrolException().getId().equals(2)){
                                            patrolLocationMassageVo.setMassage("未按时到达巡更区域");
                                        }else if(patrolLocationInfo.getPatrolException().getId().equals(3)){
                                            patrolLocationMassageVo.setMassage("离开巡更区域超时");
                                        }else if(patrolLocationInfo.getPatrolException().getId().equals(4)){
                                            patrolLocationMassageVo.setMassage("人员位置丢失");
                                        }else if(patrolLocationInfo.getPatrolException().getId().equals(5)){
                                            patrolLocationMassageVo.setMassage("人员位置无变化");
                                        }
                                        patrolLocationMassageVo.setIsAbnormal(1);
                                        patrolLocationMassageVo.setType(patrolLocationInfo.getPatrolException().getId());
                                    }
                                    patrolLocationMassageVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                    PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                                    String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                                    locationInfoVo.setGeom(geom);
                                    locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                                    locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                    locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                                    patrolLocationInfoVo.add(locationInfoVo);
                                }else{
                                    PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                                    String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                                    locationInfoVo.setGeom(geom);
                                    locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                                    locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                    locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                                    patrolLocationInfoVo.add(locationInfoVo);
                                }
                            }else{
                                PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                                String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                                locationInfoVo.setGeom(geom);
                                locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                                locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                                patrolLocationInfoVo.add(locationInfoVo);
                            }
                        }else{
                            patrolLocationMassageVo.setPatrolLocationInfoVos(patrolLocationInfoVo);
                            patrolLocationMassageVos.add(patrolLocationMassageVo);

                            patrolLocationMassageVo = new PatrolLocationMassageVo();
                            patrolLocationInfoVo = new ArrayList<>();
                            patrolLocationInfo = infoList.get(i);
                            if(patrolLocationInfo.getStatus()==1){
                                patrolLocationMassageVo.setMassage("回到巡更区域");
                                patrolLocationMassageVo.setIsAbnormal(0);
                                patrolLocationMassageVo.setType(0);
                            }else{
                                if(patrolLocationInfo.getPatrolException().getId().equals(1)){
                                    patrolLocationMassageVo.setMassage("离开巡更区域");
                                }else if(patrolLocationInfo.getPatrolException().getId().equals(2)){
                                    patrolLocationMassageVo.setMassage("未按时到达巡更区域");
                                }else if(patrolLocationInfo.getPatrolException().getId().equals(3)){
                                    patrolLocationMassageVo.setMassage("离开巡更区域超时");
                                }else if(patrolLocationInfo.getPatrolException().getId().equals(4)){
                                    patrolLocationMassageVo.setMassage("人员位置丢失");
                                }else if(patrolLocationInfo.getPatrolException().getId().equals(5)){
                                    patrolLocationMassageVo.setMassage("人员位置无变化");
                                }
                                patrolLocationMassageVo.setIsAbnormal(1);
                                patrolLocationMassageVo.setType(patrolLocationInfo.getPatrolException().getId());
                            }
                            patrolLocationMassageVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                            PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                            String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                            locationInfoVo.setGeom(geom);
                            locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                            locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                            locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                            patrolLocationInfoVo.add(locationInfoVo);
                        }
                        if(i==infoList.size()-2){
                            patrolLocationMassageVo.setPatrolLocationInfoVos(patrolLocationInfoVo);
                            patrolLocationMassageVos.add(patrolLocationMassageVo);
                        }
                    }
                }
            }
            json.append("{\"status\":true,")
                    .append("\"code\":1,")
                    .append("\"username\":\""+patrolUserRegion.getUsername()+"\",")
                    .append("\"starttime\":\""+sp.format(patrolUserRegion.getStartTime())+"\",")
                    .append("\"endtime\":\""+sp.format(patrolUserRegion.getEndTime())+"\",")
                    .append("\"patrolRegion\":"+ JSONObject.toJSONString(patrolRegion,features)+",")
                    .append("\"data\":")
                    .append(JSONObject.toJSONString(patrolLocationMassageVos,features))
                    .append("}");
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            json = new StringBuffer();
            json.append("{\"status\":false,");
            json.append("\"code\":-2,");
            json.append("\"errorMsg\":\"接口错误\"}");
            out.print(json);
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 获取最近一条轨迹线
     * @param jobNum
     * @param response
     */
    @RequestMapping("getPatrolTrailByTime")
    public void getPatrolTrailByTime(String jobNum,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();
        StringBuffer child = new StringBuffer();
        StringBuffer data = new StringBuffer();

        try {
            out = response.getWriter();
            String sql = "from PatrolUserRegion where jobNum = '" + jobNum + "' order by startTime desc";
            List<PatrolUserRegion> patrolList = patrolUserRegionService.getByHQL(sql);
            SimpleDateFormat sp = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            SimpleDateFormat sp1 = new SimpleDateFormat("HH:mm");
            if(patrolList.size()>0){
                PatrolUserRegion patrolUserRegion = patrolList.get(0);
                PatrolRegionHistory patrolRegion = patrolRegionHistoryService.getById(patrolUserRegion.getHistoryRegionId());
                String hql = "from PatrolLocationInfo where usregId = " + patrolUserRegion.getId() + " order by timestamp";
                List<PatrolLocationInfo> infoList = patrolLocationInfoService.getByHql(hql);

                List<PatrolLocationMassageVo> patrolLocationMassageVos = new ArrayList<>();
                PatrolLocationMassageVo patrolLocationMassageVo = null ;
                List<PatrolLocationInfoVo> patrolLocationInfoVo = null;
                PatrolLocationInfo patrolLocationInfo = new PatrolLocationInfo();

                Date location_time = null;
                GeoTrackFilter filter = new GeoTrackFilter(1.0D);
                for (int i=0;i<infoList.size();i++){
                    PatrolLocationInfo uli = infoList.get(i);
                    Double seconds_per_timestep = 0D;
                    if(i != 0){
                        seconds_per_timestep = Double.valueOf(String.valueOf(
                                (uli.getTimestamp().getTime() -
                                        location_time.getTime())
                                        / 1000));
                    }
                    location_time = uli.getTimestamp();
                    //卡尔曼滤波
                    filter.update_velocity2d(uli.getLat(), uli.getLon(), seconds_per_timestep);
                    double[] newLatLon = filter.get_lat_long();
                    uli.setLat(newLatLon[0]);
                    uli.setLon(newLatLon[1]);

                    /**
                     * 第一个点和最后一个单独抽离
                     */
                    if(i==0||i==(infoList.size()-1)){
                        patrolLocationMassageVo = new PatrolLocationMassageVo();
                        patrolLocationInfoVo = new ArrayList<>();
                        patrolLocationInfo = infoList.get(i);
                        if(i==0){
                            patrolLocationMassageVo.setMassage("开始巡更");
                        }else{
                            patrolLocationMassageVo.setMassage("结束巡更");
                        }
                        if(patrolLocationInfo.getStatus()==1){
                            patrolLocationMassageVo.setIsAbnormal(0);
                            patrolLocationMassageVo.setType(0);
                        }else{
                            patrolLocationMassageVo.setIsAbnormal(1);
                            patrolLocationMassageVo.setType(patrolLocationInfo.getPatrolException().getId());
                        }
                        patrolLocationMassageVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                        PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                        String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                        locationInfoVo.setGeom(geom);
                        locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                        locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                        locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                        patrolLocationInfoVo.add(locationInfoVo);
                        patrolLocationMassageVo.setPatrolLocationInfoVos(patrolLocationInfoVo);
                        patrolLocationMassageVos.add(patrolLocationMassageVo);
                        patrolLocationMassageVo = null;
                    }else{

                        if(patrolLocationMassageVo==null){//第二个点处理
                            patrolLocationMassageVo = new PatrolLocationMassageVo();
                            patrolLocationInfoVo = new ArrayList<>();
                            patrolLocationInfo = infoList.get(i);
                            if(patrolLocationInfo.getStatus()==1){
                                patrolLocationMassageVo.setIsAbnormal(0);
                                patrolLocationMassageVo.setType(0);
                            }else{
                                if(patrolLocationInfo.getPatrolException().getId().equals(1)){
                                    patrolLocationMassageVo.setMassage("离开巡更区域");
                                }else if(patrolLocationInfo.getPatrolException().getId().equals(2)){
                                    patrolLocationMassageVo.setMassage("未按时到达巡更区域");
                                }else if(patrolLocationInfo.getPatrolException().getId().equals(3)){
                                    patrolLocationMassageVo.setMassage("离开巡更区域超时");
                                }else if(patrolLocationInfo.getPatrolException().getId().equals(4)){
                                    patrolLocationMassageVo.setMassage("人员位置丢失");
                                }else if(patrolLocationInfo.getPatrolException().getId().equals(5)){
                                    patrolLocationMassageVo.setMassage("人员位置无变化");
                                }
                                patrolLocationMassageVo.setIsAbnormal(1);
                                patrolLocationMassageVo.setType(patrolLocationInfo.getPatrolException().getId());
                            }
                            patrolLocationMassageVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                            PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                            String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                            locationInfoVo.setGeom(geom);
                            locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                            locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                            locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                            patrolLocationInfoVo.add(locationInfoVo);
                        }else{
                            if(patrolLocationInfo.getStatus().equals(infoList.get(i).getStatus())){//点状态未变的情况
                                if(infoList.get(i).getStatus().equals(2)){//为异常时的处理
                                    if(!patrolLocationInfo.getPatrolException().getId().equals(infoList.get(i).getPatrolException().getId())){//为异常时异常状态改变
                                        patrolLocationMassageVo.setPatrolLocationInfoVos(patrolLocationInfoVo);
                                        patrolLocationMassageVos.add(patrolLocationMassageVo);

                                        patrolLocationMassageVo = new PatrolLocationMassageVo();
                                        patrolLocationInfoVo = new ArrayList<>();
                                        patrolLocationInfo = infoList.get(i);
                                        if(patrolLocationInfo.getStatus()==1){
                                            patrolLocationMassageVo.setMassage("回到巡更区域");
                                            patrolLocationMassageVo.setIsAbnormal(0);
                                            patrolLocationMassageVo.setType(0);
                                        }else{
                                            if(patrolLocationInfo.getPatrolException().getId().equals(1)){
                                                patrolLocationMassageVo.setMassage("离开巡更区域");
                                            }else if(patrolLocationInfo.getPatrolException().getId().equals(2)){
                                                patrolLocationMassageVo.setMassage("未按时到达巡更区域");
                                            }else if(patrolLocationInfo.getPatrolException().getId().equals(3)){
                                                patrolLocationMassageVo.setMassage("离开巡更区域超时");
                                            }else if(patrolLocationInfo.getPatrolException().getId().equals(4)){
                                                patrolLocationMassageVo.setMassage("人员位置丢失");
                                            }else if(patrolLocationInfo.getPatrolException().getId().equals(5)){
                                                patrolLocationMassageVo.setMassage("人员位置无变化");
                                            }
                                            patrolLocationMassageVo.setIsAbnormal(1);
                                            patrolLocationMassageVo.setType(patrolLocationInfo.getPatrolException().getId());
                                        }
                                        patrolLocationMassageVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                        PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                                        String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                                        locationInfoVo.setGeom(geom);
                                        locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                                        locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                        locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                                        patrolLocationInfoVo.add(locationInfoVo);
                                    }else{//未改变状态patrolLocationInfoVo追加点
                                        PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                                        String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                                        locationInfoVo.setGeom(geom);
                                        locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                                        locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                        locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                                        patrolLocationInfoVo.add(locationInfoVo);
                                    }
                                }else{//未改变状态patrolLocationInfoVo追加点
                                    PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                                    String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                                    locationInfoVo.setGeom(geom);
                                    locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                                    locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                    locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                                    patrolLocationInfoVo.add(locationInfoVo);
                                }
                            }else{//改变状态的处理
                                patrolLocationMassageVo.setPatrolLocationInfoVos(patrolLocationInfoVo);
                                patrolLocationMassageVos.add(patrolLocationMassageVo);

                                patrolLocationMassageVo = new PatrolLocationMassageVo();
                                patrolLocationInfoVo = new ArrayList<>();
                                patrolLocationInfo = infoList.get(i);
                                if(patrolLocationInfo.getStatus()==1){
                                    patrolLocationMassageVo.setMassage("回到巡更区域");
                                    patrolLocationMassageVo.setIsAbnormal(0);
                                    patrolLocationMassageVo.setType(0);
                                }else{
                                    if(patrolLocationInfo.getPatrolException().getId().equals(1)){
                                        patrolLocationMassageVo.setMassage("离开巡更区域");
                                    }else if(patrolLocationInfo.getPatrolException().getId().equals(2)){
                                        patrolLocationMassageVo.setMassage("未按时到达巡更区域");
                                    }else if(patrolLocationInfo.getPatrolException().getId().equals(3)){
                                        patrolLocationMassageVo.setMassage("离开巡更区域超时");
                                    }else if(patrolLocationInfo.getPatrolException().getId().equals(4)){
                                        patrolLocationMassageVo.setMassage("人员位置丢失");
                                    }else if(patrolLocationInfo.getPatrolException().getId().equals(5)){
                                        patrolLocationMassageVo.setMassage("人员位置无变化");
                                    }
                                    patrolLocationMassageVo.setIsAbnormal(1);
                                    patrolLocationMassageVo.setType(patrolLocationInfo.getPatrolException().getId());
                                }
                                patrolLocationMassageVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                PatrolLocationInfoVo locationInfoVo = new PatrolLocationInfoVo();
                                String geom = "["+patrolLocationInfo.getLon()+","+patrolLocationInfo.getLat()+"]";
                                locationInfoVo.setGeom(geom);
                                locationInfoVo.setStatus(patrolLocationInfo.getStatus().toString());
                                locationInfoVo.setTime(sp1.format(patrolLocationInfo.getTimestamp()));
                                locationInfoVo.setIsArrive(patrolLocationInfo.getIsArrive().toString());
                                patrolLocationInfoVo.add(locationInfoVo);
                            }
                            if(i==infoList.size()-2){
                                //倒数第二个点的处理
                                patrolLocationMassageVo.setPatrolLocationInfoVos(patrolLocationInfoVo);
                                patrolLocationMassageVos.add(patrolLocationMassageVo);
                            }
                        }
                    }
//                    data.append("{")
//                            .append("\"geom\":\"" + ("["+uli.getLon()+","+uli.getLat()+"]") + "\",")
//                            .append("\"time\":\"" + sp1.format(infoList.get(i).getTimestamp()) + "\",")
//                            .append("\"status\":\"" + infoList.get(i).getStatus() +"\",")
//                            .append("\"isArrive\":\"" + (infoList.get(i).getIsArrive()!=null&&infoList.get(i).getIsArrive()==1?1:0) +"\",")
//                            .append("\"first\":\"" + (i==0?1:0) +"\",")
//                            .append("\"last\":\"" + (i==infoList.size()-1?1:0) +"\"")
//                            .append("},");
                }
                json.append("{\"status\":true,")
                        .append("\"code\":1,")
                        .append("\"id\":"+patrolUserRegion.getId()+",")
                        .append("\"username\":\""+patrolUserRegion.getUsername()+"\",")
                        .append("\"starttime\":\""+sp.format(patrolUserRegion.getStartTime())+"\",")
                        .append("\"endtime\":\""+sp.format(patrolUserRegion.getEndTime())+"\",")
                        .append("\"patrolRegion\":"+ JSONObject.toJSONString(patrolRegion,features)+",")
                        .append("\"data\":")
                        .append(JSONObject.toJSONString(patrolLocationMassageVos,features))
                        .append("}");
            }else{
                json.append("{\"status\":true,")
                        .append("\"code\":1,")
                        .append("\"username\":\"\",")
                        .append("\"regionName\":\"\",")
                        .append("\"starttime\":\"\",")
                        .append("\"endtime\":\"\",")
                        .append("\"data\":[")
                        .append("]")
                        .append("}");
            }
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            json = new StringBuffer();
            json.append("{\"status\":false,");
            json.append("\"code\":-2,");
            json.append("\"errorMsg\":\"接口错误\"}");
            out.print(json);
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 更新巡更定位点
     * @param response
     */
    @RequestMapping("mergePatrolLocation")
    public void mergePatrolLocation(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();

        try {
            out = response.getWriter();
            String sql = "from PatrolUserRegion order by startTime desc";
            List<PatrolUserRegion> patrolList = patrolUserRegionService.getByHQL(sql);
            SimpleDateFormat sp = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            SimpleDateFormat sp1 = new SimpleDateFormat("HH:mm");
            if(patrolList.size()>0){
                for (PatrolUserRegion patrolUserRegion:patrolList){
                    String hql = "from PatrolLocationInfo where usregId = " + patrolUserRegion.getId() + " and isArrive is null order by timestamp";
                    List<PatrolLocationInfo> infoList = patrolLocationInfoService.getByHql(hql);
                    for (int i=0;i<infoList.size();i++){
                        boolean result = patrolConfigService.isLeaveDistance(infoList.get(i).getLon(),infoList.get(i).getLat(),1,patrolUserRegion.getRegionId());
                        if(!result){
                            infoList.get(i).setIsArrive(1);
                        }else{
                            infoList.get(i).setIsArrive(0);
                        }
                        patrolLocationInfoService.merge(infoList.get(i));
                    }
                }
                json.append("{\"status\":true,")
                        .append("\"code\":1,")
                        .append("\"errorMsg\":\"数据初始成功\"}");
            }else{
                json.append("{\"status\":true,")
                        .append("\"code\":1,")
                        .append("\"errorMsg\":\"数据初始成功\"}");
            }
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            json = new StringBuffer();
            json.append("{\"status\":false,");
            json.append("\"code\":-2,");
            json.append("\"errorMsg\":\"接口错误\"}");
            out.print(json);
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 同步历史巡更区域和巡更记录
     * @param response
     */
    @RequestMapping("mergeHistoryRegionAndUser")
    public void mergeHistoryRegionAndUser(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();

        try {
            out = response.getWriter();
            String hql = "from PatrolRegion order by createtime desc";
            List<PatrolRegion> patrolList = patrolRegionService.getByHQL(hql);
           if(patrolList.size()>0){
               for (PatrolRegion patrolRegion:patrolList){
                   Boolean result = patrolRegionHistoryService.exsit("regionId",patrolRegion.getId());
                   if(!result){
                       PatrolRegionHistory patrolRegionHistory = new PatrolRegionHistory();
                       patrolRegionHistory.setCreatetime(patrolRegion.getCreatetime());
                       patrolRegionHistory.setIsDel(patrolRegion.getIsDel());
                       patrolRegionHistory.setLastUpdateTime(patrolRegion.getLastUpdateTime());
                       patrolRegionHistory.setRegionName(patrolRegion.getRegionName());
                       patrolRegionHistory.setCampusNum(patrolRegion.getCampusNum());
                       patrolRegionHistory.setColor(patrolRegion.getColor());
                       patrolRegionHistory.setRegionLocation(patrolRegion.getRegionLocation());
                       patrolRegionHistory.setRegionId(patrolRegion.getId());
                       patrolRegionHistoryService.addRecord(patrolRegionHistory);
                   }
               }
           }

            String sql = "from PatrolUserRegion order by startTime desc";
            List<PatrolUserRegion> patrolUserRegions = patrolUserRegionService.getByHQL(sql);
            for(PatrolUserRegion patrolUserRegion:patrolUserRegions){
                if(patrolUserRegion.getHistoryRegionId()==null){
                    hql = "from PatrolRegionHistory where regionId = " + patrolUserRegion.getRegionId() + " order by createtime desc";
                    PatrolRegionHistory patrolRegionHistory = patrolRegionHistoryService.getByHQL(hql);
                    patrolUserRegion.setHistoryRegionId(patrolRegionHistory.getHistoryId());
                    patrolUserRegionService.merge(patrolUserRegion);
                }
            }
            json.append("{\"status\":true,")
                    .append("\"code\":1,")
                    .append("\"errorMsg\":\"数据初始成功\"}");
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            json = new StringBuffer();
            json.append("{\"status\":false,");
            json.append("\"code\":-2,");
            json.append("\"errorMsg\":\"接口错误\"}");
            out.print(json);
        } finally {
            out.flush();
            out.close();
        }
    }
}
