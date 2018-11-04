package com.enze.ep.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EpProduct {
	
	private int iproductid;
	private String vcproductcode;
	private String vcuniversalname;
	private String vcproductname;
	private String vceasycode;
	private String vcstandard;
	private String vcmanufacturer;
	private String title;//用于bigAutocomplete
	private String unitname;
	private BigDecimal numprice;
	/*
	public int getIproductid() {
		return iproductid;
	}
	public void setIproductid(int iproductid) {
		this.iproductid = iproductid;
	}
	public String getVcproductcode() {
		return vcproductcode;
	}
	public void setVcproductcode(String vcproductcode) {
		this.vcproductcode = vcproductcode;
	}
	public String getVcuniversalname() {
		return vcuniversalname;
	}
	public void setVcuniversalname(String vcuniversalname) {
		this.vcuniversalname = vcuniversalname;
	}
	public String getVcproductname() {
		return vcproductname;
	}
	public void setVcproductname(String vcproductname) {
		this.vcproductname = vcproductname;
	}
	public String getVceasycode() {
		return vceasycode;
	}
	public void setVceasycode(String vceasycode) {
		this.vceasycode = vceasycode;
	}
	public String getVcstandard() {
		return vcstandard;
	}
	public void setVcstandard(String vcstandard) {
		this.vcstandard = vcstandard;
	}
	public String getVcmanufacturer() {
		return vcmanufacturer;
	}
	public void setVcmanufacturer(String vcmanufacturer) {
		this.vcmanufacturer = vcmanufacturer;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	*/
	
	
}
