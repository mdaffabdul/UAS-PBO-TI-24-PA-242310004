//package com.GUIPersonalFinanceTracker;
//
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import com.ProjectFinalExam.*;
//
//// Skeleton class untuk Goal (Asumsikan ada di package com.ProjectFinalExam)
//class Goal {
//    String name;
//    double targetAmount;
//    double allocatedAmount;
//
//    public Goal(String name, double targetAmount) {
//        this.name = name;
//        this.targetAmount = targetAmount;
//        this.allocatedAmount = 0.0;
//    }
//    
//    public double getRemaining() {
//        return targetAmount - allocatedAmount;
//    }
//    
//    public void allocate(double amount) {
//        this.allocatedAmount += amount;
//    }
//
//    @Override
//    public String toString() {
//        return name + " (Target: Rp " + String.format("%,.0f", targetAmount) + 
//               " | Terkumpul: Rp " + String.format("%,.0f", allocatedAmount) + ")";
//    }
//}
//
//
//public class DashboardFrame extends JFrame {
//	
//	private static final long serialVersionUID = 1L;
//    private User activeUser;
//    private double currentBalance = 0.0;
//    private JLabel lblCurrentBalance;
//    private JTextArea logArea;
//    
//    // Simulasi Goals (seharusnya dimuat dari GoalsDB)
//    private Goal goalLiburan = new Goal("Dana Liburan", 5000000);
//    private Goal goalLaptop = new Goal("Beli Laptop", 10000000);
//
//	
//	public DashboardFrame(User user) {
//        this.activeUser = user;
//        
//        setTitle("Dashboard - Selamat Datang " + user.getUsername());
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 800, 500);
//        
//        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
//        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
//        setContentPane(contentPane);
//        
//        // --- Header (North) ---
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        JLabel welcomeLabel = new JLabel("Halo, " + user.getUsername() + "! Kelola Keuangan Anda.", SwingConstants.CENTER);
//        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
//        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
//        
//        lblCurrentBalance = new JLabel("Saldo Saat Ini: Rp 0", SwingConstants.CENTER);
//        lblCurrentBalance.setFont(new Font("Tahoma", Font.BOLD, 20));
//        lblCurrentBalance.setForeground(new Color(0, 128, 0));
//        headerPanel.add(lblCurrentBalance, BorderLayout.CENTER);
//        
//        contentPane.add(headerPanel, BorderLayout.NORTH);
//        
//        // --- Main Content (Center) ---
//        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        splitPane.setResizeWeight(0.5);
//        contentPane.add(splitPane, BorderLayout.CENTER);
//
//        // Panel Kiri: Input Pemasukan & Pengeluaran
//        JPanel inputPanel = createInputPanel();
//        splitPane.setLeftComponent(inputPanel);
//
//        // Panel Kanan: Log Transaksi
//        JPanel logPanel = createLogPanel();
//        splitPane.setRightComponent(logPanel);
//
//        // --- Goals Management (South) ---
//        JPanel goalsPanel = createGoalsPanel();
//        contentPane.add(goalsPanel, BorderLayout.SOUTH);
//        
//        updateBalanceDisplay();
//	}
//    
//    private JPanel createInputPanel() {
//        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
//        panel.setBorder(BorderFactory.createTitledBorder("Input Transaksi"));
//        
//        // Input Pemasukan/Pengeluaran
//        String[] types = {"Pemasukan", "Pengeluaran"};
//        JComboBox<String> cmbType = new JComboBox<>(types);
//        JTextField txtDescription = new JTextField();
//        JTextField txtAmount = new JTextField();
//        JButton btnAdd = new JButton("Tambahkan");
//        
//        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
//        formPanel.add(new JLabel("Tipe Transaksi:"));
//        formPanel.add(cmbType);
//        formPanel.add(new JLabel("Deskripsi:"));
//        formPanel.add(txtDescription);
//        formPanel.add(new JLabel("Nominal (Rp):"));
//        formPanel.add(txtAmount);
//
//        panel.add(formPanel);
//        panel.add(btnAdd);
//        
//        btnAdd.addActionListener(e -> processTransaction(
//            (String) cmbType.getSelectedItem(), 
//            txtDescription.getText(), 
//            txtAmount.getText()
//        ));
//
//        return panel;
//    }
//    
//    private JPanel createLogPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createTitledBorder("Riwayat Transaksi"));
//        logArea = new JTextArea();
//        logArea.setEditable(false);
//        JScrollPane scroll = new JScrollPane(logArea);
//        panel.add(scroll, BorderLayout.CENTER);
//        return panel;
//    }
//    
//    private JPanel createGoalsPanel() {
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
//        panel.setBorder(BorderFactory.createTitledBorder("Goals Management"));
//
//        // Goals Display
//        JTextArea goalsDisplay = new JTextArea(2, 40);
//        goalsDisplay.setEditable(false);
//        updateGoalsDisplay(goalsDisplay);
//        panel.add(new JScrollPane(goalsDisplay));
//        
//        // Input Allocation
//        JComboBox<Goal> cmbGoals = new JComboBox<>(new Goal[]{goalLiburan, goalLaptop});
//        JTextField txtAllocation = new JTextField(10);
//        JButton btnAllocate = new JButton("Alokasikan ke Goal");
//        
//        panel.add(new JLabel("Alokasi:"));
//        panel.add(cmbGoals);
//        panel.add(new JLabel("Nominal:"));
//        panel.add(txtAllocation);
//        panel.add(btnAllocate);
//        
//        btnAllocate.addActionListener(e -> processGoalAllocation(
//            (Goal) cmbGoals.getSelectedItem(), 
//            txtAllocation.getText(),
//            goalsDisplay
//        ));
//
//        return panel;
//    }
//    
//    private void processTransaction(String type, String desc, String amountStr) {
//        try {
//            double amount = Double.parseDouble(amountStr);
//            if (amount <= 0) throw new NumberFormatException();
//
//            String txType = type.equals("Pemasukan") ? "Pemasukan" : "Pengeluaran";
//            String logEntry = String.format("[%s] %s: Rp %,.2f", txType, desc, amount);
//
//            if (txType.equals("Pemasukan")) {
//                currentBalance += amount;
//            } else {
//                if (currentBalance < amount) {
//                    JOptionPane.showMessageDialog(this, "Saldo tidak mencukupi untuk pengeluaran ini!", "Error", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                currentBalance -= amount;
//            }
//
//            logArea.append(logEntry + "\n");
//            updateBalanceDisplay();
//
////            txtDescription.setText("");
////            txtAmount.setText("");
//
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(this, "Nominal harus berupa angka positif.", "Error Input", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    private void processGoalAllocation(Goal goal, String amountStr, JTextArea goalsDisplay) {
//        try {
//            double amount = Double.parseDouble(amountStr);
//            
//            if (amount <= 0 || amount > currentBalance) {
//                JOptionPane.showMessageDialog(this, "Nominal tidak valid atau saldo kurang.", "Error Alokasi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            
//            if (amount > goal.getRemaining()) {
//                amount = goal.getRemaining();
//                JOptionPane.showMessageDialog(this, "Alokasi disesuaikan menjadi sisa target: Rp " + String.format("%,.0f", amount), "Peringatan", JOptionPane.WARNING_MESSAGE);
//            }
//
//            // Kurangi saldo utama dan alokasikan ke goal
//            currentBalance -= amount;
//            goal.allocate(amount);
//
//            String logEntry = String.format("[Alokasi Goal] %s: -Rp %,.2f", goal.name, amount);
//            logArea.append(logEntry + "\n");
//            
//            updateBalanceDisplay();
//            updateGoalsDisplay(goalsDisplay);
//            
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(this, "Nominal harus berupa angka.", "Error Input", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    private void updateBalanceDisplay() {
//        lblCurrentBalance.setText("Saldo Saat Ini: Rp " + String.format("%,.2f", currentBalance));
//        lblCurrentBalance.setForeground(currentBalance < 0 ? Color.RED : new Color(0, 128, 0));
//    }
//
//    private void updateGoalsDisplay(JTextArea goalsDisplay) {
//        goalsDisplay.setText("");
//        goalsDisplay.append(goalLiburan.toString() + "\n");
//        goalsDisplay.append(goalLaptop.toString() + "\n");
//    }
//}