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
	
}
