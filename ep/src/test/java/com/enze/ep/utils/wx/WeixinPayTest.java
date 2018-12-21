package com.enze.ep.utils.wx;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import com.enze.ep.entity.EpPayInfo;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.github.wxpay.sdk.WXPayConstants.SignType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeixinPayTest {

	@Autowired
	WeixinPay weixinPay;
	@Test
	public void testGetCodeUrl() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeQrcode() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeQrcodeToByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testToBufferedImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testUrlEncode() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryOrderUsestatusByEpOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testTradeRefundQuery() {

		try {
			String out_refund_no="2018122115125437345056";
		 	weixinPay.tradeRefundQuery(out_refund_no);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @Title: 测试微信退款
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param     参数
	* @author wuxuecheng
	* @return void    返回类型
	* @throws
	 */
	@Test
	public void testTradeRefund() {
		/*try {
			File file = ResourceUtils.getFile("classpath:cert/wxpay/apiclient_cert.p12");
			String cAPath =file.getAbsolutePath();
			//String cAPath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/ca/" + companyId + "/apiclient_cert.p12");
			System.out.println("capath:=" + cAPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		 
		 
		String transaction_id="4200000221201811146327541771";
		String out_trade_no="20181114081103668450560825038943"; //订单号+随机数  见表ep_order 0825038943
	    String out_refund_no="2018111408110366848888";
		String  total_fee="1";
		
		try {
			//weixinPay.tradeRefund(transaction_id, out_trade_no, out_refund_no, total_fee);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void createSignTest() {
/*		String transaction_id="4200000221201811146327541771";
		String out_trade_no="20181114081103668450560825038943"; //订单号+随机数  见表ep_order 0825038943
	    String out_refund_no="2018111408110366848888";
		String  total_fee="1";
		
	    String appid = WeChatConfig.APPID;//微信服务号的appid    
	    String mch_id = WeChatConfig.MCHID; //微信支付商户号
	    String key = WeChatConfig.APIKEY; // 微信支付的API密钥    
	    String refund_url=WeChatConfig.REFUND_URL;//微信下单API地址  
	    String nonce_str = "1234567";    */

	    /** 
	     * 参数封装 
	     */  
	    SortedMap<String,String> packageParams = new TreeMap<String,String>(); 
	    packageParams.put("appid", "wx3d51af6b1af73327");
	    packageParams.put("mch_id", "1495104442");
	    packageParams.put("transaction_id", "4200000221201811146327541771");//微信订单号 
	    packageParams.put("out_trade_no", "20181114081103668450560825038943");//商户订单号  outTradeNo+weixinNoncestr
	    packageParams.put("nonce_str", "1234567");//随机字符串
	    packageParams.put("out_refund_no", "2018111408110366848888" );//商户退款单号 
	    
	    packageParams.put("refund_fee", "1");// 退款总金额，单位为分
	    packageParams.put("total_fee", "1" );
	    
	    //String sign = PayForUtil.createSign("UTF-8", packageParams,key);  //获取签名  
	    
	    try {
			String sign=WXPayUtil.generateSignature(packageParams, "CQzY0F9rLGvzzBjh7I84iz0S4ptRJnjN",SignType.MD5);
			System.out.println(sign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
