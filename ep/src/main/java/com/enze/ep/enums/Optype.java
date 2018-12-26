package com.enze.ep.enums;

public enum Optype {
	Edit(1, "edit"), Create(2, "create"), Delete(3, "delete");

	private int opid;
	private String opname;

	Optype(int opid, String opname) {
		this.opid = opid;
		this.opname = opname;
	}
	
	

	public int getOpid() {
		return opid;
	}



	public void setOpid(int opid) {
		this.opid = opid;
	}



	public String getOpname() {
		return opname;
	}



	public void setOpname(String opname) {
		this.opname = opname;
	}



	@Override
	public String toString() {
		return this.opid + "_" + this.opname;
	}

}
