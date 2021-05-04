import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class CustomPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel paneLogin = new JPanel();  // login
	private JPanel panelRegister = new JPanel();  // register
	private JLabel userLabel = new CustomLabel("Username:"); // Username
	private JTextField userText = new JTextField(); 
	private JLabel passLabel = new CustomLabel("Password:"); // Password
	private JPasswordField passText = new JPasswordField(20); 
	private JButton loginButton = new CustomButton("login"); 
	private JButton registerButton = new CustomButton("register"); 

	public CustomPanel() {
		super(); 
		setLayout(new BorderLayout()); 
		initLogin(); 
		initRegister();
		setListener();
	}

	private void initLogin() {  
		paneLogin.setLayout(null); 
		paneLogin.setBackground(new Color(233, 30, 99));
		
		userLabel.setBounds(24, 24, 64, 24);
		paneLogin.add(userLabel);

		userText.setBounds(96, 24, 170, 24);
		paneLogin.add(userText);

		passLabel.setBounds(24, 64, 64, 24);
		paneLogin.add(passLabel);

		passText.setBounds(96, 64, 170, 24);
		paneLogin.add(passText);

		loginButton.setBounds(24, 116, 80, 24);
		
		paneLogin.add(loginButton);

		registerButton.setBounds(184, 116, 80, 24);
		paneLogin.add(registerButton);
		
		add(paneLogin, BorderLayout.CENTER);
	}
	
	private void initRegister() {
		panelRegister.setLayout(null);
		//TODO 
		panelRegister.setBackground(Color.yellow);
	}

	private void setListener() {
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = userText.getText();
				String password = String.valueOf(passText.getPassword());
				if (verifyLogin(username, password)) {
					JOptionPane.showConfirmDialog(null, "Login is successful!", "Congratulations",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Login failed! Please try again", "Sorry",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchPanel(paneLogin, panelRegister); 
			}
		});
	}

	protected void switchPanel(JPanel current, JPanel newOne) {
		remove(current); 
		add(newOne);  
		updateUI();  
	}

	protected boolean verifyLogin(String username, String password) {
		ArrayList<String> contents = readContentsFromFile();
		for (String line : contents) {
			String[] content = line.split("\t");
			if (username.equals(content[0].trim()) && password.equals(content[1].trim())) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<String> readContentsFromFile() { 
		BufferedReader reader = null;
		ArrayList<String> contents = new ArrayList<>();
		try {
			File f = new File("admins.txt");
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				contents.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contents;
	}
}
