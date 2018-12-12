package com.enze.ep.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.WlAmdParam;
import com.enze.ep.service.HistoryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HistoryServiceImplTest {

	@Autowired
	HistoryService historyService;
	@Test
	public void testGetAmdInfo() {
		WlAmdParam wlAmdParam=new WlAmdParam(
				"JSON.LQYF.GetInfo",
				"67698D4CD7D57094F00A6C558D0B9D4444676237",
				"100852",
				"11097140",
				"1613611");
		historyService.getAmdInfo(wlAmdParam);
	}

}
