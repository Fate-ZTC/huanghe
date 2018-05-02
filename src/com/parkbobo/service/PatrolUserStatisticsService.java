package com.parkbobo.service;

import java.util.*;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.VO.PatrolExceptionVO;
import com.parkbobo.VO.PatrolStatisticsAreaVO;
import com.parkbobo.VO.PatrolStatisticsExceptionCountVO;
import com.parkbobo.dao.PatrolUserRegionDao;
import com.parkbobo.model.PatrolUserRegion;
import com.parkbobo.utils.PageAble;
import com.parkbobo.utils.message.DateUtils;

import static com.parkbobo.utils.message.DateUtils.getStartEndTimeByDayNum;

/**
 * Created by lijunhong on 18/3/31.
 */
@Service
public class PatrolUserStatisticsService {


    @Resource
    private PatrolUserRegionDao patrolUserRegionDao;

    public List<PatrolUserRegion> statisticsPatrolUserRegion(String hql) {
        return patrolUserRegionDao.getByHQL(hql);
    }


    public List<Object[]> getPatrolUserRegionBySql(String sql) {
        return patrolUserRegionDao.getBySql(sql);
    }

    public List<Object[]> getRegionIdByCampusNum(String sql) {
        return patrolUserRegionDao.getBySql(sql);
    }




    /**
     * 这里进行安防巡查数据查询,更具区域进行巡查
     * @param type          状态类型:0全部,1异常,2正常
     * @param dateType      时间类型:0,30天,1,7天,2今天,3位自定义时间
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param regionId      区域id
     * @return 返回查询分页结果
     */
    public PageAble<PatrolStatisticsAreaVO> statisticsPatrolArea(Integer type, Integer dateType, Date startTime, Date endTime,
                                                                 Integer regionId, PageAble<PatrolStatisticsAreaVO> pageAble) {
        //查询人员数量
        String sql = "SELECT id,username,job_num,region_id,start_time,end_time,status,date,campus_num"
                +" FROM view_statistics_patrol_area WHERE region_id="
                + regionId;

        String countSql = "SELECT count(*) AS total_count FROM view_statistics_patrol_area WHERE region_id=" + regionId;

        //根据异常状态进行查看
        if(type == 1) {
            //异常
            sql += " AND status=2";
            countSql += " AND status=2";
        }
        if(type == 2) {
            //正常
            sql += " AND status=1";
            countSql += " AND status=1";
        }
        //根据时间组织sql
        Map<String,Date> map = null;
        if(dateType == 0) {
            //30天
            map = getDateByNow(0,null,null);
        }
        if(dateType == 1) {
            //7天
            map = getDateByNow(1,null,null);
        }
        if(dateType == 2) {
            //今天
            map = getDateByNow(2,null,null);
        }
        if(dateType == 3) {
            //自定义事件
            map = getDateByNow(3,startTime,endTime);
        }
        if(map != null) {
            String startStr = DateUtils.dateFormatStr(map.get(DateUtils.START_DATE),DateUtils.FORMAT_DATE);
            String endStr = DateUtils.dateFormatStr(map.get(DateUtils.END_DATE),DateUtils.FORMAT_DATE);
            sql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
            countSql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
        }
        sql += " ORDER BY date DESC LIMIT " + pageAble.getPageSize() + " OFFSET " + pageAble.getStart();

        List<Object[]> countList = this.patrolUserRegionDao.getBySql(countSql);
        //统计总条数
        int count = 0;
        if(countList != null && countList.size() > 0) {
            Object o = countList.get(0);
            count = Integer.parseInt(o.toString());
        }

        List<Object[]> objects = this.patrolUserRegionDao.getBySql(sql);
        //这里进行分页查询相关内容
        List<PatrolStatisticsAreaVO> statisticsAreaVOs = null;
        if(objects != null && objects.size() > 0) {
            statisticsAreaVOs = new ArrayList<>();
            for (Object[] obj : objects) {
                //这里进行封装数据
                PatrolStatisticsAreaVO vo = new PatrolStatisticsAreaVO();
                //id
                if(obj[0] != null) {
                    vo.setId(Integer.parseInt(obj[0].toString()));
                }
                //username
                if(obj[1] != null) {
                    vo.setUsername(obj[1].toString());
                }
                //job_num
                if(obj[2] != null) {
                    vo.setJobNum(obj[2].toString());
                }
                //region_id
                if(obj[3] != null) {
                    vo.setRegionId(Integer.parseInt(obj[3].toString()));
                }
                //start_time
                if(obj[4] != null) {
                    vo.setStartTime(obj[4].toString());
                }
                //end_time
                if(obj[5] != null) {
                    vo.setEndTime(obj[5].toString());
                }
                //status
                if(obj[6] != null) {
                    vo.setStatus(Integer.parseInt(obj[6].toString()));
                }
                //date
                if(obj[7] != null) {
                    vo.setDate(obj[7].toString());
                }
                //campus_num
                if(obj[8] != null) {
                    vo.setCampusNum(Integer.parseInt(obj[8].toString()));
                }
                statisticsAreaVOs.add(vo);
            }
        }

        //下面进行一场状态查询
        if(statisticsAreaVOs != null && statisticsAreaVOs.size() > 0) {
            for (PatrolStatisticsAreaVO vo : statisticsAreaVOs) {
                if(vo.getStatus() == 2) {
                    //有异常信息
                    String exceptionSql = "SELECT exception_name,createtime,"+
                            "to_char(createtime,'YYYY-MM-DD HH24:MI:SS') AS strCreatetime "+
                            "FROM patrol_exception_info WHERE usregid=" + vo.getId() + " ORDER BY createtime DESC";
                    List<Object[]> exceptions = this.patrolUserRegionDao.getBySql(exceptionSql);
                    List<PatrolExceptionVO> exceptionVO = null;
                    if(exceptions != null && exceptions.size() > 0) {
                        //进行异常信息封装
                        exceptionVO = new ArrayList<>();
                        for(Object[] obj : exceptions) {
                            PatrolExceptionVO patrolExceptionVO = new PatrolExceptionVO();
                            patrolExceptionVO.setException_name(obj[0].toString());
                            patrolExceptionVO.setCreatetime(obj[2].toString());
                            exceptionVO.add(patrolExceptionVO);
                        }
                    }
                    //将异常信息添加
                    vo.setExceptionVOs(exceptionVO);
                }
            }
        }
        pageAble.setList(statisticsAreaVOs);
        pageAble.setTotalCount(count);
        return pageAble;
    }

