package com.parkbobo.controller;

import com.parkbobo.service.FireFightEquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *   手动触发
 * @ClassName triggerController
 * @Description: TODO
 * @Author Administrator
 * @Date 2019/8/23 9:37
 * @Version V1.0
 **/
@Controller
@RequestMapping("/triggerController")
public class TriggerController {
    @Resource
    private FireFightEquipmentService fireFightEquipmentService;


    @RequestMapping(value = "/copeToHistoryTask", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> copeToHistoryTask(){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            //重置所有消防设备的巡查状态
//            执行复制
            fireFightEquipmentService.copeToHistoryTask();
        } catch (Exception e) {
            e.printStackTrace();
            result.put("notice","失败");
            result.put("state","401");
            return result;
        }
        result.put("notice","成功");
        result.put("state","200");
        return result;
    }

    @RequestMapping(value = "/restCheckStatusTask", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> restCheckStatusTask(){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            //重置所有消防设备的巡查状态
//            执行重置
            fireFightEquipmentService.restCheckStatusTask();
        } catch (Exception e) {
            e.printStackTrace();
            result.put("notice","失败");
            result.put("state","401");
            return result;
        }
        result.put("notice","成功");
        result.put("state","200");
        return result;
    }
}
