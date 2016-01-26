package com.ss.crawler.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间工具类
 * @author ss
 *
 */
public class DateUtil {

    /**
	 * 返回系统时间组成的字符串。例如：2005-08-28
	 *
	 * @return String
	 */
	public static String getSystemDate() {
		Calendar c = Calendar.getInstance();
		int y, m, d;
		String year, month, day;
		String returnValue = "";

		y = c.get(Calendar.YEAR);
		m = c.get(Calendar.MONTH) + 1;
		d = c.get(Calendar.DATE);

		year = String.valueOf(y);

		if (m <= 9) {
			month = "0" + String.valueOf(m);
		} else {
			month = String.valueOf(m);

		}
		if (d <= 9) {
			day = "0" + String.valueOf(d);
		} else {
			day = String.valueOf(d);

		}
		returnValue = year + "-" + month + "-" + day;

		return returnValue;
	}
	
	/**
	 * <p>
	 * 返回系统时间组成的字符串,精确到秒。例如：2001-01-01 00:00:00
	 * <p>
	 * 
	 * @return String
	 */
	public static String getSystemTime() {
		Calendar c = Calendar.getInstance();
		String returnValue = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			returnValue = fmt.format(c.getTime());
		} catch (Exception e) {
		}
		return returnValue;

	}


	/**
	 * 获取基准日期若干天前的日期
	 *
	 * @param curDate
	 *            String 基准日期
	 * @param days
	 *            int 需要获取基准日期几天前的日期
	 * @return String
	 */
	public static String getBeforeDay(String curDate, int days) {
		String oldDate = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

			if (curDate.length() == 19) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else if (curDate.length() == 16) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else if (curDate.length() == 13) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH");
			}

			Calendar caldate = new GregorianCalendar();
			Date date = fmt.parse(curDate);
			caldate.setTimeInMillis(date.getTime());
			caldate.add(Calendar.DATE, 0 - days);
			/*
			 * oldDate = caldate.get(Calendar.YEAR) + "-" +
			 * (caldate.get(Calendar.MONTH) + 1) + "-" +
			 * caldate.get(Calendar.DAY_OF_MONTH);
			 */
			oldDate = fmt.format(caldate.getTime());
		} catch (Exception e) {
			return curDate;
		}
		return oldDate;
	}
	
	/**
	 * 获取基准日期若干天后的日期
	 * 
	 * @param curDate
	 *            String 基准日期
	 * @param days
	 *            int 需要获取基准日期几天后的日期
	 * @return String
	 */
	public static String getAfterDay(String curDate, int days) {
		String oldDate = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

			if (curDate.length() == 19) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else if (curDate.length() == 16) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else if (curDate.length() == 13) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH");
			}

			Calendar caldate = new GregorianCalendar();
			Date date = fmt.parse(curDate);
			caldate.setTimeInMillis(date.getTime());
			caldate.add(Calendar.DATE, days);
			/*
			 * oldDate = caldate.get(Calendar.YEAR) + "-" +
			 * (caldate.get(Calendar.MONTH) + 1) + "-" +
			 * caldate.get(Calendar.DAY_OF_MONTH);
			 */
			oldDate = fmt.format(caldate.getTime());
		} catch (Exception e) {
			return curDate;
		}
		return oldDate;
	}

	/**
	 * 获取基准日期若干天后的日期
	 * 
	 */
	public static Date getAfterDay(Date curDate, int days) {
		if(curDate == null){
			return curDate;
		}
		Calendar caldate = new GregorianCalendar();
		caldate.setTimeInMillis(curDate.getTime());
		caldate.add(Calendar.DATE, days);
		return caldate.getTime();
	}

	/**
	 *	日期转字符串
	 * @param input
	 * @param aStringFormat
	 * @return
	 */
	public static String dateTOString(Date input, String aStringFormat) {
		String strReturn = "";
		if (input == null) {
			strReturn = "3000-1-1";
		} else {
			if (StringUtils.isBlank(aStringFormat)) {
				aStringFormat = "yyyy-MM-dd";
			}
			SimpleDateFormat fmt = new SimpleDateFormat(aStringFormat);
			strReturn = fmt.format(input.getTime());
		}
		return strReturn;
	}

	/**
	 * 字符串转日期
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 是否同一天
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static boolean isSameDate(Date firstDate, Date secondDate){
    	if(firstDate != null && secondDate != null){
    		if(firstDate.getTime() == secondDate.getTime()){
        		return true;
        	}
    	}
    	return false;
    }
    
   

    /**
     * 获取当前一周的日期集合
     * @param mdate
     * @return
     */
    public static List<Date> dateToWeek(Date mdate){
		  Integer b=mdate.getDay();
		  Date fdate ;
		  List <Date> list = new ArrayList<Date>();
		  Long fTime=mdate.getTime()-b.longValue()*24*3600000;
		  for(int a=0;a<8;a++){	  
			  fdate= new Date();
			  fdate.setTime(fTime+(((long)a)*24*3600000));
			  list.add(a, fdate);
		  }
		  return list;
	}
    
    /**
   	 * 得到当前时间的前一个月
   	 * 
   	 * @return String
   	 */
   	public static String getPreMonth() {
   		Calendar calendar = Calendar.getInstance();
   		int y, m, d;
   		String year, month, day;

   		y = calendar.get(Calendar.YEAR);
   		m = calendar.get(Calendar.MONTH) + 1;
   		d = calendar.get(Calendar.DATE);

   		if (m == 1) {
   			m = 12;
   			y--;
   		} else {
   			m--;
   		}
   		year = String.valueOf(y);
   		if (m <= 9) {
   			month = "0" + String.valueOf(m);
   		} else {
   			month = String.valueOf(m);
   		}
   		if (d <= 9) {
   			day = "0" + String.valueOf(d);
   		} else {
   			day = String.valueOf(d);
   		}

   		return year + "-" + month + "-" + day;

   		//
   	}
   	
   	/**
	 * <p>
	 * 返回系统时间组成的字符串。例如：20050828
	 * <p>
	 * 
	 * @return String
	 */
	public static String getWiseYYYYMMDD() {
		Calendar c = Calendar.getInstance();
		int y, m, d;
		String year, month, day;
		String returnValue = "";

		y = c.get(Calendar.YEAR);
		m = c.get(Calendar.MONTH) + 1;
		d = c.get(Calendar.DATE);

		year = String.valueOf(y);

		if (m <= 9) {
			month = "0" + String.valueOf(m);
		} else {
			month = String.valueOf(m);

		}
		if (d <= 9) {
			day = "0" + String.valueOf(d);
		} else {
			day = String.valueOf(d);

		}
		returnValue = year + month + day;

		return returnValue;
	}

    /**
     * 返回两个日期相差天数
     * 时间精确到日小于1日为0日
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日期之间的差
     */
    public static long getQuot(String startDate, String endDate) throws Exception{
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = ft.parse(startDate);
            Date date2 = ft.parse(endDate);
            quot = date1.getTime() - date2.getTime();
            quot = quot / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
			throw new Exception(e.getMessage(),e);
        }
        return quot;
    }

	public static void main(String[] args){
		SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy");
		//前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		Date dt = new Date(1433142585);
		String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
		System.out.println(sDateTime);

	}

}