    /**
     * 根据条件进行查询全部、正常、异常相关的记录条数
     * @param dateType      时间类型:0,30天,1,7天,2今天,3位自定义时间
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param regionId      区域id
     * @return
     * @throws Exception
     */
    public PatrolStatisticsExceptionCountVO quertPatrolAreaCount(Integer dateType, Date startTime, Date endTime, Integer regionId) throws Exception{


        String allCountSql = "SELECT count(*) AS total_count FROM view_statistics_patrol_area WHERE region_id=" + regionId;
        String normalCountSql = "SELECT COUNT(*) AS normal_count FROM view_statistics_patrol_area WHERE region_id=" + regionId + " AND status=1";
        String exceptionCountSql = "SELECT COUNT(*) AS exception_count FROM view_statistics_patrol_area WHERE region_id=" + regionId + " AND status=2";

        //根据时间组织sql
        Map<String,Date> map = null;
        if(dateType == 0) {
            //30天
            map = getDateByNow(0,null,null);
        }
        if(dateType == 1) {
            //7天
            map = getDateByNow(1,null,null);
        }
        if(dateType == 2) {
            //今天
            map = getDateByNow(2,null,null);
        }
        if(dateType == 3) {
            //自定义事件
            map = getDateByNow(3,startTime,endTime);
        }
        if(map != null) {
            String startStr = DateUtils.dateFormatStr(map.get(DateUtils.START_DATE),DateUtils.FORMAT_DATE);
            String endStr = DateUtils.dateFormatStr(map.get(DateUtils.END_DATE),DateUtils.FORMAT_DATE);
            allCountSql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
            normalCountSql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
            exceptionCountSql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
        }
        List<Object[]> allCount = this.patrolUserRegionDao.getBySql(allCountSql);
        List<Object[]> normalCount = this.patrolUserRegionDao.getBySql(normalCountSql);
        List<Object[]> exceptionCount = this.patrolUserRegionDao.getBySql(exceptionCountSql);

        PatrolStatisticsExceptionCountVO countVO = new PatrolStatisticsExceptionCountVO();
        if(allCount != null && allCount.size() > 0) {
            Object obj = allCount.get(0);
            if(obj != null) {
                countVO.setAllCount(Integer.parseInt(obj.toString()));
            }
        }

        if(normalCount != null && normalCount.size() > 0) {
            Object obj = normalCount.get(0);
            if(obj != null) {
                countVO.setNormalCount(Integer.parseInt(obj.toString()));
            }
        }

        if(exceptionCount != null && exceptionCount.size() > 0) {
            Object obj = exceptionCount.get(0);
            countVO.setExceptionCount(Integer.parseInt(obj.toString()));
        }
        return countVO;
    }




