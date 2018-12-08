package com.colossus.common.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.*;

/**
 * 日期工具类
 * @author Tlsy
 * @version commerce 0.0.1
 * @date 2017/4/19  14:51
 */
public class DateUtil extends DateUtils {

    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    public static final FastDateFormat ISO_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
    public static final FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-ddZZ");
    public static final FastDateFormat ISO_TIME_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ss");
    public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ssZZ");
    public static final FastDateFormat ISO_TIME_NO_T_FORMAT = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = FastDateFormat.getInstance("HH:mm:ssZZ");
    public static final FastDateFormat SMTP_DATETIME_FORMAT;


    public static String formatUTC(long millis, String pattern) {
        return format((Date)(new Date(millis)), pattern, UTC_TIME_ZONE, (Locale)null);
    }

    public static String formatUTC(Date date, String pattern) {
        return format((Date)date, pattern, UTC_TIME_ZONE, (Locale)null);
    }

    public static String formatUTC(long millis, String pattern, Locale locale) {
        return format(new Date(millis), pattern, UTC_TIME_ZONE, locale);
    }

    public static String formatUTC(Date date, String pattern, Locale locale) {
        return format(date, pattern, UTC_TIME_ZONE, locale);
    }

    public static String format(long millis, String pattern) {
        return format((Date)(new Date(millis)), pattern, (TimeZone)null, (Locale)null);
    }

    public static String format(Date date, String pattern) {
        return format((Date)date, pattern, (TimeZone)null, (Locale)null);
    }

    public static String format(Calendar calendar, String pattern) {
        return format((Calendar)calendar, pattern, (TimeZone)null, (Locale)null);
    }

    public static String format(long millis, String pattern, TimeZone timeZone) {
        return format((Date)(new Date(millis)), pattern, timeZone, (Locale)null);
    }

    public static String format(Date date, String pattern, TimeZone timeZone) {
        return format((Date)date, pattern, timeZone, (Locale)null);
    }

    public static String format(Calendar calendar, String pattern, TimeZone timeZone) {
        return format((Calendar)calendar, pattern, timeZone, (Locale)null);
    }

    public static String format(long millis, String pattern, Locale locale) {
        return format((Date)(new Date(millis)), pattern, (TimeZone)null, locale);
    }

    public static String format(Date date, String pattern, Locale locale) {
        return format((Date)date, pattern, (TimeZone)null, locale);
    }

    public static String format(Calendar calendar, String pattern, Locale locale) {
        return format((Calendar)calendar, pattern, (TimeZone)null, locale);
    }

    public static String format(long millis, String pattern, TimeZone timeZone, Locale locale) {
        return format(new Date(millis), pattern, timeZone, locale);
    }

    public static String format(Date date, String pattern, TimeZone timeZone, Locale locale) {
        FastDateFormat df = FastDateFormat.getInstance(pattern, timeZone, locale);
        return df.format(date);
    }

    public static String format(Calendar calendar, String pattern, TimeZone timeZone, Locale locale) {
        FastDateFormat df = FastDateFormat.getInstance(pattern, timeZone, locale);
        return df.format(calendar);
    }

    static {
        SMTP_DATETIME_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
    }

    /**
     * 查询指定日期所在的周的第一天和最后一天
     *
     * @param date
     *            需要查询的指定日期
     * @return 返回map类型key：beginDate 本周第一天，endDate 本周最后一天
     */
    public static Map<String, String> getWeekDate(Date date) {
        Map<String, String> map = new HashMap<String, String>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        map.put("beginDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH));

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        map.put("endDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " 23:59:59");

        return map;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取指定当天的时间起止，终止的时间段
     *
     * @param date
     *            指定时间
     * @return 返回map类型key：beginDate 当天的起止时间，endDate 当天的最后时间（下一天的凌晨时间）
     */
    public static Map<String, String> getDateTime(Date date) {
        Map<String, String> map = new HashMap<String, String>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        map.put("beginDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        map.put("endDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH));

        return map;
    }

    /**
     * 获取指定日期所在月的第一天和最后一天
     *
     * @param date
     *            指定时间
     * @return 返回map类型key：beginDate 当天的起止时间，endDate 当天的最后时间（下一天的凌晨时间）
     */
    public static Map<String, String> getMonthDate(Date date) {
        Map<String, String> map = new HashMap<String, String>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        map.put("beginDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH));
        calendar.roll(Calendar.DATE, -1);
        map.put("endDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " 23:59:59");

        return map;
    }




    /**
     * 两个日期的差值
     *
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @return
     */
    public static int getBetweenDays(String start, String end) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(start,"yyyy-MM-dd hh-MM-ss"));
        long time1 = cal.getTimeInMillis();
        cal.setTime(parseDate(end,"yyyy-MM-dd hh-MM-ss"));
        long time2 = cal.getTimeInMillis();
        return (int) ((time2 - time1) / (1000 * 3600 * 24));
    }
    public static int getBetweenDays(Date start, Date end) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end);
        long time2 = cal.getTimeInMillis();
        return (int) ((time2 - time1) / (1000 * 3600 * 24));
    }

    /**
     * 获取一周的具体几号
     * @param firstDay 一周的第一天
     * @return
     */
    public static List<Integer> getWeekDays(String firstDay) throws Exception{
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(parseDate(firstDay,"yyyy-MM-dd hh-MM-ss"));
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        List<Integer> weekDays = new ArrayList<>(Arrays.asList(new Integer[7]));
        weekDays.set(0,calendar.get(Calendar.DAY_OF_MONTH));
        for(int i = 0 ; i < 6; i++){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            weekDays.set(i+1,calendar.get(Calendar.DAY_OF_MONTH));
        }
        return weekDays;
    }

}
