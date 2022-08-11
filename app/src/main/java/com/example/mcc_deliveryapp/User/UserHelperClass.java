package com.example.mcc_deliveryapp.User;
//~~~~~~~~~~~~~~NEED TO PUSH AND COMMIT~~~~~~~~~~~~~//
public class UserHelperClass {
	String userFullname,UserPhone,UserPass;



	public UserHelperClass() {

	}

	public UserHelperClass(String userFullname, String userPhone, String userPass) {
		this.userFullname = userFullname;
		UserPhone = userPhone;
		UserPass = userPass;

	}

	public String getUserFullname() {
		return userFullname;
	}

	public void setUserFullname(String userFullname) {
		this.userFullname = userFullname;
	}

	public String getUserPhone() {
		return UserPhone;
	}

	public void setUserPhone(String userPhone) {
		UserPhone = userPhone;
	}

	public String getUserPass() {
		return UserPass;
	}

	public void setUserPass(String userPass) {
		UserPass = userPass;
	}




}
