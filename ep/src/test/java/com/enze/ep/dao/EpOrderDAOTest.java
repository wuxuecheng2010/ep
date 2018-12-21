package com.enze.ep.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpOrder;
import com.enze.ep.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpOrderDAOTest {

	@Autowired
	EpOrderDAO epOrderDAO;

	@Test
	public void testAddOrder() {
		EpOrder eporder = new EpOrder();
		eporder.setOrdercode("55512333451119");
		eporder.setCreuserid(1);
		eporder.setSectionid(1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date birthday = null;
		try {
			birthday = sf.parse("2001-10-24");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Date birthday=new Date("2001-10-24");
		eporder.setBirthday(birthday);
		BigDecimal decimal = new BigDecimal("3.552");

		// decimal.setScale(2);
		BigDecimal setScale = decimal.setScale(2, BigDecimal.ROUND_HALF_DOWN);
		eporder.setOrdermoney(setScale);
		eporder.setMemo("测试2");
		int orderid = 0;
		epOrderDAO.addOrder(eporder);
		orderid = eporder.getOrderid();
		log.info("orderid:" + orderid);
	}

	@Test
	public void testSelectOrderByOrderid() {
		EpOrder eporder = epOrderDAO.selectOrderByOrderid(6);
		log.info(eporder.getMemo());
	}

	@Test
	public void testSelectOrderListByUserIdAndCredate() {
		String sdate="2018-12-12";
		String edate="2018-12-13";
		
		String _edate=DateUtils.getDateStringAfterX(edate, DateUtils.SHORT_DATETIME_FORMAT, 1);
		/*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d=sdf.parse(edate);
			Calendar c = Calendar.getInstance(); 
			c.setTime(d);  
			c.add(Calendar.DAY_OF_MONTH,1);
			Date d2=c.getTime();
			 _edate=sdf.format(d2);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//List<EpOrder> list = epOrderDAO.selectOrderListByUserIdAndCredate(11, "2018-12-12", _edate);
		//System.out.println(list.size());
	}

}
