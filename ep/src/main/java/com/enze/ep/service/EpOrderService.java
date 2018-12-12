package com.enze.ep.service;

import java.util.Date;
import java.util.List;

import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrderStock;
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
    List<EpOrder> findOrderListByOrderTypeAndUsestatusAndMinutesAOB(int ordertype, int usestatus, int minutes,int flagsendstore);
    
    List<EpOrders> findEpOrdersListByOrderid(int orderid);
    void finishOrderPay(EpPayInfo payinfo) throws Exception ;
    
    /**
     * 
    * @Title: setOrderWeixinNonceStr
    * @Description: TODO(记录商家订单下单时的随机码)
    * @param @param ordercode
    * @param @param noncestr    参数
    * @author wuxuecheng
    * @return void    返回类型
    * @throws
     */
    void recordOrderWeixinNonceStr(String ordercode,String noncestr);
    
    
    /**
     * 
    * @Title: doSendEPOrderToStore
    * @Description: TODO(处方单处理成分部门的临时数据)
    * @param @param order    参数
    * @author wuxuecheng
    * @return void    返回类型
    * @throws
     */
    void doSendEpOrderToStore(EpOrder order)throws Exception;
    
    /**
     * 
    * @Title: doSendOrderToStore
    * @Description: TODO(普通订单发送到药店系统 做账)
    * @param @param order    参数
    * @author wuxuecheng
    * @return void    返回类型
    * @throws
     */
    void doSendSalesOrderToStore(EpOrder order) throws Exception;
    
    
    void saveEpOrderStock(EpOrder order) throws Exception;
    
    /**
     * 
    * @Title: createSalesOrderByOrder
    * @Description: TODO(创建销售单，并且返回销售单号)
    * @param @param order
    * @param @return    参数
    * @author wuxuecheng
    * @return List<String>    返回类型
    * @throws
     */
    List<String> createSalesOrderByOrder(EpOrder order);
    
    boolean createSalesOrderDoc(String vcbillno,EpOrder order,int idepartid,int flagep);
    
    boolean createSalesOrderDtl(String vcbillno,List<EpOrderStock> orderStockList);
    
}
