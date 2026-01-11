package com.ProjectFinalExam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserFinance {
	
	private String finance_id;
	private String user_id;
	private BigDecimal monthly_income;
	private BigDecimal monthly_expense;
	
	public UserFinance(String user_id, BigDecimal monthly_income, BigDecimal monthly_expense, int currentDatabaseCount) {
		this.finance_id = generateFinanceID(currentDatabaseCount + 1);
		this.user_id = user_id;
		this.monthly_income = monthly_income;
		this.monthly_expense = monthly_expense;
	}
	
	public UserFinance() {
		
	}
	
	public static String generateFinanceID(int nomorUrutRegistrasi) {
        LocalDateTime now = LocalDateTime.now();

        String tahunBelakang = now.format(DateTimeFormatter.ofPattern("yy"));

        String bulan = now.format(DateTimeFormatter.ofPattern("MM"));
        
        String code = "756";
        
        String formatNomor = String.format("%05d", nomorUrutRegistrasi);

        String idOtomatis = code + tahunBelakang + bulan  + formatNomor;
	
        return idOtomatis;
    }

	
	public String getFinanceId() {
		return finance_id;
	}
	
	public void setFinanceId(String finance_id) {
		this.finance_id = finance_id;
	}
	
	public String getUserId() {
		return user_id;
	}
	
	public void setUserId(String user_id) {
		this.user_id = user_id;
	}
	
	public BigDecimal getMonthlyIncome() {
		return monthly_income;
	}
	
	public void setMonthlyIncome(BigDecimal income) {
		this.monthly_income = income;
	}
	
	public BigDecimal getMonthlyExpense() {
		return monthly_expense; 
	}
	
	public void setMonthlyExpense(BigDecimal expense) {
		this.monthly_expense = expense;
	}

}
