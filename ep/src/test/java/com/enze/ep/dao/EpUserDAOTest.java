package com.enze.ep.dao;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpUser;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpUserDAOTest {

	@Autowired
	EpUserDAO epUserDAO;
	
	@Test
	public void testAddUser() {
		EpUser epuser=new EpUser();
		epuser.setUsername("wangxx");
		epuser.setUsertype(2);
		epuser.setUsercode("100033");
		epuser.setSectionid(1);
		epuser.setPassword("1");
		epuser.setMemo("护士长");
		int n=epUserDAO.addUser(epuser);
		System.out.println("保存成功："+n);
		//fail("Not yet implemented");
	}

	@Test
	public void testSelectUserByUserName() {
		//fail("Not yet implemented");
		EpUser epuser=epUserDAO.selectUserByUserName("wangxiaomin");
		log.info(epuser.getUsername());
		Assert.assertEquals("123456", epuser.getPassword());
	}

	@Test
	public void testUpdateUserPsdByName() {
		EpUser epuser=epUserDAO.selectUserByUserName("wangxiaomin");
		epuser.setPassword("654321");
		epUserDAO.updateUserPsdByName(epuser);
		epuser=epUserDAO.selectUserByUserName("wangxiaomin");
		Assert.assertEquals("654321", epuser.getPassword());
	}

}
