package com.pkit.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateConvertUtils {

    private static Log log = LogFactory.getLog(DateConvertUtils.class.getName());


    /**缺省年格式*/
    public static final String DEFAULT_YEAR = "YEAR";

    /**缺省月格式*/
    public static final String DEFAULT_MONTH = "MONTH";

    /**缺省日格式*/
    public static final String DEFAULT_DATE = "DAY";

    /**缺省小时格式*/
    public static final String DEFAULT_HOUR = "HOUR";

    /**缺省分钟格式*/
    public static final String DEFAULT_MINUTE = "MINUTE";

    /**缺省秒格式*/
    public static final String DEFAULT_SECOND = "SECOND";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

    /**
     *  格式化日期时间
     * @param date 时间
     * @param f 格式
     * @return
     */
    public static String format(Date date, String f) {
        String re = "";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(f);
            re = sdf.format(date);
        }
        return re;
    }

    /**
     * 格式化日期时间
     * @param v 时间字符串
     * @param f 格式
     * @return
     * @throws java.text.ParseException
     */
    public static Date parse(String v, String f) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(f);
        return sdf.parse(v);
    }



    //返回星期几所在日期
    public static String getWeek(String sdate, String num) {
        // 再转换为时间
        Date dd = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(dd);
        if (num.equals("1")) // 返回星期一所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        else if (num.equals("2")) // 返回星期二所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        else if (num.equals("3")) // 返回星期三所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        else if (num.equals("4")) // 返回星期四所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        else if (num.equals("5")) // 返回星期五所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        else if (num.equals("6")) // 返回星期六所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        else if (num.equals("0")) // 返回星期日所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    //获取当前日期
    public static String getNowDateString(String fomatStr){
        Date now = new Date();
        SimpleDateFormat curday = new SimpleDateFormat(fomatStr);
        String date = curday.format(now);
        return date;
    }
    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     *判断原日期是否在目标日期之前
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }
    /**
     *判断原日期是否在目标日期之后
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    /**
     *判断两日期是否相同
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * 判断某个日期是否在某个日期范围
     *
     * @param beginDate
     *            日期范围开始
     * @param endDate
     *            日期范围结束
     * @param src
     *            需要判断的日期
     * @return
     */
    public static boolean between(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }

    /**
     * 获得当前月的最后一天
     * <p>
     * HH:mm:ss为0，毫秒为999
     *
     * @return
     */
    public static Date lastDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// 月份+1
        cal.set(Calendar.MILLISECOND, -1);// 毫秒-1
        return cal.getTime();
    }

    /**
     * 获得当前月的第一天
     * <p>
     * HH:mm:ss SS为零
     *
     * @return
     */
    public static Date firstDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTime();
    }


    /**
     * 两个日期做减法，返回相差天数
     * @throws java.text.ParseException
     * @throws java.text.ParseException
     */
    public static long datesub(Date date1, Date date2) throws ParseException {
        long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime() - date2.getTime() : date2.getTime() - date1.getTime();
        // 日期相减得到相差的日期
        long day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) : (date2
                .getTime() - date1.getTime())
                / (24 * 60 * 60 * 1000);
        return day + 1;
    }

    /**
     * 取得相对于当前时间增加天数/月数/年数后的日期
     * <br>
     * 欲取得当前日期5天前的日期,可做如下调用:<br>
     *        getAddDay("DATE", -5).
     * @param field,段,如"year","month","date",对大小写不敏感
     * @param amount,增加的数量(减少用负数表示),如5,-1
     * @return    格式化后的字符串 如"2010-05-28"
     * @throws java.text.ParseException
     **/

    public static String getAddDay(String field, int amount) throws ParseException {
        return getAddDay(field, amount, null);
    }

    /**
     * 取得相对于当前时间增加天数/月数/年数后的日期,按指定格式输出
     *
     * 欲取得当前日期5天前的日期,可做如下调用:<br>
     *        getAddDay("DATE", -5,'yyyy-mm-dd hh:mm').
     * @param field,段,如"year","month","date",对大小写不敏感
     * @param amount,增加的数量(减少用负数表示),如5,-1
     * @param strFormat,输出格式,如"yyyy-mm-dd","yyyy-mm-dd hh:mm"
     * @return 格式化后的字符串 如"2010-05-28"
     * @throws java.text.ParseException
     **/
    public static String getAddDay(String field, int amount, String strFormat) throws ParseException {
        return getAddDay(null, field, amount, strFormat);
    }

    /**
     * 功能：对于给定的时间增加天数/月数/年数后的日期,按指定格式输出
     *
     * @param date String 要改变的日期
     * @param field int 日期改变的字段，YEAR,MONTH,DAY
     * @param amount int 改变量
     * @param strFormat 日期返回格式
     * @return
     * @throws java.text.ParseException
     */
    public static String getAddDay(String date, String field, int amount, String strFormat) throws ParseException{
        if(strFormat == null){
            strFormat = DATE_TIME_FORMAT;
        }
        Calendar rightNow = Calendar.getInstance();
        if(date != null && !"".equals(date.trim())){
            rightNow.setTime(parseDate(date, strFormat));
        }
        if(field == null){
            return format(rightNow.getTime(), strFormat);
        }
        rightNow.add(getInterval(field), amount);
        return  format(rightNow.getTime(), strFormat);
    }


    /**
     * 获取时间间隔类型
     * @param field 时间间隔类型
     * @return 日历的时间间隔
     */
    protected static int getInterval(String field){
        String tmpField = field.toUpperCase();
        if (tmpField.equals(DEFAULT_YEAR)){
            return Calendar.YEAR;
        }else if (tmpField.equals(DEFAULT_MONTH)){
            return Calendar.MONTH;
        }else if (tmpField.equals(DEFAULT_DATE)){
            return Calendar.DATE;
        }else if(DEFAULT_HOUR.equals(tmpField)){
            return Calendar.HOUR;
        }else if(DEFAULT_MINUTE.equals(tmpField)){
            return Calendar.MINUTE;
        }else{
            return Calendar.SECOND;
        }
    }

    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }

    public static String getWeekStr(String sdate) {
        String str = "";
        str = getWeek(sdate);
        if ("1".equals(str)) {
            str = "周日";
        } else if ("2".equals(str)) {
            str = "周一";
        } else if ("3".equals(str)) {
            str = "周二";
        } else if ("4".equals(str)) {
            str = "周三";
        } else if ("5".equals(str)) {
            str = "周四";
        } else if ("6".equals(str)) {
            str = "周五";
        } else if ("7".equals(str)) {
            str = "周六";
        }
        return str;
    }

    public static String getWeekStr1(String sdate) {
        String str = "";
        str = getWeek(sdate);
        if ("星期日".equals(str)) {
            str = "周日";
        } else if ("星期一".equals(str)) {
            str = "周一";
        } else if ("星期二".equals(str)) {
            str = "周二";
        } else if ("星期三".equals(str)) {
            str = "周三";
        } else if ("星期四".equals(str)) {
            str = "周四";
        } else if ("星期五".equals(str)) {
            str = "周五";
        } else if ("星期六".equals(str)) {
            str = "周六";
        }
        return str;
    }


    public static Date parseDate(String str,String format){
        try{
            SimpleDateFormat sdf=new SimpleDateFormat(format);
            return sdf.parse(str);
        }
        catch(Exception e){throw new RuntimeException();}
    }

    public static String convertDate(Date date,String format){
        try{
            SimpleDateFormat sdf=new SimpleDateFormat(format);
            return sdf.format(date);
        }
        catch(Exception e){throw new RuntimeException();}
    }



    public static String getFormatDateTimeFromUnixTime(String str, String format) {
        Integer tmpunixtime = 0;
        try {
            if ((format == null) || (format.equals(""))) format = DATE_TIME_FORMAT;
            SimpleDateFormat tmpfmt = new SimpleDateFormat(format);
            Date tmpdate = tmpfmt.parse(str);

            tmpunixtime = Math.round(tmpdate.getTime() / 1000);
        } catch (Exception ex) {

        }
        return DataType.getString(tmpunixtime);
    }

    public static String getFormatDateTimeFromUnixTime(String strunixtime) {
        String tmpdatestr = "";
        try {
            Integer unixtime = DataType.getInteger(strunixtime);
            if ((unixtime != null) && (unixtime != 0)) {
                Timestamp tmpdate = new Timestamp(unixtime * 1000L);
                SimpleDateFormat tmpfmt = new SimpleDateFormat(DATE_TIME_FORMAT);
                tmpdatestr = tmpfmt.format(tmpdate);
            }
        } catch (Exception ex) {

        }
        return tmpdatestr;
    }

    public static String getFormatDateTimeFromUnixTime(Integer unixtime) {
        String tmpdatestr = "";
        try {
            if ((unixtime != null) && (unixtime != 0)) {
                Timestamp tmpdate = new Timestamp(unixtime * 1000L);
                SimpleDateFormat tmpfmt = new SimpleDateFormat(DATE_TIME_FORMAT);
                tmpdatestr = tmpfmt.format(tmpdate);
            }
        } catch (Exception ex) {

        }
        return tmpdatestr;
    }

    public static Integer getUnixTimeFromString(String str, String format) {
        Integer tmpunixtime = 0;
        try {
            if ((format == null) || (format.equals(""))) format = DATE_TIME_FORMAT;
            SimpleDateFormat tmpfmt = new SimpleDateFormat(format);
            Date tmpdate = tmpfmt.parse(str);

            tmpunixtime = Math.round(tmpdate.getTime() / 1000);
        } catch (Exception ex) {

        }
        return tmpunixtime;
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static int getCurrentUnixTime() {
        int tmpCurrentTime = (int) (System.currentTimeMillis() / 1000);
        return tmpCurrentTime;
    }

    public static String getCurrentTimeStr(String format) {
        String tmpret = "";
        try {
            Calendar ca = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            tmpret = sdf.format(ca.getTime());
        } catch (Exception ex) {
            log.error("getCurrentTimeStr:" + ex);
        }
        return tmpret;
    }

    public static Date getCurrentTime() {
        Calendar ca = Calendar.getInstance();
        Date tmpnow = new Date(ca.getTimeInMillis());
        return tmpnow;
    }

    public static java.sql.Date getSqlCurrentTime() {
        Calendar ca = Calendar.getInstance();
        java.sql.Date tmpnow = new java.sql.Date(ca.getTimeInMillis());
        return tmpnow;
    }

    public static String getSqlCurrentTimeStr(String format) {
        String tmpret = "";
        try {
            Calendar ca = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            tmpret = sdf.format(ca.getTime());
        } catch (Exception ex) {
            log.error("getCurrentTimeStr:" + ex);
        }
        return tmpret;
    }

    public static String getSqlCurrentTimeStr(int seconds, String format) {
        String tmpret = "";
        try {
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.SECOND, seconds);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            tmpret = sdf.format(ca.getTime());
        } catch (Exception ex) {
            log.error("getCurrentTimeStr:" + ex);
        }
        return tmpret;
    }

    public static String formatDate(java.sql.Date date, String format) {
        String tmpdatestr = "";
        try {
            SimpleDateFormat f = new SimpleDateFormat(format);
            tmpdatestr = f.format(date);
        } catch (Exception ex) {
            log.error("formatDate :" + ex);
        }
        return tmpdatestr;
    }

    public static String formatDate(Date date, String format) {
        String tmpdatestr = "";
        try {
            SimpleDateFormat f = new SimpleDateFormat(format);
            tmpdatestr = f.format(date);
        } catch (Exception ex) {
            log.error("formatDate :" + ex);
        }
        return tmpdatestr;
    }

    public static Date getUtilDate(java.sql.Date date) {
        Date tmpUtilDate = new Date(date.getTime());
        return tmpUtilDate;
    }

    public static java.sql.Date getUtilDate(Date date, String format) {
        String tmpdatestr = "";
        try {
            SimpleDateFormat f = new SimpleDateFormat(format);
            tmpdatestr = f.format(date);
        } catch (Exception ex) {
            log.error("formatDate :" + ex);
        }
        return new java.sql.Date(new Date(tmpdatestr).getTime());
    }

    public static java.sql.Time getSqlTime(java.sql.Date date) {
        java.sql.Time sTime = new java.sql.Time(date.getTime());
        return sTime;
    }

    public static Timestamp getSqlTimeStamp(java.sql.Date date) {
        Timestamp stp = new Timestamp(date.getTime());
        return stp;
    }


    public static Date getDateFromTimeStamp(Object obj) {
        Date d = null;
        if (obj != null) {
            Timestamp timestamp = (Timestamp) obj;
            d = new Date(timestamp.getTime());
        }
        return d;
    }

    public static Timestamp getTimeStampFromSQL(Object obj) {
        Timestamp timestamp = (Timestamp) obj;
        return timestamp;
    }

    public static Date getDateFromString(Object obj, String format) {
        Date tmpret = null;
        if (obj != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                tmpret = sdf.parse(obj.toString());
            } catch (Exception ex) {

            }
        }
        return tmpret;
    }

    public static java.sql.Date getSqlDate(Object obj) {
        java.sql.Date tmpdate = null;
        if (obj != null) {
            try {
                tmpdate = java.sql.Date.valueOf(obj.toString());
            } catch (Exception ex) {
                log.error("getDate :" + ex);
            }
        }
        return tmpdate;
    }

    public static java.sql.Date getSqlDate(String obj) {
        java.sql.Date tmpdate = null;
        if (obj != null) {
            try {
                tmpdate = java.sql.Date.valueOf(obj.toString());
            } catch (Exception ex) {
                log.error("getDate :" + ex);
            }
        }
        return tmpdate;
    }

    public static java.sql.Date getSqlDate(Object obj, java.sql.Date defvalue) {
        java.sql.Date tmpdate = defvalue;
        if (obj != null) {
            try {
                tmpdate = java.sql.Date.valueOf(obj.toString());
            } catch (Exception ex) {
                log.error("getDate :" + ex);
            }
        }
        return tmpdate;
    }

    public static void main(String args[]) throws Exception{
        System.out.println("---" + getAddDay(new Date().toLocaleString(),DEFAULT_DATE, -365,DATE_FORMAT));
    }
}
