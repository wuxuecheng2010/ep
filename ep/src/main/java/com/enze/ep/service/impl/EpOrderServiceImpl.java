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
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpPayInfo;
import com.enze.ep.entity.EpResult;
import com.enze.ep.entity.TbDepartMent;
import com.enze.ep.entity.TbSalesInfo;
import com.enze.ep.entity.TbSalesInfoS;
import com.enze.ep.entity.TbStockProductInfo;
import com.enze.ep.enums.OrderType;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.service.EpOrderService;
import com.enze.ep.utils.DateUtils;
import com.enze.ep.utils.alipay.AliPay;
import com.enze.ep.utils.wx.WeixinPay;

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
	
	@Autowired
	AliPay aliPay;
	
	@Autowired
	WeixinPay weixinPay;

	@Transactional
	@Override
	public void saveEpOrder(EpOrder epOrder, List<EpOrders> list) throws Exception {
		epOrderDAO.addOrder(epOrder);
		//int ordertype=epOrder.getOrdertype();//单据类型 
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
			redisTemplate.expire(key, 30, TimeUnit.SECONDS);
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
	public void doSendEpOrderToStore(EpOrder order) throws Exception{
		// 1、保存临时数据
		saveEpOrderStock(order);
		
		// 2更改销售单据flagsendstore标志
		epOrderDAO.updateOrderFlagSendStore(order.getOrderid());
	}
	
	@Transactional
	@Override
	public void doSendSalesOrderToStore(EpOrder order)throws Exception {
			// 1、保存临时数据
			saveEpOrderStock(order);
			List<String> vcbillnoList=new ArrayList<String>();
			
			// 2、分部门创建销售订单
			    vcbillnoList = createSalesOrderByOrder(order);
			
			if (vcbillnoList.size()>0) {
			// 3、提交存储过程执行记账
				approvelSalesInfoList(vcbillnoList,order.getOrderid());
			}

	}
	
	

	@Override
	public void saveEpOrderStock(EpOrder order)throws Exception {
		//boolean flag = false;
		List<EpOrderStock> epOrderStockList = new ArrayList<EpOrderStock>();
		
		int orderid = order.getOrderid();// 商家订单号
		int sectionid = order.getSectionid();// 科室或者住院病区

		List<EpOrders> orderslist = epOrdersDAO.selectOrdersByOrderid(orderid);
		//String counterids = epCounterServiceImpl.findCouteridsBySectionid(sectionid);

		for (EpOrders epOrders : orderslist) {
			int iproductid = epOrders.getIproductid();
			BigDecimal numprice = epOrders.getNumprice();
			BigDecimal totalcounts = epOrders.getTotalcounts();
			BigDecimal needcount = totalcounts;// 需要的库存数量
			int iunitid=epOrders.getIunitid();
			List<TbStockProductInfo> list = epStockProductInfoDAO
					.selectStockProductInfoListByProductIDAndSectionidAndUnitID(iproductid, sectionid,iunitid);
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
					throw new Exception("开单数据超过库存.");
				}
			}
		}

		// 保存列表对象
		int count=addOrderStockList(epOrderStockList);
		if(count!=epOrderStockList.size()) {
			throw new Exception("orderstock保存的行数和实际保存的行数不一致");
		}
		
	}

	private int addOrderStockList(List<EpOrderStock> epOrderStockList) {
		int count=0;
		for (EpOrderStock epOrderStock : epOrderStockList) {
			int n=epOrderStockDAO.addOrderStock(epOrderStock);
			count+=n;
		}
		return count;
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

	@Override
	public List<EpOrder> findOrderListByAgus(int sectionid, 
			String startdate, String enddate, String name,
			int usestatus) {
		enddate=DateUtils.getDateStringAfterX(enddate, DateUtils.SHORT_DATETIME_FORMAT, 1);
		 List<EpOrder> list=epOrderDAO.selectOrderListBySectionidAndCredateAndName(sectionid, startdate, enddate,name);
		
		 List<EpOrder> _list=new ArrayList<EpOrder>();
		 if(usestatus==2) {//全部
			 _list=list;
		 }else {
			 for(EpOrder epOrder:list) {
				 int _usestatus= epOrder.getUsestatus();
				 if(usestatus==_usestatus) {
					 _list.add(epOrder);
				 }
			}
		 }
		 return _list;
	}

	@Override
	public EpOrders findEpOrdersByOrdersid(int ordersid) {
		EpOrders epOrders=epOrdersDAO.selectOrdersByOrdersid(ordersid);
		return epOrders;
	}

	@Transactional
	@Override
	public EpResult saveEpSalesBackOrder(EpOrder epOrder, List<EpOrders> list) throws Exception {
		//1、保存退货单到数据库,及更新原始单据上面的退货数量
		int sourceorderid=epOrder.getSourceorderid();//对原始单据进行判断
		EpOrder sourceEpOrder=epOrderDAO.selectOrderByOrderid(sourceorderid);//查询原始单据
		
		int ordertype=sourceEpOrder.getOrdertype();//单据类型
		int paytypeid =sourceEpOrder.getPaytypeid();//支付类型
		int usestatus=sourceEpOrder.getUsestatus();//单据状态
		
		
		EpResult result=new EpResult();
		if(usestatus!=1)
			return  new EpResult(EpResult.FAIL,"单据不为支付状态不允许，不允许退货操作","");
		if(ordertype!=OrderType.saleorder.getTypeValue())
			return new EpResult(EpResult.FAIL,"单据不是在线支付订单，不允许退货操作","");

		//检查行明细是否合法
		if(!refundDtlValidate(list)) {
			return new EpResult(EpResult.FAIL,"退货数量不合法，请重新填写退货数量","");
		}
		//获取销售单的退款次数 并且更新退款单的 refundNum字段
		int refundNum=this.getRefundNum(sourceorderid);
		epOrder.setRefundnum(refundNum);
		this.saveEpOrder(epOrder, list);//epOrder为退货单对象  list为退货单明细
		
		String outTradeNo=sourceEpOrder.getOrdercode();
		BigDecimal refundmoney= epOrder.getOrdermoney();
		
		
		//2、发起在线退款处理
		switch (paytypeid) {
			case 1://支付宝
				
				String refundAmount=refundmoney.toString();
				String storeId=String.valueOf(epOrder.getSectionid());
				String refundReason="多买错买退货";
				List<EpOrder> epOrderList=epOrderDAO.selectOrderListBySourceOrderid(sourceorderid);
				String outRequestNo=String.valueOf(refundNum);
				String original_ordercode=epOrder.getOrdercode();
				result=aliPay.tradeRefund(outTradeNo, refundAmount, outRequestNo, refundReason, storeId,original_ordercode);
				break;
			case 2://微信
				String out_refund_no=epOrder.getOrdercode();
				String out_trade_no=outTradeNo+sourceEpOrder.getWeixinnoncestr();
				String total_fee=sourceEpOrder.getOrdermoney().multiply(new BigDecimal(100)).setScale(0).toString();
				String refund_fee=refundmoney.multiply(new BigDecimal(100)).setScale(0).toString();
				EpPayInfo epPayInfo= epPayInfoDAO.selectPayInfoByOrdercodeAndPaytypeid(sourceEpOrder.getOrdercode(),2);
				if(epPayInfo==null) {
					result=new EpResult(EpResult.FAIL,"原微信平台支付订单信息不存在或者错误，退款申请失败","");
				}else {
					String transaction_id=epPayInfo.getPlordercode();
					result=weixinPay.tradeRefund(transaction_id, out_trade_no, out_refund_no, total_fee,refund_fee);
				}
				break;
				
			default:
				result=new EpResult(EpResult.FAIL,"错误的付款类型","");
				break;
		}
		
		
		//3、更新商家单据相关状态   支付宝直接更新usestatue   微信更新WeiXinRefundAppFlag
		doAfterTradeRefund( result, epOrder,list) ;

		return result;
		
	}
	
	/**
	 * 获取销售订单的退款次数  第几次  用于下退货单用   并且这个值可用于查询退款情况用
	* @Title: getRefundNum
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param sourceorderid
	* @param @return    参数
	* @author wuxuecheng
	* @return int    返回类型
	* @throws
	 */
	private int getRefundNum(int sourceorderid) {
		List<EpOrder> epOrderList=epOrderDAO.selectOrderListBySourceOrderid(sourceorderid);
		int refundnum=0;
		if(epOrderList!=null && epOrderList.size()>0) {
			refundnum=epOrderList.size();
		}
		return refundnum;
	}

/**
 * 
* @Title: doAfterTradeRefund
* @Description: 退货处理之后  对商家退货单信息的处理    支付宝退款  直接更新usestatus   微信退款  因为是退款申请  先标识申请是否成功标志WeiXinRefundAppFlag
* @param @param result   在线退款结果
* @param @param epOrder  退款单
* @author wuxuecheng
* @return void    返回类型
* @throws
 */
	private void doAfterTradeRefund(EpResult result,EpOrder epOrder,List<EpOrders> list)throws Exception {
		if(result!=null && EpResult.SUCCESS.equals(result.getCode())) {
			
			int paytypeid=epOrder.getPaytypeid();
			switch (paytypeid) {
			case 1://支付宝
				epOrderDAO.updateOrderUsestatue(epOrder.getOrderid(),1,"");
				updateOrdersBackcountsByrefundList(list);
				break;
				
			case 2://微信
				epOrderDAO.updateOrderWeiXinRefundAppFlag(epOrder.getOrderid());
				//申请成功就认为是成功的 本来应该要放查询里面处理
				epOrderDAO.updateOrderUsestatue(epOrder.getOrderid(),1,"");
				updateOrdersBackcountsByrefundList(list);
				break;

			default:
				break;
			}
		}else {
			String memo="";
			if(result!=null) {
				memo=result.getMsg()+" "+result.getMemo();
			}
			epOrderDAO.updateOrderUsestatue(epOrder.getOrderid(), -1,memo);
		}
		
		
	}

@Override
public void updateOrdersBackcountsByrefundList(List<EpOrders> list) {
	for (EpOrders epOrders : list) {
		epOrdersDAO.updateOrdersBackcounts(epOrders);
	}
}


@Override
public boolean refundDtlValidate(List<EpOrders> list) {
	boolean flag=true;
	for(EpOrders epOrders:list) {//内存中的退货数据明细
		int ordersid=epOrders.getSourceordersid();
		EpOrders _epOrders= epOrdersDAO.selectOrdersByOrdersid(ordersid);//数据库中的
		BigDecimal _totalcounts=_epOrders.getTotalcounts();//销售数量
		BigDecimal _backcounts=_epOrders.getBackcounts();//退货件数
		//本次退货件数
		BigDecimal backcounts=epOrders.getTotalcounts();
		
		BigDecimal _canbackcounts=_totalcounts.subtract(_backcounts);
		if(backcounts.compareTo(_canbackcounts)>0) {
			//退货数量大于可退数量
			flag= false;
			break;
		}
		
	}
	return flag;
}

@Override
public List<EpOrder> findOrderByOrderTypeAndUsestatusAndMinutesAndWeixinRefundAppFlag(int ordertype, int usestatus,
		int minutes, int weixinrefundappflag) {
	return epOrderDAO.selectOrderByOrderTypeAndUsestatusAndMinutesAndWeixinRefundAppFlag(ordertype, usestatus, minutes, weixinrefundappflag);
}

@Override
public List<EpOrder> findOrderListByUseridAndDateAndNameAndUsestatus(int userid, String startdate, String enddate,
		String name, int usestatus) {

	enddate=DateUtils.getDateStringAfterX(enddate, DateUtils.SHORT_DATETIME_FORMAT, 1);
	 List<EpOrder> list=epOrderDAO.selectOrderListByUseridAndCredateAndName(userid, startdate, enddate,name);
	
	 List<EpOrder> _list=new ArrayList<EpOrder>();
	 if(usestatus==2) {//全部
		 _list=list;
	 }else {
		 for(EpOrder epOrder:list) {
			 int _usestatus= epOrder.getUsestatus();
			 if(usestatus==_usestatus) {
				 _list.add(epOrder);
			 }
		}
	 }
	 return _list;

}
	
	





	
}
