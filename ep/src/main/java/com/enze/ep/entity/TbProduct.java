package com.enze.ep.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TbProduct {
	
	private int iproductid;
	private String vcproductcode;
	private String vcuniversalname;
	private String vcproductname;
	private String vceasycode;
	private String vcstandard;
	private String vcmanufacturer;
	private String title;//用于bigAutocomplete
	private String vcproductunit;
	private String unitname;
	private BigDecimal numprice;
	
	private String usage;//处方用法
	private String frequency;//频度
	private String cfunit;//处方单位 
	private String cfyl;//处方用量
	
	
}
