package com.GUIPersonalFinanceTracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout; // Import library MigLayout
import com.ProjectFinalExam.*;
import com.DatabasePersonalFinanceTracker.*;

public class SignUpAuthFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
//	private JTextField txtEmail;
	private JTextField txtUsername;
	private JTextField txtFullname; // Tambahan field Full Name
	private JPasswordField txtPassword;
	private Auth auth;
	private JFrame parentFrame;

	public SignUpAuthFrame(Auth auth, JFrame parentFrame) {
		this.auth = auth;
		this.parentFrame = parentFrame;
		
		setTitle("SIGN UP");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Gunakan DISPOSE agar tidak menutup seluruh aplikasi
		setSize(400, 550); // Ukuran frame lebih fleksibel
		setLocationRelativeTo(null);

		// Pengaturan MigLayout: 
		// "wrap 1" = tiap komponen add() akan otomatis pindah baris
		// "insets 30" = margin luar panel
		// "fillx" = komponen melebar horizontal sesuai lebar kolom
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new MigLayout("wrap 1, insets 30, fillx", "[grow]"));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("SIGN UP CREDENTIAL");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(lblNewLabel, "gapbottom 20, center"); // Letakkan di tengah dengan jarak bawah
		
//		// --- FIELD EMAIL ---
//		contentPane.add(new JLabel("Email"), "gaptop 10");
//		txtEmail = new JTextField();
//		contentPane.add(txtEmail, "growx, h 30!");
		
		// --- FIELD USERNAME ---
		contentPane.add(new JLabel("Username"), "gaptop 10");
		txtUsername = new JTextField();
		contentPane.add(txtUsername, "growx, h 30!");

		// --- FIELD FULL NAME (BARU) ---
		contentPane.add(new JLabel("Full Name"), "gaptop 10");
		txtFullname = new JTextField();
		contentPane.add(txtFullname, "growx, h 30!");
		
		// --- FIELD PASSWORD ---
		contentPane.add(new JLabel("Password"), "gaptop 10");
		txtPassword = new JPasswordField();
		contentPane.add(txtPassword, "growx, h 30!");
		
		// --- TOMBOL REGISTER ---
		JButton btnRegister = new JButton("Register");
		btnRegister.setBackground(new Color(0, 255, 0));
		btnRegister.setForeground(Color.BLACK);
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				attemptRegistration();
			}
		});
		contentPane.add(btnRegister, "growx, h 40!, gaptop 20");
		
		// --- FOOTER ---
		JLabel lblcopyR = new JLabel("Copyright Kelompok3@2025");
		lblcopyR.setForeground(Color.GRAY);
		contentPane.add(lblcopyR, "center, gaptop 40");

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (parentFrame != null) {
					parentFrame.setVisible(true);
				}
			}
		});
	}
	
	public void attemptRegistration() {
		String username = txtUsername.getText().trim();
		String fullname = txtFullname.getText().trim(); 
		String password = new String(txtPassword.getPassword()).trim();
		
		try {
			// Validasi input
			if(username.isEmpty() || fullname.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Registrasi Gagal", JOptionPane.ERROR_MESSAGE);
				return;
			} 
			
			// Pastikan method auth.register Anda sudah mendukung penambahan parameter fullname
			// auth.register(email, username, fullname, password); 
			auth.register(username, password, fullname); 
			
			JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan Login.");
			this.dispose();
			
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
		}
	}
}