package com.enze.ep.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpFrequency;
import com.enze.ep.entity.EpUsage;
import com.enze.ep.entity.TbProduct;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpUsageDAOTest {

	@Autowired
	EpUsageDAO epUsageDao;
	
	@Autowired
	EpProductDAO epProductDAO;
	@Autowired
	EpFrequencyDAO epFrequencyDAO;

	@Test
	public void testSelectUsageListByUsageInfo() {
		//String usage="X";
		//List<EpUsage> list=epUsageDao.selectUsageListByUsageInfo2(usage);
		//System.out.println(list.size());
		
		//EpUsage epUsage=epUsageDao.selectUsageListByUsageID(20);
		//System.out.println(epUsage.getUsage());
		
		//List<TbProduct> l= epProductDAO.selectProductByProductName("50,51,52", "1");
		//System.out.println(l.size());
		
		
		List<EpFrequency> l=	epFrequencyDAO.selectFrequencyListByFrequencyInfo("a");
		System.out.println(l.size());
	}

}
