package com.enze.ep.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpCounter;

import lombok.extern.slf4j.Slf4j;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpCounterDAOTest {
	
	@Autowired
	EpCounterDAO epCounterDao;

	@Test
	public void testSelectCounterBySectionid() {
		int sectionid=1;
		List<EpCounter>  list=epCounterDao.selectCounterBySectionid(sectionid);
		log.info("list size:"+list.size());
	}

}
