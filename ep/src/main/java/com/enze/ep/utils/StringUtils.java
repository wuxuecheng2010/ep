package com.enze.ep.utils;

public class StringUtils {
	
	
	/**
	 * 
	* @Title: replaceSenChar
	* @Description: 去除sql中的特殊字符 
	* @param @param str
	* @param @return    参数
	* @author wuxuecheng
	* @return String    返回类型
	* @throws
	 */
	public static String formatForSql(String str) {
		return str.replace("'", "").replace("%", "");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
