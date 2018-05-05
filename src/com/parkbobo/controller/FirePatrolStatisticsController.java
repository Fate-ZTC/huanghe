package com.parkbobo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.parkbobo.dao.FireFightEquipmentDao;

@Controller
@RequestMapping("/fire/patrol")
public class FirePatrolStatisticsController {
	
	@Resource(name="fireFightEquipmentDaoImpl")
    private FireFightEquipmentDao fireFightEquipmentDao;
	/**
	 * 巡查统计接口
	 * @param request
	 * @param page
	 * @param pageSize
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/manage/statistics/page", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> statistics(HttpServletRequest request, int page, int pageSize, Integer type){
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sb =  new StringBuilder();
		sb.append("select t1.total,t2.finish_count,");
		sb.append("coalesce(t3.exception_count,0) as exception_count,");
		sb.append("(t1.total-t2.finish_count) no_count,");
		sb.append("t1.stat_date,");
		sb.append("replace(t1.stat_date,'-','年')||'月' stat_date_cn ");
		sb.append("from (");
		sb.append("select count(*) total,to_char(A.last_update_time, 'yyyy-mm') stat_date ");
		sb.append("from fire_fight_equipment_history A ");
		sb.append("inner join fire_fight_equipment B on A.old_id = B.id ");
		sb.append("left join fire_patrol_building_info C on B.building_code = C.building_id ");
		if(type != null && type != 0){
			if(type == 20){
				sb.append("where C.\"type\" = ").append(type).append(" or C.building_id is null ");
			} else {
				sb.append("where C.\"type\" = ").append(type).append(" ");
			}			
		}
		sb.append("group by to_char(A.last_update_time, 'yyyy-mm') ");
		sb.append(") t1 ");
		sb.append("left join (");
		sb.append("select count(*) finish_count, to_char(A.last_update_time, 'yyyy-mm') as stat_date ");
		sb.append("from fire_fight_equipment_history A ");
		sb.append("inner join fire_fight_equipment B on A.old_id = B.id ");
		sb.append("left join fire_patrol_building_info C on B.building_code = C.building_id ");
		sb.append("where A.check_status = 1 ");
		if(type != null && type != 0){
			if(type == 20){
				sb.append("and ( C.\"type\" = ").append(type).append(" or C.building_id is null) ");
			} else {
				sb.append("and C.\"type\" = ").append(type).append(" ");
			}			
		}
		sb.append("group by to_char(A.last_update_time, 'yyyy-mm') ");
		sb.append(") t2 on t2.stat_date = t1.stat_date ");
		sb.append("left join (");
		sb.append("select count(*) exception_count, to_char(A.last_update_time, 'yyyy-mm') as stat_date ");
		sb.append("from fire_fight_equipment_history A ");
		sb.append("inner join fire_fight_equipment B on A.old_id = B.id ");
		sb.append("left join fire_patrol_building_info C on B.building_code = C.building_id ");
		sb.append("where A.status != 1 ");
		if(type != null && type != 0){
			if(type == 20){
				sb.append("and ( C.\"type\" = ").append(type).append(" or C.building_id is null) ");
			} else {
				sb.append("and C.\"type\" = ").append(type).append(" ");
			}			
		}
		sb.append("group by to_char(A.last_update_time, 'yyyy-mm') ");
		sb.append(")t3 on t3.stat_date = t1.stat_date ");
		sb.append("order by t1.stat_date desc ");			
		sb.append("");	
		StringBuilder sb_count =  new StringBuilder();
		sb_count.append("select count(*) from (");
		sb_count.append(sb).append(") t ");
		Long count = fireFightEquipmentDao.getCountForJdbc(sb_count.toString());
		sb.append("limit ").append(pageSize).append(" offset ").append((page-1)*pageSize);
		List<Map<String, Object>> list = fireFightEquipmentDao.findForJdbc(sb.toString());
		result.put("status", true);
		result.put("page", page);
		result.put("pageSize", pageSize);
		double size = pageSize;
		double total = count;
		double totalPage = total/size;
		totalPage = Math.ceil(totalPage);
		if(totalPage > page){
			result.put("isNextPage", true);
		}else{
			result.put("isNextPage", false);
		}		
		result.put("count", count);
		result.put("errorCode", 0);
		result.put("list", list);
		return result;
	}
	@RequestMapping(value = "/manage/exception/page", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getException(HttpServletRequest request, String startDate, int page, int pageSize, Integer type){
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sb =  new StringBuilder();
		sb.append("select A.id,A.floorid,A.campus_num as campusNum,A.check_status as checkStatus,");
		sb.append("A.job_num as jobNum,A.last_update_time as lastUpdateTime,A.lat,A.lon,A.\"name\",");
		sb.append("A.old_id as oldId,A.status,A.username as userName ");
		sb.append("from fire_fight_equipment_history A ");
		sb.append("inner join fire_fight_equipment B on A.old_id = B.id ");
		sb.append("left join fire_patrol_building_info C on B.building_code = C.building_id ");
		sb.append("where A.status <> 1  ");
		sb.append("and to_char(A.last_update_time,'yyyy-mm') = '").append(startDate).append("' ");
		if(type != null && type != 0 ){
			if(type == 20){
				sb.append(" and ( C.\"type\" = ").append(type).append(" or C.building_id is null) ");
			} else {
				sb.append(" and C.\"type\" = ").append(type).append(" ");
			}
		}
		StringBuilder sb_count =  new StringBuilder();
		sb_count.append("select count(*) from (");
		sb_count.append(sb).append(") t ");
		Long count = fireFightEquipmentDao.getCountForJdbc(sb_count.toString());
		List<Map<String, Object>> list = fireFightEquipmentDao.findForJdbc(sb.toString());
		Map<String, Object> data = new HashMap<String, Object>();
		double size = pageSize;
		double total = count;
		double totalPage = total/size;
		totalPage = Math.ceil(totalPage);
		if(totalPage > page){
			data.put("hasNextPage", true);
		}else{
			data.put("hasNextPage", false);
		}
		if(page == 1){
			data.put("firstPage", true);
		}else{
			data.put("firstPage", false);
		}
		if(page == totalPage){
			data.put("lastPage", true);
		}else{
			data.put("lastPage", false);
		}
		data.put("allRow", count);
		data.put("currentPage", page);
		data.put("pageSize", pageSize);
		data.put("totalPage", count);
		data.put("list", list);
		result.put("status", true);		
		result.put("Code", 1);
		result.put("data", data);
		return result;
	}
	
	@RequestMapping(value = "/manage/unpatrol/page", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getUnPatrol(String startDate, int page, int pageSize, Integer type){
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sb =  new StringBuilder();
		sb.append("select A.id,A.floorid,A.campus_num as campusNum,A.check_status as checkStatus,");
		sb.append("A.job_num as jobNum,A.last_update_time as lastUpdateTime,A.lat,A.lon,A.\"name\",");
		sb.append("A.old_id as oldId,A.status,A.username as userName ");
		sb.append("from fire_fight_equipment_history A ");
		sb.append("inner join fire_fight_equipment B on A.old_id = B.id ");
		sb.append("left join fire_patrol_building_info C on B.building_code = C.building_id ");
		sb.append("where A.check_status <> 1  ");
		sb.append("and to_char(A.last_update_time,'yyyy-mm') = '").append(startDate).append("' ");
		if(type != null && type != 0 ){
			if(type == 20){
				sb.append(" and ( C.\"type\" = ").append(type).append(" or C.building_id is null) ");
			} else {
				sb.append(" and C.\"type\" = ").append(type).append(" ");
			}
		}
		StringBuilder sb_count =  new StringBuilder();
		sb_count.append("select count(*) from (");
		sb_count.append(sb).append(") t ");
		Long count = fireFightEquipmentDao.getCountForJdbc(sb_count.toString());
		List<Map<String, Object>> list = fireFightEquipmentDao.findForJdbc(sb.toString());
		Map<String, Object> data = new HashMap<String, Object>();
		double size = pageSize;
		double total = count;
		double totalPage = total/size;
		totalPage = Math.ceil(totalPage);
		if(totalPage > page){
			data.put("hasNextPage", true);
		}else{
			data.put("hasNextPage", false);
		}
		if(page == 1){
			data.put("firstPage", true);
		}else{
			data.put("firstPage", false);
		}
		if(page == totalPage){
			data.put("lastPage", true);
		}else{
			data.put("lastPage", false);
		}
		data.put("allRow", count);
		data.put("currentPage", page);
		data.put("pageSize", pageSize);
		data.put("totalPage", count);
		data.put("list", list);
		result.put("status", true);		
		result.put("Code", 1);
		result.put("data", data);
		return result;
	}

}
