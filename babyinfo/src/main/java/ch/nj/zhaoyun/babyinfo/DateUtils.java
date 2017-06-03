package ch.nj.zhaoyun.babyinfo;



import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/19.
 */

public class DateUtils {
    public static final int YEAR_RETURN = 0;

    public static final int MONTH_RETURN = 1;

    public static final int DAY_RETURN = 2;

    public static final int HOUR_RETURN= 3;

    public static final int MINUTE_RETURN = 4;

    public static final int SECOND_RETURN = 5;


    public static final String YYYY = "yyyy";

    public static final String YYYYMM = "yyyy-MM";

    public static final String YYYYMMDD = "yyyy-MM-dd";

    public static final String YYYYMMDDHH= "yyyy-MM-dd HH";

    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";

    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";


    public static long getBetween(String beginTime, String endTime, String formatPattern, int returnPattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        Date beginDate = simpleDateFormat.parse(beginTime);
        Date endDate = simpleDateFormat.parse(endTime);

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);
        switch (returnPattern) {
            case YEAR_RETURN:
                return DateUtils.getByField(beginCalendar, endCalendar, Calendar.YEAR);
            case MONTH_RETURN:
                return DateUtils.getByField(beginCalendar, endCalendar, Calendar.YEAR)*12 + DateUtils.getByField(beginCalendar, endCalendar, Calendar.MONTH);
            case DAY_RETURN:
                return DateUtils.getTime(beginDate, endDate)/(24*60*60*1000);
            case HOUR_RETURN:
                return DateUtils.getTime(beginDate, endDate)/(60*60*1000);
            case MINUTE_RETURN:
                return DateUtils.getTime(beginDate, endDate)/(60*1000);
            case SECOND_RETURN:
                return DateUtils.getTime(beginDate, endDate)/1000;
            default:
                return 0;
        }
    }

    private static long getByField(Calendar beginCalendar, Calendar endCalendar, int calendarField){
        return endCalendar.get(calendarField) - beginCalendar.get(calendarField);
    }

    private static long getTime(Date beginDate, Date endDate){
        return endDate.getTime() - beginDate.getTime();
    }

    /**
     * 两个时间之间相差距离多少天
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2) throws Exception{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days=0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(String str1, String str2,Context context) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day + context.getResources().getString(R.string.days)
                + hour +  context.getResources().getString(R.string.hours)
                + min + context.getResources().getString(R.string.minutes)
                + sec +  context.getResources().getString(R.string.seconds);
    }

    public static int[] getYMDHMS(String str) {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;
        try {
            String[] tmp = str.split(" ");
            String[] date = tmp[0].split("-");
            year = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]);
            day = Integer.parseInt(date[2]);
            String[] time = tmp[1].split(":");
            hour = Integer.parseInt(time[0]);
            min = Integer.parseInt(time[1]);
            sec = Integer.parseInt(time[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] times = {year,month,day, hour, min, sec};
        return times;
    }
    /**
     * 算星座
     * @param month
     * @param day
     * @return
     */
    public static String getConstellation(int month, int day,Context context) {
        String star = "";
        if (month == 1 && day >= 20 || month == 2 && day <= 18) {
            star = context.getResources().getString(R.string.Aquarius);
        }
        if (month == 2 && day >= 19 || month == 3 && day <= 20) {
            star = context.getResources().getString(R.string.Pisces);
//            star = "双鱼座";
        }
        if (month == 3 && day >= 21 || month == 4 && day <= 19) {
            star = context.getResources().getString(R.string.Aries);
//            star = "白羊座";
        }
        if (month == 4 && day >= 20 || month == 5 && day <= 20) {
            star = context.getResources().getString(R.string.Taurus);
//            star = "金牛座";
        }
        if (month == 5 && day >= 21 || month == 6 && day <= 21) {
            star = context.getResources().getString(R.string.Gemini);
//            star = "双子座";
        }
        if (month == 6 && day >= 22 || month == 7 && day <= 22) {
            star = context.getResources().getString(R.string.Cancer);
//            star = "巨蟹座";
        }
        if (month == 7 && day >= 23 || month == 8 && day <= 22) {
            star = context.getResources().getString(R.string.Leo);
//            star = "狮子座";
        }
        if (month == 8 && day >= 23 || month == 9 && day <= 22) {
            star = context.getResources().getString(R.string.Virgo);
//            star = "处女座";
        }
        if (month == 9 && day >= 23 || month == 10 && day <= 22) {
            star = context.getResources().getString(R.string.Libra);
//            star = "天秤座";
        }
        if (month == 10 && day >= 23 || month == 11 && day <= 21) {
            star = context.getResources().getString(R.string.Scorpio);
//            star = "天蝎座";
        }
        if (month == 11 && day >= 22 || month == 12 && day <= 21) {
            star = context.getResources().getString(R.string.Sagittarius);
//            star = "射手座";
        }
        if (month == 12 && day >= 22 || month == 1 && day <= 19) {
            star = context.getResources().getString(R.string.Capricorn);
//            star = "摩羯座";
        }
        return star;
    }

    public static void main(String[] args) {
        try {
            System.out.println(DateUtils.getBetween("2013-05-02", "2013-05-05", DateUtils.YYYYMMDD, DateUtils.DAY_RETURN));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
