package com.enze.ep.utils.alipay;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.enze.ep.dao.EpPayInfoDAO;
import com.enze.ep.entity.EpBussType;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.EpPayType;
import com.enze.ep.entity.EpResult;
import com.enze.ep.enums.OrderType;
import com.enze.ep.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AliPay {
	
	@Autowired
	EpPayInfoDAO epPayInfoDAO;
	
	//private static Log                  log = LogFactory.getLog(Main.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService   tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
    private static AlipayMonitorService monitorService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
       // Configs.init("zfbinfo.properties");
    	init();

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
            .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
            .setFormat("json").build();
    }
    
    private static void init() {
    	Configs.setAlipayPublicKey(AlipayConfig.alipay_public_key);
        Configs.setAppid(AlipayConfig.appid);
        Configs.setCancelDuration(AlipayConfig.cancel_duration);
        Configs.setHeartbeatDelay(AlipayConfig.heartbeat_delay);
        Configs.setHeartbeatDuration(AlipayConfig.heartbeat_duration);
        Configs.setMaxCancelRetry(AlipayConfig.max_cancel_retry);
        Configs.setMaxQueryRetry(AlipayConfig.max_query_retry);
        Configs.setMcloudApiDomain(AlipayConfig.mcloud_api_domain);
        Configs.setOpenApiDomain(AlipayConfig.open_api_domain);
        Configs.setPid(AlipayConfig.pid);
        Configs.setPrivateKey(AlipayConfig.private_key);
        Configs.setPublicKey(AlipayConfig.public_key);
        Configs.setQueryDuration(AlipayConfig.query_duration);
        Configs.setSignType(AlipayConfig.sign_type);
    }
    
    public  String getCodeUrl(AlipayTradePrecreateRequestBuilder builder) {
    	String alipay_pay_url="";
      

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");
                
                AlipayTradePrecreateResponse response = result.getResponse();
                alipay_pay_url=response.getQrCode().replace("\\/", "//");
                break;

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
    
        
    	
    	return alipay_pay_url;
    }
    
    //支付宝回调
    public  boolean alipayCallBack(HashMap<String, String> map) {
    	boolean bool=false;
    	 try {
	            //Configs.init("zfbinfo.properties");
	            bool = AlipaySignature.rsaCheckV2(map, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
	            if (!bool){
	                //return ServerResponse.createByError("非法请求，再请求老子就报警了");
	            	log.warn("有人测试非法攻击.");
	            }

	        } catch (AlipayApiException e) {
	            log.error("支付宝验证回调异常",e);
	        }
    	return bool;
    }
    
    
    public  EpPayInfo queryOrderUsestatusByOrder(EpOrder order) {
    	return queryOrderUsestatusByOutTradeNo(order.getOrdercode());
    }
    /**
     * 
    * @Title: queryAlipayOrderUsestatus
    * @Description: TODO(查询alipay订单支付状态)
    * @param @param outTradeNo    商家订单号
    * @author wuxuecheng
    * @return void    返回类型
    * @throws
     */
    public  EpPayInfo queryOrderUsestatusByOutTradeNo(String outTradeNo) {
    	EpPayInfo payinfo=null;
    	String tradeStatus="";
    	if(!"".equals(outTradeNo) && outTradeNo!=null){

    		AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

    		// (必填) 商户订单号，通过此商户订单号查询当面付的交易状态
            AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
            		.setOutTradeNo(outTradeNo);
    		AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
    		switch (result.getTradeStatus()) {
    			case SUCCESS:
    				//log.info("查询返回该订单支付成功: )");
    				AlipayTradeQueryResponse resp = result.getResponse();
    				tradeStatus=resp.getTradeStatus();
    				payinfo=new EpPayInfo();
	                //payinfo.setAttach(attach);
	                payinfo.setOrdercode(outTradeNo);
	                payinfo.setFee(resp.getBuyerPayAmount());
	                payinfo.setPaydate(DateUtils.formatDateToDefaultStr(resp.getSendPayDate()));
	                payinfo.setPaytypeid(EpPayType.ALIPAY);
	                payinfo.setPlordercode(resp.getTradeNo());
	                payinfo.setBusstypeid(EpBussType.SALE);
	                payinfo.setTradestatus(tradeStatus);
    				//log.info(resp.getTradeStatus());
    				break;

    			case FAILED:
    				log.error("查询返回该订单支付失败!!!");
    				break;

    			case UNKNOWN:
    				log.error("系统异常，订单支付状态未知!!!");
    				break;

    			default:
    				log.error("不支持的交易状态，交易返回异常!!!");
    				break;
    		}
 
    	}
    	return payinfo;
    }

    
    //退款操作
    /**
     * 
    * @Title: tradeRefund
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @param outTradeNo (必填) 外部订单号，需要退款交易的商户外部订单号
    * @param @param refundAmount (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
    * @param @param outRequestNo (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
    * @param @param refundReason (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
    * @param @param storeId     (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
    * @param @param myrefundno   商家退货单号  不是必须的   主要是记录一下而已
    * @author wuxuecheng
    * @return EpResult    返回类型
    * @throws
     */
    public EpResult tradeRefund(String outTradeNo,String refundAmount,
    		String outRequestNo,String refundReason,String storeId,String original_ordercode ) {
    	
    	//支付结果
    	EpResult epResult=new EpResult();
        /*// (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = "tradepay14817938139942440181";

        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = "0.01";

        // (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，
        // 对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        String outRequestNo = "";

        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "正常退款，用户买多了";

        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
        String storeId = "test_store_id";*/

        // 创建退款请求builder，设置请求参数
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
            .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason)
            .setOutRequestNo(outRequestNo).setStoreId(storeId);

        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        //String refundtradno=result.getResponse().getTradeNo();
        String code=result.getResponse().getCode();
        String msg=result.getResponse().getMsg();
        String tradestatus=result.getTradeStatus().name();
        String sub_code=result.getResponse().getSubCode();
    	String sub_msg=result.getResponse().getSubMsg();
        if("10000".equals(code)) {
        	String fundchange=result.getResponse().getFundChange();
        	if("Y".equals(fundchange)) {
        		EpPayInfo epPayInfo=new EpPayInfo();
    			epPayInfo.setPaytypeid(1);//支付宝支付
    			epPayInfo.setBusstypeid(OrderType.salebackorder.getTypeValue());
    			epPayInfo.setOrdercode(original_ordercode);
    			String plordercode=result.getResponse().getTradeNo();
    			epPayInfo.setPlordercode(plordercode);
    			BigDecimal fee =new BigDecimal( result.getResponse().getSendBackFee()).setScale(2);
    			epPayInfo.setFee(fee.toString());
    			epPayInfo.setAttach("支付宝退款");
    			epPayInfo.setTradestatus(result.getTradeStatus().name());
    			//保存支付情况
    			epPayInfoDAO.addPayInfo(epPayInfo);
    			epResult=new EpResult(EpResult.SUCCESS,"支付宝退款成功，请顾客留意支付宝退款信息","");
        	}else {
        		epResult=new EpResult(EpResult.FAIL,"支付宝退款失败，可能已经退款成功","");
        	}
        	
			
        }else {
        	//String sub_code=result.getResponse().getSubCode();
        	//String sub_msg=result.getResponse().getSubMsg();
        	epResult=new EpResult(EpResult.FAIL,"code:"+code+",msg:"+msg,"sub_code:"+sub_code+",sub_msg:"+sub_msg);
        }
        

/*        switch (result.getTradeStatus()) {
            case SUCCESS:
            	
            	EpPayInfo epPayInfo=new EpPayInfo();
				epPayInfo.setPaytypeid(1);//支付宝支付
				epPayInfo.setBusstypeid(OrderType.salebackorder.getTypeValue());
				epPayInfo.setOrdercode(myrefundno);
				String plordercode=result.getResponse().getTradeNo();
				epPayInfo.setPlordercode(plordercode);
				BigDecimal fee =new BigDecimal( result.getResponse().getSendBackFee()).setScale(2);
				epPayInfo.setFee(fee.toString());
				epPayInfo.setAttach("支付宝退款");
				epPayInfo.setTradestatus(result.getTradeStatus().name());
				//保存支付情况
				epPayInfoDAO.addPayInfo(epPayInfo);
				epResult=new EpResult(EpResult.SUCCESS,"支付宝退款成功，请顾客留意支付宝退款信息","");
                log.info("支付宝退款成功: )");
                break;

            case FAILED:
            	epResult=new EpResult(EpResult.FAIL,"支付宝退款失败!!!","TradeStatus:"+result.getTradeStatus());
                //log.error("支付宝退款失败!!!");
                break;

            case UNKNOWN:
            	epResult=new EpResult(EpResult.FAIL,"系统异常，订单退款状态未知!!!","TradeStatus:"+result.getTradeStatus());
                //log.error("系统异常，订单退款状态未知!!!");
                break;

            default:
            	epResult=new EpResult(EpResult.FAIL,"不支持的交易状态，交易返回异常!!!","TradeStatus:"+result.getTradeStatus());
                //log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }*/
        
        return epResult;
    }
    
    
    public static void main(String[] agur) {
    	String outTradeNo="2018111408110366845056";
    	new AliPay().queryOrderUsestatusByOutTradeNo(outTradeNo);
    }

	
}
