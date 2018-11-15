package com.enze.ep.utils.wx;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpPayInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeixinPayTest {

	@Autowired
	WeixinPay weixinPay;
	@Test
	public void testGetCodeUrl() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeQrcode() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeQrcodeToByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testToBufferedImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testUrlEncode() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryOrderUsestatusByEpOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryOrderUsestatus() {

		try {
		EpPayInfo payinfo=	weixinPay.queryOrderUsestatusByOrderCodeAndWeixinNonceStr("2018111408110366845056","0825038943");
			System.out.println(payinfo.getOrdercode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
