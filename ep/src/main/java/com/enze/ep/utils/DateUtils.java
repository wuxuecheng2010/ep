package com.enze.ep.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static final String DEFAULT_DATETIME_FORMAT="yyyy-MM-dd hh:mm:ss";
	public static final String SHORT_DATETIME_FORMAT="yyyy-MM-dd";
	public static final String SIMPLE_DATETIME_FORMAT="yyyyMMddhhmmss";
	
	
	public static String formatDateToDefaultStr(Date d) {
		SimpleDateFormat sf=new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		return sf.format(d);
	}
	
	public static String formatDateToShortStr(Date d) {
		SimpleDateFormat sf=new SimpleDateFormat(SHORT_DATETIME_FORMAT);
		return sf.format(d);
	}
	
	
	public static String formatSimpleStrToDefaultStr(String origin_str) {
		SimpleDateFormat sf=new SimpleDateFormat(SIMPLE_DATETIME_FORMAT);
		Date d=null;
		try {
			d = sf.parse(origin_str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s=formatDateToDefaultStr(d);
		return s;
	}
	
	//获取毫秒数
	public static int getMillisecond() {
		
		Calendar Cld = Calendar.getInstance();
		int MI = Cld.get(Calendar.MILLISECOND);
		return MI;
	}
	
	//获取给定日期之后x天的
	public static Date getDateAfterX(String dateStr,String pattern,int x) {
		
		Calendar c=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		Date date=new Date();
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, x);
		Date data_=c.getTime();
		return data_;
	}
	
	
	//获取给定日期之后x天的 文本类型
		public static String getDateStringAfterX(String dateStr,String pattern,int x) {
			Date d=getDateAfterX(dateStr, pattern, x);
			SimpleDateFormat sdf=new SimpleDateFormat(pattern);
			return sdf.format(d);
		}
	

	public static void main(String[] args) {
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			Date d=sf.parse("20181114094820");
			SimpleDateFormat sf2=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ss=sf2.format(d);
			System.out.println(ss);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}