package com.enze.ep.utils.alipay;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AliPayTest {
	
	@Autowired
	AliPay aliPay;

	@Test
	public void testQueryAlipayOrderUsestatus() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testTradeRefund() {
		String outTradeNo="2018111416111550045056";
		String refundAmount="0.01";
		String outRequestNo="";
		String refundReason="买多了";
		String storeId="1230";
/*		aliPay.tradeRefund(outTradeNo, 
				refundAmount, 
				outRequestNo, 
				refundReason, 
				storeId);*/
	}

}
