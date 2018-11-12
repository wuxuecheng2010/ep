package com.enze.ep.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class EpOrders {
	
	public static final String Prefix_Redis_Key="EpOrders";
	public static final String Prefix_Redis_Key_Separtor="-";
    
	private int ordersid;
	private int numno;
	private int orderid;
	private int iproductid;
	private String vcproductcode;
	private String vcuniversalname;
	private String vcstandard;
	private int iunitid;
	private String vcunitname;
	private BigDecimal totalcounts;
	private BigDecimal numprice;
	private BigDecimal nummoney;
	
	private String dosis;
	private String frequency;
	private String usage;
	
	
}
