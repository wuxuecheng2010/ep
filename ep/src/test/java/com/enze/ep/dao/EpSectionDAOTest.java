package com.enze.ep.dao;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpSection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpSectionDAOTest {

	@Autowired
	EpSectionDAO epSectionDAO;

	@Test
	public void testSelectSectionByID() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectSectionByHissectionid() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddSection() {
		EpSection epSection = new EpSection();
		epSection.setHissectionid("332");
		epSection.setSectionname("小儿科");
		epSection.setUsestatus("1");
		int x=epSectionDAO.addSection(epSection);
		System.out.println("sectionid:"+epSection.getSectionid());
	}

}
