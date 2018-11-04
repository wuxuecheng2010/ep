package com.enze.ep.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpProduct;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpProductDAOTest {
	
	@Autowired
	EpProductDAO epProductDAO;

	@Test
	public void testSelectProductByProductName() {
		String counterids="52";
		//String productName="吸痰杯（一次性）";
		String productName="YC";
		List<EpProduct> list=epProductDAO.selectProductByProductName(counterids, productName);
		log.info("list length:"+list.size());
	}

}