    /**
     * 这里进行安防巡查数据查询,根据人员进行巡查
     * @param type          状态类型:0全部,1异常,2正常
     * @param dateType      时间类型:0,30天,1,7天,2,今天,3自定义时间查询
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param jobNum        工号
     */
    public PageAble<PatrolStatisticsAreaVO> statisticsPatrolPerson(Integer type,Integer dateType,Date startTime,Date endTime,String jobNum,PageAble<PatrolStatisticsAreaVO> pageAble) {
        //这里通过人员id进行查询
        //查询人员数量
        String sql = "SELECT id,username,job_num,region_id,start_time,end_time,status,date,campus_num,region_name"
                +" FROM view_statistics_patrol_area WHERE job_num='"
                + jobNum + "'";

        String countSql = "SELECT count(*) AS total_count FROM view_statistics_patrol_area WHERE job_num='" + jobNum +"'";

        //根据异常状态进行查看
        if(type == 1) {
            //异常
            sql += " AND status=2";
            countSql += " AND status=2";
        }
        if(type == 2) {
            //正常
            sql += " AND status=1";
            countSql += " AND status=1";
        }
        //根据时间组织sql
        Map<String,Date> map = null;
        if(dateType == 0) {
            //30天
            map = getDateByNow(0,null,null);
        }
        if(dateType == 1) {
            //7天
            map = getDateByNow(1,null,null);
        }
        if(dateType == 2) {
            //今天
            map = getDateByNow(2,null,null);
        }
        if(dateType == 3) {
            //自定义事件
            map = getDateByNow(3,startTime,endTime);
        }
        if(map != null) {
            String startStr = DateUtils.dateFormatStr(map.get(DateUtils.START_DATE),DateUtils.FORMAT_DATE);
            String endStr = DateUtils.dateFormatStr(map.get(DateUtils.END_DATE),DateUtils.FORMAT_DATE);
            sql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
            countSql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
        }
        sql += " ORDER BY date DESC LIMIT " + pageAble.getPageSize() + " OFFSET " + pageAble.getStart();

        List<Object[]> countList = this.patrolUserRegionDao.getBySql(countSql);
        //统计总条数
        int count = 0;
        if(countList != null && countList.size() > 0) {
            Object o = countList.get(0);
            count = Integer.parseInt(o.toString());
        }

        List<Object[]> objects = this.patrolUserRegionDao.getBySql(sql);
        //这里进行分页查询相关内容
        List<PatrolStatisticsAreaVO> statisticsAreaVOs = null;
        if(objects != null && objects.size() > 0) {
            statisticsAreaVOs = new ArrayList<>();
            for (Object[] obj : objects) {
                //这里进行封装数据
                PatrolStatisticsAreaVO vo = new PatrolStatisticsAreaVO();
                //id
                if(obj[0] != null) {
                    vo.setId(Integer.parseInt(obj[0].toString()));
                }
                //username
                if(obj[1] != null) {
                    vo.setUsername(obj[1].toString());
                }
                //job_num
                if(obj[2] != null) {
                    vo.setJobNum(obj[2].toString());
                }
                //region_id
                if(obj[3] != null) {
                    vo.setRegionId(Integer.parseInt(obj[3].toString()));
                }
                //start_time
                if(obj[4] != null) {
                    vo.setStartTime(obj[4].toString());
                }
                //end_time
                if(obj[5] != null) {
                    vo.setEndTime(obj[5].toString());
                }
                //status
                if(obj[6] != null) {
                    vo.setStatus(Integer.parseInt(obj[6].toString()));
                }
                //date
                if(obj[7] != null) {
                    vo.setDate(obj[7].toString());
                }
                //campus_num
                if(obj[8] != null) {
                    vo.setCampusNum(Integer.parseInt(obj[8].toString()));
                }
                //region_name
                if(obj[9] != null) {
                    vo.setRegionName(obj[9].toString());
                }
                statisticsAreaVOs.add(vo);
            }
        }

        //下面进行一场状态查询
        if(statisticsAreaVOs != null && statisticsAreaVOs.size() > 0) {
            for (PatrolStatisticsAreaVO vo : statisticsAreaVOs) {
                if(vo.getStatus() == 2) {
                    //有异常信息
                    String exceptionSql = "SELECT exception_name,createtime,"+
                            "to_char(createtime,'YYYY-MM-DD HH24:MI:SS') AS strCreatetime "+
                            "FROM patrol_exception_info WHERE usregid=" + vo.getId() + " ORDER BY createtime DESC";
                    List<Object[]> exceptions = this.patrolUserRegionDao.getBySql(exceptionSql);
                    List<PatrolExceptionVO> exceptionVO = null;
                    if(exceptions != null && exceptions.size() > 0) {
                        //进行异常信息封装
                        exceptionVO = new ArrayList<>();
                        for(Object[] obj : exceptions) {
                            PatrolExceptionVO patrolExceptionVO = new PatrolExceptionVO();
                            patrolExceptionVO.setException_name(obj[0].toString());
                            patrolExceptionVO.setCreatetime(obj[2].toString());
                            exceptionVO.add(patrolExceptionVO);
                        }
                    }
                    //将异常信息添加
                    vo.setExceptionVOs(exceptionVO);
                }
            }
        }
        pageAble.setList(statisticsAreaVOs);
        pageAble.setTotalCount(count);
        return pageAble;
    }


