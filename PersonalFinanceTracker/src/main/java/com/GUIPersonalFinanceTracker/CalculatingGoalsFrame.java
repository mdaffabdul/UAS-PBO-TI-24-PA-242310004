package com.GUIPersonalFinanceTracker;

import net.miginfocom.swing.MigLayout;
import com.ProjectFinalExam.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import com.ProjectFinalExam.*;
import com.DatabasePersonalFinanceTracker.UserFinancesDB; // Import DB

public class CalculatingGoalsFrame extends JFrame {
    private User currentUser;
    private UserFinance finance; // Variable ini harus diisi!

    public CalculatingGoalsFrame(User user) {
        this.currentUser = (user != null) ? user : new User();
        
        // --- FIX: AMBIL DATA DARI DATABASE SAAT CONSTRUCTOR ---
        UserFinancesDB db = new UserFinancesDB();
        this.finance = db.getUserFinance(this.currentUser.getUserId());
        // ------------------------------------------------------

        setTitle("Aplikasi Menghitung Goals");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 500); 
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new MigLayout("wrap 1, insets 30, fillx", "[grow]"));
        mainPanel.setBackground(Color.WHITE);

        // ... (Kode UI Label & TextField Tetap Sama) ...
        JLabel titleLabel = new JLabel("Calculating Goals");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(titleLabel, "gapbottom 20");

        JTextField txtNama = createField(mainPanel, "Name Goals:", "Contoh: Beli Laptop");
        JTextField txtNominal = createField(mainPanel, "Target Amount (Rp):", "0");

        mainPanel.add(new JLabel("Priority:"), "gapy 10");
        String[] options = {"High", "Medium", "Low"};
        JComboBox<String> cbPrioritas = new JComboBox<>(options);
        cbPrioritas.setBackground(Color.WHITE);
        mainPanel.add(cbPrioritas, "growx, height 40!, gapbottom 30");

        JButton btnBack = new JButton("Back");
        JButton btnHitung = new JButton("Count");
        styleButton(btnHitung, new Color(74, 144, 226), Color.WHITE);
        styleButton(btnBack, new Color(240, 240, 240), Color.BLACK);

        btnBack.addActionListener(e -> {
            new DashboardFrame(currentUser).setVisible(true);
            this.dispose();
        });

        btnHitung.addActionListener(e -> {
            try {
                String nama = txtNama.getText().trim();
                String prioritas = (String) cbPrioritas.getSelectedItem();
                BigDecimal nominalTarget = parseCleanBigDecimal(txtNominal.getText());

                // Ambil data dari object finance yang sudah di-load di atas
                BigDecimal income = finance.getMonthlyIncome();
                BigDecimal expense = finance.getMonthlyExpense();
                
                // Pastikan nilai tidak null (jaga-jaga)
                if (income == null) income = BigDecimal.ZERO;
                if (expense == null) expense = BigDecimal.ZERO;

                // --- VALIDASI ---
                if (nama.isEmpty() || nama.equals("Contoh: Beli Laptop")) {
                    JOptionPane.showMessageDialog(this, "Nama Goal tidak boleh kosong!");
                    return;
                }
                
                if (nominalTarget.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Target Amount harus lebih dari 0!");
                    return;
                }

                if (income.compareTo(BigDecimal.ZERO) == 0 && expense.compareTo(BigDecimal.ZERO) == 0) {
                     int confirm = JOptionPane.showConfirmDialog(this, 
                         "Data Keuangan masih 0. Isi data Finance dulu?", 
                         "Data Kosong", JOptionPane.YES_NO_OPTION);
                     if (confirm == JOptionPane.YES_OPTION) {
                         new UserFinanceFrame(currentUser).setVisible(true);
                         this.dispose();
                         return;
                     }
                }

                // Cek Sisa Uang
                BigDecimal sisaUang = income.subtract(expense);
                if (sisaUang.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Pengeluaran >= Pemasukan. Tidak bisa menabung.", 
                        "Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                new ValidationGoalsFrame(currentUser, nama, nominalTarget, income, expense, prioritas).setVisible(true);
                this.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        mainPanel.add(btnBack, "split 2, growx, height 45!, w 120!");
        mainPanel.add(btnHitung, "growx, height 45!");
        add(mainPanel);
    }
    
    // ... (Method Helper parseCleanBigDecimal, createField, styleButton Tetap Sama) ...
     private BigDecimal parseCleanBigDecimal(String text) throws NumberFormatException {
        if (text == null) return BigDecimal.ZERO;
        String clean = text.trim();
        if (clean.isEmpty() || clean.equals("0")) return BigDecimal.ZERO;
        clean = clean.replaceAll("(?i)rp", "").replaceAll("\\s", "").replaceAll("\\.", "").replace(",", ".");
        if (!clean.matches("\\d+(\\.\\d+)?")) throw new NumberFormatException("Invalid format");
        return new BigDecimal(clean);
    }

    private JTextField createField(JPanel p, String label, String placeholder) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        p.add(lbl, "gapy 10");
        JTextField f = new JTextField(placeholder);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setForeground(Color.GRAY);
        f.setPreferredSize(new Dimension(0, 40));
        f.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(Color.BLACK); } }
            public void focusLost(FocusEvent e) { if (f.getText().isEmpty()) { f.setForeground(Color.GRAY); f.setText(placeholder); } }
        });
        p.add(f, "growx");
        return f;
    }

    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg); b.setForeground(fg); b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false); b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }
}