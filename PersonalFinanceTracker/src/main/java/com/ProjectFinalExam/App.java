package com.ProjectFinalExam;

import java.sql.Connection;
import java.awt.EventQueue;
import com.DatabasePersonalFinanceTracker.*;
import com.GUIPersonalFinanceTracker.*;

public class App implements AuthSuccessListener {
	
	private Auth auth;
	
    public static void main( String[] args ) throws Exception {

    	Connection db = new ConnectionDB().connect();
    	
    	Auth auth = new Auth(db);
    	App appInstance = new App(auth);
    	
    	EventQueue.invokeLater(() -> {
    		try {
    			LoginAuthFrame frame = new LoginAuthFrame(auth, appInstance);
    			frame.setVisible(true);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	});
    }
    
    public App(Auth auth) {
    	this.auth = auth;
    }
    
    @Override
    public void onLoginSuccess(User loggedInUser) {
    	EventQueue.invokeLater(() -> {
    		try {
    			DashboardFrame dashboard = new DashboardFrame(loggedInUser);
    			dashboard.setVisible(true);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	});
    }
    
}