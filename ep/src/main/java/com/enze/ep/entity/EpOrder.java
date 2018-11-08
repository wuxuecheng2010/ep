package com.enze.ep.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class EpOrder {

	private int orderid;
	private String ordercode;
	private int creuserid ;
	private Date credate;
	private Date paydate;
	private BigDecimal ordermoney;
	private int paytypeid;
	private int usestatus;
	private String memo;
	private int sectionid;
	
	private String name;
	private String bedno;
	private String address;
	private String idcard;
	private Date birthday;
	private String sex;
	private String micard;
	private String hicard;
	
}
