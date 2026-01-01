package com.fmx.xiaomeng.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;



/**
 * Date工具类
 *
 * @author honghu
 * @date 2025-12-20
 */
public final class DateUtil {


    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_PATTERN_DS = "yyyyMMdd";

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN_DS_HHMMSS = "yyyyMMddHHmmss";

    public static final DateTimeFormatter NORMAL_DATETIME_DTF = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    public static final DateTimeFormatter NORMAL_DATE_DTF = java.time.format.DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static final DateTimeFormatter DATE_PATTERN_DS_DTF = java.time.format.DateTimeFormatter.ofPattern(DATE_PATTERN_DS);


    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YEAR = "yyyy";
    public static final String DATE_FORMAT_MONTH_DATE = "MM-dd";
    public static final String DATE_FORMAT_MONTH = "yyyy-MM";
    public static final String DATE_TIME_FORMAT_NUM = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_NUM = "yyyyMMdd";
    public static final String DATE_FORMAT_START = "yyyy-MM-dd 00:00:00";
    public static final String DATE_FORMAT_END = "yyyy-MM-dd 23:59:59";
    public static final String DATE_FORMAT_MONTH_START = "yyyy-MM-01 00:00:00";
    public static final String DATE_FORMAT_YEAR_START = "yyyy-01-01 00:00:00";
    public static final String DATE_FORMAT_YEAR_END = "yyyy-12-31 23:59:59";
    public static final String DATE_FORMAT_HHMMSS = "HH:mm:ss";
    public static final String DATE_FORMAT_START_PEREND = "00:00:00";
    public static final String DATE_FORMAT_END_PEREND = "23:59:59";
    public static final String DATE_FORMAT_HHMM = "yyyy-MM-dd HH:mm";

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDate() {
        return nowDate(DATE_FORMAT_NUM);
    }

    /**
     * 获取当前年,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) + "";
    }

    /**
     * 获取上一年,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String lastYear() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        return cal.get(Calendar.YEAR) + "";
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Date nowDateTime() {
        return strToDate(nowDateTimeStr(), DATE_FORMAT);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDateTimeStr() {
        return nowDate(DATE_FORMAT);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDate(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return dft.format(new Date());
    }

    /**
     * 获取当天零点
     *
     * @return
     */
    public static Date nowDateZeroTime() {
        return strToDate(nowDate(DATE_FORMAT_DATE), DATE_FORMAT_DATE);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDateTime(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return dft.format(new Date());
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Integer getNowTime() {
        long t = (System.currentTimeMillis() / 1000L);
        return Integer.parseInt(String.valueOf(t));
    }

    /**
     * 获取当前时间戳（秒级）
     *
     * @return
     */
    public static Long getTime() {
        return (System.currentTimeMillis() / 1000L);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Date nowDateTimeReturnDate(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return strToDate(dft.format(new Date()), DATE_FORMAT);
    }

    /**
     * convert a date to string in a specifies fromat.
     *
     * @param date
     * @param DATE_FORMAT
     * @return
     */
    public static String dateToStr(Date date, String DATE_FORMAT) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
        return myFormat.format(date);
    }

    /**
     * convert a date to string in a specifies fromat.
     *
     * @param date
     * @return
     */
    public static String dateToNormalStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
        return myFormat.format(date);
    }


    /**
     * parse a String to Date in a specifies fromat.
     *
     * @param dateStr
     * @param DATE_FORMAT
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String dateStr, String DATE_FORMAT) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return myFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * date add Second
     *
     * @param date
     * @param num
     * @return
     */
    public static Date addSecond(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, num);
        return calendar.getTime();
    }

