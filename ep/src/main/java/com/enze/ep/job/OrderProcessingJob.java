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
	

  /**
   * 
  * @Title: salesOrderProcessing
  * @Description: 护士订单处理
  * @param @throws Exception    参数
  * @author wuxuecheng
  * @return void    返回类型
  * @throws
   */
  @Scheduled(initialDelay = 20000,fixedRate = 15000)
  public void salesOrderProcessing()throws Exception{
	  log.info("Start salesOrderProcessing");
	  int ordertype=EpOrderType.sales_order;
	  int usestatus=EpOrderUsestatus.payed;
	  int minutes=-100000;
	  int flagsendstore=0;
	  //查询未结账的销售单据  时间   类型等因数查询
	  List<EpOrder> list=epOrderServiceImpl.findOrderListByOrderTypeAndUsestatusAndMinutesAOB(ordertype, usestatus, minutes,flagsendstore);
	  
	  for(EpOrder order:list) {
		  epOrderServiceImpl.doSendSalesOrderToStore(order);
	  }
	  log.info("End salesOrderProcessing");
  
  }
  
  /**
   * 
  * @Title: salesRefundProcessing
  * @Description: 在线销售 退货处理过程
  * @param @throws Exception    参数
  * @author wuxuecheng
  * @return void    返回类型
  * @throws
   */
@Scheduled(initialDelay = 20000,fixedRate = 15000)
  public void salesRefundProcessing()throws Exception{
	  log.info("Start salesRefundProcessing");
	  int ordertype=EpOrderType.sales_order_refund;
	  int usestatus=EpOrderUsestatus.payed;
	  int minutes=-100000;
	  int flagsendstore=0;
	  //查询未结账的销售单据  时间   类型等因数查询
	  List<EpOrder> list=epOrderServiceImpl.findOrderListByOrderTypeAndUsestatusAndMinutesAOB(ordertype, usestatus, minutes,flagsendstore);
	  
	  for(EpOrder order:list) {
		  epOrderServiceImpl.doSendRefundOrderToStore(order);
	  }
	  log.info("End salesOrderProcessing");
  
  }
  
  
  /**
   * 
  * @Title: epOrderProcessing
  * @Description: 电子处方处理 （因提前算好库存id  及时性问题差 作废）
  * @param @throws Exception    参数
  * @author wuxuecheng
  * @return void    返回类型
  * @throws
   */
  //@Scheduled(initialDelay = 30000,fixedRate = 15000)
  /*public void epOrderProcessing()throws Exception{
	  log.info("Start epOrderProcessing");
	  int ordertype=EpOrderType.electronic_prescribing;
	  int usestatus=EpOrderUsestatus.initial;
	  int minutes=-100000;
	  int flagsendstore=0;
	  //查询未结账的销售单据  时间   类型等因数查询
	  List<EpOrder> list=epOrderServiceImpl.findOrderListByOrderTypeAndUsestatusAndMinutesAOB(ordertype, usestatus, minutes,flagsendstore);
	  
	  for(EpOrder order:list) {
		  epOrderServiceImpl.doSendEpOrderToStore(order);
	  }
	  log.info("End epOrderProcessing");
  
  }*/
  

}
