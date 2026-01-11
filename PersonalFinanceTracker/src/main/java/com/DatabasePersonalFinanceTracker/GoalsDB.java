package com.DatabasePersonalFinanceTracker;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import com.ProjectFinalExam.*;

public class GoalsDB {
    
    public void createData(Goal goal) throws SQLException {
    	String sql = "INSERT INTO goals (goal_id, user_id, goal_name, target_amount, priority) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, goal.getGoalId());
            ps.setString(2, goal.getUserId());
            ps.setString(3, goal.getGoalName());
            ps.setBigDecimal(4, goal.getTargetAmount());
            ps.setString(5, goal.getPriority());
            ps.executeUpdate();
        }
    }
    
    public void updateGoals(Goal goal) throws SQLException {
        String sql = "UPDATE goals SET goal_name = ?, target_amount = ?, priority = ? WHERE goal_id = ?";
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, goal.getGoalName());
            ps.setBigDecimal(2, goal.getTargetAmount());
            ps.setString(3, goal.getPriority());
            ps.setString(4, goal.getGoalId());
            ps.executeUpdate();
        }
    }
    
    public void deleteGoals(String goal_id) {
        String sql = "DELETE FROM goals WHERE goal_id = ?";
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, goal_id);
            ps.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM goals";
        try (Connection conn = new ConnectionDB().connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    public ArrayList<Goal> getAllTableGoals(User user) {
        ArrayList<Goal> listGoals = new ArrayList<>();
        String sql = "SELECT g.goal_id, g.user_id, g.goal_name, g.target_amount, g.priority, "
                   + "(uf.monthly_income - uf.monthly_expense) AS monthly_saving, "
                   + "CEIL(g.target_amount / NULLIF((uf.monthly_income - uf.monthly_expense), 0)) AS monthly_estimate "
                   + "FROM goals g "
                   + "INNER JOIN user_finances uf ON g.user_id = uf.user_id "
                   + "WHERE g.user_id = ?";
        
        try (Connection conn = new ConnectionDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Goal goal = new Goal(); 
                goal.setGoalId(rs.getString("goal_id"));
                goal.setUserId(rs.getString("user_id"));
                goal.setGoalName(rs.getString("goal_name"));
                goal.setTargetAmount(rs.getBigDecimal("target_amount"));
                goal.setPriority(rs.getString("priority"));
                goal.setMonthlySaving(rs.getBigDecimal("monthly_saving"));
                goal.setMonthlyEstimate(rs.getInt("monthly_estimate"));
                listGoals.add(goal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listGoals;
    }
}