package com.parkbobo.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.*;
import com.parkbobo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 巡更pc定位接口
 * @author ZQ
 * @version 1.0
 * @since 2018-1-25 14:38:18
 */
@Controller
public class PatrolPcInterfaceController {

    @Resource
    private PatrolUserService patrolUserService;

    @Resource
    private PatrolConfigService patrolConfigService;

    @Resource
    private PatrolUserRegionService patrolUserRegionService;

    @Resource
    private PatrolLocationInfoService patrolLocationInfoService;

    @Resource
    private PatrolEmergencyService patrolEmergencyService;

    @Resource
    PatrolRegionService patrolRegionService;

    @Resource
    private PatrolPauseService patrolPauseService;


    private static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect};

    /**
     * pc端查看所有用户的最新定位点
     * @param response
     * @throws IOException
     */
    @RequestMapping("pcRegionCurrentCrew")
    public void pcRegionCurrentCrew(HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String hql = "FROM PatrolUser WHERE isDel=0";
        List<PatrolUser> allUsers = patrolUserService.getAll(hql);
        List<PatrolUserRegion> patrolUsersRe = new ArrayList<PatrolUserRegion>();
        List<PatrolLocationInfo> patrolLocationInfos = new ArrayList<PatrolLocationInfo>();
        Date lastUpdateTime=null;
        String regionName=null;
        for (PatrolUser patrolUser : allUsers) {
            String jobNum = patrolUser.getJobNum();
            List<PatrolUserRegion> patrolUserRegions = patrolUserRegionService.getByProperty("jobNum", jobNum,"startTime",false);
            if (patrolUserRegions!=null && patrolUserRegions.size()>0) {
                PatrolUserRegion patrolUserRegion = patrolUserRegions.get(0);
                //这里进行判断是否存在结束时间,结束时间为null说明没有正在巡查的人员
                lastUpdateTime=patrolUserRegion.getLastUpdateTime();
                regionName=patrolRegionService.getById(patrolUserRegion.getRegionId()).getRegionName();
                if(null == patrolUserRegion.getEndTime()) {
                    patrolUsersRe.add(patrolUserRegion);
                }
            }
        }

        if (patrolUsersRe!=null && patrolUsersRe.size()>0) {
            for (PatrolUserRegion patrolUserRegion : patrolUsersRe) {
                List<PatrolLocationInfo> patrolLocationInfo = patrolLocationInfoService.getByProperty("usregId", patrolUserRegion.getId(), "timestamp", false);
                if(patrolLocationInfo!=null && patrolLocationInfo.size()>0) {
                    patrolLocationInfo.get(0).setRegionName(patrolRegionService.getById(patrolUserRegion.getRegionId()).getRegionName());
                    patrolLocationInfos.add(patrolLocationInfo.get(0));
                }
            }

            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
            String strDate=sdf.format(lastUpdateTime);
            //获取刷新时间
            PatrolConfig patrolConfig = patrolConfigService.getById(1);
            Integer refreshTime = patrolConfig.getRefreshTime();
            if(refreshTime == null) {refreshTime = 5;}//设置默认为5秒

            if(patrolLocationInfos != null && patrolLocationInfos.size()>0) {
                out.print("{\"status\":\"true\",\"Code\":1,\"refreshTime\":"+refreshTime+",\"lastUpdateTime\":"+"\""+strDate+"\""+",\"regionName\":"+"\""+regionName+"\""+",\"data\":"+ JSONObject.toJSONString(patrolLocationInfos,features)+"}");
            }else{
                out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无信息\"}");
            }
        }else{
            out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无信息\"}");
        }
        out.flush();
        out.close();
    }



    /**
     * pc端根据区域查询相关人员
     * @param regionId 区域id
     * @param response
     * @throws IOException
     */
    @RequestMapping("pcRegionCrew")
    public void pcRegionCrew(Integer regionId,HttpServletResponse response) throws IOException{
        //TODO 这里有所改动
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String hql = "FROM PatrolUserRegion WHERE regionId=" + regionId + " AND endTime is NULL";
        List<PatrolUserRegion> patrolUserRegions = patrolUserRegionService.getByHQL(hql);
        if (patrolUserRegions != null && patrolUserRegions.size() > 0) {
            out.print("{\"status\":\"true\",\"Code\":1,\"data\":"+JSONObject.toJSONString(patrolUserRegions,features)+"}");
        }else{
            out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\"暂无该区域巡查信息\"}");
        }
        out.flush();
        out.close();
    }



    /**
     * pc端发起暂停
     * @param response
     * @param cause
     * @param usercode
     * @param username
     */
    @RequestMapping("pcStartPause")
    public void pcStartPause(HttpServletResponse response, String cause, String usercode, String username) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();
        byte[] b= new byte[0];
        if(cause!=null){
            b=cause.getBytes("ISO_8859-1");
        }
        String cause1=new String(b,"UTF-8");
        b= new byte[0];
        if(usercode!=null){
            b=usercode.getBytes("ISO_8859-1");
        }
        String usercode1=new String(b,"UTF-8");
        b= new byte[0];
        if(username!=null){
            b=username.getBytes("ISO_8859-1");
        }
        String username1=new String(b,"UTF-8");

        try {
            out = response.getWriter();

            PatrolPause checkPause = patrolPauseService.checkPauseStatus();
            if(checkPause == null){
                PatrolPause patrolPause = new PatrolPause();
                patrolPause.setCause(cause1);
                //patrolPause.setCause(cause);
                patrolPause.setPauseStart(new Date());
                //patrolPause.setUsercode(usercode);
                patrolPause.setUsercode(usercode1);
                //patrolPause.setUsername(username);
                patrolPause.setUsername(username1);

                patrolPauseService.add(patrolPause);

                json.append("{\"status\":true,");
                json.append("\"code\":1,");
                json.append("\"errorMsg\":\"暂停成功\"}");
            } else{
                json.append("{\"status\":false,");
                json.append("\"code\":-2,");
                json.append("\"errorMsg\":\"当前正在暂停\"}");
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
     * pc端结束暂停
     * @param response
     */
    @RequestMapping("pcStopPause")
    public void pcStopPause(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();

        try {
            out = response.getWriter();
            PatrolPause patrolPause = patrolPauseService.checkPauseStatus();
            if(patrolPause != null){
                patrolPause.setPauseEnd(new Date());
                patrolPauseService.update(patrolPause);

                json.append("{\"status\":true,");
                json.append("\"code\":1,");
                json.append("\"errorMsg\":\"结束成功\"}");
            } else{
                json.append("{\"status\":false,");
                json.append("\"code\":-11,");
                json.append("\"errorMsg\":\"当前没有暂停\"}");
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
     * pc端获取暂停状态
     * @param response
     */
    @RequestMapping("pcGetPauseStatus")
    public void pcGetPauseStatus(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        StringBuffer json = new StringBuffer();

        try {
            out = response.getWriter();
            PatrolPause patrolPause = patrolPauseService.checkPauseStatus();
            if(patrolPause != null){

                json.append("{\"status\":true,");
                json.append("\"code\":1,");
                json.append("\"errorMsg\":\"获取成功\",");
                json.append("\"pauseTime\":" + (System.currentTimeMillis() - patrolPause.getPauseStart().getTime()) +"}");
            } else{
                json.append("{\"status\":false,");
                json.append("\"code\":-11,");
                json.append("\"errorMsg\":\"当前没有暂停\",");
                json.append("\"pauseTime\":0}");
            }
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            json = new StringBuffer();
            json.append("{\"status\":false,");
            json.append("\"code\":-2,");
            json.append("\"errorMsg\":\"接口错误\",");
            json.append("\"pauseTime\":0}");
            out.print(json);
        } finally {
            out.flush();
            out.close();
        }
    }


}
