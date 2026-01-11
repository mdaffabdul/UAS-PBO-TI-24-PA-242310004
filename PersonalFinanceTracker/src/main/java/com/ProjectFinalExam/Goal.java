package com.ProjectFinalExam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Goal {

//	String nama;
//    double target, pemasukan, pengeluaran;
//    String prioritas;
//    int estimasiBulan;
//
//    public Goal(String nama, double target, double pemasukan, double pengeluaran, String prioritas) {
//        this.nama = nama;
//        this.target = target;
//        this.pemasukan = pemasukan;
//        this.pengeluaran = pengeluaran;
//        this.prioritas = prioritas;
//        double sisa = pemasukan - pengeluaran;
//        this.estimasiBulan = (sisa > 0) ? (int) Math.ceil(target / sisa) : 0;
//    }

	private String goal_id;
	private String user_id;
	private String goal_name;
	private BigDecimal target_amount;
	private BigDecimal monthly_saving;
	private String priority;
	private int monthly_estimate;
	
	public Goal(String user_id, String goal_name, BigDecimal target_amount, BigDecimal monthly_saving, String priority, int monthly_estimate, int countDatabase) {
		this.goal_id = generateGoalID(countDatabase + 1);
		this.user_id = user_id;
		this.goal_name = goal_name;
		this.target_amount = target_amount;
		this.monthly_saving = monthly_saving;
		this.priority = priority;
		this.monthly_estimate = monthly_estimate;
	}
	
	public Goal() {
		
	}
	
	public static String generateGoalID(int nomorUrutRegistrasi) {
        LocalDateTime now = LocalDateTime.now();

        String tahunBelakang = now.format(DateTimeFormatter.ofPattern("yy"));

        String bulan = now.format(DateTimeFormatter.ofPattern("MM"));
        
        String code = "424";
        
        String formatNomor = String.format("%05d", nomorUrutRegistrasi);

        String idOtomatis = code + tahunBelakang + bulan  + formatNomor;
	
        return idOtomatis;
    }

	public static int calculateEstimate(BigDecimal target, BigDecimal savings) {
        if (savings == null || savings.compareTo(BigDecimal.ZERO) <= 0) return 0;
        return target.divide(savings, 0, RoundingMode.CEILING).intValue();
    }
	
	public String getGoalName() {
		return goal_name;
	}
	
	public void setGoalName(String goal_name) {
		this.goal_name = goal_name;
	}
	
    public String getGoalId() {
    	return goal_id;
    }
    
    public void setGoalId(String goal_id) {
    	this.goal_id = goal_id;
    }
    
    public String getUserId() {
    	return user_id;
    }
    
    public void setUserId(String user_id) {
    	this.user_id = user_id;
    }
    
    public BigDecimal getTargetAmount() {
    	return target_amount;
    }
    
    public void setTargetAmount(BigDecimal target_amount) {
    	this.target_amount = target_amount;
    }
    
    public BigDecimal getMonthlySaving() {
    	return monthly_saving;
    }
    
    public void setMonthlySaving(BigDecimal monthly_saving) {
    	this.monthly_saving = monthly_saving;
    }
    
    public String getPriority() {
    	return priority;
    }
    
    public void setPriority(String priority) {
    	this.priority = priority;
    }
    
    public int getMonthlyEstimate() {
    	return monthly_estimate;
    }
    
    public void setMonthlyEstimate(int monthly_estimate) {
    	this.monthly_estimate = monthly_estimate;
    }
    
    public int monthly_estimate() {
    	UserFinance finance = new UserFinance();
    	Goal goal = new Goal();
    	BigDecimal targetGoal = goal.getTargetAmount(); 
        BigDecimal income = finance.getMonthlyIncome();
        BigDecimal expense = finance.getMonthlyExpense();
        
        BigDecimal savingsPerMonth = income.subtract(expense);
        
        if (savingsPerMonth.compareTo(BigDecimal.ZERO) <= 0) {
            return 0; 
        }
        int estimate = targetGoal.divide(savingsPerMonth, 0, RoundingMode.CEILING).intValue();
    	
    	return estimate;
    }
    
}
