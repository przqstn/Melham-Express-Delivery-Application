package com.example.mcc_deliveryapp.User;
//~~~~~~~~~~~~~~NEED TO PUSH AND COMMIT~~~~~~~~~~~~~//
public class UserHelperClass {
	String userFullname,UserPhone,UserPass,UserCPass;

	String passwordHash;

	public UserHelperClass() {

	}

	public UserHelperClass(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public UserHelperClass(String userFullname, String userPhone, String userPass, String userCPass) {
		this.userFullname = userFullname;
		UserPhone = userPhone;
		UserPass = userPass;
		UserCPass = userCPass;
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

	public String getUserCPass() {
		return UserCPass;
	}

	public void setUserCPass(String userCPass) {
		UserCPass = userCPass;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
