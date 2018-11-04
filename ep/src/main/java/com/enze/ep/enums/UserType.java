package com.enze.ep.enums;

public enum UserType {
	DOCTOR("医生",1),NURSE("护士",2);
	private String typeName;
	private int typeValue;
	
	UserType(String typeName,int typeValue) {
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
