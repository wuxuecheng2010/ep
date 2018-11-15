package com.enze.ep.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EpOrderStock {

	private int osid;
	private int orderid;
	private int ordersid;
	private int idepartid;
	private int stockisid;
	private BigDecimal qty;

}
