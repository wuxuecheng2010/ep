package com.enze.ep.utils.wx;

import java.net.Inet4Address;  
import java.net.InetAddress;  
import java.net.InterfaceAddress;  
import java.net.NetworkInterface;  
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;  
import java.util.Enumeration;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
import java.util.SortedMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayConstants.SignType;  


public class PayForUtil {  

private static Logger lg=Logger.getLogger(PayForUtil.class);  

/**  
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。  
     * @return boolean  
     */    
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {    
        StringBuffer sb = new StringBuffer();    
        Set es = packageParams.entrySet();    
        Iterator it = es.iterator();    
        while(it.hasNext()) {    
            Map.Entry entry = (Map.Entry)it.next();    
            String k = (String)entry.getKey();    
            String v = (String)entry.getValue();    
            if(!"sign".equals(k) && null != v && !"".equals(v)) {    
                sb.append(k + "=" + v + "&");    
            }    
        }    
        sb.append("key=" + API_KEY);    

        //算出摘要    
        String mysign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();    
        String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();    

        return tenpaySign.equals(mysign);    
    }    

    /**  
     * @author chenp 
     * @Description：sign签名  
     * @param characterEncoding  
     *            编码格式  
     * @param parameters  
     *            请求参数  
     * @return  
     */    
    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {    
        StringBuffer sb = new StringBuffer();    
        Set es = packageParams.entrySet();    
        Iterator it = es.iterator();    
        while (it.hasNext()) {    
            Map.Entry entry = (Map.Entry) it.next();    
            String k = (String) entry.getKey();    
            String v = (String) entry.getValue();    
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {    
                sb.append(k + "=" + v + "&");    
            }    
        }    
        sb.append("key=" + API_KEY);    
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();    
        return sign;    
    }    
    
    public static String generateSignature( Map<Object, Object> data, String key, SignType signType) throws Exception {
        Set<Object> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXPayConstants.FIELD_SIGN)) {
                continue;
            }
            if (((String)data.get(k)).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(((String)data.get(k)).trim()).append("&");
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        }
        else if (SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }
    
    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据
     * @param key 密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
    

    /**  
     * @author chenp 
     * @Description：将请求参数转换为xml格式的string  
     * @param parameters  
     *            请求参数  
     * @return  
     */    
    public static String getRequestXml(SortedMap<Object, Object> parameters) {    
        StringBuffer sb = new StringBuffer();    
        sb.append("<xml>");    
        Set es = parameters.entrySet();    
        Iterator it = es.iterator();    
        while (it.hasNext()) {    
            Map.Entry entry = (Map.Entry) it.next();    
            String k = (String) entry.getKey();    
            String v = (String) entry.getValue();    
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {    
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");    
            } else {    
                sb.append("<" + k + ">" + v + "</" + k + ">");    
            }    
        }    
        sb.append("</xml>");    
        return sb.toString();    
    }    

    /**  
     * @author chenp 
     * @Description：将请求参数转换为xml格式的string  
     * @param parameters  
     *            请求参数  
     * @return  
     */    
    public static String getRequestXml2(SortedMap<Object, Object> parameters) {    
        StringBuffer sb = new StringBuffer();    
        sb.append("<xml>");    
        Set es = parameters.entrySet();    
        Iterator it = es.iterator();    
        while (it.hasNext()) {    
            Map.Entry entry = (Map.Entry) it.next();    
            String k = (String) entry.getKey();    
            String v = (String) entry.getValue();    
            /*if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {    
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");    
            } else {    
                sb.append("<" + k + ">" + v + "</" + k + ">");    
            }    */
            sb.append("<" + k + ">" + v + "</" + k + ">");   
        }    
        sb.append("</xml>");    
        return sb.toString();    
    }
    
    /**  
     * 取出一个指定长度大小的随机正整数.  
     *   
     * @param length  
     *            int 设定所取出随机数的长度。length小于11  
     * @return int 返回生成的随机数。  
     */    
    public static int buildRandom(int length) {    
        int num = 1;    
        double random = Math.random();    
        if (random < 0.1) {    
            random = random + 0.1;    
        }    
        for (int i = 0; i < length; i++) {    
            num = num * 10;    
        }    
        return (int) ((random * num));    
    }    

    /**  
     * 获取当前时间 yyyyMMddHHmmss  
     *  @author chenp 
     * @return String  
     */    
    public static String getCurrTime() {    
        Date now = new Date();    
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");    
        String s = outFormat.format(now);    
        return s;    
    }  
    /** 
     * 获取本机IP地址 
     * @author chenp 
     * @return 
     */  
    public static String localIp(){  
        String ip = null;  
        Enumeration allNetInterfaces;  
        try {  
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();              
            while (allNetInterfaces.hasMoreElements()) {  
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();  
                List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();  
                for (InterfaceAddress add : InterfaceAddress) {  
                    InetAddress Ip = add.getAddress();  
                    if (Ip != null && Ip instanceof Inet4Address) {  
                        ip = Ip.getHostAddress();  
                    }  
                }  
            }  
        } catch (SocketException e) {  
        lg.warn("获取本机Ip失败:异常信息:"+e.getMessage());  
        }  
        return ip;  
    }  

}  