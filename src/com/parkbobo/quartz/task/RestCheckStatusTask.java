package com.parkbobo.quartz.task;

import java.text.ParseException;
import javax.annotation.Resource;
import com.parkbobo.service.FireFightEquipmentService;

public class RestCheckStatusTask {
	/**
	 * 启动任务
	 * 
	 * */
	@Resource
	private FireFightEquipmentService fireFightEquipmentService;
	public void startJob() throws ParseException
	{
		getRestCheckStatusTask();
	}
	/**
	 * 每月一号2点重置所有消防设备的巡查状态
	 */
	@SuppressWarnings("deprecation")
	private void getRestCheckStatusTask() {
//		System.out.println("执行重置");
		this.fireFightEquipmentService.updateAll();
	}
	public static void main(String[] args) {
	}
}
