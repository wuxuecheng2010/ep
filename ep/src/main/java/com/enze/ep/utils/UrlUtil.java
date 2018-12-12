package com.enze.ep.utils;


import java.io.UnsupportedEncodingException;
/**
 * urlת�롢����
 *
 * @author lifq 
 * @date 2015-3-17 ����04:09:35
 */
public class UrlUtil {
    private final static String ENCODE = "GBK"; 
    /**
     * URL ����
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 ����04:09:51
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * URL ת��
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 ����04:10:28
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * @return void
     * @author lifq
     * @date 2015-3-17 ����04:09:16
     */
    public static void main(String[] args) {
        String str = "北京";
        System.out.println(getURLEncoderString(str));
        System.out.println(getURLDecoderString(str));
        
    }

}