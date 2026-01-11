package com.ProjectFinalExam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {

	private static final String KODE_APP = "548";
	private String user_id;
	private String username;
	private String password;
	private String fullname;
	
	public User() {
		
	}
	
	public User(String username, String password, String fullname, int currentDatabaseCount) {
		this.user_id = generateUserID(currentDatabaseCount + 1);
		this.username = username;
		this.password = password;
		this.fullname = fullname;
	}
	
	public static String generateUserID(int nomorUrutRegistrasi) {
        LocalDateTime now = LocalDateTime.now();

        String tahunBelakang = now.format(DateTimeFormatter.ofPattern("yy"));

        String bulan = now.format(DateTimeFormatter.ofPattern("MM"));
        
        String formatNomor = String.format("%05d", nomorUrutRegistrasi);

        String idOtomatis = KODE_APP + tahunBelakang + bulan  + formatNomor;
	
        return idOtomatis;
    }

//    private static int loadLastRegistrationNumber() {
//	        return lastSavedRegistrationNumber;
//	    }
//
//    private static void saveNewRegistrationNumber(int newNumber) {
//	        lastSavedRegistrationNumber = newNumber;
//	    }

//    public static String registrasiUserBaru(int currentCount) {
//        int currentCount = loadLastRegistrationNumber();
//
//        int newRegistrationNumber = currentCount + 1;
//
//        saveNewRegistrationNumber(newRegistrationNumber);
//
//        return generateUserID(newRegistrationNumber);
//    }
    
    public String getUserId() {
    	return user_id;
    }
    
    public void setUserId(String user_id) {
    	this.user_id = user_id;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
    
    public String getFullname() {
    	return fullname;
    }
    
    public void setFullname(String fullname) {
    	this.fullname = fullname;
    }
    
}
