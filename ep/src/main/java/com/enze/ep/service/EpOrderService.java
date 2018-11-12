package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpPayInfo;

public interface EpOrderService {
    void saveEpOrder(EpOrder epOrder,List<EpOrders> list) throws Exception;
    EpOrder findEpOrderById(int orderid);
    List<EpOrders> findEpOrdersListByOrderid(int orderid);
    void finishOrderPay(EpPayInfo payinfo) throws Exception ;
}
