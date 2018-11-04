package com.enze.ep.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpProductDAO;
import com.enze.ep.entity.EpProduct;
import com.enze.ep.service.EpProductService;

@Service
public class EpProductServiceImpl implements EpProductService{
	@Autowired
	EpProductDAO epProductDAO;
	
	@Override
	public List<EpProduct> findEpProductListByProductNameAndCounters(String counterids, String productName) {
		return epProductDAO.selectProductByProductName(counterids, productName);
	}

}
