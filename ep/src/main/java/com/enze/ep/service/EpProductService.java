package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.TbProduct;
import com.enze.ep.entity.TbProductPrice;

public interface EpProductService {

	public List<TbProduct> findEpProductListByProductNameAndCounters(String counterids,String productName);

	public TbProductPrice findEpProductPriceByProductid(int iproductid);

}
