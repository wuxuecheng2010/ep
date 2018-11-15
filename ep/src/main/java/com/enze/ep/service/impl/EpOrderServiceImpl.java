package com.enze.ep.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enze.ep.dao.EpOrderDAO;
import com.enze.ep.dao.EpOrderStockDAO;
import com.enze.ep.dao.EpOrdersDAO;
import com.enze.ep.dao.EpPayInfoDAO;
import com.enze.ep.dao.EpStockProductInfoDAO;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrderStock;
import com.enze.ep.entity.EpOrderType;
import com.enze.ep.entity.EpOrderUsestatus;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.EpStockProductInfo;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.service.EpOrderService;

@Service
public class EpOrderServiceImpl implements EpOrderService {

	@Autowired
	EpOrderDAO epOrderDAO;

	@Autowired
	EpOrdersDAO epOrdersDAO;
	
	@Autowired
	EpPayInfoDAO epPayInfoDAO;
	
	/*@Autowired
	EpCounterDAO epCounterDAO;*/
	
	@Autowired
	EpStockProductInfoDAO epStockProductInfoDAO;
	
	@Autowired
	EpCounterService epCounterServiceImpl;
	
	@Autowired
	EpOrderStockDAO epOrderStockDAO;
	
	@Autowired
	RedisTemplate redisTemplate;

	@Transactional
	@Override
	public void saveEpOrder(EpOrder epOrder, List<EpOrders> list) throws Exception {
		epOrderDAO.addOrder(epOrder);
		int orderid = epOrder.getOrderid();
		for (EpOrders epOrders : list) {
			epOrders.setOrderid(orderid);
			epOrdersDAO.addOrders(epOrders);
		}
		
	}

	@Override
	public EpOrder findEpOrderById(int orderid) {

		String key=EpOrder.Prefix_Redis_Key+EpOrder.Prefix_Redis_Key_Separtor+orderid;
		EpOrder epOrder=(EpOrder) redisTemplate.opsForValue().get(key);
        if(epOrder==null) {
        	epOrder=epOrderDAO.selectOrderByOrderid(orderid);
        	redisTemplate.opsForValue().set(key, epOrder);
        	redisTemplate.expire(key, 150, TimeUnit.SECONDS);
        }
		return epOrder;
		//return epOrderDAO.selectOrderByOrderid(orderid);
	}


	@Override
	public EpOrder findEpOrderByIdRealTime(int orderid) {
		EpOrder epOrder=epOrderDAO.selectOrderByOrderid(orderid);
		return epOrder;
	}

	

	@Override
	public List<EpOrders> findEpOrdersListByOrderid(int orderid) {
		String key=EpOrders.Prefix_Redis_Key+EpOrders.Prefix_Redis_Key_Separtor+orderid;
		
		List<EpOrders> list=(List<EpOrders>) redisTemplate.opsForValue().get(key);
        if(list==null) {
        	list=epOrdersDAO.selectOrdersByOrderid(orderid);
        	redisTemplate.opsForValue().set(key, list);
        	redisTemplate.expire(key, 150, TimeUnit.SECONDS);
        }
		return list;
	}

	
	@Transactional
	@Override
	public void finishOrderPay(EpPayInfo payinfo)throws Exception {
		//记录支付日志
		epPayInfoDAO.addPayInfo(payinfo);
		//更新支付信息
		epOrderDAO.updateOrderByPayInfo(payinfo);
		
		//启动药店系统记账
	}

	@Override
	public List<EpOrder> findOrderListByOrderTypeAndUsestatusAndMinutesAOB(int ordertype, int usestatus, int minutes,int flagsendstore) {
		List<EpOrder> list=epOrderDAO.selectOrderByOrderTypeAndUsestatusAndMinutesAndFlagSendStoreAOB(ordertype, usestatus, minutes,flagsendstore);
		return list;
	}

	@Override
	public void recordOrderWeixinNonceStr(String ordercode, String noncestr) {
		epOrderDAO.updateOrderWeixinNonceStrByCode(ordercode, noncestr);
	}

	@Transactional
	@Override
	public void doSendOrderToStore(EpOrder order) {
		
		int usestatus=order.getUsestatus();//是否付款
		int ordertype=order.getOrdertype();//普通销售单类型
		int flagsendstore=order.getFlagsendstore();//是否发生到药店系统
		int orderid=order.getOrderid();//商家订单号
		int sectionid=order.getSectionid();//科室或者住院病区
		
		//判断订单状态
		if(usestatus==EpOrderUsestatus.payed && ordertype==EpOrderType.sales_order && flagsendstore==0) {
			//获取订单明细数据
			List<EpOrders> orderslist=epOrdersDAO.selectOrdersByOrderid(orderid);
			String counterids=epCounterServiceImpl.findCouteridsBySectionid(sectionid);
			
			//根据orders和柜台  创建临时的中间表数据ep_order_stock
			saveOrderStock(orderslist,counterids);
			
			//创建药店订单主表
			
			//创建药店订单细表
			
			//提交数据处理存储过程
			
			//更改销售单据flagsendstore标志
			
		}
		
	}

	@Override
	public void saveOrderStock(List<EpOrders> orderslist, String counterids) {
		for(EpOrders epOrders:orderslist) {
			int iproductid=epOrders.getIproductid();
			BigDecimal numprice= epOrders.getNumprice();
			BigDecimal totalcounts= epOrders.getTotalcounts();
			BigDecimal needcount=totalcounts;//需要的库存数量
			List<EpStockProductInfo> list=epStockProductInfoDAO.selectStockProductInfoListByProductIDAndCounterIDS(iproductid, counterids);
			for(int i=0;i<list.size();i++) {
				EpStockProductInfo epStockProductInfo=list.get(i);
				BigDecimal numstocks= epStockProductInfo.getNumstocks();
				needcount=needcount.subtract(numstocks);//需求-库存
				int n=needcount.compareTo(BigDecimal.ZERO);
				EpOrderStock epOrderStock=new EpOrderStock();
				int orderid=epOrders.getOrderid();
				int ordersid=epOrders.getOrdersid();
				int idepartid=epStockProductInfo.getIdepartid();
				int stockisid=epStockProductInfo.getIsid();
				epOrderStock.setOrderid(orderid);
				epOrderStock.setOrdersid(ordersid);
				epOrderStock.setIdepartid(idepartid);
				epOrderStock.setStockisid(stockisid);
				if(n<=0) {
					//创建 ep_order_stock对象并保存   
					BigDecimal qty=epOrders.getTotalcounts();
					epOrderStock.setQty(qty);
					epOrderStockDAO.addOrderStock(epOrderStock);
					break;//跳出
				}else {
					//占用当前库存行
					BigDecimal qty=epStockProductInfo.getNumstocks();
					epOrderStock.setQty(qty);
					epOrderStockDAO.addOrderStock(epOrderStock);
				}
				
			}
		}
		
	}





}
