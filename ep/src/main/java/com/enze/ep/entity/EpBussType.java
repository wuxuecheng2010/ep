package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpBussType {
	public static final int SALE = 1;//销售
	public static final int SALEBACK = 2;//退货
	private int busstypeid;
	private String busstypename;
}
