package com.enze.ep.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enze.ep.dao.EpOrderDAO;
import com.enze.ep.dao.EpOrdersDAO;
import com.enze.ep.dao.EpPayInfoDAO;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.service.EpOrderService;

@Service
public class EpOrderServiceImpl implements EpOrderService {

	@Autowired
	EpOrderDAO epOrderDAO;

	@Autowired
	EpOrdersDAO epOrdersDAO;
	
	@Autowired
	EpPayInfoDAO epPayInfoDAO;

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
		return epOrderDAO.selectOrderByOrderid(orderid);
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



}
