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
public class OrderProcessingJob {
	
	@Autowired
	EpOrderService epOrderServiceImpl;
	

  //  20秒钟之后开始，每15秒一次
  @Scheduled(initialDelay = 20000,fixedRate = 15000)
  public void processing(){
	  log.info("*****************Start OrderProcessing***************");
	  int ordertype=EpOrderType.sales_order;
	  int usestatus=EpOrderUsestatus.payed;
	  int minutes=-100000;
	  int flagsendstore=0;
	  //查询未结账的销售单据  时间   类型等因数查询
	  List<EpOrder> list=epOrderServiceImpl.findOrderListByOrderTypeAndUsestatusAndMinutesAOB(ordertype, usestatus, minutes,flagsendstore);
	  
	  for(EpOrder order:list) {
		  epOrderServiceImpl.doSendEpOrderToStore(order);
	  }
	  log.info("*****************End   OrderProcessing***************");
  
  }

}
