package com.enze.ep.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrderType;
import com.enze.ep.entity.EpOrderUsestatus;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.service.EpOrderService;
import com.enze.ep.utils.alipay.AliPay;
import com.enze.ep.utils.wx.WeixinPay;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QueryJob {
	
	@Autowired
	EpOrderService epOrderServiceImpl;
	
	@Autowired
	AliPay aliPay;
	
	@Autowired
	WeixinPay weixinPay;

  //10秒钟之后开始，每5秒一次
  @Scheduled(initialDelay = 10000,fixedRate = 5000)
  public void queryOrderPayState(){
	  log.info("*****************Start queryOrderPayState***************");
	  int ordertype=EpOrderType.sales_order;
	  int usestatus=EpOrderUsestatus.initial;
	  int minutes=-1000;
	  int flagsendstore=0;
	  //查询未结账的销售单据  时间   类型等因数查询
	  List<EpOrder> list=epOrderServiceImpl.findOrderListByOrderTypeAndUsestatusAndMinutesAOB(ordertype, usestatus, minutes,flagsendstore);
	  for(EpOrder order:list) {
		  doQuery(order);
	  }
	  log.info("*****************end queryOrderPayState*****************");
  
  }
  
  
  private void doQuery(EpOrder order) {
	 new Runnable() {//支付宝
		@Override
		public void run() {
			EpPayInfo payinfo=aliPay.queryOrderUsestatusByOrder(order);
			if(payinfo!=null && "TRADE_SUCCESS".equals(payinfo.getTradestatus()) ) {
				try {
					epOrderServiceImpl.finishOrderPay(payinfo);
				} catch (Exception e) {
					log.error("商家单号向alipay查询成功，但修改本地单据状态出错："+order.getOrdercode()+e.getMessage());
				}
			}
		}
	}.run();
	
	new Runnable() {//微信
		@Override
		public void run() {
			try {
				EpPayInfo payinfo=weixinPay.queryOrderUsestatusByOrder(order);
				if(payinfo!=null && "SUCCESS".equals(payinfo.getTradestatus())) {
					epOrderServiceImpl.finishOrderPay(payinfo);
				}
			} catch (Exception e) {
				log.error("商家单号向alipay查询成功，但修改本地单据状态出错："+order.getOrdercode()+e.getMessage());
			}
		}
	}.run();
	  
	  
  }


}
