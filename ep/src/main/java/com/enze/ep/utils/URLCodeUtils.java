package com.enze.ep.utils;

import java.io.UnsupportedEncodingException;

public class URLCodeUtils {

	/**
	 * 
	* @Title: encoding
	* @Description: TODO(转码)
	* @param @param str
	* @param @param enc
	* @param @return
	* @param @throws Exception    参数
	* @author wuxuecheng
	* @return String    返回类型
	* @throws
	 */
	public static String encoding(String str, String enc) throws UnsupportedEncodingException {
		String res = "";
		res = java.net.URLEncoder.encode(str, enc);
		return res;
	}
	
	/**
	 * 
	* @Title: decoding
	* @Description: TODO(解码)
	* @param @param res
	* @param @param enc
	* @param @return
	* @param @throws Exception    参数
	* @author wuxuecheng
	* @return String    返回类型
	* @throws
	 */
	public static String decoding(String res,String enc) throws UnsupportedEncodingException {
		String str="";
		str=java.net.URLDecoder.decode(res,"UTF-8");
		return str;
	}

	
}
