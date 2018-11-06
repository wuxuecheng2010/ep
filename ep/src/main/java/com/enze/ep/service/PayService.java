package com.enze.ep.service;

public interface PayService {
  String weixinPay(String userId, String productId) throws Exception;
}
