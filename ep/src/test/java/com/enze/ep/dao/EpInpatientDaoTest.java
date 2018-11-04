package com.enze.ep.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpInpatient;

import lombok.extern.slf4j.Slf4j;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpInpatientDaoTest {
	
	@Autowired
	EpInpatientDao epInpatientDao;

	@Test
	public void testAddInpatient() {
		EpInpatient epInpatient=new EpInpatient();
		epInpatient.setAge(10);
		epInpatient.setBedno("1002床");
		epInpatient.setDescribe("还好");
		epInpatient.setHisinid(102);
		epInpatient.setIdcard("223312144656223233");
		epInpatient.setMemo("还好的");
		epInpatient.setName("张三");
		epInpatient.setSectionid(1);
		epInpatient.setUsestatus(1);
		epInpatientDao.addInpatient(epInpatient);
	}

	@Test
	public void testSelectInpatientByInpatientName() {
		String inpatientName="312";
		List<EpInpatient> list=epInpatientDao.selectInpatientByInpatientName(inpatientName);
		log.info("list size:"+list.size());
	}

}
