package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpPayInfo;

public interface EpOrderService {
    void saveEpOrder(EpOrder epOrder,List<EpOrders> list) throws Exception;
    EpOrder findEpOrderById(int orderid);
    /**
     * 
    * @Title: findEpOrderByIdRealTime  实时查询订单信息  及时性较高
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @param orderid
    * @param @return    参数
    * @author wuxuecheng
    * @return EpOrder    返回类型
    * @throws
     */
    EpOrder findEpOrderByIdRealTime(int orderid) ;
    List<EpOrders> findEpOrdersListByOrderid(int orderid);
    void finishOrderPay(EpPayInfo payinfo) throws Exception ;
}
