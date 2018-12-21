package com.enze.ep.dao;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpOrders;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpOrdersDAOTest {
	
	@Autowired
	EpOrdersDAO epOrdersDAO;

	@Test
	public void testAddOrders() {
		EpOrders eporders=new EpOrders();
		eporders.setIproductid(1);
		eporders.setNummoney(new BigDecimal("532").setScale(2));
		eporders.setNumno(1);
		eporders.setNumprice(new BigDecimal("435").setScale(2));
		eporders.setOrderid(5);
		eporders.setTotalcounts(new BigDecimal("32").setScale(0));
		eporders.setVcproductcode("1223");
		eporders.setVcstandard("zhengc");
		eporders.setVcunitname("tai");
		eporders.setVcuniversalname("有点意思fff");
		int i=epOrdersDAO.addOrders(eporders);
		
		log.info("成功新增："+i+"行orders");
	}

	@Test
	public void testSelectOrdersByOrderid() {
		List<EpOrders> list=epOrdersDAO.selectOrdersByOrderid(5);
		log.info("size:"+list.size());
	}

	@Test
	public void testSelectOrdersByOrdersid() {
		EpOrders epOrders=epOrdersDAO.selectOrdersByOrdersid(1);
		log.info(epOrders.getVcproductcode());
	}
	
	@Test
	public void testUpdateOrdersBackcounts() {
		EpOrders epOrders	=epOrdersDAO.selectOrdersByOrdersid(77);
		epOrdersDAO.updateOrdersBackcounts(epOrders);
	}

}