    /**
     * date add Second return String
     *
     * @param date
     * @param num
     * @return
     */
    public static String addSecond(Date date, int num, String DATE_FORMAT) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, num);
        return dateToStr(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     */
    public static final String addDay(String newDate, int num, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date currdate = format.parse(newDate);
            Calendar ca = Calendar.getInstance();
            ca.setTime(currdate);
            ca.add(Calendar.DATE, num);
            return format.format(ca.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     */
    public static final String addDay(Date newDate, int num, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar ca = Calendar.getInstance();
        ca.setTime(newDate);
        ca.add(Calendar.DATE, num);
        return format.format(ca.getTime());
    }


    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     */
    public static final Date addDay(Date newDate, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(newDate);
        ca.add(Calendar.DATE, num);
        return ca.getTime();
    }

    /**
     * convert long to date
     *
     * @param date 待转换时间戳
     * @return 转换后时间
     */
    public static Date timeStamp11ToDate(Integer date) {
        return new Date(date);
    }

    /**
     * convert long to date string
     *
     * @param date        待转换时间戳
     * @param DATE_FORMAT 格式化时间
     * @return 格式化后的时间
     */
    public static String timeStamp11ToDate(Integer date, String DATE_FORMAT) {
        return dateToStr(new Date(date), DATE_FORMAT);
    }

    /**
     * compare two date String with a pattern
     *
     * @param date1
     * @param date2
     * @param pattern
     * @return
     */
    public static int compareDate(String date1, String date2, String pattern) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(pattern);
        try {
            Date dt1 = DATE_FORMAT.parse(date1);
            Date dt2 = DATE_FORMAT.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 检查日期格式是否合法
     *
     * @param date
     * @param style
     * @return
     */
    public static boolean checkDateFormat(String date, String style) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(style);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 计算两个时间之间的天数差
     *
     * @param beforeDay 开始时间
     * @param afterDay  结束时间
     * @return 相差天数
     */
    public static long getTwoDateDays(Date beforeDay, Date afterDay) {
        SimpleDateFormat sm = new SimpleDateFormat(DATE_FORMAT_NUM);
        long days = -1;
        try {
            days = (sm.parse(sm.format(afterDay)).getTime() - sm.parse(sm.format(beforeDay)).getTime()) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }


    //获取时间戳11位
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.parseInt(timestamp);
    }

    //获取时间戳11位
    public static int getSecondTimestamp(String date) {
        if (null == date) {
            return 0;
        }
        Date date1 = strToDate(date, DATE_FORMAT);
        if (date1 == null) {
            return 0;
        }
        String timestamp = String.valueOf(date1.getTime() / 1000);
        return Integer.parseInt(timestamp);
    }

    //获取时间戳10位
    public static int getSecondTimestamp(Long timeMillis) {
        if (null == timeMillis) {
            return 0;
        }
        String timestamp = String.valueOf(timeMillis / 1000);
        return Integer.parseInt(timestamp);
    }

    //获取时间戳11位
    public static int getSecondTimestamp() {
        Date date = strToDate(nowDateTime(DATE_FORMAT), DATE_FORMAT);
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.parseInt(timestamp);
    }

    /**
     * 获得昨天日期:yyyy-MM-dd  HH:mm:ss
     */
    public static String getYesterdayStr() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return startSdf.format(c.getTime());
    }

    /**
     * 获得本周第一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getWeekStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_MONTH, 0);
        c.set(Calendar.DAY_OF_WEEK, 2);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return startSdf.format(c.getTime());
    }

    /**
     * 获得本周最后一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getWeekEndDay() {
        return addDay(getWeekStartDay(), 7, DATE_FORMAT);
    }

    /**
     * 获得上周第一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getLastWeekStartDay() {
        return addDay(getWeekStartDay(), -7, DATE_FORMAT);
    }

    /**
     * 获得上周最后一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getLastWeekEndDay() {
        return addDay(getLastWeekStartDay(), 7, DATE_FORMAT);
    }

    /**
     * 获得本月最后一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getMonthEndDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return endSdf.format(c.getTime());
    }

    /**
     * 获得上月第一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getLastMonthStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-01 00:00:00");
        return startSdf.format(c.getTime());
    }

    /**
     * 获得上月最后一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getLastMonthEndDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return endSdf.format(c.getTime());
    }

    /**
     * 获得上年第一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getLastYearStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-01-01 00:00:00");
        return startSdf.format(c.getTime());
    }

    /**
     * 获得上年最后一天:yyyy-MM-dd HH:mm:ss
     */
    public static String getLastYearEndDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-12-31 23:59:59");
        return endSdf.format(c.getTime());
    }


    /**
     * 两个日期之前的相差天数
     *
     * @param starDate 开始日期
     * @param endDate  结束日期
     * @return 相差天数
     */
    public static int daysBetween(Date starDate, Date endDate) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(starDate);

        long time1 = cal.getTimeInMillis();

        cal.setTime(endDate);

        long time2 = cal.getTimeInMillis();

        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));

    }

    /**
     * 获取指定日期指定格式字符串
     *
     * @param dateStr
     * @param DATE_FORMAT
     * @return
     * @throws ParseException
     */
    public static String appointedDayStrToFormatStr(String dateStr, String STR_DATE_FORMAT, String DATE_FORMAT) {
        Date date = DateUtil.strToDate(dateStr, STR_DATE_FORMAT);
        return DateUtil.dateToStr(date, DATE_FORMAT);
    }

    /**
     * 获取当前时间小时
     *
     * @return 当前时间小时 默认24小时
     */
    public static int getCurrentHour() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    /**
     * 13位时间戳转字符串
     *
     * @param timestamp
     * @param dateFormat
     * @return
     */
    public static String timestamp2DateStr(Long timestamp, String dateFormat) {
        if (Objects.isNull(timestamp)) {
            return "";
        }
        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = DATE_FORMAT;
        }
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }


    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_FORMAT_DATE);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 根据周数，获取开始日期、结束日期
     *
     * @param week 周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return 返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        org.joda.time.LocalDate date = new org.joda.time.LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date    日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date    日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date  日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date  日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }


    /**
     * 对日期的【月】进行加/减
     *
     * @param date   日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static String addDateMonths(Date date, int months) {
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateTime dateTime = new DateTime(date);
        Date date1 = dateTime.plusMonths(months).toDate();
        return simpleDateFormat.format(date1);
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date  日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    public static String zeroize(int num) {
        return (num < 10 ? "0" : "") + num;
    }

    public static String timeStamp2ReadableTimeOld(long timestamp) {

        // 转成秒时间戳
        timestamp = timestamp / 1000;

        long curTimestamp = new Date().getTime() / 1000; //当前时间戳
        long timestampDiff = curTimestamp - timestamp; // 参数时间戳与当前时间戳相差秒数

        Date curDate = new Date(curTimestamp * 1000); // 当前时间日期对象
        Date tmDate = new Date(timestamp * 1000);  // 参数时间戳转换成的日期对象

        int Y = tmDate.getYear(), m = tmDate.getMonth() + 1, d = tmDate.getDate();
        int H = tmDate.getHours(), i = tmDate.getMinutes(), s = tmDate.getSeconds();

        if (timestampDiff < 60) { // 一分钟以内
            return "刚刚";
        } else if (timestampDiff < 3600) { // 一小时前之内
            return Math.floor(timestampDiff / 60) + "分钟前";
        } else if (curDate.getYear() == Y && curDate.getMonth() + 1 == m && curDate.getDate() == d) {
            return "今天" + zeroize(H) + ':' + zeroize(i);
        } else {
            Date newDate = new Date((curTimestamp - 86400) * 1000); // 参数中的时间戳加一天转换成的日期对象
            if (newDate.getYear() == Y && newDate.getMonth() + 1 == m && newDate.getDate() == d) {
                return "昨天" + zeroize(H) + ':' + zeroize(i);
            } else if (curDate.getYear() == Y) {
                return zeroize(m) + "月" + zeroize(d) + "日" + zeroize(H) + ":" + zeroize(i);
            } else {
                return Y + "年" + zeroize(m) + "月" + zeroize(d) + "日" + zeroize(H) + ":" + zeroize(i);
            }
        }
    }

    public static String timeStamp2ReadableTime(long timestamp) {
        // 假设这是一个UNIX时间戳（以毫秒为单位）
        long curTimestamp = System.currentTimeMillis(); // 当前时间的时间戳

        // 将时间戳转换为Instant对象
        //Instant instant = Instant.ofEpochSecond(timestamp);  //秒时间戳
        Instant instant = Instant.ofEpochMilli(timestamp); //毫秒时间戳
        // 将Instant转换为特定时区的ZonedDateTime对象，默认使用系统时区
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        // 从ZonedDateTime对象获取年份
        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonthValue();
        int day = zonedDateTime.getDayOfMonth();
        int hour = zonedDateTime.getHour();
        int minute = zonedDateTime.getMinute();
        int second = zonedDateTime.getSecond();

        //获取当前年份
        Instant curInstant = Instant.ofEpochMilli(curTimestamp); //毫秒时间戳
        // 将Instant转换为特定时区的ZonedDateTime对象，默认使用系统时区
        ZonedDateTime curZonedDateTime = curInstant.atZone(ZoneId.systemDefault());
        // 从ZonedDateTime对象获取年份
        int nowYear = curZonedDateTime.getYear();

        long timestampDiff = (curTimestamp - timestamp) / 1000; // 参数时间戳与当前时间戳相差秒数
        if (timestampDiff < 10) { // 十秒以内
            return "刚刚";
        } else if (timestampDiff < 60) {
            return timestampDiff + "秒前";
        } else if (timestampDiff < 3600) { // 一小时前之内
            //return Math.floor((int)(timestampDiff / 60)) + "分钟前";
            return (timestampDiff / 60) + "分钟前";
        } else if (timestampDiff < 86400) {
            //return Math.floor((int)(timestampDiff / 3600)) + "小时前";
            return (timestampDiff / 3600) + "小时前";
        } else if (timestampDiff < 604800) { //86400*7
            Date newDate = new Date((curTimestamp - 86400) * 1000); // 参数中的时间戳加一天转换成的日期对象
            if (timestampDiff < 172800) {//86400*2
                return "昨天";
            } else if (timestampDiff < 259200) { //86400*3
                return "前天";
            } else {
                return timestampDiff / 86400 + "天前";
            }
        } else if (nowYear == year) {
            return zeroize(month) + "-" + zeroize(day);

        } else {
            return year + "/" + zeroize(month) + "/" + zeroize(day);

            // 1.1看去年12月份的帖子，也是几天前，所以也用月份就行，年份就不管了
//            return zeroize(month) + "-" + zeroize(day);
        }
    }

    public static void main(String[] args) {
        System.out.println(timeStamp2ReadableTime(1703162408000L)); //2023-12-21 20:40:08
        System.out.println(timeStamp2ReadableTime(1703151608000L)); //2023-12-21 17:40:08
        System.out.println(timeStamp2ReadableTime(1703147680000L)); //2023-12-21 16:34:40
        System.out.println(timeStamp2ReadableTime(1703140480000L));//2023-12-21 14:34:40
        System.out.println(timeStamp2ReadableTime(1703062480000L));//2023-12-20 16:54:40
        System.out.println(timeStamp2ReadableTime(1703044480000L));//2023-12-20 11:54:40
        System.out.println(timeStamp2ReadableTime(1702958080000L));//2023-12-19 11:54:40
        System.out.println(timeStamp2ReadableTime(1702871680000L));//2023-12-18 11:54:40
        System.out.println(timeStamp2ReadableTime(1687060480000L));//2023-06-18 11:54:40
        System.out.println(timeStamp2ReadableTime(1670212480000L));//2022-12-05 11:54:40

    }



    public static int calculateWeekNum(LocalDate date, String termStartDate) {
        java.time.LocalDate startDate = java.time.LocalDate.parse(termStartDate, java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        long daysBetween = ChronoUnit.DAYS.between(startDate, date);
        return (int) Math.ceil(daysBetween / 7.0); //向上取整
    }




    /**
     * LocalDateTime 转 Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * Date 转 LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime parse
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(dt -> LocalDateTime.parse(dt, NORMAL_DATETIME_DTF))
                .orElse(null);
    }

    /**
     * LocalDate parse
     *
     * @param date
     * @return
     */
    public static LocalDate parseLocalDate(String date) {
        return Optional.ofNullable(date)
                .map(dt -> LocalDate.parse(dt, NORMAL_DATE_DTF))
                .orElse(null);
    }

    /**
     * ds parse
     *
     * @param ds
     * @return
     */
    public static LocalDate parseDs(String ds) {
        return Optional.ofNullable(ds)
                .map(dt -> LocalDate.parse(dt, DATE_PATTERN_DS_DTF))
                .orElse(null);
    }
}
