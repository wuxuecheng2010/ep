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
	private String age;
	private String symptom;
	private String idcard;
	private String micard;
	private String hicard;
	
	private String weixinnoncestr;//微信下单时创建的随机码  订单查询的时 跟ordercode组合成out_trade_no
	private int flagsendstore;//是否处理到药店 0初始  1已经处理
	private int flagclosed;//闭环标志 表示完成与药店的结账或者处方的处理 0初始  1闭环
	
	private int sourceorderid;
	private int refundnum;// 退货单序号   销售单被退款申请成功多少次
	
}
