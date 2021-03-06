package com.enze.ep.utils.wx;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.enze.ep.dao.EpPayInfoDAO;
import com.enze.ep.entity.EpBussType;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.EpPayType;
import com.enze.ep.entity.EpResult;
import com.enze.ep.enums.OrderType;
import com.enze.ep.service.EpOrderService;
import com.enze.ep.utils.DateUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeixinPay {

	// public static Logger lg=Logger.getLogger(WeixinPay.class);
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	@Autowired
	EpOrderService epOrderServiceImpl;
	
	@Autowired
	EpPayInfoDAO epPayInfoDAO;

	/**
	 * 获取微信支付的二维码地址
	 * 
	 * @return
	 * @author chenp
	 * @throws Exception
	 */
	public String getCodeUrl(WeChatParams ps) throws Exception {
		/**
		 * 账号信息
		 */
		String appid = WeChatConfig.APPID;// 微信服务号的appid
		String mch_id = WeChatConfig.MCHID; // 微信支付商户号
		String key = WeChatConfig.APIKEY; // 微信支付的API密钥
		String notify_url = WeChatConfig.WECHAT_NOTIFY_URL_PC;// 回调地址【注意，这里必须要使用外网的地址】
		String ufdoder_url = WeChatConfig.UFDODER_URL;// 微信下单API地址
		String trade_type = "NATIVE"; // 类型【网页扫码支付】

		/**
		 * 时间字符串
		 */
		String currTime = PayForUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayForUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;

		/**
		 * 参数封装
		 */
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);// 随机字符串
		packageParams.put("body", ps.body);// 支付的商品名称
		packageParams.put("out_trade_no", ps.out_trade_no + nonce_str);// 商户订单号【备注：每次发起请求都需要随机的字符串，否则失败。】
		packageParams.put("total_fee", ps.total_fee);// 支付金额
		packageParams.put("spbill_create_ip", PayForUtil.localIp());// 客户端主机
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("attach", ps.attach);// 额外的参数【业务类型+会员ID+支付类型】

		String sign = PayForUtil.createSign("UTF-8", packageParams, key); // 获取签名
		packageParams.put("sign", sign);

		String requestXML = PayForUtil.getRequestXml(packageParams);// 将请求参数转换成String类型
		log.info("微信支付请求参数的报文" + requestXML);
		String resXml = HttpUtil.postData(ufdoder_url, requestXML); // 解析请求之后的xml参数并且转换成String类型
		Map map = XMLUtil.doXMLParse(resXml);
		log.info("微信支付响应参数的报文" + resXml);
		String urlCode = (String) map.get("code_url");

		epOrderServiceImpl.recordOrderWeixinNonceStr(ps.out_trade_no, nonce_str);

		return urlCode;
	}

	/**
	 * 将路径生成二维码图片
	 * 
	 * @author chenp
	 * @param content
	 * @param response
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void encodeQrcode(String content, HttpServletResponse response) {

		if (StringUtils.isBlank(content))
			return;
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 250, 250, hints);
			BufferedImage image = toBufferedImage(bitMatrix);
			// 输出二维码图片流
			try {
				ImageIO.write(image, "png", response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (WriterException e1) {
			e1.printStackTrace();
		}
	}

	public byte[] encodeQrcodeToByte(String content) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		if (StringUtils.isBlank(content))
			return null;
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
			BufferedImage image = toBufferedImage(bitMatrix);
			// 输出二维码图片流
			try {
				ImageIO.write(image, "png", out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (WriterException e1) {
			e1.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * 类型转换
	 * 
	 * @author chenp
	 * @param matrix
	 * @return
	 */
	public BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) == true ? BLACK : WHITE);
			}
		}
		return image;
	}

	// 特殊字符处理
	public String UrlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, "UTF-8").replace("+", "%20");
	}

	public EpPayInfo queryOrderUsestatusByOrder(EpOrder order) throws Exception {
		return queryOrderUsestatusByOrderCodeAndWeixinNonceStr(order.getOrdercode(), order.getWeixinnoncestr());
	}

	/**
	 * 
	 * @Title: queryOrderUsestatus @Description: TODO(这里用一句话描述这个方法的作用) @param @param
	 *         outTradeNo =ordercode+noncestr(拼接) @param @return @param @throws
	 *         Exception 参数 @author wuxuecheng @return EpPayInfo 返回类型 @throws
	 */
	public EpPayInfo queryOrderUsestatusByOrderCodeAndWeixinNonceStr(String outTradeNo, String weixinNoncestr)
			throws Exception {

		/**
		 * 账号信息
		 */
		String appid = WeChatConfig.APPID;// 微信服务号的appid
		String mch_id = WeChatConfig.MCHID; // 微信支付商户号
		String key = WeChatConfig.APIKEY; // 微信支付的API密钥
		String orderquery_url = WeChatConfig.ORDERQUERY_URL;// 微信下单API地址

		/**
		 * 时间字符串
		 */
		String currTime = PayForUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayForUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;

		/**
		 * 参数封装
		 */
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();

		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("out_trade_no", outTradeNo + weixinNoncestr);
		packageParams.put("nonce_str", nonce_str);// 随机字符串

		String sign = PayForUtil.createSign("UTF-8", packageParams, key); // 获取签名
		packageParams.put("sign", sign);

		String requestXML = PayForUtil.getRequestXml(packageParams);// 将请求参数转换成String类型
		log.info("微信支付请求参数的报文" + requestXML);
		String resXml = HttpUtil.postData(orderquery_url, requestXML); // 解析请求之后的xml参数并且转换成String类型
		Map map = XMLUtil.doXMLParse(resXml);

		EpPayInfo payinfo = null;
		String trade_state = (String) map.get("trade_state");
		if ("SUCCESS".equals(trade_state)) {
			payinfo = new EpPayInfo();
			payinfo.setAttach((String) map.get("attach"));
			payinfo.setOrdercode(outTradeNo);
			String total_fee = (String) map.get("total_fee");
			BigDecimal fee = new BigDecimal(total_fee).divide(new BigDecimal(100));
			payinfo.setFee(fee.toString());
			String time_end = (String) map.get("time_end");
			String payDate = DateUtils.formatSimpleStrToDefaultStr(time_end);
			payinfo.setPaydate(payDate);
			payinfo.setPaytypeid(EpPayType.WEIXIN);
			payinfo.setPlordercode((String) map.get("transaction_id"));
			payinfo.setBusstypeid(EpBussType.SALE);
			payinfo.setTradestatus(trade_state);
		}

		log.info("微信支付响应参数的报文" + resXml);
		// String urlCode = (String) map.get("code_url");
		return payinfo;
	}

	// 微信订单号 ,商户订单号 ,商户退款单号 ,金额（分）
	/**
	 * 
	 * @Title: tradeRefund @Description: TODO(这里用一句话描述这个方法的作用) @param transaction_id
	 * 微信订单号 @param @param out_trade_no 商户订单号 @param @param out_refund_no
	 * 商户退款单号 @param @param total_fee 金额（分） @param @throws Exception 参数 @author
	 * wuxuecheng @return void 返回类型 @throws
	 */
	public EpResult tradeRefund(String transaction_id, String out_trade_no, String out_refund_no, String total_fee,String refund_fee)
			throws Exception {

		//支付结果
		EpResult epResult=new EpResult();
		
		
		/**
		 * 账号信息
		 */
		String appid = WeChatConfig.APPID;// 微信服务号的appid
		String mch_id = WeChatConfig.MCHID; // 微信支付商户号
		String key = WeChatConfig.APIKEY; // 微信支付的API密钥
		String refund_url = WeChatConfig.REFUND_URL;// 微信退款申请API地址

		/**
		 * 时间字符串
		 */
		String currTime = PayForUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayForUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;

		/**
		 * 参数封装
		 */
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);// 随机字符串
		packageParams.put("out_refund_no", out_refund_no);// 商户退款单号
		packageParams.put("out_trade_no", out_trade_no);// 商户订单号 outTradeNo+weixinNoncestr
		packageParams.put("refund_fee", refund_fee);// 退款总金额，单位为分
		packageParams.put("total_fee", total_fee);
		packageParams.put("transaction_id", transaction_id);// 微信订单号
		String sign = PayForUtil.createSign("UTF-8", packageParams, key); // 获取签名
		packageParams.put("sign", sign);

		String requestXML = PayForUtil.getRequestXml(packageParams);// 将请求参数转换成String类型
		Map<String, String> doXMLtoMap = new HashMap<String, String>();
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		File file = ResourceUtils.getFile("classpath:cert/wxpay/apiclient_cert.p12");
		String cAPath = file.getAbsolutePath();
		System.out.println("capath:=" + cAPath);
		FileInputStream instream = new FileInputStream(new File(cAPath));
		try {
			keyStore.load(instream, mch_id.toCharArray());
		} finally {
			instream.close();
		}
		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpPost = new HttpPost(refund_url);
			httpPost.setEntity(new StringEntity(requestXML, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			doXMLtoMap = XMLUtil.doXMLParse(jsonStr);
			String return_code=doXMLtoMap.get("return_code");
			String return_msg=doXMLtoMap.get("return_msg");
			
			//String out_refund_no=doXMLtoMap.get("out_refund_no");
			if("FAIL".equals(return_code)) {
				epResult=new EpResult(EpResult.FAIL,"微信退款申请失败",return_code+":"+return_msg);
				return epResult;
			} 
			String result_code=doXMLtoMap.get("result_code");
			if("SUCCESS".equals(result_code)) {
				EpPayInfo epPayInfo=new EpPayInfo();
				epPayInfo.setPaytypeid(2);//微信支付
				epPayInfo.setBusstypeid(OrderType.salebackorder.getTypeValue());
				epPayInfo.setOrdercode(out_refund_no);
				String refund_id=doXMLtoMap.get("refund_id");
				epPayInfo.setPlordercode(refund_id);
				//String refund_fee=doXMLtoMap.get("refund_fee");
				BigDecimal fee = new BigDecimal(total_fee).divide(new BigDecimal(100));
				epPayInfo.setFee(fee.toString());
				epPayInfo.setAttach("微信退款申请");
				epPayInfo.setTradestatus(result_code);
				//保存支付情况
				epPayInfoDAO.addPayInfo(epPayInfo);
				epResult=new EpResult(EpResult.SUCCESS,"微信退款申请成功，请顾客留意微信退款信息","");
			}else {
				String err_code=doXMLtoMap.get("err_code");
				String err_code_des=doXMLtoMap.get("err_code_des");
				epResult=new EpResult(EpResult.FAIL,"微信退款申请失败",err_code+":"+err_code_des);
			}
			response.close();
		} finally {
			httpclient.close();
		}

		return epResult;
	}
	
	//退款查询
	public EpResult tradeRefundQuery( String out_refund_no)
			throws Exception {
		//查询结果
		EpResult epResult=new EpResult();
		
		/**
		 * 账号信息
		 */
		String appid = WeChatConfig.APPID;// 微信服务号的appid
		String mch_id = WeChatConfig.MCHID; // 微信支付商户号
		String key = WeChatConfig.APIKEY; // 微信支付的API密钥
		String refund_query_url = WeChatConfig.REFUND_QUERY_URL;// 微信退款申请API地址

		/**
		 * 时间字符串
		 */
		String currTime = PayForUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayForUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;
		

		/**
		 * 参数封装
		 */
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);// 随机字符串
		packageParams.put("out_refund_no", out_refund_no);// 商户退款单号
		String sign = PayForUtil.createSign("UTF-8", packageParams, key); // 获取签名
		packageParams.put("sign", sign);

		String requestXML = PayForUtil.getRequestXml(packageParams);// 将请求参数转换成String类型
		String resXml = HttpUtil.postData(refund_query_url, requestXML); // 解析请求之后的xml参数并且转换成String类型
		Map map = XMLUtil.doXMLParse(resXml);

		String return_code =(String)map.get("return_code");
		String return_msg  =(String)map.get("return_msg");
		if("SUCCESS".equals(return_code)) {
			String result_code=(String)map.get("result_code");
			if("SUCCESS".equals(result_code)) {
				
				String refund_status_0=(String)map.get("refund_status_0");
				if("SUCCESS".equals(refund_status_0)) {
					epResult=new EpResult(EpResult.SUCCESS,"退款成功","");
				}else {
					String err_code=(String)map.get("err_code");
					String err_code_des=(String)map.get("err_code_des");
					epResult=new EpResult(EpResult.FAIL,err_code,err_code_des);
				}
				
			}else {
				
				String err_code=(String)map.get("err_code");
				String err_code_des=(String)map.get("err_code_des");
				epResult=new EpResult(EpResult.FAIL,err_code,err_code_des);
				
			}
			
			
			
		}else {
			
			epResult=new EpResult(EpResult.FAIL,return_code,return_msg);
		}
		
		return epResult;
	}
	

}