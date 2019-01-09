package com.enze.ep.service.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.EpFrequency;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EpFrequencyServiceImplTest {
	@Autowired
	EpFrequencyServiceImpl epFrequencyServiceImpl;
	@Test
	public void testFindFrequencyListByFrequency() {
		List<EpFrequency> l=epFrequencyServiceImpl.findFrequencyListByFrequency("a'");
		System.out.println(l.size());
	}

}
