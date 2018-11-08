package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpPayType {
	public static final int ALIPAY = 1;
	public static final int WEIXIN = 2;
	private int paytypeid;
	private String paytypename;
	private String icon;
	private int usestatus;
}
