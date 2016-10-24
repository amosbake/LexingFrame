package com.lexing.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 时间格式工具类
 */
public class TimeUtils {

    public static final long ONE_WEEK_MILLISECONDS = 3600 * 24 * 1000 * 7;
    public static final long ONE_DAY_MILLISECONDS = 3600 * 24 * 1000;

    /**
     * 获取现在日期字符串
     *
     * @param format 格式字符串
     * @return 返回短时间字符串格式yyyy MM dd HH mm ss
     */
    public static String getStringDateShort(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    /**
     * 获取根据offset计算出的日期字符串
     * @param offset 相对当前日期的偏移天数
     * @param format 格式字符串
     * @return 返回短时间字符串格式yyyy MM dd
     */
    public static String getStringDateShort(int offset, String format) {
        Date currentTime = new Date();
        long t = currentTime.getTime();
        t += 24*3600*1000*offset;
        Date targetTime = new Date(t);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(targetTime);
    }

    /**
     * 获取时间
     *
     * @return 小时:分;秒 HH mm ss
     */
    public static String getStringTimeShort(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    /**
     * 根据指定年月日和格式获取日期字符串
     */
    public static String getDateStrByNumber(int year, int month, int day, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return formatter.format(calendar.getTime());
    }

    /**
     * 根据指定时分秒和格式获取时间字符串
     */
    public static String getTimeStrByNumber(int hour, int minute, int second, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970, 1, 1, hour, minute, second);
        return formatter.format(calendar.getTime());
    }

    /**
     * 获取日期字符的日
     * @param date yyyy-MM-dd
     * @return dd的数值
     */
    public static int getMonthFromShortString(String date){
        return Integer.parseInt(date.substring(5, 7));
    }

    /**
     * 获取日期字符的年
     * @param date yyyy-MM-dd
     * @return yyyy的数值
     */
    public static int getYearFromShortString(String date){
        return Integer.parseInt(date.substring(0, 4));
    }

    /**
     * 获取日期字符的月
     * @param date yyyy-MM-dd
     * @return MM的数值
     */
    public static int getDayFromShortString(String date){
        return Integer.parseInt(date.substring(8, 10));
    }

    /**
     * 获取周几
     * @param dateStr 日期字符串
     * @param inputFormat 格式
     * @return day的数值, , -1: 格式不合法
     */
    public static int getDayInWeek(String dateStr, String inputFormat){
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat(inputFormat);
        try {
            Date date = format.parse(dateStr);
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            //do nothing here
        }
        return -1;
    }

    /**
     * 获取周几
     * @param dateLong long型的时间毫秒
     * @return day的数值, 定义在java.util.Calendar类中
     */
    public static int getDayInWeek(long dateLong){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateLong);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取某周的第几天的日期
     * @param dateLong 周中的某一日
     * @param dayInWeek 星期，定义在java.util.Calendar类中
     * @return 日期的长整形数值
     */
    public static long getDateInWeek(long dateLong, int dayInWeek){
        if(dateLong < 0 || dayInWeek < 1 || dayInWeek > 7){
           return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateLong);
        int day = TimeUtils.getDayInWeek(dateLong);
        return dateLong + (dayInWeek - day) * ONE_DAY_MILLISECONDS;
    }

    /**
     * 获取日期字符的时
     * @param time
     * @return HH的数值
     */
    public static int getHourFromShortString(String time){
        return Integer.parseInt(time.substring(0, 2));
    }

    /**
     * 获取日期字符的分
     * @param time
     * @return mm的数值
     */
    public static int getMinuteFromShortString(String time){
        return Integer.parseInt(time.substring(3, 5));
    }

    /**
     * 获取日期字符的秒
     * @param time yyyy-MM-dd
     * @return ss的数值
     */
    public static int getSecondFromShortString(String time){
        return Integer.parseInt(time.substring(6, 8));
    }

    /**
     * 判断时间是否在给定时间段内
     * @param time  需要判断的时间
     * @param start 时间段开头
     * @param end   时间段结尾
     * @return  true-在时间段内；false-不在时间段内
     */
    public static boolean isBetween(String time, String start, String end){
        DateFormat format = new SimpleDateFormat("HH-mm-ss");
        try {
            Date tTime = format.parse(time);
            Date tStart = format.parse(time);
            Date tEnd = format.parse(time);
            if((tTime.getTime() == tStart.getTime())
                    && (tStart.getTime() == tEnd.getTime())){
                return true;
            }
            if(tTime.after(tStart) && tTime.before(tEnd))
                return true;
        } catch (ParseException e) {
            //do nothing here
        }
        return false;
    }

    /**
     * 比较字符类型的日期
     * @param dateStr1 yyyy MM dd
     * @param format1 日期格式1
     * @param dateStr2 yyyy MM dd
     * @param format2 日期格式2
     * @return >0:如果date1比date2大; <0: 如果date1比date2小; =0:如果date1等于date2
     */
    public static int compareDateString(String dateStr1, String format1, String dateStr2, String format2){
        DateFormat formater1 = new SimpleDateFormat(format1);
        DateFormat formater2 = new SimpleDateFormat(format2);
        try {
            Date date1 = formater1.parse(dateStr1);
            Date date2 = formater2.parse(dateStr2);
            return (int)(date1.getTime() - date2.getTime());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 比较字符类型的时间
     * @param timeStr1 HH mm ss
     * @param format1 时间格式1
     * @param timeStr2 HH mm ss
     * @param format2 时间格式2
     * @return >0:如果time1比time2大; <0: 如果time1比time2小; =0:如果time1等于time2
     */
    public static int compareTimeString(String timeStr1, String format1, String timeStr2, String format2){
        DateFormat formater1 = new SimpleDateFormat(format1);
        DateFormat formater2 = new SimpleDateFormat(format2);
        try {
            Date time1 = formater1.parse(timeStr1);
            Date time2 = formater2.parse(timeStr2);
            return (int)(time1.getTime() - time2.getTime());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取两个日期字符串间的间隔，单位为日
     * @param dateStr1
     * @param dateStr2
     * @return 间隔日
     */
    public static int getDaysBetween(String dateStr1, String dateStr2, String format){
        DateFormat formater = new SimpleDateFormat(format);
        try {
            Date date1 = formater.parse(dateStr1);
            Date date2 = formater.parse(dateStr2);
            return Math.abs((int)((date1.getTime() - date2.getTime())/1000/3600/24));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取两个长整形日期间的间隔，单位为日
     * @param dateLong1
     * @param dateLong2
     * @return 间隔日
     */
    public static int daysBetween(long dateLong1, long dateLong2){
        return (int)((dateLong2 - dateLong1) / ONE_DAY_MILLISECONDS);
    }

    /**
     * 获取某日期后offset日的日期
     * @param day 日期
     * @param informat 输入格式
     * @param offset 天数
     * @param outFormat 输出格式
     * @return
     */
    public static String getNextDays(String day, String informat, int offset, String outFormat){
        DateFormat formater1 = new SimpleDateFormat(informat);
        DateFormat formater2 = new SimpleDateFormat(outFormat);
        try {
            Date date = formater1.parse(day);
            Date nextDay = new Date(date.getTime() + offset * 24*3600*1000);
            return formater2.format(nextDay);
        } catch (Exception e){
            return "";
        }
    }

    /**
     * 将日期转换为"今天""明天""昨天"的表述
     * @param day 要转换的日期
     * @param inFormat 输入格式
     * @param today 今天的日期
     * @param todayFormat 今天的日期格式
     */
    public static String parseDate2Day(String day, String inFormat, String today, String todayFormat){
        DateFormat formater1 = new SimpleDateFormat(inFormat);
        DateFormat formater2 = new SimpleDateFormat(todayFormat);
        try {
            Date date = formater1.parse(day);
            Date todayDate = formater2.parse(today);
            long timeBetween = date.getTime() - todayDate.getTime();
            int dayBetween = (int)timeBetween / 24*3600*1000;
            switch (dayBetween) {
                case 0:
                    return "今天";
                case 1:
                    return "明天";
                case -1:
                    return "昨天";
                default:
                    return day;
            }
        } catch (Exception e){
            return "";
        }
    }

    /**
     * 将日期转换为"今天""明天""昨天"的表述
     * @param day 要转换的日期
     * @param inFormat 输入格式
     * @param todayLong 今天的日期
     */
    public static String parseDate2Day(String day, String inFormat, long todayLong){
        DateFormat formater1 = new SimpleDateFormat(inFormat);
        try {
            Date date = formater1.parse(day);
            Date todayDate = new Date(todayLong);
            long timeBetween = date.getTime() - todayDate.getTime();
            int dayBetween = (int)timeBetween / 24*3600*1000;
            switch (dayBetween) {
                case 0:
                    return "今天";
                case 1:
                    return "明天";
                case -1:
                    return "昨天";
                default:
                    return day;
            }
        } catch (Exception e){
            return "";
        }
    }

    /**
     * 将日期转换为"今天""明天""昨天"的表述
     * @param day 要转换的日期
     * @param inFormat 输入格式
     * @param outFormat 输出格式
     * @param todayLong 今天的日期
     */
    public static String parseDate2Day(String day, String inFormat, String outFormat, long todayLong){
        DateFormat formater1 = new SimpleDateFormat(inFormat);
        DateFormat formaterOut = new SimpleDateFormat(outFormat);
        try {
            Date date = formater1.parse(day);
            Date todayDate = new Date(todayLong);
            long timeBetween = date.getTime() - todayDate.getTime();
            int dayBetween = (int)timeBetween / 24*3600*1000;
            switch (dayBetween) {
                case 0:
                    return "今天";
                case 1:
                    return "明天";
                case -1:
                    return "昨天";
                default:
                    return formaterOut.format(date);
            }
        } catch (Exception e){
            return "";
        }
    }

    /**
     * 将日期转化为制定格式
     * @param dateStr 日期字符串
     * @param formatIn 输入格式
     * @param formatOut 输出格式
     */
    public static String parseDateToFormat(String dateStr, String formatIn, String formatOut){
        DateFormat formater1 = new SimpleDateFormat(formatIn);
        DateFormat formater2 = new SimpleDateFormat(formatOut);
        try {
            Date date = formater1.parse(dateStr);
            return formater2.format(date);
        } catch (Exception e){
            return "";
        }
    }

    /**
     * 获得今天最后一秒的时刻
     * @param time 当前时间
     */
    public static Date getEndOfDay(Date time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 59, 59);
        return calendar.getTime();
    }

    /**
     * 将时间字符串转换为long型的毫秒数值
     * @param timeStr 时间
     * @param format 格式
     */
    public static long timeString2Long(String timeStr, String format){
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(timeStr);
            return date.getTime();
        } catch (Exception e) {
            //do nothing here
        }
        return 0;
    }

    /**
     * 将date转换为时间字符串
     * @param date 时间
     * @param format 格式
     */
    public static String date2String(Date date, String format){
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.format(date);
        } catch (Exception e) {
            //do nothing here
        }
        return "";
    }

    /**
     * 将long型的毫秒数值转换为时间字符串
     * @param timeLong 时间
     * @param format 格式
     */
    public static String timeLong2String(long timeLong, String format){
        Date date = new Date(timeLong);
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.format(date);
        } catch (Exception e) {
            //do nothing here
        }
        return "";
    }


    /**
     * 获取一年中的最后一周的周数
     * @param year 年份
     */
    public static boolean isLastWeekOfYear(int year, int week){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.DECEMBER, 31);
        return calendar.get(Calendar.WEEK_OF_YEAR) == week;
    }

    /**
     * 获取一年中的最后一周的周数
     * @param year 年份
     */
    public static boolean isFirstWeekOfYear(int year, int week){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1);
        return calendar.get(Calendar.WEEK_OF_YEAR) == week;
    }

/* 下面的方法用来在登录时同步服务器时间 */

    /**
     * 根据登录时间推算出当前服务器时间
     * @param serverLoginTime 登录时的服务器时间
     * @param localLoginTime 登陆时的本地时间
     */
    public static long getServerCurrentTime(long serverLoginTime, long localLoginTime){
        return serverLoginTime + new Date().getTime() - localLoginTime;
    }

    /**
     * 计算日期之间相差多少周。支持跨年
     * @return 日期之间相差的周数， 如后一个时间早于前一个时间，返回结果为负
     */
    public static int weeksBetween(long date, long dateCompared){
        int sign = 1;
        if(date > dateCompared){
            sign = -1;
            long temp = date;
            date = dateCompared;
            dateCompared = temp;
        }

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(new Date(date));
        end.setTime(new Date(dateCompared));

        int sumSunday = 0;
        while(start.compareTo(end) <= 0) {
            int w = start.get(Calendar.DAY_OF_WEEK);
            if(w == Calendar.SUNDAY)
                sumSunday ++;

//循环，每次天数加1
            start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);
        }
        return sumSunday * sign;
    }

