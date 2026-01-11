package com.GUIPersonalFinanceTracker;

import com.DatabasePersonalFinanceTracker.GoalsDB;
import com.ProjectFinalExam.Goal;
import com.ProjectFinalExam.User;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class ListGoalsFrame extends JFrame {
    private JPanel cardContainer;
    private JLabel lblPageInfo;
    private JButton btnPrev, btnNext;
    private JComboBox<String> cbSort;
    
    private ArrayList<Goal> allGoals;
    private ArrayList<Goal> displayedGoals;
    
    private int currentPage = 0;
    private final int ITEMS_PER_PAGE = 3; 
    private User currentUser;

    public ListGoalsFrame(User user) {
        this.currentUser = user;
        setTitle("Daftar Goals");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        initComponent();
        refreshData();
    }

    private void refreshData() {
        try {
            GoalsDB db = new GoalsDB();
            allGoals = db.getAllTableGoals(currentUser);
            if (allGoals == null) allGoals = new ArrayList<>();
        } catch (Exception e) {
            allGoals = new ArrayList<>();
            e.printStackTrace();
        }
        displayedGoals = new ArrayList<>(allGoals);
        sortData();
    }

    private void initComponent() {
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[]20[grow]20[]"));

        // --- 1. HEADER ---
        JPanel headerPanel = new JPanel(new MigLayout("fillx, insets 0", "[][grow][right]"));
        headerPanel.setBackground(Color.WHITE);

        JButton btnBack = new JButton("←");
        styleNavButton(btnBack);
        btnBack.addActionListener(e -> {
            new DashboardFrame(currentUser).setVisible(true);
            this.dispose();
        });

        JLabel lblTitle = new JLabel("Daftar Goals");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        String[] sortOptions = {"Terbaru", "Prioritas (High-Low)", "Prioritas (Low-High)"};
        cbSort = new JComboBox<>(sortOptions);
        cbSort.setBackground(Color.WHITE);
        cbSort.addActionListener(e -> {
            currentPage = 0;
            sortData();
        });

        headerPanel.add(btnBack, "w 50!, h 30!");
        headerPanel.add(lblTitle, "gapleft 10");
        headerPanel.add(cbSort, "w 150!");
        add(headerPanel, "growx, wrap");

        // --- 2. BODY (SCROLLABLE) ---
        cardContainer = new JPanel(new MigLayout("fillx, wrap 1, insets 0", "[grow]", "[]"));
        cardContainer.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, "grow, wrap");

        // --- 3. FOOTER ---
        JPanel footer = new JPanel(new MigLayout("fillx, insets 0", "[]push[]5[]"));
        footer.setBackground(Color.WHITE);
        lblPageInfo = new JLabel("1/1 Page");
        btnPrev = new JButton("Previous");
        btnNext = new JButton("Next");
        styleNavButton(btnPrev);
        styleNavButton(btnNext);

        btnPrev.addActionListener(e -> { if (currentPage > 0) { currentPage--; updateUIContent(); } });
        btnNext.addActionListener(e -> { if ((currentPage + 1) * ITEMS_PER_PAGE < displayedGoals.size()) { currentPage++; updateUIContent(); } });

        footer.add(lblPageInfo);
        footer.add(btnPrev, "w 100!, h 35!");
        footer.add(btnNext, "w 100!, h 35!");
        add(footer, "growx");
    }

    private void sortData() {
        if (displayedGoals == null) return;
        String selected = (String) cbSort.getSelectedItem();
        if ("Prioritas (High-Low)".equals(selected)) {
            Collections.sort(displayedGoals, (g1, g2) -> getPriorityScore(g2) - getPriorityScore(g1));
        } else if ("Prioritas (Low-High)".equals(selected)) {
            Collections.sort(displayedGoals, (g1, g2) -> getPriorityScore(g1) - getPriorityScore(g2));
        } else {
            displayedGoals = new ArrayList<>(allGoals);
            Collections.reverse(displayedGoals);
        }
        updateUIContent();
    }

    private int getPriorityScore(Goal g) {
        if ("High".equalsIgnoreCase(g.getPriority())) return 3;
        if ("Medium".equalsIgnoreCase(g.getPriority())) return 2;
        return 1;
    }

    private void updateUIContent() {
        cardContainer.removeAll();
        if (displayedGoals.isEmpty()) {
            cardContainer.add(new JLabel("Belum ada goals."), "center, gaptop 50");
        } else {
            int start = currentPage * ITEMS_PER_PAGE;
            int end = Math.min(start + ITEMS_PER_PAGE, displayedGoals.size());
            for (int i = start; i < end; i++) {
                cardContainer.add(createGoalCard(displayedGoals.get(i)), "growx, gapbottom 15");
            }
        }
        int totalPages = (int) Math.ceil((double) displayedGoals.size() / ITEMS_PER_PAGE);
        lblPageInfo.setText((currentPage + 1) + "/" + (totalPages == 0 ? 1 : totalPages) + " Page");
        btnPrev.setEnabled(currentPage > 0);
        btnNext.setEnabled((currentPage + 1) * ITEMS_PER_PAGE < displayedGoals.size());
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    private JPanel createGoalCard(Goal goal) {
        JPanel card = new JPanel(new MigLayout("wrap 2, insets 15, fillx", "[grow]10[right]"));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel lblName = new JLabel(goal.getGoalName());
        lblName.setFont(new Font("SansSerif", Font.BOLD, 16));
        card.add(lblName, "span 2, gapbottom 5");

        addCardRow(card, "Target:", "Rp " + goal.getTargetAmount());
        addCardRow(card, "Sisa Tabungan/Bln:", "Rp " + goal.getMonthlySaving());
        
        JLabel lblPVal = new JLabel(goal.getPriority());
        lblPVal.setFont(new Font("SansSerif", Font.BOLD, 12));
        if ("High".equalsIgnoreCase(goal.getPriority())) lblPVal.setForeground(Color.RED);
        else if ("Medium".equalsIgnoreCase(goal.getPriority())) lblPVal.setForeground(Color.ORANGE);
        else lblPVal.setForeground(Color.GREEN);
        
        card.add(new JLabel("Prioritas:"), "left");
        card.add(lblPVal, "right, wrap");
        addCardRow(card, "Estimasi:", goal.getMonthlyEstimate() + " Bulan");

        // TOMBOL EDIT & HAPUS
        JButton btnEdit = new JButton("Edit");
        JButton btnRemove = new JButton("Hapus");
        styleActionBtn(btnEdit, new Color(74, 144, 226));
        styleActionBtn(btnRemove, new Color(220, 53, 69));

        btnEdit.addActionListener(e -> showEditDialog(goal));
        btnRemove.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Hapus Goal?") == JOptionPane.YES_OPTION) {
                new GoalsDB().deleteGoals(goal.getGoalId());
                refreshData();
            }
        });

        card.add(btnEdit, "split 2, right, gaptop 10, w 70!, h 25!");
        card.add(btnRemove, "w 70!, h 25!");
        return card;
    }

    // --- FITUR EDIT ---
    private void showEditDialog(Goal goal) {
        JDialog editDialog = new JDialog(this, "Edit Goal", true);
        editDialog.setSize(350, 400);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new MigLayout("fillx, insets 20", "[grow]", "[]10[]20[]10[]20[]20[]"));

        JTextField txtName = new JTextField(goal.getGoalName());
        JTextField txtTarget = new JTextField(goal.getTargetAmount().toString());
        String[] priorities = {"High", "Medium", "Low"};
        JComboBox<String> cbP = new JComboBox<>(priorities);
        cbP.setSelectedItem(goal.getPriority());

        editDialog.add(new JLabel("Nama Goal:"), "wrap");
        editDialog.add(txtName, "growx, wrap");
        editDialog.add(new JLabel("Target Amount (Rp):"), "wrap");
        editDialog.add(txtTarget, "growx, wrap");
        editDialog.add(new JLabel("Prioritas:"), "wrap");
        editDialog.add(cbP, "growx, wrap");

        JButton btnSave = new JButton("Simpan Perubahan");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            try {
                goal.setGoalName(txtName.getText());
                goal.setTargetAmount(new BigDecimal(txtTarget.getText()));
                goal.setPriority((String) cbP.getSelectedItem());
                
                new GoalsDB().updateGoals(goal);
                editDialog.dispose();
                refreshData();
                JOptionPane.showMessageDialog(this, "Berhasil diubah!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Input tidak valid!");
            }
        });

        editDialog.add(btnSave, "growx, h 40!");
        editDialog.setVisible(true);
    }

    private void addCardRow(JPanel p, String label, String value) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(Color.GRAY);
        
        JLabel v = new JLabel(value);
        // Mengatur font secara eksplisit pada objek label, bukan di constraint add()
        v.setFont(new Font("SansSerif", Font.BOLD, 12)); 
        
        p.add(l, "left");
        p.add(v, "right, wrap"); // Hapus bagian 'font sansserif bold 12' dari sini
    }

    private void styleNavButton(JButton b) {
        b.setBackground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    private void styleActionBtn(JButton b, Color color) {
        b.setForeground(color);
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(color));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}