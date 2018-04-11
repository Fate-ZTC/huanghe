package com.parkbobo.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.parkbobo.VO.PatrolStatisticsAreaVO;
import com.parkbobo.VO.PatrolStatisticsExceptionCountVO;
import com.parkbobo.model.PatrolRegion;
import com.parkbobo.model.PatrolUser;
import com.parkbobo.service.PatrolUserStatisticsService;
import com.parkbobo.utils.PageAble;
import com.parkbobo.utils.message.DateUtils;
import com.parkbobo.utils.message.MessageBean;
import com.parkbobo.utils.message.MessageListBean;

/**
 * Created by lijunhong on 18/3/31.
 * 安防巡更统计相关内容
 */
@Controller
@RequestMapping("/statistics")
public class PatrolUserStatisticsController {



    private final Integer PAGE_SIZE = 20;

    @Resource
    private PatrolUserStatisticsService statisticsService;

    /**
     * 查询巡查人员
     * @param campusNum     校区id
     * @param response
     */
    @RequestMapping(value = "/user")
    public void userStatistics(int campusNum, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            MessageListBean<PatrolUser> messageListBean = new MessageListBean<>();
            if (campusNum <= 0) {
                messageListBean.setCode(200);
                messageListBean.setMessage("设置校区id不能为空");
                messageListBean.setStatus(true);
            }
            String sql = "SELECT username,job_num FROM patrol_user WHERE campus_num=" + campusNum + " AND is_del=0 GROUP BY username,job_num";
            List<PatrolUser> patrolUsers = new ArrayList<>();
            List<Object[]> objects = statisticsService.getPatrolUserRegionBySql(sql);
            if(objects != null && objects.size() > 0)
            for(Object[] o : objects) {
                PatrolUser patrolUser = new PatrolUser();
                patrolUser.setUsername(o[0].toString());
                patrolUser.setJobNum(o[1].toString());
                patrolUsers.add(patrolUser);
            }
            messageListBean.setCode(200);
            messageListBean.setStatus(true);
            messageListBean.setMessage("success");
            messageListBean.setData(patrolUsers);
            String json = JSON.toJSONString(messageListBean);
            out.write(json);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            out.flush();
            out.close();
        }
    }


    /**
     * 查询巡查区域
     * @param campusNum     校区id
     * @param response
     */
    @RequestMapping("/regionStatistics")
    public void regionStatistics(int campusNum,HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            MessageBean messageBean = new MessageBean();
            if(campusNum <= 0) {
                messageBean.setCode(200);
                messageBean.setMessage("必须上传校区id信息");
                messageBean.setStatus(false);
                String json = JSON.toJSONString(messageBean);
                out.write(json);
            }

            String sql = "SELECT region_name,id FROM patrol_region WHERE campus_num=" + campusNum;
            List<Object[]> regions = statisticsService.getPatrolUserRegionBySql(sql);
            List<PatrolRegion> patrolRegions = null;
            if(regions != null && regions.size() > 0) {
                patrolRegions = new ArrayList<>();
                for(Object[] object : regions) {
                    PatrolRegion patrolRegion = new PatrolRegion();
                    patrolRegion.setRegionName(object[0].toString());
                    patrolRegion.setId(Integer.parseInt(object[1].toString()));
                    patrolRegions.add(patrolRegion);
                }
                if(patrolRegions.size() > 0) {
                    messageBean.setData(patrolRegions);
                }
            }
            messageBean.setCode(200);
            messageBean.setMessage("success");
            messageBean.setStatus(true);
            out.write(JSON.toJSONString(messageBean));
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            out.flush();
            out.close();
        }
    }


    /**
     * 这里跳转到统计查询默认页面进行安防统计查询
     * @param regionId      区域id
     * @return
     */
    @RequestMapping("/toPatrolArea")
    public ModelAndView toPatrolArea(int regionId,int page,int pageSize) {
        ModelAndView mv = new ModelAndView();
        //这里进行数据判断
        if(regionId <= 0) {
            //TODO 这里数据不能为null
            return null;
        }
        page = (page <= 0 ? 1 : page);
        pageSize = (pageSize <= 0 ? PAGE_SIZE : pageSize);
        PageAble<PatrolStatisticsAreaVO> pageAble = new PageAble<>(page,pageSize);
        pageAble = statisticsService.statisticsPatrolArea(0,1,null,null,regionId,pageAble);
        try {
            PatrolStatisticsExceptionCountVO countVO = statisticsService.quertPatrolAreaCount(1,null,null,regionId);
            mv.addObject("countVO",countVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("data",JSON.toJSONString(pageAble));
        mv.addObject("regionId",regionId);
        mv.addObject("page",1);
        mv.addObject("type",0);
        mv.addObject("dateType",1);
        mv.setViewName("patrol_app_page/patrol-area-all");
        return mv;
    }



    /**
     *
     * 安防巡查巡查统计中根据区域进行统计获取统计数据
     * @param type          状态类型:0全部,1异常,2正常
     * @param dateType      时间类型:0,30天,1,7天,2今天,3位自定义时间
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param regionId      区域id
     * @param page          当前页
     * @param pageSize      每页显示的条数
     */
    @RequestMapping("/getPatrolAreaData")
    public void getPatrolAreaData(Integer type,Integer dateType,String startTime,String endTime, Integer regionId,Integer page,
                                  Integer pageSize, HttpServletResponse response) {

        PrintWriter out = null;
        MessageBean<PageAble> messageBean = new MessageBean<>();
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();

            //下面对时间进行转化
            Date startDate = null;
            Date endDate = null;
            if(startTime != null && endTime != null && !"".equals(startTime) && !"".equals(endTime)) {
                //下面进行时间转化
                startDate = DateUtils.stringFormatDate(startTime);
                endDate = DateUtils.stringFormatDate(endTime);
            }

            messageBean.setCode(200);
            if(regionId <= 0) {
                messageBean.setStatus(false);
                messageBean.setMessage("fail");
                messageBean.setMessage("缺少参数");
                String json = JSON.toJSONString(messageBean);
                out.write(json);
                return;
            }
            page = (page <= 0 ? 1 : page);
            pageSize = (pageSize <= 0 ? PAGE_SIZE : pageSize);
            PageAble<PatrolStatisticsAreaVO> pageAble = new PageAble<>(page,pageSize);
            pageAble = statisticsService.statisticsPatrolArea(type,dateType,startDate,endDate,regionId,pageAble);
            PatrolStatisticsExceptionCountVO countVO = statisticsService.quertPatrolAreaCount(dateType,startDate,endDate,regionId);

            messageBean.addPropertie("countVO",countVO);
            messageBean.addPropertie("page",page);
            messageBean.setMessage("success");
            messageBean.setStatus(true);
            messageBean.setData(pageAble);
            String json = JSON.toJSONString(messageBean);
            out.write(json);
        }catch (Exception e) {
            e.printStackTrace();
            messageBean.setMessage("exception" + e);
            messageBean.setStatus(false);
            String json = JSON.toJSONString(messageBean);
            out.write(json);
        }finally {
            out.flush();
            out.close();
        }
    }




    /**
     * 根据人员id统计该人巡查信息
     * @param jobNum        工号
     * @param page          当前页数
     * @param pageSize      每页条数
     * @return
     */
    @RequestMapping("/toPatrolPerson")
    public ModelAndView toPatrolPerson(String jobNum,int page,int pageSize) {
        ModelAndView mv = new ModelAndView();
            //这里进行数据判断
        MessageBean<PageAble> messageBean = new MessageBean<>();
        messageBean.setCode(200);
        if (jobNum != null && "".equals(jobNum)) {
            messageBean.setMessage("fail");
            messageBean.setStatus(false);
            messageBean.setMessage("请求参数不能为空");
        }
        page = (page <= 0 ? 1 : page);
        pageSize = (pageSize <= 0 ? PAGE_SIZE : pageSize);
        PageAble<PatrolStatisticsAreaVO> pageAble = new PageAble<>(page, pageSize);
        //这里进行查询数据
        pageAble = statisticsService.statisticsPatrolPerson(0, 1, null, null, jobNum, pageAble);
        //这里进行查询数据条数
        PatrolStatisticsExceptionCountVO countVO = null;
        try {
            countVO = statisticsService.queryPatrolPersonCount(1,null,null,jobNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("countVO",countVO);
        mv.addObject("page",page);
        mv.addObject("jobNum",jobNum);
        mv.addObject("data",JSON.toJSONString(pageAble));
        mv.setViewName("patrol_app_page/patrol-person-all");
        return mv;
    }


    /**
     * 安防巡查根据人员工号进行获取指定条件的数据
     * @param type          状态类型:0全部,1异常,2正常
     * @param dateType      时间类型:0,30天,1,7天,2今天,3位自定义时间
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param jobNum        工号
     * @param page          当前页
     * @param pageSize      每页大小
     * @param response
     */
    @RequestMapping("/getPatrolPersonData")
    public void getPatrolPersonData(Integer type,Integer dateType,String startTime,String endTime,String jobNum,Integer page,
                                    Integer pageSize, HttpServletResponse response) {

        PrintWriter out = null;
        MessageBean<PageAble> messageBean = new MessageBean<>();
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();

            //下面对时间进行转化
            Date startDate = null;
            Date endDate = null;
            if(startTime != null && endTime != null && !"".equals(startTime) && !"".equals(endTime)) {
                //下面进行时间转化
                startDate = DateUtils.stringFormatDate(startTime);
                endDate = DateUtils.stringFormatDate(endTime);
            }

            messageBean.setCode(200);
            if(jobNum != null && "".equals(jobNum)) {
                messageBean.setStatus(false);
                messageBean.setMessage("fail");
                messageBean.setMessage("缺少参数");
                String json = JSON.toJSONString(messageBean);
                out.write(json);
                return;
            }
            page = (page <= 0 ? 1 : page);
            pageSize = (pageSize <= 0 ? PAGE_SIZE : pageSize);
            PageAble<PatrolStatisticsAreaVO> pageAble = new PageAble<>(page,pageSize);
            pageAble = statisticsService.statisticsPatrolPerson(type,dateType,startDate,endDate,jobNum,pageAble);
            PatrolStatisticsExceptionCountVO countVO = statisticsService.queryPatrolPersonCount(dateType,startDate,endDate,jobNum);

            messageBean.addPropertie("countVO",countVO);
            messageBean.addPropertie("page",page);
            messageBean.addPropertie("jobNum",jobNum);
            messageBean.setMessage("success");
            messageBean.setStatus(true);
            messageBean.setData(pageAble);
            String json = JSON.toJSONString(messageBean);
            out.write(json);
        }catch (Exception e) {
            e.printStackTrace();
            messageBean.setMessage("fail");
            messageBean.setStatus(false);
            String json = JSON.toJSONString(messageBean);
            out.write(json);
        }finally {
            out.flush();
            out.close();
        }

    }

}