    /**
     * 判断时间是否是白天
     */
    public static boolean isDayTime(String time, String format){
        DateFormat formatter = new SimpleDateFormat(format);
        try{
            Date date = formatter.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.HOUR) > 6 && calendar.get(Calendar.HOUR) < 19;
        } catch (Exception e){
            //do nothing here
        }
        return true;
    }

    /**
     * 判断时间是否是白天
     */
    public static boolean isDayTime(long time){
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar.get(Calendar.HOUR_OF_DAY) >= 6 && calendar.get(Calendar.HOUR_OF_DAY) < 19;
        } catch (Exception e){
            //do nothing here
        }
        return true;
    }

    public static boolean isSameWorkWeek(long date1, long date2){
        return isSameNaturalWeek(date1 - 24 * 3600 * 1000, date2 - 24 * 3600 * 1000);
    }

    /**
     * 判断是否在同一周
     */
    public static boolean isSameNaturalWeek(long date1, long date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int subYear = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
        if(subYear == 0 && calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR)){
            return true;
        }
        else if(subYear==1 && calendar2.get(Calendar.MONTH)==11) {
            if(calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        //例子:cal1是"2004-12-31"，cal2是"2005-1-1"
        else if(subYear==-1 && calendar1.get(Calendar.MONTH)==11) {
            if(calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    /**
     * 判断是否在同一天
     */
    public static boolean isSameDay(long date1, long date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断是否在同一天
     */
    public static boolean isSameDay(String dateStr1, String format1, String dateStr2, String format2){
        DateFormat formatter1 = new SimpleDateFormat(format1);
        DateFormat formatter2 = new SimpleDateFormat(format2);
        try{
            Date date1 = formatter1.parse(dateStr1);
            Date date2 = formatter2.parse(dateStr2);
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTime(date1);
            calendar2.setTime(date2);

            return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR) && calendar1.get(Calendar.DAY_OF_WEEK) == calendar2.get(Calendar.DAY_OF_WEEK);
        } catch (Exception e){
            return false;
        }
    }

    public static final String Monday = "星期一";
    public static final String Tuesday = "星期一";
    public static final String Wednesday = "星期三";
    public static final String Thursday = "星期四";
    public static final String Friday = "星期五";
    public static final String Saturday = "星期六";
    public static final String Sunday = "星期天";

    /**
     * 获取星期几的文本
     */
    public static String getDayStr(long time){
        if(time >= 0){
            int day = getDayInWeek(time);
            switch (day){
                case Calendar.MONDAY:
                    return Monday;
                case Calendar.TUESDAY:
                    return Tuesday;
                case Calendar.WEDNESDAY:
                    return Wednesday;
                case Calendar.THURSDAY:
                    return Thursday;
                case Calendar.FRIDAY:
                    return Friday;
                case Calendar.SATURDAY:
                    return Saturday;
                case Calendar.SUNDAY:
                    return Sunday;
            }
        }
        return null;
    }

    private static HashMap<String, Long> timingMap = new HashMap<>();

    /**
     * 计时开始工具方法
     * @param tag 计时标志
     */
    public static void timingStart(String tag){
        if(timingMap.containsKey(tag)){
            timingMap.remove(tag);
        }
        long timing = System.currentTimeMillis();
        timingMap.put(tag, timing);
    }

    /**
     * 计时结束工具方法
     * @param tag 计时标志
     */
    public static void timingEnd(final String tag){
        if(!timingMap.containsKey(tag)){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.d(tag + "Timing end", "tag not exist: " + tag);
                }
            });
            return;
        }
        long time = System.currentTimeMillis() - timingMap.get(tag);
        final StringBuilder sb = new StringBuilder();
        if(time > 1000 * 60 * 60){
            sb.append(time/1000/60/60).append("h");
        }
        time = time % (1000 * 60 * 60);
        if(time > 1000 * 60){
            sb.append(time/1000/60).append("m");
        }
        time = time % (1000 * 60);
        if(time > 1000){
            sb.append(time/1000).append("s");
        }
        time = time % (1000);
        if(time >= 0){
            sb.append(time).append("ms");
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(tag + " Timing end", "timecost: " + sb.toString());
            }
        });
        timingMap.remove(tag);
    }
}
