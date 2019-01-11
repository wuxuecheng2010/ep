package com.enze.ep.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TbSalesInfo {
	
	public static final String VCBILLNO_PREFIX="ZXXS";//在线销售
	public static final String VCBILLNO_REFUND_PREFIX="ZXTH";//在线销售
	public static final String DEFAULT_CREATER_MAN="admin";
	public static final int DEFAULT_SALES_PERSON=34;

	private int ibillid;
	private String vcbillno;
	private int idepartid;
	private Date dtbilldate;
	private String createdby;
	private Date creationdate;
	private Date lastupdatedate;
	private int lastupdatedby;
	private int bstatus;
	private String imembercardid;
	private int iprescriptionid; //处方单ID
	private String vcmemo;
	private BigDecimal nummoneyall;
	private int flagapp;
	private int salespreson;
	private BigDecimal numdiscount;//折扣
	private int ipackage;
	private String  vcpreson;
	private int numtime;
	private int numprint;
	private String cashier_account;
	private Date appdate;
	private int dyqk;
	private int cfsb;
	private int paytypeid;//支付类型

}
