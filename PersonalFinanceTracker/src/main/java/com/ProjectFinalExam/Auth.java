package com.ProjectFinalExam;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import com.DatabasePersonalFinanceTracker.*;

public class Auth {

	private UsersDB userDB;
	
	public Auth(Connection conn) {
		this.userDB = new UsersDB();
	}
	
	public void register(String username, String password, String fullname) {
		int count = userDB.countUser();
		User newUser = new User(username, password, fullname, count);
		try {
			userDB.createData(newUser);
//			JOptionPane.showMessageDialog(null, "Success! Akun berhasil terdaftar.");
		} catch (SQLException e) {
			System.err.println("Gagal menyimpan ke database: " + e.getMessage());
		}
	}
	
	public User login (String username, String password) throws IllegalArgumentException {
		User foundUser = null;
		try {
			foundUser = userDB.getUserByUsernameAndPassword(username, password);
			if (foundUser == null) {
				throw new IllegalArgumentException("Akun tidak ditemukan atau password salah");
			}
			return foundUser;
			
		} catch (SQLException e) {
			System.err.println("SQL Error saat login: " + e.getMessage());
			throw new IllegalArgumentException("Terjadi kesalahan sistem");
		}
	}

	
	public void tampil() {
		int i = userDB.countUser();
		JOptionPane.showMessageDialog(null, "Jumlah akun : " + i);
	}
}
