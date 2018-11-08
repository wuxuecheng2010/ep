package com.enze.ep.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpBussType;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.EpPayType;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpPayInfoDAOTest {

	@Autowired
	EpPayInfoDAO epPayInfoDAO;
	
	@Test
	public void testAddOrder() {
		EpPayInfo epPayInfo=new EpPayInfo();
		epPayInfo.setAttach("xxxx");
		epPayInfo.setBaseinfo("baseinfo");
		epPayInfo.setBusstypeid(EpBussType.SALE);
		epPayInfo.setFee("45000");
		epPayInfo.setOrdercode("100001");
		epPayInfo.setPaydate("2018-10-20");
		epPayInfo.setPaytypeid(EpPayType.WEIXIN);
		epPayInfo.setPlordercode("xxxxxxxxxxxx");
		epPayInfoDAO.addPayInfo(epPayInfo);
	}

	@Test
	public void testSelectPayInfoByOrdercode() {
		List<EpPayInfo> epPayInfo=epPayInfoDAO.selectPayInfoByOrdercode("100001");
		log.info("list size:"+epPayInfo.size());
	}

}
