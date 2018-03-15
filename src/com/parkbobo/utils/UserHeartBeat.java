package com.parkbobo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserHeartBeat {
	
	public void Show(){
		Calendar cal=Calendar.getInstance();
		long date=cal.getTime().getTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String result=sdf.format(date);
		System.out.println(result);
		System.out.println("Spring Quartz Test");
	}
	
	public static void main(String args[]){
		new UserHeartBeat().Show();
	}

}
