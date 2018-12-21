package com.enze.ep.enums;

public enum OrderType {
	saleorder("便民订单",1),eporder("电子处方",2),salebackorder("便民退货单",3);
	private String typeName;
	private int typeValue;
	
	OrderType(String typeName,int typeValue) {
		this.typeName=typeName;
		this.typeValue=typeValue;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(int typeValue) {
		this.typeValue = typeValue;
	}

}
