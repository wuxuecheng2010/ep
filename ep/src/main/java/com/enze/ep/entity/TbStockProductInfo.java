package com.enze.ep.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TbStockProductInfo {

	private int isid;
	private int idepartid;
	private int icounterid;
	private int iproductid;
	private BigDecimal numstocks;
	private BigDecimal numinprice;
	private String vcbatchnumber;
	private Date dtenter;
	private Date dtlasttime;
	private Date dtusefulllife;
	private int iproviderid;
	private int ibilldetailid;
	private Date dtlastconserve;
	private int isales;
	private int vcproductunit;
	private String vcconfirmfile;
	private String flagstop;
	private Date makedate;
	private int ichailing;
	private int numbili;
	private String flagcgrk;

}
