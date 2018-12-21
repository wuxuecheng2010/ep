package com.enze.ep.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrderUsestatus;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpResult;
import com.enze.ep.entity.EpUser;
import com.enze.ep.entity.TbProductPrice;
import com.enze.ep.enums.OrderType;
import com.enze.ep.service.EpOrderService;
import com.enze.ep.service.EpProductService;
import com.enze.ep.service.EpUserService;
import com.enze.ep.utils.DateUtils;
import com.enze.ep.utils.IdGenerator;
import com.enze.ep.utils.MyAuthUtils;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	public static final IdGenerator ID_GENERATOR = IdGenerator.INSTANCE;
	
	@Autowired
	EpOrderService epOrderServiceImpl;
	
	@Autowired
	EpProductService epProductService;
	
	@Autowired
	MyAuthUtils myAuthUtils;
	
	@Autowired
	EpUserService epUserService;
	
	@Autowired
	EpOrderUsestatus epOrderUsestatus;
	
	
	//创建订单
	@ResponseBody
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public Map<String,String> createOrder(HttpServletRequest request,HttpServletResponse response) {
		//开票人信息
		String usercode=request.getParameter("usercode");
		EpUser epUser=epUserService.findEpUserByUserCode(usercode);
		
		//EpUser epUser=myAuthUtils.getEpUserByCookie(request, response);
		int userid=epUser.getUserid();
		int sectionid=epUser.getSectionid();
		
		//订单信息
		EpOrder epOrder=new EpOrder();
		List<EpOrders> list=new ArrayList<EpOrders>();
		
		String ordercode=ID_GENERATOR.nextId();
		epOrder.setOrdercode(ordercode);
		
		String order=request.getParameter("order");//获取参数
		JSONObject orderJsonObject=JSON.parseObject(order);
		int ordertype=orderJsonObject.getInteger("ordertype");
		String outpatientnumber=orderJsonObject.getString("outpatientnumber");
		String bedno=orderJsonObject.getString("bedno");
		String name=orderJsonObject.getString("name");
		String address=orderJsonObject.getString("address");
		String sex=orderJsonObject.getString("sex");
		String age=orderJsonObject.getString("age");
		String idcard=orderJsonObject.getString("idcard");
		String micard=orderJsonObject.getString("micard");
		String hicard=orderJsonObject.getString("hicard");
		String symptom=orderJsonObject.getString("symptom");
		String memo=orderJsonObject.getString("memo");
		
		epOrder.setOrdertype(ordertype);
		epOrder.setOutpatientnumber(outpatientnumber);
		epOrder.setBedno(bedno);
		epOrder.setName(name);
		epOrder.setAddress(address);
		epOrder.setSex(sex);
		epOrder.setAge(age);
		epOrder.setIdcard(idcard);
		epOrder.setMicard(micard);
		epOrder.setHicard(hicard);
		epOrder.setSymptom(symptom);
		epOrder.setMemo(memo);
		epOrder.setCreuserid(userid);
		epOrder.setSectionid(sectionid);
		
		BigDecimal ordermoney=new BigDecimal("0").setScale(0);
		String orders=request.getParameter("orders");
		JSONArray ordersJsonArray = JSON.parseArray(orders);
		for(int i=0;i<ordersJsonArray.size();i++) {
			JSONObject obj=ordersJsonArray.getJSONObject(i);
			int numno=obj.getInteger("numno");
			int iproductid=obj.getInteger("iproductid");
			TbProductPrice epProductPrice=epProductService.findEpProductPriceByProductid(iproductid);
			String vcproductcode=obj.getString("vcproductcode");
			String vcuniversalname=obj.getString("vcuniversalname");
			String vcstandard=obj.getString("vcstandard");
			int iunitid=obj.getInteger("iunitid");
			String unitname=obj.getString("unitname");
			BigDecimal totalcounts=obj.getBigDecimal("totalcounts").setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal numprice=epProductPrice.getNumprice();
			BigDecimal nummoney=totalcounts.multiply(numprice);
			ordermoney=ordermoney.add(nummoney);
			String dosis=obj.getString("dosis");
			String frequency=obj.getString("frequency");
			String usage=obj.getString("usage");
			
			EpOrders epOrders=new EpOrders();
			epOrders.setIproductid(iproductid);
			epOrders.setNummoney(nummoney);
			epOrders.setNumno(numno);
			epOrders.setNumprice(numprice);
			epOrders.setTotalcounts(totalcounts);
			epOrders.setVcproductcode(vcproductcode);
			epOrders.setVcstandard(vcstandard);
			epOrders.setIunitid(iunitid);
			epOrders.setVcunitname(unitname);
			epOrders.setVcuniversalname(vcuniversalname);
			epOrders.setDosis(dosis);
			epOrders.setFrequency(frequency);
			epOrders.setUsage(usage);
			list.add(epOrders);
		}
		epOrder.setOrdermoney(ordermoney);
		Map<String,String> map=new HashMap<String,String>();
		try {
			epOrderServiceImpl.saveEpOrder(epOrder, list);
			map.put("code", "success");
			map.put("ordercode", epOrder.getOrdercode());
			map.put("orderid",Integer.toString(epOrder.getOrderid()));
			map.put("msg", "保存成功");
		} catch (Exception e) {
			map.put("code", "fail");
			map.put("msg", "保存失败:"+e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
	
	
	
	
	
	
	@RequestMapping(value="getOrder/{orderid}",method=RequestMethod.GET)
	@ResponseBody
	public EpOrder getOrder(@PathVariable(name="orderid") int orderid) {
		EpOrder eporder=epOrderServiceImpl.findEpOrderByIdRealTime(orderid);
		return eporder;
	}
	
	@RequestMapping(value="success/{orderid}/{usercode}",method=RequestMethod.GET)
	public ModelAndView success( HttpServletRequest request,HttpServletResponse response,@PathVariable(name="orderid") int orderid,@PathVariable(name="usercode") String  usercode) {
		EpUser epUser=epUserService.findEpUserByUserCode(usercode);
		ModelMap map=myAuthUtils.getAuthInfoByEpUser(epUser);
		EpOrder eporder=epOrderServiceImpl.findEpOrderByIdRealTime(orderid);
		List<EpOrders> eporderslist=epOrderServiceImpl.findEpOrdersListByOrderid(orderid);
		map.put("order", eporder);
		map.put("orderslist", eporderslist);
		return new ModelAndView("order/success",map);
	}

	//获取 用户开单记录
	@RequestMapping(value="orderlist/{usercode}",method=RequestMethod.GET)
	public ModelAndView userOrderList(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(name="usercode") String  usercode) {
		
		EpUser epUser=epUserService.findEpUserByUserCode(usercode);
		ModelMap map=myAuthUtils.getAuthInfoByEpUser(epUser);
		
		//String enddate=request.getParameter("enddate");
		//enddate=enddate==null?
		//默认的起止日期
		String _enddate=DateUtils.formatDateToShortStr(new Date());
		String _startdate=DateUtils.getDateStringAfterX(_enddate, DateUtils.SHORT_DATETIME_FORMAT, -5);
		
		String enddate=request.getParameter("enddate");
		String startdate=request.getParameter("startdate");
		enddate=(enddate==null)?_enddate:enddate;
		startdate= (startdate==null)?_startdate:startdate;
		map.put("enddate", enddate);
		map.put("startdate", startdate);
		
		String usestatus=request.getParameter("usestatus");
		int _usestatus=(usestatus==null)?2:Integer.valueOf(usestatus);//0=初始  1=已支付 2=全部
		//int usestatus=1;//0=初始  1=已支付
		map.put("usestatus", _usestatus);
		map.put("usestatuslist", epOrderUsestatus.getEpOrderUsestatusList());
		
		
		int sectionid=epUser.getSectionid();//病区
		
		String name=request.getParameter("name");
		name=(name==null)?"":name;
		map.put("name", name);
		
		List<EpOrder> orderlist=epOrderServiceImpl.findOrderListByAgus(sectionid, startdate, enddate, name, _usestatus);
		map.put("orderlist", orderlist);
		return new ModelAndView("order/orderlist",map);
		
	}
	
	//获取订单明细 页面
	@RequestMapping(value="orderdtl/{orderid}/{usercode}",method=RequestMethod.GET)
	public ModelAndView userOrderDetail(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(name="orderid") int  orderid,
			@PathVariable(name="usercode") String  usercode
			) {
		EpUser epUser=epUserService.findEpUserByUserCode(usercode);
		ModelMap map=myAuthUtils.getAuthInfoByEpUser(epUser);
		
		EpOrder eporder=epOrderServiceImpl.findEpOrderById(orderid);
		List<EpOrders> orderslist= epOrderServiceImpl.findEpOrdersListByOrderid(orderid);
		map.put("eporder", eporder);
		map.put("orderslist", orderslist);
		
		
		return new ModelAndView("order/orderdtl",map);
	}
	
	
		//创建退货单
		@ResponseBody
		@RequestMapping(value="/createsaleback",method=RequestMethod.POST)
		public EpResult createSaleBackOrder(HttpServletRequest request,HttpServletResponse response) {
			//开票人信息
			String usercode=request.getParameter("usercode");
			EpUser epUser=epUserService.findEpUserByUserCode(usercode);
			
			//EpUser epUser=myAuthUtils.getEpUserByCookie(request, response);
			int userid=epUser.getUserid();
			int sectionid=epUser.getSectionid();
			
			//退货单信息
			//String doc=request.getParameter("doc");
			String dtl=request.getParameter("dtl");
			
			
			//订单信息
			EpOrder epOrder=new EpOrder();
			List<EpOrders> list=new ArrayList<EpOrders>();
			
			String ordercode=ID_GENERATOR.nextId();
			epOrder.setOrdercode(ordercode);
			
			String originalorder=request.getParameter("doc");//原始单据信息
			JSONObject orderJsonObject=JSON.parseObject(originalorder);
			int sourceorderid =orderJsonObject.getInteger("orderid");
			EpOrder originalEpOrder=epOrderServiceImpl.findEpOrderById(sourceorderid);
			
			int ordertype=OrderType.salebackorder.getTypeValue();//退货单
			String outpatientnumber=originalEpOrder.getOutpatientnumber();//orderJsonObject.getString("outpatientnumber");
			String bedno=originalEpOrder.getBedno();//orderJsonObject.getString("bedno");
			String name=originalEpOrder.getName();//orderJsonObject.getString("name");
			String address=originalEpOrder.getAddress();//orderJsonObject.getString("address");
			String sex=originalEpOrder.getSex();//orderJsonObject.getString("sex");
			String age=originalEpOrder.getAge();//orderJsonObject.getString("age");
			String idcard=originalEpOrder.getIdcard();//orderJsonObject.getString("idcard");
			String micard=originalEpOrder.getMicard();//orderJsonObject.getString("micard");
			String hicard=originalEpOrder.getHicard();//orderJsonObject.getString("hicard");
			String symptom=originalEpOrder.getSymptom();//orderJsonObject.getString("symptom");
			//String memo=orderJsonObject.getString("memo");
			int paytypeid=originalEpOrder.getPaytypeid();
			
			epOrder.setOrdertype(ordertype);
			epOrder.setOutpatientnumber(outpatientnumber);
			epOrder.setBedno(bedno);
			epOrder.setName(name);
			epOrder.setAddress(address);
			epOrder.setSex(sex);
			epOrder.setAge(age);
			epOrder.setIdcard(idcard);
			epOrder.setMicard(micard);
			epOrder.setHicard(hicard);
			epOrder.setSymptom(symptom);
			epOrder.setPaytypeid(paytypeid);
			//epOrder.setMemo(memo);
			epOrder.setCreuserid(userid);
			epOrder.setSectionid(sectionid);
			epOrder.setSourceorderid(sourceorderid);
			
			BigDecimal ordermoney=new BigDecimal("0").setScale(0);
			String orders=request.getParameter("dtl");
			JSONArray ordersJsonArray = JSON.parseArray(orders);
			for(int i=0;i<ordersJsonArray.size();i++) {
				JSONObject obj=ordersJsonArray.getJSONObject(i);
				
				int numno=obj.getInteger("numno");
				int ordersid=obj.getInteger("ordersid");
				EpOrders originalEpOrders=epOrderServiceImpl.findEpOrdersByOrdersid(ordersid);
				
				int iproductid=originalEpOrders.getIproductid();//obj.getInteger("iproductid");
				//TbProductPrice epProductPrice=epProductService.findEpProductPriceByProductid(iproductid);
				String vcproductcode=originalEpOrders.getVcproductcode();//obj.getString("vcproductcode");
				String vcuniversalname=originalEpOrders.getVcuniversalname();//obj.getString("vcuniversalname");
				String vcstandard=originalEpOrders.getVcstandard();//obj.getString("vcstandard");
				int iunitid=originalEpOrders.getIunitid();//obj.getInteger("iunitid");
				String unitname=originalEpOrders.getVcunitname();//obj.getString("unitname");
				int sourceordersid=ordersid;
				BigDecimal totalcounts=obj.getBigDecimal("backcounts").setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal numprice=originalEpOrders.getNumprice();//epProductPrice.getNumprice();
				BigDecimal nummoney=totalcounts.multiply(numprice);
				ordermoney=ordermoney.add(nummoney);
				//String dosis=obj.getString("dosis");
				//String frequency=obj.getString("frequency");
				//String usage=obj.getString("usage");
				
				EpOrders epOrders=new EpOrders();
				epOrders.setIproductid(iproductid);
				epOrders.setNummoney(nummoney);
				epOrders.setNumno(numno);
				epOrders.setNumprice(numprice);
				epOrders.setTotalcounts(totalcounts);
				epOrders.setVcproductcode(vcproductcode);
				epOrders.setVcstandard(vcstandard);
				epOrders.setIunitid(iunitid);
				epOrders.setVcunitname(unitname);
				epOrders.setVcuniversalname(vcuniversalname);
				epOrders.setSourceordersid(sourceordersid);
				//epOrders.setDosis(dosis);
				//epOrders.setFrequency(frequency);
				//epOrders.setUsage(usage);
				list.add(epOrders);
			}
			epOrder.setOrdermoney(ordermoney);
			Map<String,String> map=new HashMap<String,String>();
			EpResult result=new EpResult();
			try {
				result=epOrderServiceImpl.saveEpSalesBackOrder(epOrder, list);
			} catch (Exception e) {
				result=new EpResult(EpResult.FAIL,"退款失败",e.getMessage());
			}
			return result;
		}
		
	
	
}
