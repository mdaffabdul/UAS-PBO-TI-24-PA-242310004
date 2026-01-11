package com.DatabasePersonalFinanceTracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.ProjectFinalExam.User;

public class UsersDB {
	
	public void createData (User user) throws SQLException {
		String id_user = user.getUserId();
		String username = user.getUsername();
		String password = user.getPassword();
		String fullname = user.getFullname();
		Timestamp created_at = Timestamp.valueOf(LocalDateTime.now());
		
		String sql = "INSERT INTO users (user_id, username, password, fullname, created_at) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection conn = new ConnectionDB().connect();
			 PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, id_user);
			ps.setString(2, username);
			ps.setString(3, password);
			ps.setString(4, fullname);
			ps.setTimestamp(5, created_at);
			
			ps.executeUpdate();

		}
	}

	public void readAllData() throws SQLException {
		String sql = "SELECT id_user, email, username, password FROM users";
		
		try (Connection conn = new ConnectionDB().connect();
			 PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				String id_user = rs.getString("id_user");
				String email = rs.getString("email");
				String username = rs.getString("username");
				String password = rs.getString("password");
				
				System.out.println("ID: " +  id_user + "\nEmail: " + email + "\nUsername: " + username + "\nPassword: " + password);
			}
		}
	}
	
	public void updateUsername(String user_id, String username) throws SQLException {
		String sql = "UPDATE users SET username = ? WHERE id_user = ?";
		
		try (Connection conn = new ConnectionDB().connect();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
//			ps.setString(2, fullname);
			ps.setString(2, user_id);
			
			int rowsAffected = ps.executeUpdate();
			System.out.println(rowsAffected + " baris berhasil diperbarui");
		}
	}
	
	public void deleteData(String user_id) throws SQLException {
	    String sql = "DELETE FROM users WHERE user_id = ?";
	    
	    try (Connection conn = new ConnectionDB().connect();
	    	 PreparedStatement ps = conn.prepareStatement(sql)) {
	    	ps.setString(1, user_id);
	    	ps.executeUpdate();
	    }
	}
	
	
	public User getUserByUsernameAndPassword(String username, String password) throws SQLException {
		
		String sql = "SELECT user_id, username, password, fullname FROM users WHERE username = ? AND password = ?";
		User foundUser = null;
		
		try (Connection conn = new ConnectionDB().connect();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					foundUser = new User();
					
					foundUser.setUserId(rs.getString("user_id"));
					foundUser.setUsername(rs.getString("username"));
					foundUser.setPassword(rs.getString("password"));
					foundUser.setFullname(rs.getString("fullname"));
				}
			}
		}
		return foundUser;
	}
	
	public boolean checkUsernameExists(String username) throws SQLException {
		String sql = "SELECT 1 FROM users WHERE username = ?";
		
		try (Connection conn = new ConnectionDB().connect();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try(ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	public int countUser() {
		int total = 0;
		String sql = "SELECT COUNT(user_id) AS total FROM users";
		
		try (Connection conn = new ConnectionDB().connect();
			 PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				total = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.getMessage();
		}
		return total;
	}
	
}
