package com.enze.ep.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpProductDAO;
import com.enze.ep.entity.TbProduct;
import com.enze.ep.entity.TbProductPrice;
import com.enze.ep.service.EpProductService;

@Service
public class EpProductServiceImpl implements EpProductService{
	
	@Autowired
	EpProductDAO epProductDAO;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List<TbProduct> findEpProductListByProductNameAndCounters(String counterids, String productName) {
		return epProductDAO.selectProductByProductName(counterids, productName);
	}

	@Override
	public TbProductPrice findEpProductPriceByProductid(int iproductid) {
		String key=TbProductPrice.Prefix_Redis_Key+TbProductPrice.Prefix_Redis_Key_Separtor+iproductid;
		TbProductPrice epProductPrice=(TbProductPrice) redisTemplate.opsForValue().get(key);
        if(epProductPrice==null) {
        	epProductPrice=epProductDAO.selectProductByProductid(iproductid);
        	redisTemplate.opsForValue().set(key, epProductPrice);
        	redisTemplate.expire(key, 10, TimeUnit.SECONDS);
        }

		return epProductPrice;
	}

}
