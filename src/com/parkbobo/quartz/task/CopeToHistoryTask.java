package com.parkbobo.quartz.task;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.parkbobo.model.FireFightEquipment;
import com.parkbobo.model.FireFightEquipmentHistory;
import com.parkbobo.service.FireFightEquipmentHistoryService;
import com.parkbobo.service.FireFightEquipmentService;


public class CopeToHistoryTask {
	/**
	 * 启动任务
	 * 
	 * */
	@Resource
	private FireFightEquipmentService fireFightEquipmentService;
	@Resource
	private FireFightEquipmentHistoryService fireFightEquipmentHistoryService;
	public void startJob() throws ParseException
	{
		getCopeToHistoryTask();
	}
	/**
	 * 每月一号2点重置所有消防设备的巡查状态
	 */
	@SuppressWarnings("deprecation")
	private void getCopeToHistoryTask() {
		System.out.println("执行复制");
		List<FireFightEquipment> fireFightEquipments = fireFightEquipmentService.getAll();
		for (FireFightEquipment fireFightEquipment: fireFightEquipments) {
			FireFightEquipmentHistory history = new FireFightEquipmentHistory();
			history.setCampusNum(fireFightEquipment.getCampusNum());
			//这里每个月需要进行对巡查状态进行变更,所有设备为未巡查状态
//			history.setCheckStatus(fireFightEquipment.getCheckStatus());
			history.setCheckStatus((short)0);			//改为为巡查状态
			history.setOldId(fireFightEquipment.getId());
			//这里是每月的时间
			history.setLastUpdateTime(new Date());
			history.setLat(fireFightEquipment.getLat());
			history.setLon(fireFightEquipment.getLon());
			history.setStatus(fireFightEquipment.getStatus());
			history.setName(fireFightEquipment.getName());
			//设置楼层id
			if(null != fireFightEquipment.getFloorid()) {
				history.setFloorid(fireFightEquipment.getFloorid());
			}
			//设置大楼id
			if(null != fireFightEquipment.getBuildingCode()) {
				history.setBuildingCode(fireFightEquipment.getBuildingCode());
			}
			this.fireFightEquipmentHistoryService.add(history);
		}
	}

}
