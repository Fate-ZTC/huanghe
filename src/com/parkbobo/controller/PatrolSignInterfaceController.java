package com.parkbobo.controller;

import com.alibaba.fastjson.JSON;
import com.parkbobo.VO.PatrolsignStatisticVO;
import com.parkbobo.model.*;
import com.parkbobo.service.*;
import com.parkbobo.utils.PageBean;
import com.parkbobo.utils.message.MessageBean;
import com.system.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 巡更签到接口
 * @author RY
 * @version 1.0
 * @since 2018-7-9 10:12:26
 */
@Controller
public class PatrolSignInterfaceController {
	
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

	private static double EARTH_RADIUS = 6738.137;

	/**
	 * 巡更端拉取巡更签到配置信息
	 * @param response
	 * @param jobNum
	 */
	@RequestMapping("loadPatrolSignConfig")
	public void loadPatrolSignConfig(HttpServletResponse response, String jobNum){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();
		try {
			out = response.getWriter();
			PatrolConfig config = patrolConfigService.getById(1);
			PatrolUserRegion patrolUserRegion = patrolUserRegionService.getCountTime(jobNum);
			if(config != null){
                json.append("{\"status\":true,");
                json.append("\"code\":1,");
                json.append("\"errorMsg\":\"获取成功\",");
                json.append("\"data\":{");
                json.append("\"beaconSignDistance\":" + config.getBeaconSignDistance() + ",");
                json.append("\"gpsSignDistance\":" + config.getGpsSignDistance() + ",");
                json.append("\"signRange\":" + config.getSignRange() + ",");
                if(patrolUserRegion != null){
					json.append("\"usregId\":" + patrolUserRegion.getId() + ",");
                    json.append("\"patrolTime \":" + caculatePatrolTime(patrolUserRegion.getStartTime(), config.getSignRange()) + "");
                } else{
					json.append("\"usregId\":-1,");
                    json.append("\"patrolTime\":-1");
                }
                json.append("}}");
            } else{
                json.append("{\"status\":false,");
                json.append("\"code\":-2,");
                json.append("\"errorMsg\":\"未查询到配置信息\",");
                json.append("\"data\":{}}");
            }
            out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = new StringBuffer();
			json.append("{\"status\":false,");
			json.append("\"code\":-2,");
			json.append("\"errorMsg\":\"接口错误\",");
			json.append("\"data\":{}}");
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * 巡更端根据巡更区域拉取区域巡更签到点位及标签信息
	 * @param response
	 * @param usregId
	 */
	@RequestMapping("loadPatrolSignPoint")
	public void loadPatrolSignPoint(HttpServletResponse response, Integer usregId){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();
		StringBuffer tmpJson = new StringBuffer();
		try {
			out = response.getWriter();

			PatrolConfig config = patrolConfigService.getById(1);
			PatrolUserRegion patrolUserRegion = patrolUserRegionService.getById(usregId);

			String hql = "from PatrolSignPointInfo where patrolRegion.id = " + patrolUserRegion.getRegionId();
			List<PatrolSignPointInfo> pointList = patrolSignPointInfoService.getByHql(hql);

			if(pointList.size() > 0){
				json.append("{\"status\":true,");
				json.append("\"code\":1,");
				json.append("\"errorMsg\":\"获取成功\",");
				json.append("\"data\":[");

				for(PatrolSignPointInfo patrolSignPointInfo : pointList){
					String queryBeaconHql = "from PatrolBeaconInfo where patrolSignPointInfo.pointId = " + patrolSignPointInfo.getPointId();
					List<PatrolBeaconInfo> beaconInfoList = patrolBeaconInfoService.getByHql(queryBeaconHql);
					StringBuffer beaconJson = new StringBuffer();
					for(PatrolBeaconInfo beaconInfo : beaconInfoList){
						beaconJson.append("{");
						beaconJson.append("\"major\":" + beaconInfo.getMajor() + ",");
						beaconJson.append("\"minor\":" + beaconInfo.getMinor() );
						beaconJson.append("},");
					}
					if(beaconJson.length() > 0){
						beaconJson.deleteCharAt(beaconJson.length() - 1);
					}
					tmpJson.append("{");
					tmpJson.append("\"pointId\":" + patrolSignPointInfo.getPointId() + ",");
					tmpJson.append("\"pointName\":\"" + patrolSignPointInfo.getPointName() + "\",");
					tmpJson.append("\"lng\":" + patrolSignPointInfo.getLng() + ",");
					tmpJson.append("\"lat\":" + patrolSignPointInfo.getLat() + ",");
					tmpJson.append("\"hasSigned\":" + calculateSignStatus(patrolUserRegion.getStartTime(), config.getSignRange(), patrolSignPointInfo.getPointId(), patrolUserRegion.getJobNum()) + ",");
					tmpJson.append("\"beaconList\":[" + beaconJson + "]");
					tmpJson.append("},");
				}
				if(tmpJson.length() > 0){
					tmpJson.deleteCharAt(tmpJson.length() - 1);
				}

				json.append(tmpJson + "]}");
			} else{
				json.append("{\"status\":false,");
				json.append("\"code\":-2,");
				json.append("\"errorMsg\":\"未查询到签到点位信息\",");
				json.append("\"data\":{}}");
			}
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = new StringBuffer();
			json.append("{\"status\":false,");
			json.append("\"code\":-2,");
			json.append("\"errorMsg\":\"接口错误\",");
			json.append("\"data\":[]}");
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * 巡更端签到接口
	 * @param response
	 * @param locationInfo
	 */
	@RequestMapping("patrolSign")
	public void patrolSign(HttpServletResponse response, String locationInfo){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();
		StringBuffer tmpJson = new StringBuffer();

		try {
			out = response.getWriter();

			PatrolSignLocationInfo locationInfoObject = JSON.parseObject(locationInfo, PatrolSignLocationInfo.class);

			if(locationInfoObject != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				PatrolConfig config = patrolConfigService.getById(1);
                PatrolUserRegion patrolUserRegion = patrolUserRegionService.getById(locationInfoObject.getUsregId());

                String[] signRange = new String[2];
                Date signTime = new Date();
                // 延时提交判断
                if(StringUtil.isNotEmpty(locationInfoObject.getLocationTime())){
                    signTime = sdf.parse(locationInfoObject.getLocationTime());
                }

                // 计算签到时间所在签到周期
                Date rangeStart = new Date(signTime.getTime() - ((signTime.getTime() - patrolUserRegion.getStartTime().getTime()) % (config.getSignRange() * 60 * 1000)));
                Date rangeEnd = new Date(rangeStart.getTime() + config.getSignRange() * 60 * 1000);
                signRange[0] = sdf.format(rangeStart);
                signRange[1] = sdf.format(rangeEnd);

				// 判断是否包含有效数据
                Map<String, PatrolSignPointInfo> effectivePointMap = new HashMap<String, PatrolSignPointInfo>();
                String hql = "from PatrolSignPointInfo where patrolRegion.id = " + patrolUserRegion.getRegionId();
                List<PatrolSignPointInfo> pointInfoList = patrolSignPointInfoService.getByHql(hql);

				if(locationInfoObject.getBeaconList() != null && locationInfoObject.getBeaconList().size() > 0){
					String beaconQueryHql = "from PatrolBeaconInfo where patrolSignPointInfo.patrolRegion.id = " + patrolUserRegion.getRegionId();
					List<PatrolBeaconInfo> beaconInfoList = patrolBeaconInfoService.getByHql(beaconQueryHql);

					for(PatrolSignBeaconScan beaconScan : locationInfoObject.getBeaconList()){
						for(PatrolBeaconInfo beaconInfo : beaconInfoList){
							if(beaconScan.getMajor().equals(beaconInfo.getMajor())
									&& beaconScan.getMinor().equals(beaconInfo.getMinor())
									&& !effectivePointMap.containsKey(beaconInfo.getPatrolSignPointInfo().getPointId() + "-1")){
								effectivePointMap.put(beaconInfo.getPatrolSignPointInfo().getPointId() + "-1", beaconInfo.getPatrolSignPointInfo());
							}
						}
					}

				}  else{
					for(PatrolSignPointInfo signPointInfo : pointInfoList){
						if(isEffective(signPointInfo.getLng() + "," + signPointInfo.getLat(), locationInfoObject.getLngLat(), config.getGpsSignDistance().toString())
								&& !effectivePointMap.containsKey(signPointInfo.getPointId() + "-2")){
							effectivePointMap.put(signPointInfo.getPointId() + "-2", signPointInfo);
						}
					}
				}

                // 判断所在签到周期是否已经签到
				// 未签到，生成签到记录
				String signSuccessPointIds = "";
				if(effectivePointMap.keySet().size() > 0){
					for(String effectiveKey : effectivePointMap.keySet()){
						String[] pointIdAndSignMode = effectiveKey.split("-");
						String recordQueryHql = "from PatrolSignRecord where signTime > '" + signRange[0]
								+ "' and signTime < '" + signRange[1]
								+ "' and patrolSignPointInfo.pointId = " + pointIdAndSignMode[0]
								+ " and jobNum = '" + patrolUserRegion.getJobNum() + "' ";
						if(patrolSignRecordService.getByHql(recordQueryHql).size() == 0){
							PatrolSignRecord patrolSignRecord = new PatrolSignRecord();
							patrolSignRecord.setJobNum(patrolUserRegion.getJobNum());
							patrolSignRecord.setPatrolSignPointInfo(effectivePointMap.get(effectiveKey));
							patrolSignRecord.setSignMode(Integer.valueOf(pointIdAndSignMode[1]));
							patrolSignRecord.setSignTime(signTime);
							patrolSignRecord.setSignType(1);
							patrolSignRecord.setUsername(patrolUserRegion.getUsername());

							patrolSignRecordService.add(patrolSignRecord);
							signSuccessPointIds += pointIdAndSignMode[0] + ",";
						}
					}

					// 校验签到结果
					if(signSuccessPointIds.length() > 0){
						String[] signSuccessPointIdsArray = signSuccessPointIds.split(",");
						for(String str : signSuccessPointIdsArray){
							if(StringUtil.isNotEmpty(str)){
								tmpJson.append("{\"pointId\":" + str + "},");
							}
						}
						json.append("{\"status\":true,");
						json.append("\"code\":1,");
						json.append("\"errorMsg\":\"签到成功\",");
						json.append("\"data\":[" + tmpJson.deleteCharAt(tmpJson.length() - 1) + "]}");
					} else{
						json.append("{\"status\":false,");
						json.append("\"code\":-1000,");
						json.append("\"errorMsg\":\"有效签到点位已经签到\",");
						json.append("\"data\":[]}");
					}
				} else{
					json.append("{\"status\":false,");
					json.append("\"code\":-2,");
					json.append("\"errorMsg\":\"请靠近蓝牙标签或签到点位后重试\",");
					json.append("\"data\":[]}");
				}

            } else{
                json.append("{\"status\":false,");
                json.append("\"code\":-2,");
                json.append("\"errorMsg\":\"定位信息为空\",");
                json.append("\"data\":[]}");
            }
            out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = new StringBuffer();
			json.append("{\"status\":false,");
			json.append("\"code\":-2,");
			json.append("\"errorMsg\":\"接口错误\",");
			json.append("\"data\":[]}");
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * 根据工号、天数、开始日期
	 * 获取签到记录，按天分组
	 * @param response
	 * @param jobNum
	 * @param dateCount
	 * @param queryStartDate
	 */
	@RequestMapping("patrolSignRecord")
	public void patrolSignRecord(HttpServletResponse response, String jobNum, Integer dateCount, String queryStartDate){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();
		StringBuffer tmpJson = new StringBuffer();

		try {
			out = response.getWriter();

			if(StringUtil.isEmpty(queryStartDate)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                queryStartDate = sdf.format(new Date());
            }

			String hql = "from PatrolSignUserDateView where jobNum = '" + jobNum
                    + "' and signDate <= '" + queryStartDate
                    +"' order by signDate desc";
			PageBean<PatrolSignUserDateView> pageBean = patrolSignUserDateViewService.pageQuery(hql, dateCount + 1, 1);

			if(pageBean.getList().size() > 0){
                Boolean hasNext = pageBean.getList().size() > dateCount ? true : false;
                String nextQueryStartDate = "";
                if(hasNext){
                    nextQueryStartDate = pageBean.getList().get(dateCount).getSignDate();
                    for(int i = 0; i < dateCount; i++){
                        tmpJson.append(loadJsonWithJobNumDate(jobNum, pageBean.getList().get(i).getSignDate()) + ",");
                    }
                } else{
                    for(int i = 0; i < pageBean.getList().size(); i++){
                        tmpJson.append(loadJsonWithJobNumDate(jobNum, pageBean.getList().get(i).getSignDate()) + ",");
                    }
                }

                json.append("{\"status\":true,");
                json.append("\"code\":1,");
                json.append("\"errorMsg\":\"获取成功\",");
                json.append("\"hasNext\":" + hasNext + ",");
                json.append("\"queryStartDate\":\"" + nextQueryStartDate + "\",");
                json.append("\"data\":[" + tmpJson.deleteCharAt(tmpJson.length() - 1) + "]");
                json.append("}");
                System.out.println(json);

            } else{
                json.append("{\"status\":false,");
                json.append("\"code\":-2,");
                json.append("\"errorMsg\":\"没有签到信息了\",");
                json.append("\"hasNext\":false,");
                json.append("\"queryStartDate\":\"\",");
                json.append("\"data\":[]}");
            }

			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = new StringBuffer();
			json.append("{\"status\":false,");
			json.append("\"code\":-2,");
			json.append("\"errorMsg\":\"接口错误\",");
			json.append("\"hasNext\":false,");
			json.append("\"queryStartDate\":\"\",");
			json.append("\"data\":[]}");
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * 管理端获取签到点位数据
	 * @param response
	 */
	@RequestMapping("pointStatistic")
	public void pointStatistic(HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();
		StringBuffer tmpJson = new StringBuffer();

		try {
			out = response.getWriter();

			PatrolConfig patrolConfig = patrolConfigService.getConfig(1);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Long systemMillis = System.currentTimeMillis();
			String hql = "from PatrolUserRegion where startTime > '" + sdf.format(new Date(systemMillis)) + "' ";
			List<PatrolUserRegion> patrolUserRegionList = patrolUserRegionService.getByHQL(hql);

			Map<Integer, Long> regionMap = new HashMap<Integer, Long>();
			for(PatrolUserRegion userRegion : patrolUserRegionList){
				Long endMillis = systemMillis;
				if(userRegion.getEndTime() != null){
					endMillis = userRegion.getEndTime().getTime();
				}

				if(!regionMap.containsKey(userRegion.getRegionId())){
					regionMap.put(userRegion.getRegionId(), calculateNeedSignCount(userRegion.getStartTime().getTime(), endMillis, patrolConfig.getSignRange(), patrolConfig.getOvertimeDeal()));
				} else{
					Long needSignCount = regionMap.get(userRegion.getRegionId());
					needSignCount += calculateNeedSignCount(userRegion.getStartTime().getTime(), endMillis, patrolConfig.getSignRange(), patrolConfig.getOvertimeDeal());
					regionMap.put(userRegion.getRegionId(), needSignCount);
				}
			}

			String pointQueryHql = "from PatrolSignPointInfo";
			List<PatrolSignPointInfo> pointInfoList = patrolSignPointInfoService.getByHql(pointQueryHql);
			if(pointInfoList.size() > 0){
				for(PatrolSignPointInfo pointInfo : pointInfoList){
					String recordCountHql = "from PatrolSignRecord where signTime > '" + sdf.format(new Date(systemMillis)) + "' and signType = 1";
					Integer signedCount = patrolSignRecordService.pageQuery(recordCountHql, 1, 1).getAllRow();
					tmpJson.append("{");
					tmpJson.append("\"pointId\":" + pointInfo.getPointId() + ",");
					tmpJson.append("\"pointName\":\"" + pointInfo.getPointName() + "\",");
					tmpJson.append("\"lng\":" + pointInfo.getLng() + ",");
					tmpJson.append("\"lat\":" + pointInfo.getLat() + ",");
					tmpJson.append("\"signedCount\":" + signedCount + ",");
					tmpJson.append("\"expectedCount\":" + (regionMap.containsKey(pointInfo.getPatrolRegion().getId()) ? regionMap.get(pointInfo.getPatrolRegion().getId()) : 0));
					tmpJson.append("},");
				}

				json.append("{\"status\":true,");
				json.append("\"code\":1,");
				json.append("\"errorMsg\":\"获取成功\",");
				json.append("\"data\":[" + tmpJson.deleteCharAt(tmpJson.length() - 1) + "]}");
			} else{
				json.append("{\"status\":false,");
				json.append("\"code\":-2,");
				json.append("\"errorMsg\":\"没有点位信息\",");
				json.append("\"data\":[]}");
			}
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = new StringBuffer();
			json.append("{\"status\":false,");
			json.append("\"code\":-2,");
			json.append("\"errorMsg\":\"接口错误\",");
			json.append("\"data\":[]}");
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * 管理端发起暂停
	 * @param response
	 * @param cause
	 * @param usercode
	 * @param username
	 */
	@RequestMapping("startPause")
	public void startPause(HttpServletResponse response, String cause, String usercode, String username){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();

		try {
			out = response.getWriter();

			PatrolPause checkPause = patrolPauseService.checkPauseStatus();
			if(checkPause == null){
				PatrolPause patrolPause = new PatrolPause();
				patrolPause.setCause(cause);
				patrolPause.setPauseStart(new Date());
				patrolPause.setUsercode(usercode);
				patrolPause.setUsername(username);

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
	 * 管理端结束暂停
	 * @param response
	 */
	@RequestMapping("stopPause")
	public void stopPause(HttpServletResponse response){
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
	 * 管理端获取暂停状态
	 * @param response
	 */
	@RequestMapping("getPauseStatus")
	public void getPauseStatus(HttpServletResponse response){
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

	/**
	 * 管理端根据日期获取点到统计
	 * @param response
	 * @param dateString
	 */
	@RequestMapping("patroSignStatisticWithDate")
	public void patroSignStatisticWithDate(HttpServletResponse response, String dateString){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();

		try {
			out = response.getWriter();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startTime = dateString + " 00:00:00";

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(dateString));
			calendar.add(Calendar.DATE, 1);
			String endTime = sdf.format(calendar.getTime()) + " 00:00:00";

			json.append("{\"status\":true,");
			json.append("\"code\":1,");
			json.append("\"errorMsg\":\"获取成功\",");
			json.append("\"data\":[" + statisticSignWithTimeRange(startTime, endTime) + "]}");

			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = new StringBuffer();
			json.append("{\"status\":false,");
			json.append("\"code\":-2,");
			json.append("\"errorMsg\":\"接口错误\",");
			json.append("\"data\":[]}");
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}

	}

	/**
	 * 管理端根据时段获取点到统计
	 * @param response
	 * @param type 1：今天，2：过去7天，3：过去30天
	 */
	@RequestMapping("patroSignStatistic")
	public void patroSignStatistic(HttpServletResponse response, Integer type){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();

		try {
			out = response.getWriter();

			String startTime = "";
			String endTime = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			String thisDay = sdf.format(calendar.getTime());

			switch (type){
				case 1:
				{
					startTime = thisDay + " 00:00:00";
					calendar.add(Calendar.DATE, 1);
					endTime = sdf.format(calendar.getTime()) + " 00:00:00";
					break;
				}
				case 2:
				{
					endTime = thisDay + " 00:00:00";
					calendar.add(Calendar.DATE, -7);
					startTime = sdf.format(calendar.getTime()) + " 00:00:00";
					break;
				}
				case 3:
				{
					endTime = thisDay + " 00:00:00";
					calendar.add(Calendar.DATE, -30);
					startTime = sdf.format(calendar.getTime()) + " 00:00:00";
					break;
				}
				default:break;
			}


			json.append("{\"status\":true,");
			json.append("\"code\":1,");
			json.append("\"errorMsg\":\"获取成功\",");
			json.append("\"data\":[" + statisticSignWithTimeRange(startTime, endTime) + "]}");

			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = new StringBuffer();
			json.append("{\"status\":false,");
			json.append("\"code\":-2,");
			json.append("\"errorMsg\":\"接口错误\",");
			json.append("\"data\":[]}");
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}

	}

	/**
	 * 管理端根据点位获取今日未签明细
	 * @param response
	 * @param pointId
	 */
	@RequestMapping("loadNoSignWithPoint")
	public void loadNoSignWithPoint(HttpServletResponse response, Integer pointId){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		StringBuffer json = new StringBuffer();
		StringBuffer tmpJson = new StringBuffer();

		try {
			out = response.getWriter();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String hql = "from PatrolSignRecord where signTime > '" + sdf.format(new Date())
					+ " 00:00:00' and signType = 2 order by signTime ";
			List<PatrolSignRecord> recordList = patrolSignRecordService.getByHql(hql);

			Map<String, String> noSignMap = new HashMap<String, String>();
			for(PatrolSignRecord record : recordList){
				String key = record.getJobNum() + "-" + record.getUsername();
				if(!noSignMap.containsKey(key)){
					noSignMap.put(key, record.getNoSignRange());
				} else{
					String noSignRange = noSignMap.get(key);
					noSignRange += "," + record.getNoSignRange();
					noSignMap.put(key, noSignRange);
				}
			}

			for(String key : noSignMap.keySet()){
				String[] user = key.split("-");
				String[] noSignRanges = noSignMap.get(key).split(",");

				StringBuffer noSignRangeJson = new StringBuffer();
				for(String str : noSignRanges){
					noSignRangeJson.append("{\"range\":\"" + str + "\"},");
				}


				tmpJson.append("{");
				tmpJson.append("\"jobNum\":\"" + user[0] + "\",");
				tmpJson.append("\"username\":\"" + user[1] + "\",");
				tmpJson.append("\"noSignList\":[" + noSignRangeJson.deleteCharAt(noSignRangeJson.length() - 1) + "]");
				tmpJson.append("},");
			}

			if(tmpJson.length() > 0){
				json.append("{\"status\":true,");
				json.append("\"code\":1,");
				json.append("\"errorMsg\":\"获取成功\",");
				json.append("\"data\":[" + tmpJson.deleteCharAt(tmpJson.length() - 1) + "]}");
			} else{
				json.append("{\"status\":false,");
				json.append("\"code\":-2,");
				json.append("\"errorMsg\":\"没有未签记录\",");
				json.append("\"data\":[]}");
			}

			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = new StringBuffer();
			json.append("{\"status\":false,");
			json.append("\"code\":-2,");
			json.append("\"errorMsg\":\"接口错误\",");
			json.append("\"data\":[]}");
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * 根据开始时间、结束时间获取签到统计
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public StringBuffer statisticSignWithTimeRange(String startTime, String endTime){
		StringBuffer json = new StringBuffer();

		Map<String, PatrolsignStatisticVO> statisticVOMap = new HashMap<String, PatrolsignStatisticVO>();
		Map<Integer, Integer> regionPointCountMap = new HashMap<Integer, Integer>();

		PatrolConfig patrolConfig = patrolConfigService.getConfig(1);

		String hql = "from PatrolUserRegion where startTime > '" + startTime
				+ "' and (endTime is null or endTime < '" + endTime + "') ";

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

		List<PatrolUser> patrolUserList = patrolUserService.getAllUser();

		for(PatrolUser patrolUser : patrolUserList){
			if(statisticVOMap.containsKey(patrolUser.getJobNum())){
				Integer effectiveSign = patrolSignRecordService.countEffectiveWithTimeRange(patrolUser.getJobNum(), startTime, endTime);
				PatrolsignStatisticVO statisticVO = statisticVOMap.get(patrolUser.getJobNum());
				json.append("{");
				json.append("\"jobNum\":\"" + statisticVO.getJobNum() + "\",");
				json.append("\"username\":\"" + statisticVO.getUsername() + "\",");
				json.append("\"expectedCount\":" + statisticVO.getExpectedCount() + ",");
				json.append("\"signedCount\":" + effectiveSign + ",");
				json.append("\"noSignCount\":" + (statisticVO.getExpectedCount() - effectiveSign));
				json.append("},");
			} else{
				json.append("{");
				json.append("\"jobNum\":\"" + patrolUser.getJobNum() + "\",");
				json.append("\"username\":\"" + patrolUser.getUsername() + "\",");
				json.append("\"expectedCount\":0,");
				json.append("\"signedCount\":0,");
				json.append("\"noSignCount\":0");
				json.append("},");
			}
		}

		return json.deleteCharAt(json.length() - 1);
	}

	/**
	 * 根据工号、日期
	 * 获取签到记录，倒序
	 * @param jobNum
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public StringBuffer loadJsonWithJobNumDate(String jobNum, String date) throws ParseException {
		StringBuffer json = new StringBuffer();
		StringBuffer tmpJson = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		String dateString = "";
		if(date.equals(sdf.format(new Date()))){
			dateString = "今天";
		} else{
			dateString = df.format(sdf.parse(date));
		}

		String hql = "from PatrolSignRecord where jobNum = '" + jobNum
				+ "' and to_char(signTime, 'yyyy-MM-dd') = '" + date
				+ "' order by signTime desc";
		List<PatrolSignRecord> recordList = patrolSignRecordService.getByHql(hql);

		for(PatrolSignRecord record : recordList){
			tmpJson.append("{");
			tmpJson.append("\"pointName\":\"" + (record.getPatrolSignPointInfo()==null?"":record.getPatrolSignPointInfo().getPointName()) + "\",");
			if(record.getPatrolSignPointInfo()!=null&&record.getPatrolSignPointInfo().getPatrolRegion()!=null){
			    tmpJson.append("\"regionName\":\"" + record.getPatrolSignPointInfo().getPatrolRegion().getRegionName() + "\",");
            }else{
                tmpJson.append("\"regionName\":\""+""+"\",");
            }
			tmpJson.append("\"signStatus\":" + record.getSignType() + ",");
			tmpJson.append("\"signTime\":\"" + (record.getSignTime() == null ? "" : simpleDateFormat.format(record.getSignTime())) + "\",");
			tmpJson.append("\"signRange\":\"" + (record.getNoSignRange() == null ? "" : record.getNoSignRange()) + "\"");
			tmpJson.append("},");
		}

		json.append("{");
		json.append("\"dateString\":\"" + dateString + "\",");
		json.append("\"recordList\":[" + tmpJson.deleteCharAt(tmpJson.length() - 1) + "]");
		json.append("}");

		return json;
	}

	/**
	 * 计算用户当前位置是否在签到范围内
	 * @param center，中点坐标
	 * @param lonlat，用户当前坐标
	 * @param distance，有效距离
	 * @return
	 */
	public Boolean isEffective(String center, String lonlat, String distance){
		Boolean isIn = false;
		String[] centerArray = center.split(",");
		double lng1 = Double.valueOf(centerArray[0]);
		double lat1 = Double.valueOf(centerArray[1]);
		String[] lonlatArray = lonlat.split(",");
		double lng2 = Double.valueOf(lonlatArray[0]);
		double lat2 = Double.valueOf(lonlatArray[1]);
		double radLat1 = getRadian(lat1);
		double radLat2 = getRadian(lat2);
		double a = radLat1 - radLat2;//两点纬度差
		double b = getRadian(lng1) - getRadian(lng2);//两点的经度差
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2 ), 2) + Math.cos(radLat1)
				* Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

		double d = Double.valueOf(distance);
		s = s * EARTH_RADIUS * 1000;
//		System.out.println("中点："+center + "||当前位置："+lonlat+"||距离："+s);
		if(d > s){
			isIn = true;
		}
		return isIn;
	}

	/**
	 * 角度计算
	 * @param degree
	 * @return
	 */
	public double getRadian(double degree){
		return degree * Math.PI / 180.0;
	}

	/**
	 * 根据开始时间、签到周期
	 * 计算在最新一个周期已巡更时间
	 * @param starttime
	 * @param signRange
	 * @return
	 */
	public Long caculatePatrolTime(Date starttime, Integer signRange){
		return (System.currentTimeMillis() - starttime.getTime()) % (signRange * 60 * 1000);
	}

	/**
	 * 根据巡更开始时间、签到周期、点位ID、工号
	 * 判断在最新一个签到周期是否已签到
	 * @param starttime
	 * @param signRange
	 * @param pointId
	 * @param jobNum
	 * @return
	 */
	public Boolean calculateSignStatus(Date starttime, Integer signRange, Integer pointId, String jobNum){
		Date rangeStart = new Date(System.currentTimeMillis() - ((System.currentTimeMillis() - starttime.getTime()) % (signRange * 60 * 1000)));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String hql = "from PatrolSignRecord where signTime > '" + sdf.format(rangeStart)
				+ "' and patrolSignPointInfo.pointId = " + pointId
				+ " and jobNum= '" + jobNum + "' ";
		return patrolSignRecordService.getByHql(hql).size() > 0;
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
}
