package com.GUIPersonalFinanceTracker;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import com.ProjectFinalExam.*;
import com.DatabasePersonalFinanceTracker.*;

public class DashboardFrame extends JFrame {

    private User user;

    public DashboardFrame(User loggedInUser) {
        // Mengamankan data user
        this.user = (loggedInUser != null) ? loggedInUser : new User();

        setTitle("Personal Finance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // --- LAYOUT UTAMA ---
        // "wrap 1" = Elemen tersusun ke bawah
        // "insets 30" = Margin pinggir layar 30px
        JPanel mainPanel = new JPanel(new MigLayout("wrap 1, insets 30, fillx", "[grow]", "[]0[]20[]20[]push[]")); 
        mainPanel.setBackground(Color.WHITE);

        // =================================================================================
        // 1. HEADER PANEL (CONTAINER KHUSUS UTK JUDUL & TANGGAL)
        // =================================================================================
        // Menggunakan panel khusus agar posisi kanan-kiri lebih stabil
        // "[grow]" = kolom kiri (judul) mengambil sisa ruang
        // "[right]" = kolom kanan (tanggal) menempel ke kanan
        JPanel headerPanel = new JPanel(new MigLayout("insets 0, fillx", "[grow][right]"));
        headerPanel.setBackground(Color.WHITE);

        // A. Judul Kecil (Kiri)
        JLabel lblAppTitle = new JLabel("Personal Finance Tracker");
        lblAppTitle.setForeground(Color.GRAY);
        lblAppTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        // "aligny bottom" agar teksnya agak turun mendekati baris "Selamat Datang" nanti
        headerPanel.add(lblAppTitle, "aligny bottom"); 

        // B. Date Box (Kanan)
        String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd - MMMM - yyyy", new Locale("id", "ID")));
        
        JPanel dateBox = new JPanel(new MigLayout("insets 5 15 5 15", "[center]"));
        dateBox.setBackground(Color.WHITE);
        dateBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        JLabel lblDate = new JLabel(dateString);
        lblDate.setFont(new Font("SansSerif", Font.BOLD, 12));
        dateBox.add(lblDate);

        // Masukkan dateBox ke headerPanel di kolom kanan
        headerPanel.add(dateBox);

        // Masukkan Header Panel ke Main Panel
        mainPanel.add(headerPanel, "growx, wrap");

        // =================================================================================
        // 2. WELCOME SECTION
        // =================================================================================
        String name = (user.getFullname() != null) ? user.getFullname() : "User";
        JLabel lblWelcome = new JLabel("Selamat Datang, " + name);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        // "gaptop -5" menarik teks ini ke atas agar dekat dengan header panel
        mainPanel.add(lblWelcome, "gaptop -5, gapbottom 20");

        // =================================================================================
        // 3. ESTIMASI TABUNGAN CARD
        // =================================================================================
        JPanel savingsCard = new JPanel(new MigLayout("wrap 1, insets 15", "[grow]"));
        savingsCard.setBackground(new Color(245, 245, 245));
        savingsCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel lblEstTitle = new JLabel("Estimasi tabungan/bulanan");
        lblEstTitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        // Logika ambil data DB
        BigDecimal savingsValue = BigDecimal.ZERO;
        try {
             UserFinancesDB financeDB = new UserFinancesDB();
             savingsValue = financeDB.getLatestSavings(user.getUserId());
        } catch (Exception e) {
             savingsValue = BigDecimal.ZERO; 
        }
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        JLabel lblEstValue = new JLabel(currencyFormat.format(savingsValue));
        lblEstValue.setFont(new Font("SansSerif", Font.BOLD, 26));

        savingsCard.add(lblEstTitle);
        savingsCard.add(lblEstValue, "gaptop 10");
        mainPanel.add(savingsCard, "growx, h 120!, gapbottom 20");

        // =================================================================================
        // 4. MENU BUTTONS
        // =================================================================================
        mainPanel.add(createMenuButton("Add Goals", "⌨"), "growx, h 85!, gapbottom 15");
        mainPanel.add(createMenuButton("Finance", "Rp"), "growx, h 85!, gapbottom 15");
        mainPanel.add(createMenuButton("List Goals", "≡"), "growx, h 85!");

        // =================================================================================
        // 5. LOGOUT BUTTON
        // =================================================================================
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLogout.setForeground(new Color(220, 53, 69)); 
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 1));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah anda yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });

        // "gaptop push" mendorong tombol ini ke dasar window
        mainPanel.add(btnLogout, "gaptop push, align center, w 100!, h 30!");

        setContentPane(mainPanel); 
    }

    private JButton createMenuButton(String text, String iconStr) {
        JButton btn = new JButton("  " + iconStr + "        " + text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                new EmptyBorder(0, 20, 0, 20)
        ));

        btn.addActionListener(e -> {
            if (text.contains("List")) {
                new ListGoalsFrame(user).setVisible(true);
                this.dispose();
            } else if (text.contains("Add")) {
                new CalculatingGoalsFrame(user).setVisible(true);
                this.dispose();
            } else if (text.contains("Finance")) {
                new UserFinanceFrame(user).setVisible(true);
                this.dispose();
            }
        });

        return btn;
    }
}