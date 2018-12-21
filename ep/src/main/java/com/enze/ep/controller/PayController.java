package com.enze.ep.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.enze.ep.entity.EpBussType;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.EpPayType;
import com.enze.ep.entity.EpUser;
import com.enze.ep.service.EpOrderService;
import com.enze.ep.service.EpUserService;
import com.enze.ep.utils.DESUtils;
import com.enze.ep.utils.MyAuthUtils;
import com.enze.ep.utils.alipay.AliPay;
import com.enze.ep.utils.alipay.AlipayConfig;
import com.enze.ep.utils.wx.PayForUtil;
import com.enze.ep.utils.wx.WeChatConfig;
import com.enze.ep.utils.wx.WeChatParams;
import com.enze.ep.utils.wx.WeixinPay;
import com.enze.ep.utils.wx.XMLUtil;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {
	
	
	
	private static final String Prefix_Redis_Key_Weixin_QrCode="qrcode-weixin";
	private static final String Prefix_Redis_Key_Alipay_QrCode="qrcode-alipay";
	private static final String Prefix_Redis_Key_Separtor="-";
	
	@Autowired
	EpOrderService epOrderServiceImpl;
	
	@Autowired
	EpUserService epUserServiceImpl;
	
	@Autowired
	MyAuthUtils myAuthUtils;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Autowired
	WeixinPay weixinPay;
	
	@Autowired
	AliPay alipay;
	
	//打开扫码界面
	@RequestMapping(value="/payqrcode/{orderid}/{usercode}",method=RequestMethod.GET)
	public ModelAndView payqrcode(HttpServletRequest request, HttpServletResponse response,@PathVariable(name="orderid")String  orderid,@PathVariable(name="usercode")String  usercode) {
		//ModelMap map=new ModelMap();
		//ModelMap map=myAuthUtils.getAuthInfo(request,response);
		EpUser epUser=epUserServiceImpl.findEpUserByUserCode(usercode);
		ModelMap map=myAuthUtils.getAuthInfoByEpUser(epUser);
		map.put("orderid", orderid);
		
		EpOrder eporder=epOrderServiceImpl.findEpOrderById(Integer.valueOf(orderid));
		List<EpOrders> orderslist=epOrderServiceImpl.findEpOrdersListByOrderid(Integer.valueOf(orderid));
		map.put("eporder", eporder);
		map.put("orderslist", orderslist);
		return new ModelAndView("pay/payqrcode",map);
	}
	
	
	//返回支付宝支付链接
	@ResponseBody
	@RequestMapping(value="/alipay/{orderid}",method=RequestMethod.GET)
	public Map<String,String> alipay(@PathVariable(name="orderid") int orderid) {
		String key=Prefix_Redis_Key_Alipay_QrCode+Prefix_Redis_Key_Separtor+orderid;
		String payurl=(String) redisTemplate.opsForValue().get(key);
		if(payurl==null) {
			//获取销售清单信息
			EpOrder eporder=epOrderServiceImpl.findEpOrderById(orderid);
			List<EpOrders> list=epOrderServiceImpl.findEpOrdersListByOrderid(orderid);
			
			  // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
	        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
	        String outTradeNo =eporder.getOrdercode();// "tradeprecreate" + System.currentTimeMillis()+ (long) (Math.random() * 10000000L);

	        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
	        String subject = "利群药店当面付扫码消费";

	        // (必填) 订单总金额，单位为元，不能超过1亿元
	        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
	        String totalAmount = eporder.getOrdermoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString();

	        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
	        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
	        String undiscountableAmount = "0";

	        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
	        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
	        String sellerId = "";

	        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
	        String body = "购买商品共"+totalAmount+"元";

	        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
	        String operatorId = "operator_id";

	        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
	        String storeId = "store_id";

	        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
	        ExtendParams extendParams = new ExtendParams();
	        extendParams.setSysServiceProviderId("2088100200300400500");

	        // 支付超时，定义为120分钟
	        String timeoutExpress = "120m";

	        // 商品明细列表，需填写购买商品详细信息，
	        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
	        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
	        for(EpOrders eporders:list) {
	        	GoodsDetail goods = GoodsDetail.newInstance(eporders.getVcproductcode(), eporders.getVcuniversalname(), eporders.getNumprice().longValue(), eporders.getTotalcounts().intValue());
	        	goodsDetailList.add(goods);
	        }

	        // 创建扫码支付请求builder，设置请求参数
	        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
	            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
	            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
	            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
	            .setTimeoutExpress(timeoutExpress)
	            .setNotifyUrl(AlipayConfig.notifyUrl)//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
	            .setGoodsDetailList(goodsDetailList);
			
	        payurl=alipay.getCodeUrl(builder);
	        redisTemplate.opsForValue().set(key, payurl);
        	redisTemplate.expire(key, 100, TimeUnit.MINUTES);
	        
		}
		
		Map<String,String> map=new HashMap<String,String>();
		map.put("payurl", payurl);
		map.put("code", "success");
		return map;
	}
	
	
	
	//返回微信支付链接
		@ResponseBody
		@RequestMapping(value="/weixin/{orderid}",method=RequestMethod.GET)
		public Map<String,String> weixin(@PathVariable(name="orderid") int orderid) {
			
			String key=Prefix_Redis_Key_Weixin_QrCode+Prefix_Redis_Key_Separtor+orderid;
			String payurl=(String) redisTemplate.opsForValue().get(key);
				if(payurl==null) {
				//String payurl="";
				EpOrder eporder=epOrderServiceImpl.findEpOrderById(Integer.valueOf(orderid));
				//微信支付
				WeChatParams ps=new WeChatParams();
				String total_fee=eporder.getOrdermoney().multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
				ps.setTotal_fee(total_fee);
				ps.setBody("在线利群药店");
				ps.setMemberid(eporder.getIdcard());
				ps.setOut_trade_no(eporder.getOrdercode());
				ps.setAttach("销售订单");
				try {
					payurl=weixinPay.getCodeUrl(ps);
					redisTemplate.opsForValue().set(key, payurl);
		        	redisTemplate.expire(key, 100, TimeUnit.MINUTES);
					//String qrcode_path="/pay/strtoqr?str="+payurl;
				} catch (Exception e) {
					e.printStackTrace();
					redisTemplate.expire(key, 1, TimeUnit.SECONDS);//出现异常直接废弃
				}
				
			}
			Map<String,String> map=new HashMap<String,String>();
			map.put("payurl", payurl);
			map.put("code", "success");
			return map;
		}

	
		//文字转二维码工具
		@ResponseBody
		@RequestMapping(value="/strtoqr",method=RequestMethod.GET)
		public byte[] strtoqr(String str) {
			byte[] bytes=weixinPay.encodeQrcodeToByte(str);
			return bytes;
		}

		
		
	
		/** 
		* 支付宝回调
		* @param request 
		* @param response 
		* @throws Exception 
		*/  
		@RequestMapping(value="/alipay_notify_url",method=RequestMethod.POST)  
		public void alipay_notify_url_pc(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
			HashMap<String, String> map = Maps.newHashMap();
	        Map<String, String[]> parameterMap = request.getParameterMap();
	        Set<String> set = parameterMap.keySet();
	        for (String s: set){
	            String[] strings = parameterMap.get(s);
	            String str="";
	            for (int i=0;i<strings.length;i++){
	                str=i==strings.length-1?str+strings[i]:str+strings[i]+",";
	            }
	            map.put(s,str);
	        }
	        log.info("支付宝回调：sign:{},trade_status:{},参数：{}",map.get("sign"),map.get("trade_status"),map.toString());

	        //验签是不是支付宝发起的回调
	        map.remove("sign_type");
	        boolean bool=alipay.alipayCallBack(map);//延签
	        if(bool) {
	        	String out_trade_no = map.get("out_trade_no");
	    	    String trade_status = map.get("trade_status");
	    	    String total_fee=map.get("total_amount");
	    	    String time_end=map.get("gmt_payment");
	    	    String transaction_id=map.get("trade_no");
	    	    String attach=map.get("body");
	    	    if("TRADE_SUCCESS".equals(trade_status)||"TRADE_FINISHED".equals(trade_status)) {
	    	    	
	    	    	EpPayInfo payinfo=new EpPayInfo();
	    	    	payinfo.setAttach(attach);
	                payinfo.setOrdercode(out_trade_no);
	                payinfo.setFee(total_fee);
	                payinfo.setPaydate(time_end);
	                payinfo.setPaytypeid(EpPayType.WEIXIN);
	                payinfo.setPlordercode(transaction_id);
	    	    	
	    	    	epOrderServiceImpl.finishOrderPay(payinfo);
	    	    }
	    	    
	        }
	        

		}
		

	
	/** 
	* 微信回调
	* @param request 
	* @param response 
	* @throws Exception 
	*/  
	@RequestMapping(value="/wechat_notify_url",method=RequestMethod.POST)  
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
	                payinfo.setBusstypeid(EpBussType.SALE);
	                
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
			weixin_pay_url=weixinPay.getCodeUrl(ps);
			String qrcode_path="/pay/strtoqr?str="+weixin_pay_url;
			map.put("weixin", qrcode_path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("pay/scantopay",map);
	}
	
	
	
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
			codeurl = weixinPay.getCodeUrl(ps);
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
		byte[] bytes=weixinPay.encodeQrcodeToByte(c);
		return bytes;
	}
	
}
