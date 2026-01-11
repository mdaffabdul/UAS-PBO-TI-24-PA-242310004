package com.GUIPersonalFinanceTracker;

import net.miginfocom.swing.MigLayout;
import com.ProjectFinalExam.User;
import com.ProjectFinalExam.UserFinance;
import com.DatabasePersonalFinanceTracker.UserFinancesDB;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

public class UserFinanceFrame extends JFrame {

    private UserFinance finance; 
    private User user;
    private JTextField txtIncome;
    private JTextField txtExpense;
    private UserFinancesDB financeDB; 

    public UserFinanceFrame(User user) {
        this.user = (user != null) ? user : new User();
        this.financeDB = new UserFinancesDB(); 
        
        this.finance = new UserFinance();
        this.finance.setUserId(this.user.getUserId());
        this.finance.setMonthlyIncome(BigDecimal.ZERO);
        this.finance.setMonthlyExpense(BigDecimal.ZERO);

        setTitle("Manage Finances");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new MigLayout("fillx, insets 30, wrap 1", "[grow]"));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Manage Finances");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainPanel.add(lblTitle, "gapbottom 25");

        // --- INPUT INCOME DENGAN PLACEHOLDER ---
        mainPanel.add(new JLabel("Monthly Income:"), "gapbottom 5");
        txtIncome = new JTextField();
        setupPlaceholder(txtIncome, "1000000"); // Contoh placeholder
        mainPanel.add(txtIncome, "growx, h 40!, gapbottom 15");

        // --- INPUT EXPENSE DENGAN PLACEHOLDER ---
        mainPanel.add(new JLabel("Monthly Expense:"), "gapbottom 5");
        txtExpense = new JTextField();
        setupPlaceholder(txtExpense, "500000"); // Contoh placeholder
        mainPanel.add(txtExpense, "growx, h 40!, gapbottom 30");

        // --- BUTTON SECTION ---
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBack.addActionListener(e -> {
            new DashboardFrame(user).setVisible(true);
            this.dispose();
        });

        JButton btnSave = new JButton("Save Financial");
        btnSave.setBackground(Color.WHITE);
        btnSave.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(this::saveData);

        mainPanel.add(btnBack, "split 2, right, w 100!, h 40!, gapright 10");
        mainPanel.add(btnSave, "w 140!, h 40!");

        add(mainPanel);
    }

    /**
     * Helper untuk mengatur Placeholder pada JTextField
     */
    private void setupPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void saveData(ActionEvent e) {
        try {
            // Validasi: Jika text masih berupa placeholder, anggap nilainya 0 atau berikan peringatan
            String incomeText = txtIncome.getText().trim();
            String expenseText = txtExpense.getText().trim();

            // Cek apakah user benar-benar mengisi atau hanya placeholder yang tertinggal
            if (txtIncome.getForeground().equals(Color.GRAY)) incomeText = "0";
            if (txtExpense.getForeground().equals(Color.GRAY)) expenseText = "0";

            BigDecimal income = new BigDecimal(incomeText);
            BigDecimal expense = new BigDecimal(expenseText);

            if (income.compareTo(BigDecimal.ZERO) < 0 || expense.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Nilai tidak boleh negatif!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isDataExists = financeDB.checkUserHasData(user.getUserId());

            if (isDataExists) {
                finance.setUserId(user.getUserId());
                finance.setMonthlyIncome(income);
                finance.setMonthlyExpense(expense);
                financeDB.updateFinance(finance);
            } else {
                int currentCount = financeDB.countUserFinances();
                UserFinance newFinance = new UserFinance(user.getUserId(), income, expense, currentCount);
                financeDB.createData(newFinance);
            }

            JOptionPane.showMessageDialog(this, "Data keuangan berhasil disimpan!");
            new DashboardFrame(user).setVisible(true);
            this.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mohon masukkan angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + ex.getMessage());
        }
    }
}