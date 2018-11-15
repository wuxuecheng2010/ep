package com.enze.ep.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final String DEFAULT_DATETIME_FORMAT="yyyy-MM-dd hh:mm:ss";
	public static final String SIMPLE_DATETIME_FORMAT="yyyyMMddhhmmss";
	
	
	public static String formatDateToDefaultStr(Date d) {
		SimpleDateFormat sf=new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
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