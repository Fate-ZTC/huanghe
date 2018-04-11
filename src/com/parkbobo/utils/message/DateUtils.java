package com.parkbobo.utils.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by lijunhong on 17/12/7.
 * 时间处理相关工具
 */
public class DateUtils {

    public static final String START_DATE = "startDate";      //开始时间
    public static final String END_DATE = "endDate";          //结束时间
    public static final Long ONE_DAY_TIME = 1*24*60*60*1000l; //一天的时间毫秒值


    public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";         //时间格式
    public static final String FORMAT_DATE_SALSH = "yyyy/MM/dd HH:mm:ss";   //时间格式
    public static final long DURING_DAY = 86400 * 1000;


    /**
     * 将字符串的时间格式转化成另外一种格式
     * @param formatDate
     * @param reulstFormDate
     * @param dateStr
     * @return
     */
    public static String stringFormatDate(String formatDate,String reulstFormDate,String dateStr) {
        SimpleDateFormat format = null;
        format = new SimpleDateFormat(formatDate);
        try {
            Date resultDate = format.parse(dateStr);
            format = new SimpleDateFormat(reulstFormDate);
            return format.format(resultDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将时间类型转化成字符串类型
     * @param date      时间
     * @param format    格式类型
     * @return
     */
    public static String dateFormatStr(Date date,String format) {
        if(format != null && "".equals(format)) {format = FORMAT_DATE;}
        if(date == null) {date = new Date();}
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将字符串类型时间转化成时间类型
     * @param dateStr   时间字符串
     * @return
     */
    public static Date stringFormatDate(String dateStr) {
        Date date = null;
        if(dateStr != null && "".equals(dateStr)) { return new Date();}
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.FORMAT_DATE);
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    

    

    /**
     * 根据当前时间获取本周开始时间和结束时间(周一为一周第一天)
     * @param now
     * @return
     */
    public static Map<String,Date> getNowWeek(Date now) {
        if(now == null)
            return null;

        Map<String,Date> map = new HashMap<>();
        Date monday = getMonday(now);
        Date sunday = getSunday(now);
        monday = getDayOfBeginTime(monday).getTime();
        sunday = getDayOfEndTime(sunday).getTime();
        map.put(START_DATE,monday);
        map.put(END_DATE,sunday);
        return map;
    }

    /**
     * 根据当前时间获取本月(开始结束)
     * @param now
     * @return
     */
    public static Map<String,Date> getNowMonth(Date now) {
        if(now == null)
            return null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Map<String,Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        //这里处理跨年操作
        int days = calendar.getActualMaximum(Calendar.DATE);

        //设置上个月时间
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        //得到一个月开始时间
        Date date = new Date(calendar.getTime().getTime());
        date = getDayOfBeginTime(date).getTime();
        map.put(START_DATE,date);

        //得到一个月结束时间
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,days);
        date = new Date(calendar.getTime().getTime());
        date = getDayOfEndTime(date).getTime();
        map.put(END_DATE,date);
        return map;
    }


    /**
     * 通过当前时间获取昨天的时间
     * @param now
     * @return
     */
    public static Map<String,Date> getYesterdayDate(Date now) {
        if(now == null)
            return null;

        Map<String,Date> yeasterday = new HashMap<>();
        Date date = new Date();
        Calendar startCal = getDayOfBeginTime(now);
        long startTime = startCal.getTime().getTime() - ONE_DAY_TIME;
        date = new Date(startTime);
        date = getDayOfEndTime(date).getTime();
        yeasterday.put(END_DATE,date);

        Calendar endCal = getDayOfEndTime(now);
        long endtime = endCal.getTime().getTime() - ONE_DAY_TIME;
        date = new Date(endtime);
        date = getDayOfBeginTime(date).getTime();
        yeasterday.put(START_DATE,date);
        return yeasterday;
    }

    public static void main(String[] args) {
        getYesterdayDate(new Date());
    }


    /**
     * 根据当前时间获取上周开始和结束时间(周一为一周第一天)
     * @param now
     * @return
     */
    public static Map<String,Date> getLastWeek(Date now) {
        if(now == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        //获取上周时间
        calendar.set(Calendar.DAY_OF_YEAR,(dayOfYear - dayOfWeek));
        now = calendar.getTime();

        Map<String,Date> map = new HashMap<>();
        Date monday = getMonday(now);
        //获取上周一时间
        monday = getDayOfBeginTime(monday).getTime();
        Date sunday = getSunday(now);
        //获取上周天时间
        sunday = getDayOfEndTime(sunday).getTime();
        map.put(START_DATE,monday);
        map.put(END_DATE,sunday);
        return map;
    }


    /**
     * 根据当前事前计算上个月开始日期和结束日期
     * @param now   当前日期
     * @return
     */
    public static Map<String,Date> getLastMonth(Date now) {
        if(now == null)
            return null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Map<String,Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(Calendar.MONTH,month - 1);

        //这里处理跨年操作
        if(month == 0) {
            calendar.set(Calendar.YEAR,year-1);
            calendar.set(Calendar.MONTH,11);
            year = calendar.get(Calendar.YEAR);
        }
        month = calendar.get(Calendar.MONTH);
        int days = calendar.getActualMaximum(Calendar.DATE);

        //设置上个月时间
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        //得到一个月开始时间
        Date date = new Date(calendar.getTime().getTime());
        date = getDayOfBeginTime(date).getTime();
        map.put(START_DATE,date);

        //得到一个月结束时间
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,days);
        date = new Date(calendar.getTime().getTime());
        date = getDayOfEndTime(date).getTime();
        map.put(END_DATE,date);

        return map;
    }




    /**
     * 得到本周周一
     *
     * @return 时间
     */
    public static Date getMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        day_of_week = (day_of_week == 0 ? 7 : day_of_week);
        calendar.add(Calendar.DATE, -day_of_week + 1);
        return calendar.getTime();
    }

    /**
     * 得到本周周日
     *
     * @return 时间
     */
    public static Date getSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        day_of_week = (day_of_week == 0 ? 7 : day_of_week);
        calendar.add(Calendar.DATE, - day_of_week + 7);
        return calendar.getTime();
    }



    /**
     * 获取一天之中最后一秒的日期
     *
     * @param date 时间
     * @return 日期
     */
    public static Calendar getDayOfEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    /**
     * 获取一天之中开始一秒的日期
     *
     * @param date 时间
     * @return 日期
     */
    public static Calendar getDayOfBeginTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 根据开始时间结束时间计算中间天数
     * @param start     开始时间
     * @param end       结束时间
     * @return
     */
    public static int getDayNumByStartAndEnd(Date start,Date end) {
        if(start != null && "".equals(start) || end != null && "".equals(end)) {
            return 0;
        }
        long time = end.getTime() - start.getTime();
        int diff = (int) (time / DURING_DAY);
        return diff;
    }


    /**
     * 根据指定时间天数进行查询开始时间和结束时间
     * @param date          指定时间
     * @param dayNum        指定时间天数(向前天数)
     * @return
     */
    public static Map<String,Date> getStartEndTimeByDayNum(Date date,int dayNum) {
        Map<String,Date> dateMap = new HashMap<>();

        if(date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        long nowDateSec = calendar.getTime().getTime();
        long startTime = nowDateSec - (dayNum * ONE_DAY_TIME);
        System.out.println(startTime);
        System.out.println(new Date().getTime());
        System.out.println(new Date(startTime).getTime());
        Date startDate = getDayOfBeginTime(new Date(startTime)).getTime();
        Date endDate = getDayOfEndTime(date).getTime();
        dateMap.put(DateUtils.START_DATE,startDate);
        dateMap.put(DateUtils.END_DATE,endDate);
        return dateMap;
    }
}
