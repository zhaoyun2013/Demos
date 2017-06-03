/**************************************************************************/
/*                                                                        */
/* Copyright (c) 2012-2013 Jiangsu Hongxin System Integration Co., Ltd.   */
/*                                                                        */
/* PROPRIETARY RIGHTS of Jiangsu Hongxin are involved in the 　　　　　　 */
/* subject matter of this material.  All manufacturing, reproduction, use,*/
/* and sales rights pertaining to this subject matter are governed by the */
/* license agreement. The recipient of this software implicitly accepts   */
/* the terms of the license.                                              */
/* 本软件文档资料是江苏鸿信公司的资产,任何人士阅读和使用本资料必须获得   					  */
/* 相应的书面授权,承担保密责任和接受相应的法律约束.                  				  */
/*                                                                        */
/**************************************************************************/

package com.zhaoyun.mymvp.utils;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期时间处理工具类， 封装了一些常用的时间日期处理方法
 * 
 */
public class DateUtils {

	/** 日期格式：yyyy-MM-dd */
	public static final String YYYYMMDD = "yyyy-MM-dd";

	/** 日期格式：yyyyMMddHHmmss */
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/** 日期格式：yyyy-MM-dd HH:mm 24小时制，精确到分钟 */
	public static final String YYYYMMDD_HHMM = "yyyy-MM-dd HH:mm";

	/** 日期格式：yyyy-MM-dd HH:mm:ss 24小时制，精确到秒 */
	public static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	
	/** 日期格式：MM-dd HH:mm 24小时制*/
	public static final String MMDD_HHMM = "MM-dd HH:mm";

	/**
	 * string str = string.format("%04d", 12); 结果："0012"
	 string str = string.format("%4d", 12); 结果："  12"
	 string str = string.format("%,d", 121212); 结果："121,212"
	 string str = string.format("%1$5s", "abc");结果："  abc"
	 string str = string.format("%1$tf %1$tt", new date());结果："2009-01-19 17:39:25"
	 string.format("%1$ty%1$tm%1$td%1$th%1$tm%1$ts%1$tl", new date())
	 * */

	
	/**
	 * 按指定时间格式获取当前时间字符串
	 * 
	 * @param dateFormat
	 *            时间格式字符串
	 * @return
	 */
	public static String getNowTimeStr(String dateFormat) {
		return new SimpleDateFormat(dateFormat, Locale.getDefault()).format(new Date());
	}

	/**
	 * 
	 * @param dateFormat
	 * @param when
	 * @return
	 */
	public static String getTimeStr(String dateFormat, long when) {
		return new SimpleDateFormat(dateFormat, Locale.getDefault()).format(when);
	}

