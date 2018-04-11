package com.parkbobo.controller;

import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.parkbobo.VO.ModifyFireEquipmentVO;
import com.parkbobo.dao.FireFightEquipmentDao;
import com.parkbobo.service.FirePatrolEquipmentSychService;
import com.parkbobo.utils.message.MessageBean;

/**
 * Created by lijunhong on 18/4/8.
 */
@Controller
@RequestMapping("/firePatrolEquipmentSynch")
public class FirePatrolEquipmentSynchController {


    @Resource
    private FirePatrolEquipmentSychService firePatrolEquipmentSychService;


    @Resource(name="fireFightEquipmentDaoImpl")
    private FireFightEquipmentDao fireFightEquipmentDao;


    /**
     * 进行同步消防设备相关内容(批量添加功能)
     */
    @RequestMapping("/sychequipment")
    public void sychEquipment(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;

        MessageBean messageBean = new MessageBean();
        messageBean.setCode(200);
        try {
            out = response.getWriter();
            firePatrolEquipmentSychService.saveFirePatrolEquipment();
            messageBean.setMessage("success");
            messageBean.setStatus(true);
            out.write(JSON.toJSONString(messageBean));
        }catch (Exception e) {
            e.printStackTrace();
            messageBean.setMessage("同步数据出现错误,请联系技术");
            messageBean.setStatus(false);
            out.write(JSON.toJSONString(messageBean));
        }finally {
            out.flush();
            out.close();
        }
    }


    @RequestMapping("/modify")
    @ResponseBody
    public void modifyEquipment(@RequestBody ModifyFireEquipmentVO equipmentVO, HttpServletResponse response) {
        PrintWriter out = null;
        MessageBean messageBean = new MessageBean();
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            out = response.getWriter();
            //下面进行设置相关内容
            if(null == equipmentVO.getType()) {
                messageBean.setMessage("请求参数不能为空");
                messageBean.setStatus(false);
                messageBean.setCode(500);
                out.write(JSON.toJSONString(messageBean));
                return;
            }

            boolean isExist = fireFightEquipmentDao.existsByProperty("pointid",equipmentVO.getPointid());
            messageBean.setCode(200);
            //下面进行设置相关内容
            if(1 == equipmentVO.getType()) {
                //增加操作
                if(!isExist) {
                    firePatrolEquipmentSychService.addFirePatrolEquipment(equipmentVO);
                    messageBean.setMessage("添加成功");
                    messageBean.setStatus(true);
                }else {
                    messageBean.setMessage("该设备已存在");
                    messageBean.setStatus(false);
                }
                out.write(JSON.toJSONString(messageBean));
                return;
            }else if(2 == equipmentVO.getType()) {

                //删除操作
                if(isExist) {
                    //存在设备信息
                    int result = firePatrolEquipmentSychService.deleteFirePatrolEquipment(equipmentVO.getPointid());
                    if(result > 0) {
                        //删除成功
                        messageBean.setMessage("删除设备信息成功");
                        messageBean.setStatus(true);
                    }else {
                        //没有删除
                        messageBean.setMessage("没有删除设备信息");
                        messageBean.setStatus(false);
                    }
                }else {
                    //没有查询到设备信息
                    messageBean.setMessage("不存在设备信息");
                    messageBean.setStatus(false);
                }
                out.write(JSON.toJSONString(messageBean));
                return;
            }else if(3 == equipmentVO.getType()) {
                //修改操作
                if(isExist) {
                    //存在设备信息
                    boolean result = firePatrolEquipmentSychService.modifyFirePatrolEquipment(equipmentVO);
                    if(result) {
                        //修改成功
                        messageBean.setMessage("修改设备信息成功");
                        messageBean.setStatus(true);
                    }else {
                        //修改失败
                        messageBean.setMessage("修改设备信息失败");
                        messageBean.setStatus(false);
                    }
                }else {
                    //不存在设备信息
                    messageBean.setMessage("没有查询到相关设备信息");
                    messageBean.setStatus(false);
                }
                out.write(JSON.toJSONString(messageBean));
                return;
            }
        }catch (Exception e) {
            messageBean.setCode(500);
            messageBean.setStatus(false);
            messageBean.setMessage("系统错误,请联系技术人员");
            out.write(JSON.toJSONString(messageBean));
            e.printStackTrace();
        }finally {
            out.flush();
            out.close();
        }
    }
}