    /**
     * 根据人员id查询安防巡更统计相关记录条数
     * @param dateType      时间类型:0,30天,1,7天,2,今天,3自定义时间查询
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param jobNum        工号
     * @return
     */
    public PatrolStatisticsExceptionCountVO queryPatrolPersonCount(Integer dateType,Date startTime,Date endTime,String jobNum) throws Exception {
        String allCountSql = "SELECT count(*) AS total_count FROM view_statistics_patrol_area WHERE job_num='" + jobNum + "'";
        String normalCountSql = "SELECT COUNT(*) AS normal_count FROM view_statistics_patrol_area WHERE job_num='" + jobNum + "' AND status=1";
        String exceptionCountSql = "SELECT COUNT(*) AS exception_count FROM view_statistics_patrol_area WHERE job_num='" + jobNum + "' AND status=2";

        //根据时间组织sql
        Map<String,Date> map = null;
        if(dateType == 0) {
            //30天
            map = getDateByNow(0,null,null);
        }
        if(dateType == 1) {
            //7天
            map = getDateByNow(1,null,null);
        }
        if(dateType == 2) {
            //今天
            map = getDateByNow(2,null,null);
        }
        if(dateType == 3) {
            //自定义事件
            map = getDateByNow(3,startTime,endTime);
        }
        if(map != null) {
            String startStr = DateUtils.dateFormatStr(map.get(DateUtils.START_DATE),DateUtils.FORMAT_DATE);
            String endStr = DateUtils.dateFormatStr(map.get(DateUtils.END_DATE),DateUtils.FORMAT_DATE);
            allCountSql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
            normalCountSql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
            exceptionCountSql += " AND start_date BETWEEN '" + startStr + "' AND '" + endStr + "'";
        }
        List<Object[]> allCount = this.patrolUserRegionDao.getBySql(allCountSql);
        List<Object[]> normalCount = this.patrolUserRegionDao.getBySql(normalCountSql);
        List<Object[]> exceptionCount = this.patrolUserRegionDao.getBySql(exceptionCountSql);

        PatrolStatisticsExceptionCountVO countVO = new PatrolStatisticsExceptionCountVO();
        if(allCount != null && allCount.size() > 0) {
            Object obj = allCount.get(0);
            if(obj != null) {
                countVO.setAllCount(Integer.parseInt(obj.toString()));
            }
        }

        if(normalCount != null && normalCount.size() > 0) {
            Object obj = normalCount.get(0);
            if(obj != null) {
                countVO.setNormalCount(Integer.parseInt(obj.toString()));
            }
        }

        if(exceptionCount != null && exceptionCount.size() > 0) {
            Object obj = exceptionCount.get(0);
            countVO.setExceptionCount(Integer.parseInt(obj.toString()));
        }
        return countVO;
    }


    /**
     * 根据时间类型返回开始时间和结束时间
     * @param dateType      时间类型:0,30天,1,7天,2,今天,3自定义时间查询
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return
     */
    public Map<String,Date> getDateByNow(int dateType,Date startTime,Date endTime) {
        Map<String,Date> dateMap = new HashMap<>();
        //根据时间类型进行查看
        if(dateType == 0) {
            //30天
            dateMap = getStartEndTimeByDayNum(new Date(),30);
        }
        if(dateType == 1) {
            //7天
            dateMap = getStartEndTimeByDayNum(new Date(),7);
        }
        if(dateType == 2) {
            //今天
            Date startDate = DateUtils.getDayOfBeginTime(new Date()).getTime();
            Date endDate = DateUtils.getDayOfEndTime(new Date()).getTime();
            dateMap.put(DateUtils.START_DATE,startDate);
            dateMap.put(DateUtils.END_DATE,endDate);
        }
        if(dateType == 3) {
            if(startTime != null && endTime != null) {
                //自定义事件
                dateMap.put(DateUtils.START_DATE, startTime);
                dateMap.put(DateUtils.END_DATE, endTime);
            }
        }
        return dateMap;
    }
}
