package com.GUIPersonalFinanceTracker;

import com.DatabasePersonalFinanceTracker.GoalsDB;
import com.ProjectFinalExam.Goal;
import com.ProjectFinalExam.User;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode; 
import java.text.NumberFormat;
import java.util.Locale;

public class ValidationGoalsFrame extends JFrame {
    private User currentUser;

    public ValidationGoalsFrame(User user, String nama, BigDecimal target, BigDecimal income, BigDecimal expense, String priority) {
        this.currentUser = user;
        setTitle("Review Goal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        setLayout(new MigLayout("fillx, insets 25, wrap 1", "[grow]"));

        JLabel lblTitle = new JLabel("Konfirmasi Goal");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(lblTitle, "gapbottom 20");

        // --- 1. HITUNG LOGIKA MATEMATIKA ---
        
        // Hitung Sisa (Income - Expense)
        BigDecimal sisa = income.subtract(expense);
        
        // Hitung Estimasi (Target / Sisa)
        int estimasi = 0;
        if (sisa.compareTo(BigDecimal.ZERO) > 0) {
            estimasi = target.divide(sisa, 0, RoundingMode.CEILING).intValue();
        } else {
             // Jika sisa 0 atau minus, set estimasi jadi angka sangat besar atau handle error
             estimasi = 999; 
        }

        // --- 2. TAMPILAN KARTU REVIEW ---
        JPanel card = new JPanel(new MigLayout("fillx, insets 20, wrap 2", "[grow][right]"));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        addInfo(card, "Nama Goal:", nama);
        addInfo(card, "Target:", formatRupiah(target));
        addInfo(card, "Tabungan/Bulan:", formatRupiah(sisa));
        addInfo(card, "Prioritas:", priority);
        addInfo(card, "Estimasi Waktu:", estimasi + " Bulan");

        add(card, "growx, gapbottom 30");

        // --- 3. TOMBOL AKSI ---
        JButton btnSimpan = new JButton("Simpan ke Database");
        JButton btnBatal = new JButton("Edit Kembali");
        styleButton(btnSimpan, new Color(74, 144, 226), Color.WHITE);
        
        btnBatal.addActionListener(e -> {
            new CalculatingGoalsFrame(currentUser).setVisible(true);
            this.dispose();
        });

        // Variabel final untuk akses di lambda
        final int finalEstimasi = estimasi; 
        final BigDecimal finalSisa = sisa;

        // --- LOGIKA SIMPAN KE DATABASE (FIXED) ---
        btnSimpan.addActionListener(e -> {
            try {
                // A. Inisialisasi Database
                GoalsDB db = new GoalsDB();
                
                // B. Ambil jumlah data saat ini untuk generate ID (Auto Increment Manual)
                int currentCount = db.getCount();

                // C. Buat Object Goal
                // Urutan parameter HARUS SESUAI dengan Constructor di Goal.java Anda:
                // (user_id, goal_name, target, monthly_saving, priority, estimate, db_count)
                Goal newGoal = new Goal(
                    currentUser.getUserId(), 
                    nama, 
                    target, 
                    finalSisa,     
                    priority, 
                    finalEstimasi, 
                    currentCount   // Parameter penting untuk generate ID
                );
                
                // D. Simpan ke Database
                db.createData(newGoal);
                
                // E. Sukses
                JOptionPane.showMessageDialog(this, "Goal berhasil disimpan!");
                new ListGoalsFrame(currentUser).setVisible(true);
                this.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        add(btnSimpan, "growx, h 45!, gapbottom 10");
        add(btnBatal, "growx, h 45!");
    }

    private void addInfo(JPanel p, String l, String v) {
        JLabel lblLabel = new JLabel(l);
        JLabel lblValue = new JLabel(v);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 14));
        p.add(lblLabel);
        p.add(lblValue);
    }

    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false);
    }

    private String formatRupiah(BigDecimal val) {
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(val);
    }
}