	public static String getTimeStr(String newFormat, String oldFormat, String time) {
		SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.CHINA);
		SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat, Locale.CHINA);

		try {
			return newDateFormat.format(oldDateFormat.parse(time));
		} catch (Exception e) {
		}

		return "";
	}
	
	public static String getDateStr(GregorianCalendar calendar){
		String y = String.format("%04d", calendar.get(Calendar.YEAR));
		String m = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
		String d = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		return y + "-" + m + "-" + d;
	}

	public static String getDateStrWithoutDay(GregorianCalendar calendar){
		String y = String.format("%04d", calendar.get(Calendar.YEAR));
		String m = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
//		String d = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		return y + "-" + m;
	}

	public static String getFullTimeStr(GregorianCalendar calendar){
		String y = String.format("%04d", calendar.get(Calendar.YEAR));
		String m = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
		String d = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		String H = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		String M = String.format("%02d", calendar.get(Calendar.MINUTE));
		String S = String.format("%02d", calendar.get(Calendar.SECOND));
		return y + "-" + m + "-" + d + " " + H + ":" + M + ":" + S;
	}

	public static String getFullTimeStrWithoutSec(GregorianCalendar calendar){
		String y = String.format("%04d", calendar.get(Calendar.YEAR));
		String m = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
		String d = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		String H = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		String M = String.format("%02d", calendar.get(Calendar.MINUTE));
		return y + "-" + m + "-" + d + " " + H + ":" + M;
	}

	
	public static String getTimeStr(GregorianCalendar calendar){
		String H = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		String M = String.format("%02d", calendar.get(Calendar.MINUTE));
		String S = String.format("%02d", calendar.get(Calendar.SECOND));
		return H + ":" + M + ":" + S;
	}

	public static String getTimeStrWithoutSec(GregorianCalendar calendar){
		String H = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		String M = String.format("%02d", calendar.get(Calendar.MINUTE));
		return H + ":" + M;
	}

	public static String getTimeStrForFileName(GregorianCalendar calendar){
		String y = String.format("%04d", calendar.get(Calendar.YEAR));
		String m = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
		String d = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		String H = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		String M = String.format("%02d", calendar.get(Calendar.MINUTE));
		String S = String.format("%02d", calendar.get(Calendar.SECOND));
		return y + m + d + H + M + S;
	}

	/**
	 * 获取当前时间毫秒值 <br>
	 * 
	 * @return
	 */
	public static long getNowTimeInMillis() {
		return new Date().getTime();
	}

	/**
	 * Function: 判断给定时间是否是今天 <br>
	 * 
	 * @param when
	 * @return true 是今天否则为 false
	 */
	public static boolean isToday(long when) {
		Time time = new Time();
		time.set(when);

		int thenYear = time.year;
		int thenMonth = time.month;
		int thenMonthDay = time.monthDay;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year) && (thenMonth == time.month) && (thenMonthDay == time.monthDay);
	}

	/**
	 * 
	 * 获取一个简短的时间字符串,如果传入时间是当天则返回 时：分 ，否则返回 月日，如跨年 则返回年月日<br>
	 * 
	 * @param when
	 * @return
	 */
	public static String getShortTime(long when) {
		String shortTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
		String time = sdf.format(when);

		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(when);

		if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
			if (calendar.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
				shortTime = time.substring(12);
			} else {
				shortTime = time.substring(5, 11);
			}

		} else {
			shortTime = time.substring(0, 11);
		}

		return shortTime;
	}


	/**
	 * 
	 * 获取一个简短的时间字符串,如果传入时间是当天则返回 时：分 ，否则返回 月日，如跨年 则返回年月日<br>
	 * 
	 * @param date 传入的日期
	 * @return
	 */
	public static String getShortTime(Date date) {
		return getShortTime(date.getTime());
	}

	/**
	 * 
	 *  获取一个简短的时间字符串,如果传入时间是当天则返回 时：分 ，否则返回 月日，如跨年 则返回年月日<br>
	 * 
	 * @param timeStr 时间的字符串类型表示形式 
	 * @param sdf 与timeStr对应的时间格式化对象,例:<br>(new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA))
	 * @return
	 */
	public static String getShortTime(String timeStr, SimpleDateFormat sdf) {
		try {
			return getShortTime(sdf.parse(timeStr));
		} catch (ParseException e) {
			Log.e("DateUtil", "日期字符串转换日期错误", e);
		}

		return "";
	}	
	
	
	
	
	/**
	 * 根据传入的字符串格式的时间，获取该时间与当前的时间差的文字表达方式(N天/小时前/刚刚等)
	 * 
	 * @param strTime <br>传入的字符串格式的时间， 该参数的格式必须为yyyy-MM-dd HH:mm:ss
	 * 	<br>注: 该时间必须在当前时间之前 
	 * @return 距离当前时间差的直观表达方式，如果传入的时间格式不对或者在当前时间之后，则返回空字符串
	 */
	public static String getDisplayTimeBeforeNow(String strTime){
		if(TextUtils.isEmpty(strTime)){
			return "";
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date now = new Date();
		
		int s1 = (int) (date.getTime()/1000);
		int s2 = (int) (now.getTime()/1000);
		
		if(s1 > s2){
			return "";
		}
		
		int daysOffset = (s2-s1)/(24*60*60);
		int secOffset = s2-s1;
				
		if (daysOffset > 365) {
			return (daysOffset / 365) + "年前";
		} else if (daysOffset > 30) {
			return (daysOffset / 30) + "个月前";
		} else if (daysOffset > 0) {
			return daysOffset + "天前";
		} else if (secOffset > 3600) {
			return (secOffset / 3600) + "小时前";
		} else if (secOffset > 60) {
			return (secOffset / 60) + "分钟前";
		} else {
			return "刚刚";
		}
	}
	
	/**
	 * 将字符串时间转换为秒数
	 * @param time 时间的字符串表示 格式为：yyyy-MM-dd HH:mm:ss
	 */
	public static int getSecsByStringTime(String time){
		String[] timeArray = time.split(" ");
		if(timeArray == null || timeArray.length != 2){
			return -1;
		}
		
		String[] timeArray1 = timeArray[0].split("-");
		if(timeArray1 == null || timeArray1.length != 3){
			return -1;
		}
		String[] timeArray2 = timeArray[1].split(":");
		if(timeArray2 == null || timeArray2.length != 3){
			return -1;
		}
		
		GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(timeArray1[0]), Integer.parseInt(timeArray1[1])-1, Integer.parseInt(timeArray1[2]), Integer.parseInt(timeArray2[0]), Integer.parseInt(timeArray2[1]), Integer.parseInt(timeArray2[2]));
		
		return (int) (calendar.getTimeInMillis()/1000);
		
	}
	
	public static String putOffDateWithChinese(String date){
		date=date.replace("年", "-");
		date=date.replace("月", "-");
		date=date.replace("号", "-");
		return date;
	}
	
	public static String addDateWithChinese(String date){
		String dates[]=date.split("-");
		date=dates[0]+"年"+dates[1]+"月"+dates[2]+"号";
		return date;
	}
	
	public static String getWeekDay(int day){
		String weeky="";
		switch (day) {
		case 1:
			weeky= "星期日";
			break;
		case 2:
			weeky= "星期一";
			break;
		case 3:
			weeky= "星期二";
			break;
		case 4:
			weeky= "星期三";
			break;
		case 5:
			weeky= "星期四";
			break;
		case 6:
			weeky= "星期五";
			break;
		case 7:
			weeky= "星期六";
			break;
		}
		return weeky;
	}
	
	/**
	 * 将日期转换为毫秒
	 * */
	public static String getMSecFromDate(String time){
		if(TextUtils.isEmpty(time)){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date=sdf.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(date.getTime()/1000);
	}
	
}
