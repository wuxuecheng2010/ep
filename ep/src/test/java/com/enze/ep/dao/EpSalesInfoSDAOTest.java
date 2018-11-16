package com.enze.ep.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.TbSalesInfoS;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpSalesInfoSDAOTest {

	@Autowired
	EpSalesInfoSDAO epSalesInfoSDAO;
	
	@Test
	public void testAddSalesInfoS() {
		TbSalesInfoS tbSalesInfoS=new TbSalesInfoS();
		tbSalesInfoS.setVcbillno("CKTT123456");
		epSalesInfoSDAO.addSalesInfoS(tbSalesInfoS);
		
	}

}
