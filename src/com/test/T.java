package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.MD5;
import com.parkbobo.utils.MD532;

public class T {
	public static void main(String[] args) throws ParseException {
//		104.07089
//		7f310b6ec1cfb7edaf687271fe062967
//		System.out.println(1443408728966l + 30 * 60 * 1000 < System.currentTimeMillis());
//		Double d = 104.070890d;
		//System.out.println(d);
//		String strDate = "2015-10-1 00:00:00";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = sdf.parse(strDate);
//		System.out.println(date.getTime());
//		System.out.println(new Date().getTime());
		//System.out.println(MD5.getDefaultInstance().MD5Encode("104.070890" + Configuration.getInstance().getValue("key")));
		//String md5Encode = MD5.getDefaultInstance().MD5Encode("{'ParkCode': '66666666','CarPort':'1001','CarPortDesc':'','CarNo':'川A58642','StartTime':'2016-01-29 11:36:30','EndTime':'2016-01-29 13:36:30','ConAmount':'10','Qn':'Cpu7NgDLdG81PGNA8wm4UpX6GMZGO0spm4PVRsNyi5c=','SubTime':'2016-01-29 11:36:30'}6ffba0ad08d94f07ab960ec188af8e6d");
		String md5Encode = MD532.getDefaultInstance().encryption("postgres");
		System.out.println(md5Encode);
	}      
	       
	       
	       
}          
           