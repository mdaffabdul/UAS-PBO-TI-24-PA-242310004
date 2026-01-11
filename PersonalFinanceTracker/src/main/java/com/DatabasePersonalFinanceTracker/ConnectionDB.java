package com.DatabasePersonalFinanceTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnectionDB {
	
	private Connection conn;
	
	public Connection connect() {
		String host = "localhost:3306";
		String dbName = "personalfinancetracker_db";
		String dbuser = "root";
		String dbpassword = "";
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+"/"+dbName,dbuser,dbpassword);
//			JOptionPane.showMessageDialog(null, "Login Berhasil");
		} catch (SQLException e ) {
			JOptionPane.showMessageDialog(null, "Koneksi database gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Driver JDBC tidak ditemukan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return conn;
	}
	
	public void close() {
		try {
			if (this.conn != null && !this.conn.isClosed()) {
				this.conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
