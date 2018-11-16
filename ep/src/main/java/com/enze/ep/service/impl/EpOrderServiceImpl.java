package com.enze.ep.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enze.ep.dao.EpOrderDAO;
import com.enze.ep.dao.EpOrderStockDAO;
import com.enze.ep.dao.EpOrdersDAO;
import com.enze.ep.dao.EpPayInfoDAO;
import com.enze.ep.dao.EpSalesInfoDAO;
import com.enze.ep.dao.EpSalesInfoSDAO;
import com.enze.ep.dao.EpStockProductInfoDAO;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrderStock;
import com.enze.ep.entity.EpOrderType;
import com.enze.ep.entity.EpOrderUsestatus;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.TbDepartMent;
import com.enze.ep.entity.TbSalesInfo;
import com.enze.ep.entity.TbSalesInfoS;
import com.enze.ep.entity.TbStockProductInfo;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.service.EpOrderService;
import com.enze.ep.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EpOrderServiceImpl implements EpOrderService {

	@Autowired
	EpOrderDAO epOrderDAO;

	@Autowired
	EpOrdersDAO epOrdersDAO;

	@Autowired
	EpPayInfoDAO epPayInfoDAO;

	@Autowired
	EpSalesInfoDAO epSalesInfoDAO;

	@Autowired
	EpSalesInfoSDAO epSalesInfoSDAO;

	@Autowired
	EpStockProductInfoDAO epStockProductInfoDAO;

	@Autowired
	EpCounterService epCounterServiceImpl;

	@Autowired
	EpOrderStockDAO epOrderStockDAO;

	@Autowired
	RedisTemplate redisTemplate;

	@Transactional
	@Override
	public void saveEpOrder(EpOrder epOrder, List<EpOrders> list) throws Exception {
		epOrderDAO.addOrder(epOrder);
		int orderid = epOrder.getOrderid();
		for (EpOrders epOrders : list) {
			epOrders.setOrderid(orderid);
			epOrdersDAO.addOrders(epOrders);
		}

	}

	@Override
	public EpOrder findEpOrderById(int orderid) {

		String key = EpOrder.Prefix_Redis_Key + EpOrder.Prefix_Redis_Key_Separtor + orderid;
		EpOrder epOrder = (EpOrder) redisTemplate.opsForValue().get(key);
		if (epOrder == null) {
			epOrder = epOrderDAO.selectOrderByOrderid(orderid);
			redisTemplate.opsForValue().set(key, epOrder);
			redisTemplate.expire(key, 150, TimeUnit.SECONDS);
		}
		return epOrder;
		// return epOrderDAO.selectOrderByOrderid(orderid);
	}

	@Override
	public EpOrder findEpOrderByIdRealTime(int orderid) {
		EpOrder epOrder = epOrderDAO.selectOrderByOrderid(orderid);
		return epOrder;
	}

	@Override
	public List<EpOrders> findEpOrdersListByOrderid(int orderid) {
		String key = EpOrders.Prefix_Redis_Key + EpOrders.Prefix_Redis_Key_Separtor + orderid;

		List<EpOrders> list = (List<EpOrders>) redisTemplate.opsForValue().get(key);
		if (list == null) {
			list = epOrdersDAO.selectOrdersByOrderid(orderid);
			redisTemplate.opsForValue().set(key, list);
			redisTemplate.expire(key, 150, TimeUnit.SECONDS);
		}
		return list;
	}

	@Transactional
	@Override
	public void finishOrderPay(EpPayInfo payinfo) throws Exception {
		// 记录支付日志
		epPayInfoDAO.addPayInfo(payinfo);
		// 更新支付信息
		epOrderDAO.updateOrderByPayInfo(payinfo);

		// 启动药店系统记账
	}

	@Override
	public List<EpOrder> findOrderListByOrderTypeAndUsestatusAndMinutesAOB(int ordertype, int usestatus, int minutes,
			int flagsendstore) {
		List<EpOrder> list = epOrderDAO.selectOrderByOrderTypeAndUsestatusAndMinutesAndFlagSendStoreAOB(ordertype,
				usestatus, minutes, flagsendstore);
		return list;
	}

	@Override
	public void recordOrderWeixinNonceStr(String ordercode, String noncestr) {
		epOrderDAO.updateOrderWeixinNonceStrByCode(ordercode, noncestr);
	}

	@Transactional
	@Override
	public void doSendEpOrderToStore(EpOrder order) {
		// 1、保存临时数据
		boolean flagsave = saveEpOrderStock(order);
		if(flagsave)
		// 2更改销售单据flagsendstore标志
		epOrderDAO.updateOrderFlagSendStore(order.getOrderid());
	}
	
	@Transactional
	@Override
	public void doSendSalesOrderToStore(EpOrder order) {
			// 1、保存临时数据
			boolean flagsave = saveEpOrderStock(order);
			List<String> vcbillnoList=new ArrayList<String>();
			if (flagsave) {
			// 2、分部门创建销售订单
			    vcbillnoList = createSalesOrderByOrder(order);
			}
			if (vcbillnoList.size()>0) {
			// 3、提交存储过程执行记账
				approvelSalesInfoList(vcbillnoList,order.getOrderid());
			}

	}
	
	

	@Override
	public boolean saveEpOrderStock(EpOrder order) {
		//boolean flag = false;
		List<EpOrderStock> epOrderStockList = new ArrayList<EpOrderStock>();
		
		int orderid = order.getOrderid();// 商家订单号
		int sectionid = order.getSectionid();// 科室或者住院病区

		List<EpOrders> orderslist = epOrdersDAO.selectOrdersByOrderid(orderid);
		String counterids = epCounterServiceImpl.findCouteridsBySectionid(sectionid);

		for (EpOrders epOrders : orderslist) {
			int iproductid = epOrders.getIproductid();
			BigDecimal numprice = epOrders.getNumprice();
			BigDecimal totalcounts = epOrders.getTotalcounts();
			BigDecimal needcount = totalcounts;// 需要的库存数量
			List<TbStockProductInfo> list = epStockProductInfoDAO
					.selectStockProductInfoListByProductIDAndCounterIDS(iproductid, counterids);
			for (int i = 0; i < list.size(); i++) {

				TbStockProductInfo epStockProductInfo = list.get(i);
				BigDecimal numstocks = epStockProductInfo.getNumstocks();
				BigDecimal _needcount = needcount;
				needcount = needcount.subtract(numstocks);// 需求-库存
				int n = needcount.compareTo(BigDecimal.ZERO);
				EpOrderStock epOrderStock = new EpOrderStock();
				int ordersid = epOrders.getOrdersid();
				int idepartid = epStockProductInfo.getIdepartid();
				int stockisid = epStockProductInfo.getIsid();
				epOrderStock.setOrderid(orderid);
				epOrderStock.setOrdersid(ordersid);
				epOrderStock.setIdepartid(idepartid);
				epOrderStock.setStockisid(stockisid);
				epOrderStock.setNumprice(numprice);
				if (n <= 0) {
					// 创建 ep_order_stock对象并保存
					// BigDecimal qty = epOrders.getTotalcounts();
					epOrderStock.setQty(_needcount.abs());
					epOrderStockList.add(epOrderStock);
					// epOrderStockDAO.addOrderStock(epOrderStock);
					break;// 跳出
				} else {
					// 占用当前库存行
					BigDecimal qty = epStockProductInfo.getNumstocks();
					epOrderStock.setQty(qty);
					epOrderStockList.add(epOrderStock);
					// epOrderStockDAO.addOrderStock(epOrderStock);
				}

				if (i == list.size() - 1 && n > 0) {
					log.error("Orderid:" + epOrders.getOrderid() + "Ordersid:" + epOrders.getOrdersid() + "库存缺："
							+ needcount.toString() + "自动处理失败，请人工处理");
					// 设置当前对象中的
					return false;
				}
			}
		}

		// 保存列表对象
		addOrderStockList(epOrderStockList);
		return true;
	}

	private void addOrderStockList(List<EpOrderStock> epOrderStockList) {
		for (EpOrderStock epOrderStock : epOrderStockList) {
			epOrderStockDAO.addOrderStock(epOrderStock);
		}
	}

	@Override
	public List<String> createSalesOrderByOrder(EpOrder order) {
		List<String> vcbillnoList=new ArrayList<String>();
		String vcbillno = "";
		List<TbDepartMent> epDepartMentlist = epOrderStockDAO.selectDepartsFromOrderStock(order.getOrderid());
		int orderid = order.getOrderid();
		for (TbDepartMent epDepartMent : epDepartMentlist) {
			int idepartid = epDepartMent.getIdepartid();

			// 创建主表
			vcbillno = epSalesInfoDAO.getSalesInfoDoc(TbSalesInfo.VCBILLNO_PREFIX, idepartid);
			vcbillno += DateUtils.getMillisecond();
			int flagep = order.getOrdertype() == EpOrderType.electronic_prescribing ? 1 : 0;// 是否为处方单
			boolean flagcreate = createSalesOrderDoc(vcbillno, order, idepartid, flagep);

			// 创建细表
			if (flagcreate) {
				List<EpOrderStock> orderStockList = epOrderStockDAO.selectOrderStockByOrderIDAndDepartID(orderid,
						idepartid);
				createSalesOrderDtl(vcbillno, orderStockList);
			}
			vcbillnoList.add(vcbillno);
		}

		return vcbillnoList;
	}

	@Override
	public boolean createSalesOrderDoc(String vcbillno, EpOrder order, int idepartid, int flagep) {
		TbSalesInfo tbSalesInfo = new TbSalesInfo();
		tbSalesInfo.setVcbillno(vcbillno);
		tbSalesInfo.setIdepartid(idepartid);
		// tbSalesInfo.setDtbilldate(new Date());
		tbSalesInfo.setCreatedby(TbSalesInfo.DEFAULT_CREATER_MAN);
		// tbSalesInfo.setCreationdate(new Date());
		// tbSalesInfo.setLastupdatedate(new Date());
		tbSalesInfo.setBstatus(0);
		// tbSalesInfo.setImembercardid(0);
		tbSalesInfo.setIprescriptionid(order.getOrderid());//网单id
		tbSalesInfo.setNummoneyall(order.getOrdermoney());
		tbSalesInfo.setFlagapp(0);
		tbSalesInfo.setSalespreson(TbSalesInfo.DEFAULT_SALES_PERSON);
		// tbSalesInfo.setNumdiscount(0);
		tbSalesInfo.setIpackage(1);
		tbSalesInfo.setVcpreson(order.getName());
		tbSalesInfo.setDyqk(0);
		tbSalesInfo.setCfsb(flagep);
		tbSalesInfo.setPaytypeid(order.getPaytypeid());
		epSalesInfoDAO.addSalesInfo(tbSalesInfo);
		return true;
	}

	@Override
	public boolean createSalesOrderDtl(String vcbillno, List<EpOrderStock> orderStockList) {
		float q = 1.0f;
		for (EpOrderStock epOrderStock : orderStockList) {
			int stockisid = epOrderStock.getStockisid();// 库存明细索引
			TbStockProductInfo tbStockProductInfo = epStockProductInfoDAO.selectStockProductInfoByISID(stockisid);

			TbSalesInfoS tbSalesInfos = new TbSalesInfoS();
			tbSalesInfos.setVcbillno(vcbillno);
			tbSalesInfos.setIproductid(tbStockProductInfo.getIproductid());
			tbSalesInfos.setNumsales(epOrderStock.getQty());
			tbSalesInfos.setNumprice(epOrderStock.getNumprice());
			tbSalesInfos.setVcbatchnumber(tbStockProductInfo.getVcbatchnumber());
			tbSalesInfos.setVcconfirmfile(tbStockProductInfo.getVcconfirmfile());
			tbSalesInfos.setVcproductunit(tbStockProductInfo.getVcproductunit());
			tbSalesInfos.setNuminprice(tbStockProductInfo.getNuminprice());
			tbSalesInfos.setDtusefulllife(tbStockProductInfo.getDtusefulllife());
			tbSalesInfos.setIproviderid(tbStockProductInfo.getIproviderid());
			tbSalesInfos.setIstockinforid(stockisid);
			tbSalesInfos.setNummoney(
					epOrderStock.getNumprice().multiply(epOrderStock.getQty()).setScale(0, BigDecimal.ROUND_HALF_UP));
			tbSalesInfos.setQueue(q);
			tbSalesInfos.setCreatedby(TbSalesInfo.DEFAULT_CREATER_MAN);
			tbSalesInfos.setIchailing(0);
			tbSalesInfos.setNumpricezd(epOrderStock.getNumprice());
			tbSalesInfos.setIpackage(1);
			tbSalesInfos.setIcounterid(tbStockProductInfo.getIcounterid());
			tbSalesInfos.setIflagaccpayed(0);
			q += 0.2f;
			epSalesInfoSDAO.addSalesInfoS(tbSalesInfos);
		}
		return true;
	}

	private void approvelSalesInfoList(List<String> vcbillnoList,int orderid) {
		for(String vcbillno:vcbillnoList) {
			int approvelSalesInfoResult = epSalesInfoDAO.approvelSalesInfo(vcbillno, "Y",
					TbSalesInfo.DEFAULT_CREATER_MAN);
			switch (approvelSalesInfoResult) {
			case -1:
				log.error("审核销售单据：{}，发现该单据已经审核.(原始网单ORDERID：{})", vcbillno,orderid);
				break;
			case -2:
				log.error("审核销售单据：{}，发生错误.(原始网单ORDERID：{})", vcbillno,orderid);
				break;
			case 1:
				// 更改销售单据flagsendstore标志
				epOrderDAO.updateOrderFlagSendStore(orderid);
				epOrderDAO.updateOrderFlagClosed(orderid);
				log.info("销售单据：{}，结算成功.(原始网单ORDERID：{})", vcbillno,orderid);
				break;

			default:
				break;
			}
			
		}
	}


	
}
