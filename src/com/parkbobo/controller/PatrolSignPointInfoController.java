package com.parkbobo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolSignPointInfo;
import com.parkbobo.service.PatrolBeaconInfoService;
import com.parkbobo.service.PatrolRegionService;
import com.parkbobo.service.PatrolSignPointInfoService;
import com.parkbobo.utils.message.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 巡更点位管理
 * @version 1.0
 * @author RY
 * @since 2018-7-16 10:35:49
 */

@Controller
public class PatrolSignPointInfoController {
    @Resource
    private PatrolRegionService patrolRegionService;
    @Resource
    private PatrolSignPointInfoService patrolSignPointInfoService;
    @Resource
    private PatrolBeaconInfoService patrolBeaconInfoService;

    /**
     * 进入巡更签到点位管理
     * @param regionId 区域ID
     * @return
     */
    @RequestMapping("patrolSignPortInfo_toManage")
    public ModelAndView toPatrolSignPortInfoManage(Integer regionId){
        ModelAndView mv = new ModelAndView();

        PatrolRegion patrolRegion = patrolRegionService.getById(regionId);

        String hql = "from PatrolSignPointInfo";
        List<PatrolSignPointInfo> pointInfoList = patrolSignPointInfoService.getByHql(hql);

        mv.addObject("patrolRegion", JSON.toJSONString(patrolRegion));
        mv.addObject("pointInfoList", JSON.toJSONString(pointInfoList, SerializerFeature.DisableCircularReferenceDetect));
        mv.setViewName("manager/system/patrolSignPortInfo/patrolSignPortInfo-manage");
        return mv;
    }

    /**
     * 保存签到点位信息
     * @param pointId 点位ID,添加时没有
     * @param pointName 点位名称
     * @param lng 经度
     * @param lat 纬度
     * @param regionId 区域ID
     * @param beaconId 标签ID
     * @return
     */
    @ResponseBody
    @RequestMapping("patrolSignPortInfo_save")
    public String saveSignPointInfo(Integer pointId, String pointName, Double lng, Double lat, Integer regionId, Integer beaconId){
        MessageBean<PatrolSignPointInfo> messageBean = new MessageBean<PatrolSignPointInfo>();
        try {
            if(pointId != null){
                PatrolSignPointInfo pointInfo = patrolSignPointInfoService.get(pointId);
                pointInfo.setPointName(pointName);
                pointInfo.setUpdateTime(new Date());

                patrolSignPointInfoService.update(pointInfo);
                patrolBeaconInfoService.updatePointInfo(beaconId, pointId);

                messageBean.setCode(200);
                messageBean.setStatus(true);
                messageBean.setData(pointInfo);
                messageBean.setMessage("更新成功");
            } else{
                if(patrolSignPointInfoService.isInRegion(regionId, lng, lat)){
                    PatrolRegion patrolRegion = patrolRegionService.getById(regionId);
                    PatrolSignPointInfo pointInfo = new PatrolSignPointInfo();
                    pointInfo.setPointName(pointName);
                    pointInfo.setUpdateTime(new Date());
                    pointInfo.setLat(lat);
                    pointInfo.setLng(lng);
                    pointInfo.setPatrolRegion(patrolRegion);

                    PatrolSignPointInfo newSignPointInfo = patrolSignPointInfoService.add(pointInfo);
                    patrolBeaconInfoService.updatePointInfo(beaconId, newSignPointInfo.getPointId());

                    messageBean.setCode(200);
                    messageBean.setStatus(true);
                    messageBean.setData(newSignPointInfo);
                    messageBean.setMessage("添加成功");
                } else{
                    messageBean.setCode(-1);
                    messageBean.setStatus(false);
                    messageBean.setMessage("签到点位不在该巡更区域，请重新设置点位位置");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setCode(-1);
            messageBean.setStatus(false);
            messageBean.setMessage("接口错误");
        }
        return JSON.toJSONString(messageBean);
    }

    @ResponseBody
    @RequestMapping("patrolSignPortInfo_delete")
    public String deleteSignPointInfo(Integer pointId){
        MessageBean<PatrolSignPointInfo> messageBean = new MessageBean<PatrolSignPointInfo>();
        try {
            patrolSignPointInfoService.delete(pointId);
            messageBean.setCode(200);
            messageBean.setStatus(true);
            messageBean.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setCode(-1);
            messageBean.setStatus(false);
            messageBean.setMessage("接口错误");
        }
        return JSON.toJSONString(messageBean);
    }
}
