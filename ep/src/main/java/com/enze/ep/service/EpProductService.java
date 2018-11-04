package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.EpProduct;

public interface EpProductService {

	public List<EpProduct> findEpProductListByProductNameAndCounters(String counterids,String productName);
}
