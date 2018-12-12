package com.enze.ep.entity;


public class EpUser {
	public static final String Prefix_Redis_Key="EpUser";
	public static final String Prefix_Redis_Key_Separtor="-";
	//public static final String Prefix_Redis_Key_Separtor_H="--";//adm信息提取物分隔符
	
	
	private int userid;
	private int hisuserid;
	private String username;
	private String usercode;
	private int usertype;
	private String password;
	private int sectionid;
	private String memo;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getSectionid() {
		return sectionid;
	}
	public void setSectionid(int sectionid) {
		this.sectionid = sectionid;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getHisuserid() {
		return hisuserid;
	}
	public void setHisuserid(int hisuserid) {
		this.hisuserid = hisuserid;
	}

}
