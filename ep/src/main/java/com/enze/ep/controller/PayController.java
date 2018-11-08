package com.enze.ep.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.EpPayType;
import com.enze.ep.service.EpOrderService;
import com.enze.ep.utils.wx.PayForUtil;
import com.enze.ep.utils.wx.WeChatConfig;
import com.enze.ep.utils.wx.WeChatParams;
import com.enze.ep.utils.wx.WeixinPay;
import com.enze.ep.utils.wx.XMLUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {
	
	@Autowired
	EpOrderService epOrderServiceImpl;
	
	@RequestMapping(value="/scantopay/{orderid}",method=RequestMethod.GET)
	public ModelAndView scanToPay(@PathVariable(name="orderid") String orderid) {
		ModelMap map=new ModelMap();
		EpOrder eporder=epOrderServiceImpl.findEpOrderById(Integer.valueOf(orderid));
		//微信支付
		WeChatParams ps=new WeChatParams();
		String total_fee=eporder.getOrdermoney().multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
		ps.setTotal_fee(total_fee);
		ps.setBody("在线利群药店");
		ps.setMemberid(eporder.getIdcard());
		ps.setOut_trade_no(eporder.getOrdercode());
		ps.setAttach("销售订单");
		String weixin_pay_url="";
		try {
			weixin_pay_url=WeixinPay.getCodeUrl(ps);
			String qrcode_path="/pay/strtoqr?str="+weixin_pay_url;
			map.put("weixin", qrcode_path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("pay/scantopay",map);
	}
	
	@ResponseBody
	@RequestMapping(value="/strtoqr",method=RequestMethod.GET)
	public byte[] strtoqr(String str) {
		byte[] bytes=WeixinPay.encodeQrcodeToByte(str);
		return bytes;
	}

	
	/** 
	* pc端微信支付之后的回调方法 
	* @param request 
	* @param response 
	* @throws Exception 
	*/  
	@RequestMapping(value="/wechat_notify_url_pc",method=RequestMethod.POST)  
	public void wechat_notify_url_pc(HttpServletRequest request,HttpServletResponse response) throws Exception{    

	        //读取参数    
	        InputStream inputStream ;    
	        StringBuffer sb = new StringBuffer();    
	        inputStream = request.getInputStream();    
	        String s ;    
	        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));    
	        while ((s = in.readLine()) != null){    
	            sb.append(s);    
	        }    
	        in.close();    
	        inputStream.close();    

	        //解析xml成map    
	        Map<String, String> m = new HashMap<String, String>();    
	        m = XMLUtil.doXMLParse(sb.toString());    

	        //过滤空 设置 TreeMap    
	        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();          
	        Iterator<String> it = m.keySet().iterator();    
	        while (it.hasNext()) {    
	            String parameter = it.next();    
	            String parameterValue = m.get(parameter);    

	            String v = "";    
	            if(null != parameterValue) {    
	                v = parameterValue.trim();    
	            }    
	            packageParams.put(parameter, v);    
	        }    
	        // 微信支付的API密钥    
	        String key = WeChatConfig.APIKEY; // key    

	        log.info("微信支付返回回来的参数："+packageParams);    
	        //判断签名是否正确    
	        if(PayForUtil.isTenpaySign("UTF-8", packageParams,key)) {    
	            //------------------------------    
	            //处理业务开始    
	            //------------------------------    
	            String resXml = "";    
	            if("SUCCESS".equals((String)packageParams.get("result_code"))){    
	                // 这里是支付成功    
	            //执行自己的业务逻辑开始  
	            	String app_id = (String)packageParams.get("appid");  
	                String mch_id = (String)packageParams.get("mch_id");    
	                String openid = (String)packageParams.get("openid");   
	                String is_subscribe = (String)packageParams.get("is_subscribe");//是否关注公众号  

	                //附加参数【商标申请_0bda32824db44d6f9611f1047829fa3b_15460】--【业务类型_会员ID_订单号】  
	                String attach = (String)packageParams.get("attach");  
	                //商户订单号  
	                String out_trade_no = (String)packageParams.get("out_trade_no");    
	                //付款金额【以分为单位】  
	                String total_fee = (String)packageParams.get("total_fee");    
	                //微信生成的交易订单号  
	                String transaction_id = (String)packageParams.get("transaction_id");//微信支付订单号  
	                //支付完成时间  
	                String time_end=(String)packageParams.get("time_end");  

/*	                log.info("app_id:"+app_id);  
	                log.info("mch_id:"+mch_id);    
	                log.info("openid:"+openid);    
	                log.info("is_subscribe:"+is_subscribe);    
	                log.info("out_trade_no:"+out_trade_no);    
	                log.info("total_fee:"+total_fee);    
	                log.info("额外参数_attach:"+attach);   
	                log.info("time_end:"+time_end);*/   
	                
	                EpPayInfo payinfo=new EpPayInfo();
	                payinfo.setAttach(attach);
	                payinfo.setOrdercode(out_trade_no);
	                payinfo.setFee(total_fee);
	                payinfo.setPaydate(time_end);
	                payinfo.setPaytypeid(EpPayType.WEIXIN);
	                payinfo.setPlordercode(transaction_id);
	                
	                try {
	                	//商家单据信息变更及支付信息记录
	                	epOrderServiceImpl.finishOrderPay(payinfo);
	                	resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"    
		                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> "; 
					} catch (Exception e) {
						log.error(e.getMessage());
						log.error(payinfo.toString());
						resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"    
		                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> "; 
					}
	                
	                
	                //执行自己的业务逻辑结束  
	                log.info("支付成功");    
	                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.    
	                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"    
	                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";    

	            } else {    
	            	log.info("支付失败,错误信息：" + packageParams.get("err_code"));    
	                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"    
	                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";    
	            }    
	            //------------------------------    
	            //处理业务完毕    
	            //------------------------------    
	            BufferedOutputStream out = new BufferedOutputStream(    
	                    response.getOutputStream());    
	            out.write(resXml.getBytes());    
	            out.flush();    
	            out.close();  
	        } else{    
	        	log.info("通知签名验证失败");    
	        }    

	    }  
	
	
	
	
	/*********************测试********************/
	@RequestMapping(value="/wx",method=RequestMethod.GET)
	public String getPayQRCode(HttpServletRequest request,HttpServletResponse response) {
		WeChatParams ps=new WeChatParams();
		ps.setTotal_fee("1");
		ps.setBody("货物");
		ps.setMemberid("100000000");
		ps.setOut_trade_no("JY0001000002");
		ps.setAttach("没有了");
		String codeurl="";
		try {
			codeurl = WeixinPay.getCodeUrl(ps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return codeurl;
	}
	
	/*********************测试********************/
	@ResponseBody
	@RequestMapping(value="/qr",method=RequestMethod.GET)
	public byte[] getPayQrCodeByte(HttpServletRequest request,HttpServletResponse response) {
		String c="1234560";
		byte[] bytes=WeixinPay.encodeQrcodeToByte(c);
		return bytes;
	}
	
}
