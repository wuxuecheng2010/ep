package com.enze.ep.job;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderProcessingJobTest {
	
	@Autowired
	OrderProcessingJob orderProcessingJob;
	@Test
	public void testSalesOrderProcessing() {
		try {
			orderProcessingJob.salesOrderProcessing();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSalesRefundProcessing() {
		try {
			orderProcessingJob.salesRefundProcessing();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
