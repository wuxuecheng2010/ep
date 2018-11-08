package com.enze.ep.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpProductDAO;
import com.enze.ep.entity.EpProduct;
import com.enze.ep.entity.EpProductPrice;
import com.enze.ep.service.EpProductService;

@Service
public class EpProductServiceImpl implements EpProductService{
	
	@Autowired
	EpProductDAO epProductDAO;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List<EpProduct> findEpProductListByProductNameAndCounters(String counterids, String productName) {
		return epProductDAO.selectProductByProductName(counterids, productName);
	}

	@Override
	public EpProductPrice findEpProductPriceByProductid(int iproductid) {
		String key=EpProductPrice.Prefix_Redis_Key+EpProductPrice.Prefix_Redis_Key_Separtor+iproductid;
		EpProductPrice epProductPrice=(EpProductPrice) redisTemplate.opsForValue().get(key);
        if(epProductPrice==null) {
        	epProductPrice=epProductDAO.selectProductByProductid(iproductid);
        	redisTemplate.opsForValue().set(key, epProductPrice);
        	redisTemplate.expire(key, 10, TimeUnit.SECONDS);
        }

		return epProductPrice;
	}

}
