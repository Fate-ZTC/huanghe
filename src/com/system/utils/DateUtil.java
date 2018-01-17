package com.system.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期操作工具类
 * @author LH
 * @since 1.0
 */
public class DateUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	private static final SimpleDateFormat YEARFORMAT = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat MONTHFORMAT = new SimpleDateFormat("MM");
	private static final SimpleDateFormat  DATETIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat DATEFORMATNOSPLIT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat DATETIMENOSEPARATORFORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat MONTHDAYFORMAT = new SimpleDateFormat("MM月dd日");
    /**
     * 格式化日期与时间
     */
    public static String formatDatetime(long timestamp) {
        return DATETIMEFORMAT.format(new Date(timestamp));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(long timestamp) {
        return DATEFORMAT.format(new Date(timestamp));
    }
    public static String formatDateNoSplit(long timestamp) {
        return DATEFORMATNOSPLIT.format(new Date(timestamp));
    }
    
    public static String formatMonthDay(long timestamp) {
        return MONTHDAYFORMAT.format(new Date(timestamp));
    }
    public static String formatDatetimeNoSeparator (long timestamp) {
        return DATETIMENOSEPARATORFORMAT.format(new Date(timestamp));
    }
    public static String formatYear(long timestamp){
    	return YEARFORMAT.format(new Date(timestamp));
    }
    public static String formatMonth(long timestamp){
    	return MONTHFORMAT.format(new Date(timestamp));
    }
    
    /**
     * 格式化时间
     */
    public static String formatTime(long timestamp) {
        return TIMEFORMAT.format(new Date(timestamp));
    }

    /**
     * 获取当前日期与时间
     */
    public static String getCurrentDatetime() {
        return DATETIMEFORMAT.format(new Date());
    }

    /**
     * 获取当前日期
     */
    public static String getCurrentDate() {
        return DATEFORMAT.format(new Date());
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        return TIMEFORMAT.format(new Date());
    }

    /**
     * 解析日期与时间
     */
    public static Date parseDatetime(String str) {
        Date date = null;
        try {
            date = DATETIMEFORMAT.parse(str);
        } catch (ParseException e) {
            logger.error("解析日期字符串出错！格式：yyyy-MM-dd HH:mm:ss", e);
        }
        return date;
    }

    /**
     * 解析日期
     */
    public static Date parseDate(String str) {
        Date date = null;
        try {
            date = DATEFORMAT.parse(str);
        } catch (ParseException e) {
            logger.error("解析日期字符串出错！格式：yyyy-MM-dd", e);
        }
        return date;
    }
   

    /**
     * 解析时间
     */
    public static Date parseTime(String str) {
        Date date = null;
        try {
            date = TIMEFORMAT.parse(str);
        } catch (ParseException e) {
            logger.error("解析日期字符串出错！格式：HH:mm:ss", e);
        }
        return date;
    }
    public static String getMonthFirstDay(){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        return day_first;
    }
    public static String getYearFirstDay(){
    	return formatYear(System.currentTimeMillis()) + "-01-01";
    }
}
