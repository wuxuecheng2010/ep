package com.enze.ep.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TbProductPrice {
	
	public static final String Prefix_Redis_Key="EpProductPrice";
	public static final String Prefix_Redis_Key_Separtor="-";
	
	private int iproductid;
	private BigDecimal numprice;
	
}
