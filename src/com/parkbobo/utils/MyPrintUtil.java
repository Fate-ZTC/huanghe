package com.parkbobo.utils;
/**
 * 打印输出辅助类
 * @author LH
 *
 */
public class MyPrintUtil {
	private static MyPrintUtil myPrintUtil;
	
	private MyPrintUtil(){}
	
	public synchronized static MyPrintUtil getDefaultInstance(){
		if(myPrintUtil==null){
			myPrintUtil = new MyPrintUtil();
		}
		return myPrintUtil;
	}
	public void out(Object out)
	{
		System.out.println(out.toString());
	}
}
