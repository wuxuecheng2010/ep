package com.enze.ep.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class EpOrder {
	
	public static final String Prefix_Redis_Key="EpOrder";
	public static final String Prefix_Redis_Key_Separtor="-";

	private int orderid;
	private int ordertype;
	private String ordercode;
	private int creuserid ;
	private Date credate;
	private Date paydate;
	private BigDecimal ordermoney;
	private int paytypeid;
	private int usestatus;
	private String memo;
	private int sectionid;
	
	private String outpatientnumber;
	private String name;
	private String bedno;
	private String address;
	private Date birthday;
	private String sex;
	private int age;
	private String symptom;
	private String idcard;
	private String micard;
	private String hicard;
	
	
	
}
