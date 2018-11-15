package com.enze.ep.utils.alipay;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.enze.ep.entity.EpBussType;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.EpPayType;
import com.enze.ep.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AliPay {
	
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

    
    public static void main(String[] agur) {
    	String outTradeNo="2018111408110366845056";
    	new AliPay().queryOrderUsestatusByOutTradeNo(outTradeNo);
    }

	
}
