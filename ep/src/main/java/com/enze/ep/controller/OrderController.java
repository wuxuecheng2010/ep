package com.enze.ep.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.entity.EpProductPrice;
import com.enze.ep.entity.EpUser;
import com.enze.ep.service.EpOrderService;
import com.enze.ep.service.EpProductService;
import com.enze.ep.utils.IdGenerator;
import com.enze.ep.utils.MyAuthUtils;
import com.enze.ep.utils.OrderCodeFactory;

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
	
	@ResponseBody
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public Map<String,String> createOrder(HttpServletRequest request,HttpServletResponse response) {
		//开票人信息
		EpUser epUser=myAuthUtils.getEpUserByCookie(request, response);
		long userid=epUser.getUserid();
		int sectionid=epUser.getSectionid();
		//订单信息
		EpOrder epOrder=new EpOrder();
		List<EpOrders> list=new ArrayList<EpOrders>();
		
		String name=request.getParameter("name");
		String bedno=request.getParameter("bedno");
		String address=request.getParameter("address");
		epOrder.setName(name);
		epOrder.setBedno(bedno);
		epOrder.setAddress(address);
		epOrder.setCreuserid(epUser.getUserid());
		epOrder.setSectionid(sectionid);
		//String ordercode=OrderCodeFactory.getOrderCode(userid);//32位太长 微信无法使用
		String ordercode=ID_GENERATOR.nextId();
		epOrder.setOrdercode(ordercode);
		BigDecimal ordermoney=new BigDecimal("0").setScale(0);
		String orders=request.getParameter("orders");
		JSONArray ordersJsonArray = JSON.parseArray(orders);
		for(int i=0;i<ordersJsonArray.size();i++) {
			JSONObject obj=ordersJsonArray.getJSONObject(i);
			int numno=obj.getInteger("numno");
			int iproductid=obj.getInteger("iproductid");
			EpProductPrice epProductPrice=epProductService.findEpProductPriceByProductid(iproductid);
			String vcproductcode=obj.getString("vcproductcode");
			String vcuniversalname=obj.getString("vcuniversalname");
			String vcstandard=obj.getString("vcstandard");
			int iunitid=obj.getInteger("iunitid");
			String unitname=obj.getString("unitname");
			BigDecimal totalcounts=obj.getBigDecimal("totalcounts").setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal numprice=epProductPrice.getNumprice();
			BigDecimal nummoney=totalcounts.multiply(numprice);
			ordermoney=ordermoney.add(nummoney);
			
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

}
