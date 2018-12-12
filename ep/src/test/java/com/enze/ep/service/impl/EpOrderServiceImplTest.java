package com.enze.ep.service.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.dao.EpOrderDAO;
import com.enze.ep.dao.EpOrdersDAO;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.service.EpOrderService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpOrderServiceImplTest {
    
	@Autowired
	EpOrderService epOrderServiceImpl;
	@Autowired
	EpOrderDAO epOrderDAO;
	@Autowired
	EpOrdersDAO epOrdersDAO;
	@Test
	public void testSaveEpOrder() {
		
		EpOrder epOrder=epOrderDAO.selectOrderByOrderid(5);
		epOrder.setOrdercode("888888");
		List<EpOrders> list=epOrdersDAO.selectOrdersByOrderid(5);
		try {
			epOrderServiceImpl.saveEpOrder(epOrder, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testFindEpOrderById() {
		
		EpOrder epOrder= epOrderServiceImpl.findEpOrderById(10);
		System.out.println(epOrder.getAddress());
	}
	@Test
	public void testFindEpOrdersListByOrderid() {
		List<EpOrders> list=epOrderServiceImpl.findEpOrdersListByOrderid(10);
		System.out.println(list.size());
		
	}
	
	@Test
	public void testSaveOrderStock() {
		
		EpOrder order=epOrderServiceImpl.findEpOrderById(36);
		try {
			epOrderServiceImpl.saveEpOrderStock(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testDoSendEpOrderToStore() {
		EpOrder order=epOrderServiceImpl.findEpOrderById(10);
		try {
			epOrderServiceImpl.doSendEpOrderToStore(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLog() {
		log.error("hello {} ,good by{} e","ooooo",5555);
	}

}
