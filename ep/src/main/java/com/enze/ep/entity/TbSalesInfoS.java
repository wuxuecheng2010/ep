package com.enze.ep.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TbSalesInfoS {

	private int isid;
	private String  vcbillno;
	private int iproductid;
	private BigDecimal numsales;
	private BigDecimal numprice;
	private String vcbatchnumber;
	private String vcconfirmfile;
	private int vcproductunit;
	private BigDecimal numinprice;
	private Date dtusefulllife;
	private int iproviderid;
	private BigDecimal numcountrywideretailprice;
	private int istockinforid;
	private BigDecimal nummoney;
	private float queue;
	private String createdby;
	private Date creationdate;
	private Date lastupdatedate;
	private int lastupdatedby;
	private int ichailing;
	private BigDecimal numpricezd;
	private int ipackage;
	private int icounterid;
	private int iflagaccpayed;
	private int epordersid;

}
