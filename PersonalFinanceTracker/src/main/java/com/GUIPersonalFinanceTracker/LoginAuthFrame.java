package com.GUIPersonalFinanceTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout; // Pastikan library MigLayout sudah ditambahkan
import com.ProjectFinalExam.*;
import com.DatabasePersonalFinanceTracker.*;

public class LoginAuthFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	protected JFrame frame;
	private Auth auth;
	private AuthSuccessListener listener;	

	public LoginAuthFrame(Auth auth, AuthSuccessListener listener) {
		this.auth = auth;
		this.listener = listener;
		
		setTitle("AUTHENTIFICATION");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 414, 508);
		
		// Inisialisasi MigLayout pada contentPane
		// "wrap 1" otomatis memindah komponen ke baris baru setelah ditambahkan
		// "insets 30" memberikan margin di sekeliling panel
		// "fillx" membuat kolom mengikuti lebar horizontal
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new MigLayout("wrap 1, insets 30, fillx", "[grow]"));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Sign In With Required Credentials");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		// "gapbottom 20" memberikan jarak ke bawah sebelum komponen berikutnya
		contentPane.add(lblNewLabel, "gapbottom 20");
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblUsername, "gaptop 10");
		
		txtUsername = new JTextField();
		txtUsername.setBackground(new Color(192, 192, 192));
		// "growx" membuat field mengisi seluruh lebar, "h 30!" memaksa tinggi 30px
		contentPane.add(txtUsername, "growx, h 30!");
		txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblPassword, "gaptop 10");
		
		txtPassword = new JPasswordField(); 
		txtPassword.setBackground(new Color(192, 192, 192));
		contentPane.add(txtPassword, "growx, h 30!");
		txtPassword.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBackground(new Color(0, 255, 0));
		// "gaptop 20" memberikan jarak pemisah yang cukup dari input field
		contentPane.add(btnLogin, "growx, h 35!, gaptop 20");
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onLoginButtonClick(); 
			}
		});

		JLabel lblNewLabel_3 = new JLabel("Already Have an Account?");
		contentPane.add(lblNewLabel_3, "gaptop 30");
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.setForeground(new Color(0, 0, 0));
		btnSignUp.setBackground(new Color(0, 255, 0));
		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				attemptRegistration();
			}
		});
		// "w 130!" mengatur lebar tombol agar tidak terlalu lebar (mengikuti desain asli)
		contentPane.add(btnSignUp, "w 130!, h 30!");

	}
	
	private void attemptRegistration() {
        SignUpAuthFrame signUp = new SignUpAuthFrame(auth, this);
        this.setVisible(false);
        signUp.setVisible(true);
    }
	
	private void onLoginButtonClick() { 
		String username = txtUsername.getText().trim(); 
        String password = new String(txtPassword.getPassword()).trim(); 
        
        try {
        	User loggedInUser = auth.login(username, password); 
        	
        	if (listener != null) {
        		listener.onLoginSuccess(loggedInUser);
        		this.dispose();
        	}
        } catch (IllegalArgumentException e) {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
	}
}