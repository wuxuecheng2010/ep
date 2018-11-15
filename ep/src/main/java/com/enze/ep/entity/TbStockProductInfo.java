package com.enze.ep.entity;

import java.math.BigDecimal;

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
   
}
