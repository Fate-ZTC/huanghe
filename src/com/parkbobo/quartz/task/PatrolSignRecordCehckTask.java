package com.parkbobo.quartz.task;

import com.parkbobo.model.*;
import com.parkbobo.service.*;
import com.system.utils.StringUtil;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 巡更签到-签到记录检查定时任务
 * @version 1.0
 * @author RY
 * @since 2018-7-10 20:03:28
 */

public class PatrolSignRecordCehckTask {
    @Resource
    private PatrolUserRegionService patrolUserRegionService;
    @Resource
    private PatrolSignPointInfoService patrolSignPointInfoService;
    @Resource
    private PatrolSignRecordService patrolSignRecordService;
    @Resource
    private PatrolConfigService patrolConfigService;
    @Resource
    private PatrolPauseService patrolPauseService;

    public void startJob() throws ParseException {
        signRecordCheck();
    }

    public void signRecordCheck() throws ParseException {

        // 读取巡更签到配置信息
        PatrolConfig patrolConfig = patrolConfigService.getConfig(1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        // 当前时间倒推一个签到周期，作为读取已结束巡更的时间判断
        calendar.add(Calendar.MINUTE, -patrolConfig.getSignRange());
        String signRangeTimeString = sdf.format(calendar.getTime());

        // 查询未结束、结束不到一个签到周期的巡更信息
        String hql = "from PatrolUserRegion where endTime is null or endTime > '" + signRangeTimeString + "' ";
        List<PatrolUserRegion> userRegionList = patrolUserRegionService.getByHQL(hql);

        // 构建一个巡更区域-点位列表的MAP，避免重复从数据库获取
        Map<Integer, List<PatrolSignPointInfo>> regionPointsMap = new HashMap<Integer, List<PatrolSignPointInfo>>();

        // 遍历寻根信息
        // 计算需要检查的时间范围
        // 未结束的巡更，只需要检查上一个签到周期范围
        // 已结束的巡更，需要根据多出时间处理方式判断是否需要检查超出时间范围
        // 计算时间范围，还需要考虑暂停时间
        for(PatrolUserRegion userRegion : userRegionList){
            Date startTime = userRegion.getStartTime();
            Date endTime = userRegion.getEndTime() == null ? calendar.getTime() : userRegion.getEndTime();
            String startTimeString = sdf.format(startTime);
            String endTimeString = sdf.format(endTime);
            // 读取暂停时间
            String pauseQueryHql = "from PatrolPause where "
                    + "(pauseStart > '" + startTimeString + "' and pauseStart < '" + endTimeString + "') "
                    + "or (pauseEnd > '" + startTimeString + "' and pauseEnd < '" + endTimeString + "') order by pauseStart ";
            List<PatrolPause> pauseList = patrolPauseService.getByHql(pauseQueryHql);

            // 计算暂停总时长
            Long pauseMillis = 0L;
            // 构建巡更时间区域-时长List
            List<String> patrolTimeRange = new ArrayList<String>();
            if(pauseList.size() == 0){
                patrolTimeRange.add(startTimeString + "," + endTimeString);
            }
            String patrolStartString = "";
            for(PatrolPause pause : pauseList){
                Date pauseStart, pauseEnd;
                if(pause.getPauseStart().after(startTime)){
                    pauseStart = pause.getPauseStart();
                    if(StringUtil.isNotEmpty(patrolStartString)){
                        patrolTimeRange.add(patrolStartString + "," + sdf.format(pauseStart));
                    } else{
                        patrolTimeRange.add(startTimeString + "," + sdf.format(pauseStart));
                    }
                } else {
                    pauseStart = startTime;
                }

                if(pause.getPauseEnd().after(endTime)){
                    pauseEnd = endTime;
                } else{
                    pauseEnd = pause.getPauseEnd();
                    patrolStartString = sdf.format(pauseEnd);
                }

                pauseMillis += (pauseEnd.getTime() - pauseStart.getTime());

            }

            // 计算巡更总时长
            Long patrolMillis = (endTime.getTime() - startTime.getTime()) - pauseMillis;

            if(patrolMillis > (patrolConfig.getSignRange() * 60 * 1000L)){
                Long overTime = patrolMillis % (patrolConfig.getSignRange() * 60 * 1000L);
                Date rangeStartTime, rangeEndTime, overTimeStart, overTimeEnd;
                rangeStartTime = rangeEndTime = overTimeStart = overTimeEnd = null;
                Long rangeTime = patrolConfig.getSignRange() * 60 * 1000L;
                // 计算超出周期的时间范围与上个签到周期的时间范围
                for(int i = patrolTimeRange.size() - 1; i >= 0; i--){
                    String[] patrolRange = patrolTimeRange.get(i).split(",");
                    Date rangeStart = sdf.parse(patrolRange[0]);
                    Date rangeEnd = sdf.parse(patrolRange[1]);

                    if(i == patrolTimeRange.size() - 1){
                        overTimeEnd = rangeEnd;
                    }
                    if(overTime > 0L){
                        if(rangeEnd.getTime() - rangeStart.getTime() >= overTime){
                            overTime = 0L;
                            rangeEndTime = overTimeStart = new Date(rangeEnd.getTime() - overTime);
                        } else{
                            overTime -= (rangeEnd.getTime() - rangeStart.getTime());
                        }
                    } else if(rangeTime > 0L){
                        if(rangeEnd.getTime() - rangeStart.getTime() >= rangeTime){
                            overTime = 0L;
                            rangeStartTime = new Date(rangeEnd.getTime() - rangeTime);
                        } else{
                            rangeTime -= (rangeEnd.getTime() - rangeStart.getTime());
                        }
                    } else{
                        break;
                    }
                }

                if(!regionPointsMap.containsKey(userRegion.getRegionId())){
                    String pointQueryHql = "from PatrolSignPointInfo where patrolRegion.id = " + userRegion.getRegionId();
                    List<PatrolSignPointInfo> pointInfoList = patrolSignPointInfoService.getByHql(pointQueryHql);
                    regionPointsMap.put(userRegion.getRegionId(), pointInfoList);
                }

                checkSignRecord(sdf.format(rangeStartTime), sdf.format(rangeEndTime), userRegion.getJobNum(), userRegion.getUsername(), regionPointsMap.get(userRegion.getRegionId()));
                if(userRegion.getEndTime() != null && patrolConfig.getOvertimeDeal() == 1){
                    checkSignRecord(sdf.format(overTimeStart), sdf.format(overTimeEnd), userRegion.getJobNum(), userRegion.getUsername(), regionPointsMap.get(userRegion.getRegionId()));
                }

            }

        }

    }

    /**
     * 根据开始时间、结束时间、工号、姓名、巡更区域点位列表
     * 检查是否已签到
     * 如果未签，添加未签记录
     * @param startTime
     * @param endTime
     * @param jobNum
     * @param username
     * @param pointInfoList
     * @throws ParseException
     */
    public void checkSignRecord(String startTime, String endTime, String jobNum, String username, List<PatrolSignPointInfo> pointInfoList) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        for(PatrolSignPointInfo pointInfo : pointInfoList){
            if(!patrolSignRecordService.checkSignedWithTimeRange(jobNum, startTime, endTime, pointInfo.getPointId())){
                calendar.setTime(sdf.parse(endTime));
                calendar.add(Calendar.MINUTE, -1);

                PatrolSignRecord signRecord = new PatrolSignRecord();
                signRecord.setUsername(username);
                signRecord.setSignType(2);
                signRecord.setSignTime(calendar.getTime());
                signRecord.setPatrolSignPointInfo(pointInfo);
                signRecord.setJobNum(jobNum);
                signRecord.setNoSignRange(startTime.substring(10) + "-" + endTime.substring(10));

                patrolSignRecordService.add(signRecord);
            }
        }
    }
}
