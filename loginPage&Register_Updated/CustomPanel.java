import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.*;

//by Haoxi Wu & Marina Beshay
public class CustomPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JPanel paneLogin = new JPanel();  // login
    Register register = new Register();  // register
    private final JLabel userLabel = new CustomLabel("Username:"); // Username
    private final JTextField userText = new JTextField();
    private final JLabel passLabel = new CustomLabel("Password:"); // Password
    private final JPasswordField passText = new JPasswordField(20);
    private final JButton loginButton = new CustomButton("Login");
    private final JButton registerButton = new CustomButton("Register");
    private final JButton resetButton = new CustomButton("Reset");
    private final JLabel welcome = new CustomLabel("Welcome to Instant Messenger");

    public CustomPanel() {
        super();
        setLayout(new BorderLayout());
        initLogin();
        setListener();
    }

    private void initLogin() {
        paneLogin.setLayout(null);
        paneLogin.setBackground(new Color(30, 209, 233, 101));

        welcome.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        welcome.setBounds(50, 10, 250, 24);
        paneLogin.add(welcome);

        userLabel.setBounds(24, 50, 80, 24);
        paneLogin.add(userLabel);

        userText.setBounds(96, 50, 170, 24);
        paneLogin.add(userText);

        passLabel.setBounds(24, 80, 80, 24);
        paneLogin.add(passLabel);

        passText.setBounds(96, 80, 170, 24);
        paneLogin.add(passText);

        loginButton.setForeground(Color.BLACK);
        loginButton.setBounds(24, 116, 80, 24);


        paneLogin.add(loginButton);

        registerButton.setForeground(Color.BLACK);
        registerButton.setBounds(184, 116, 80, 24);
        paneLogin.add(registerButton);

        resetButton.setForeground(Color.BLACK);
        resetButton.setBounds(100, 150, 80, 24);
        paneLogin.add(resetButton);

        add(paneLogin, BorderLayout.CENTER);
    }

    private void setListener() {
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = String.valueOf(passText.getPassword());
            if (username.equals("") || password.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter your information or click Register button to sign up", "Sorry",
						JOptionPane.ERROR_MESSAGE);
				userText.setText("");
				passText.setText("");
			} else {
				if (verifyLogin(username, password)) {
					JOptionPane.showMessageDialog(null, "Login is successful!", "Congratulations",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (username.contains("@")) {
						JOptionPane.showMessageDialog(null, "Please enter your username", "Sorry",
								JOptionPane.ERROR_MESSAGE);
						userText.setText("");
					} else {
						JOptionPane.showMessageDialog(null, "Login failed! Please try again", "Sorry",
								JOptionPane.ERROR_MESSAGE);
						userText.setText("");
						passText.setText("");
					}
				}
			}
        });
        registerButton.addActionListener(e -> switchPanel(paneLogin, register));

        resetButton.addActionListener(e -> {
            userText.setText("");
            passText.setText("");
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
            String[] content = line.split(",");
            if (username.equals(content[0].trim()) && password.equals(content[2].trim())) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> readContentsFromFile() {
        BufferedReader reader;
        ArrayList<String> contents = new ArrayList<>();
        try {
            File f = new File("admins.txt");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line;
            while ((line = reader.readLine()) != null) {
                contents.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
}
