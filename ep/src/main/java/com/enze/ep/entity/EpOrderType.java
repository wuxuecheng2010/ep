package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpOrderType {
	public static final int sales_order = 1;
	public static final int electronic_prescribing = 2;
	private int ordertypeid;
	private String ordertypename;
}
