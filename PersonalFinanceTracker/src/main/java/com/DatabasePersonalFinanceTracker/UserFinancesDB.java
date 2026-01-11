package com.DatabasePersonalFinanceTracker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.sql.*;
import com.ProjectFinalExam.*;

public class UserFinancesDB {
    
    // Cek data user
    public boolean checkUserHasData(String userId) {
        String sql = "SELECT user_id FROM user_finances WHERE user_id = ?";
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hitung total untuk generate ID
    public int countUserFinances() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM user_finances";
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void createData(UserFinance finance) {
        // HAPUS last_update dari query agar sesuai tabel SQL Anda
        String sql = "INSERT INTO user_finances (finance_id, user_id, monthly_income, monthly_expense, last_updated) VALUES (?, ?, ?, ?, ?)";
        Timestamp last_updated = Timestamp.valueOf(LocalDateTime.now());
        
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, finance.getFinanceId());
            ps.setString(2, finance.getUserId());
            ps.setBigDecimal(3, finance.getMonthlyIncome());
            ps.setBigDecimal(4, finance.getMonthlyExpense());
            ps.setTimestamp(5, last_updated); 
    
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateFinance(UserFinance finance) {
        String sql = "UPDATE user_finances SET monthly_income = ?, monthly_expense = ?, last_updated = ? WHERE user_id = ?";
        Timestamp last_updated = Timestamp.valueOf(LocalDateTime.now());
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBigDecimal(1, finance.getMonthlyIncome());
            ps.setBigDecimal(2, finance.getMonthlyExpense());
            ps.setTimestamp(3, last_updated);
            ps.setString(4, finance.getUserId());
    
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public BigDecimal getLatestSavings(String user_id) {
        String sql = "SELECT finance_id, monthly_income, monthly_expense FROM user_finances WHERE user_id = ?";
        
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                BigDecimal income = rs.getBigDecimal("monthly_income");
                BigDecimal expense = rs.getBigDecimal("monthly_expense");
                
                return income.subtract(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    // Helper method untuk mengambil object UserFinance lengkap (Dipakai di CalculatingGoals)
    public UserFinance getUserFinance(String user_id) {
        String sql = "SELECT finance_id, monthly_income, monthly_expense FROM user_finances WHERE user_id = ?";
        UserFinance uf = new UserFinance();
        
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                uf.setFinanceId(rs.getString("finance_id"));
                uf.setUserId(user_id);
                uf.setMonthlyIncome(rs.getBigDecimal("monthly_income"));
                uf.setMonthlyExpense(rs.getBigDecimal("monthly_expense"));
                return uf;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return object kosong dengan nilai 0 jika tidak ketemu, biar tidak NullPointer
        uf.setMonthlyIncome(BigDecimal.ZERO);
        uf.setMonthlyExpense(BigDecimal.ZERO);
        return uf;
    }
}