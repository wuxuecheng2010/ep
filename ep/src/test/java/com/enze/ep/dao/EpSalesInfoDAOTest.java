package com.enze.ep.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.TbSalesInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpSalesInfoDAOTest {
	@Autowired
	EpSalesInfoDAO epSalesInfoDAO;

	@Test
	public void testGetPurchaseOrderNumber() {
		Map<String, Object> params = new HashMap<String, Object>(3);
		String s=epSalesInfoDAO.getSalesInfoDoc("XXKK",28);
		System.out.println("xx:"+s);
	}
	
	@Test
	public void testAddSalesInfo() {
		TbSalesInfo tbSalesInfo=new TbSalesInfo();
		tbSalesInfo.setVcbillno("CKTT123456");
		tbSalesInfo.setDyqk(0);
		
		epSalesInfoDAO.addSalesInfo(tbSalesInfo);
	}
	
	@Test
	public void testApprovelSalesInfo() {
		
		int res=epSalesInfoDAO.approvelSalesInfo("LSKP181116003","Y",TbSalesInfo.DEFAULT_CREATER_MAN);
		System.out.println("****************"+res);
		
	}

}
