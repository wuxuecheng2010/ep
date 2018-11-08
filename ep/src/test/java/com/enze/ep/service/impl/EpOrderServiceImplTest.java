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

}