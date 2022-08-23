package com.example.mcc_deliveryapp.User;
//~~~~~~~~~~~~~~NEED TO PUSH AND COMMIT~~~~~~~~~~~~~//
public class UserHelperClass {
	String userFullname,UserPhone,userEmail,UserPass, mainAdd, secondaryAdd;


	public UserHelperClass() {

	}

	public UserHelperClass(String userFullname, String userPhone, String userEmail, String userPass,
						   String mainAdd, String secondaryAdd) {
		this.userFullname = userFullname;
		this.userEmail = userEmail;
		UserPhone = userPhone;
		UserPass = userPass;
		this.mainAdd = mainAdd;
		this.secondaryAdd = secondaryAdd;
	}
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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

	public void setUserPass(String userPass) {UserPass = userPass;}

	public String getMainAdd(){ return mainAdd;}

	public void setMainAdd(String mainAdd){ this.mainAdd = mainAdd;}

	public String getSecondaryAdd(){ return secondaryAdd;}

	public void setSecondaryAdd(String secondaryAdd){ this.secondaryAdd = secondaryAdd;}





}
