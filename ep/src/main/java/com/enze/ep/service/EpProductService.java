package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.EpProduct;
import com.enze.ep.entity.EpProductPrice;

public interface EpProductService {

	public List<EpProduct> findEpProductListByProductNameAndCounters(String counterids,String productName);

	public EpProductPrice findEpProductPriceByProductid(int iproductid);

}
