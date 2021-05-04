import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;


/**
 * Login GUI for login page
 * 
 * Sources:
 * https://stackoverflow.com/questions/218155/how-do-i-change-jpanel-inside-a-jframe-on-the-fly
 * https://docs.oracle.com/javase/7/docs/api/java/awt/CardLayout.html
 */

public class Login extends JComponent implements Runnable {
    //PANELS for login page
	private final JPanel paneContainer = new JPanel();
	private final CardLayout layoutContainer = new CardLayout();
	
	
    private final JPanel paneLogin = new JPanel();
    
	//LABELS for login page
	private final JLabel userLabel = new JLabel("Username:");
    private final JLabel passLabel = new JLabel("Password:"); // Password
    private final JLabel welcome = new JLabel("Welcome to Instant Messenger");

    //BUTTONS for login page
    private final JButton loginButton = new JButton("Login");
    private final JButton registerButton = new JButton("Register");
    private final JButton resetButton = new JButton("Reset");

    //TEXTFIELDS for login page
    private final JTextField userText = new JTextField();
    private final JPasswordField passText = new JPasswordField();

    //LABELS FOR REGISTER PAGE
    private final JPanel panelRegister = new JPanel();
    private final JLabel userRegLabel = new JLabel("Username:");
    private final JLabel passRegLabel = new JLabel("Password:");
    private final JLabel confirmRegLabel = new JLabel("Confirm Password:");
    private final JLabel create_an_account = new JLabel("Create an Account:");

    //BUTTONS FOR REGISTER PAGE
    private final JButton createAccount = new JButton("Create Account");
    private final JButton loginPage = new JButton("Back to Login Page");

    //TEXTFIELDS FOR REGISTER PAGE
    private final JTextField userRegText = new JTextField();
    private final JPasswordField passRegText = new JPasswordField();
    private final JPasswordField confirmRegText = new JPasswordField();


    boolean value = true;
    JFrame frame = new JFrame("Instant Messenger");
    Container content = frame.getContentPane();
    
    private ArrayList<String> contents = new ArrayList<String>();
    
    String command = "";
    String inputtedUsername = "";
    String inputtedPassword = "";
    
    protected void switchPanel(String panelName) {
        //remove(current);
        //add(newOne);
    	switch(panelName) {
    		case "panelRegister":
    			layoutContainer.next(content);
    			break;
    		case "paneLogin":
    			layoutContainer.previous(content);
    			break;
    	}
    	//layoutContainer.show(content, panelName);
    	//System.out.println("Swapped Panel");
    	
    	frame.revalidate();
    	frame.repaint();
    }


    protected boolean verifyLogin(String username, String password) {
        //ArrayList<String> contents = readContentsFromFile();
        for (String line : contents) {
            String[] content1 = line.split(" ");
            if (username.equals(content1[0].trim()) && password.equals(content1[1].trim())) {
                return true;
            }
        }
        return false;
    }

    /*
    private ArrayList<String> readContentsFromFile() {
        BufferedReader reader;
        ArrayList<String> contents = new ArrayList<>();
        try {
            File f = new File("Server/logins.txt");
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
	*/

    /*
    protected boolean verifyAccount(String username) { //this checks to see if username exists in the file.
        //ArrayList<String> contents = readContentsFromFile();
        for (String line : contents) {
            String[] content2 = line.split(" ");
            if (username.equals(content2[0].trim())) {
                return true;
            }
        }
        return false;

    }

    /*
    private void appendAccount(String username, String password) { //appends account information in a new line with commas. This function is
        //implemented at the end of the do while loop
        try {
            String information = username + " " + password;
            File file = new File("Server/logins.txt"); //admins.txt becomes logins.txt //this will be merged
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("\n" + information.trim());
            bufferedWriter.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred, please check if specified file exists", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
    */
    
    public String pingServer() {    	   	
    	if(command != null && !(command.equals("")) &&
    			inputtedUsername != null && !(inputtedUsername.equals("")) &&
				inputtedPassword != null && !(inputtedPassword.equals(""))) {
    		
    		//System.out.println("Username: " + inputtedUsername + " Password: " + inputtedPassword);
    		
			return command + " " + inputtedUsername + " " + inputtedPassword;
		}
    	return null;
    }
    
    public void resetCredentials() {
    	inputtedUsername = "";
    	inputtedPassword = "";
    	command = "";
    }
    
    public void close() {
    	frame.dispose();
    }
    
    public JPanel registerPanelFunction() {
        panelRegister.setLayout(null);
        panelRegister.setBackground(new Color(30, 209, 233, 101));

        create_an_account.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        create_an_account.setBounds(140, 10, 250, 24);
        panelRegister.add(create_an_account);

        userRegLabel.setBounds(24, 50, 80, 24);
        userRegLabel.setForeground(Color.BLACK);
        panelRegister.add(userRegLabel);

        userRegText.setBounds(180, 50, 170, 24);
        panelRegister.add(userRegText);

        passRegLabel.setBounds(24, 80, 80, 24);
        passRegLabel.setForeground(Color.BLACK);
        panelRegister.add(passRegLabel);

        passRegText.setBounds(180, 80, 170, 24);
        panelRegister.add(passRegText);

        confirmRegLabel.setBounds(24, 110, 120, 24);
        confirmRegLabel.setForeground(Color.BLACK);	
        panelRegister.add(confirmRegLabel);

        confirmRegText.setBounds(180, 110, 170, 24);
        panelRegister.add(confirmRegText);

        createAccount.setForeground(Color.BLACK);
        createAccount.setBounds(180, 250, 170, 24);
        panelRegister.add(createAccount);

        loginPage.setForeground(Color.BLACK);
        loginPage.setBounds(180, 290, 170, 24);
        panelRegister.add(loginPage);
        content.add(panelRegister, BorderLayout.CENTER);

        return null;
    }

    public JPanel loginPanelFunction() {
        //LOGIN PAGE PANEL GUI
        paneLogin.setLayout(null);
        paneLogin.setBackground(new Color(30, 209, 233, 101));

        welcome.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        welcome.setBounds(50, 10, 500, 30);
        paneLogin.add(welcome);


        userLabel.setBounds(24, 50, 80, 24);
        userLabel.setForeground(Color.BLACK);
        paneLogin.add(userLabel);

        userText.setBounds(180, 50, 170, 24);
        paneLogin.add(userText);

        passLabel.setBounds(24, 90, 80, 24);
        passLabel.setForeground(Color.BLACK);
        paneLogin.add(passLabel);

        passText.setBounds(180, 90, 170, 24);
        paneLogin.add(passText);

        loginButton.setForeground(Color.BLACK);
        loginButton.setBounds(180, 250, 170, 24);
        paneLogin.add(loginButton);

        registerButton.setForeground(Color.BLACK);
        registerButton.setBounds(180, 290, 170, 24);
        paneLogin.add(registerButton);

        resetButton.setForeground(Color.BLACK);
        resetButton.setBounds(180, 330, 170, 24);
        paneLogin.add(resetButton);

        content.add(paneLogin, BorderLayout.CENTER);
        return null;
    }

    public void run() {
        //FRAME
        //content.setLayout(new BorderLayout());
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);

        content.setLayout(layoutContainer);
        //paneContainer.setPreferredSize(content.getSize());
        
        loginPanelFunction();
        registerPanelFunction();
        loginFunction();
        
        layoutContainer.show(content, paneLogin.getName());
        
        registerButton.addActionListener(setActionListener());
        resetButton.addActionListener(setActionListener());
        loginPage.addActionListener(setActionListener());
        createAccount.addActionListener(setActionListener());
    }
    

    public void loginFunction() {
        String username = userText.getText();
        String password = String.valueOf(passText.getPassword());
        loginButton.addActionListener(e -> {
            try {
                if (userText.getText().equals("") || passText.getPassword().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter your information or click Register button to sign up", "Sorry",
                            JOptionPane.ERROR_MESSAGE);
                    userText.setText("");
                    passText.setText("");
                } else {
                	inputtedUsername = userText.getText();
                	command = "lu";
                	
                	//System.out.println("Command: " + command);
                	
                	inputtedPassword = String.valueOf(passText.getPassword());
                	
                	//inputtedPassword = passText.getPassword();
                	
                	userText.setText("");
                	passText.setText("");
                }
                
                /*
                if (verifyLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login is successful!", "Congratulations",
                            JOptionPane.INFORMATION_MESSAGE);
//                    GUI gui = new GUI();
//                    SwingUtilities.invokeLater(gui);
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed! Please try again", "Sorry",
                            JOptionPane.ERROR_MESSAGE);
                }
                */
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "ERROR", "Sorry",
                        JOptionPane.ERROR_MESSAGE);
            }
            });
        }

    public ActionListener setActionListener() {
        return e -> {
        	
            if (e.getSource() == resetButton) {
                userText.setText("");
                passText.setText("");
            }
            if (e.getSource() == registerButton) {
            	//switchPanel(paneLogin, panelRegister);
            	switchPanel("panelRegister");
            }

            if (e.getSource() == createAccount) {
            	String username = userRegText.getText();
                String password = String.valueOf(passRegText.getPassword());
                String rePassword = String.valueOf(confirmRegText.getPassword());
            	
            	if (username == null || password.equals("") || rePassword.equals("")) {
            		JOptionPane.showMessageDialog(null, "Please enter your information", "Error",
            									  JOptionPane.ERROR_MESSAGE);
            	} else if (!(password.equals(rePassword))) {
            		JOptionPane.showMessageDialog(null, "Passwords do not match", "Error",
            									  JOptionPane.ERROR_MESSAGE);
            		userRegText.setText("");
            		passRegText.setText("");
            		confirmRegText.setText("");
            	}
            	
            	if(username.contains(" ") || password.contains(" ")) {
            		JOptionPane.showMessageDialog(null, "Username/Password should not contain spaces", "Error",
							  JOptionPane.ERROR_MESSAGE);
            	
            		userRegText.setText("");
            		passRegText.setText("");
            		confirmRegText.setText("");
            	}
            	
            	else if (!(username.isBlank())) { //if username field is not blank and it doesn't exist in the file,
                    //then append account information into the file
                    //appendAccount(username, password);
                    //JOptionPane.showMessageDialog(null, "Account Created, Please go back to login page to login", "Confirmation",
                    //        JOptionPane.INFORMATION_MESSAGE);
                    //value = true;
                    
                    inputtedUsername = username;
                    inputtedPassword = password;
                	command = "nu";
                	
                    userRegText.setText("");
                    passRegText.setText("");
                    confirmRegText.setText("");
                    //break;
            	}
            	
            	/*
            	do { //do while that loops around whenever there are any errors
                    String username = userRegText.getText();
                    String password = String.valueOf(passRegText.getPassword());
                    String rePassword = String.valueOf(confirmRegText.getPassword());

                    try {
                        if (username == null || password.equals("") || rePassword.equals("")) {
                            JOptionPane.showMessageDialog(null, "Please enter your information", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {

                            if (!(password.equals(rePassword))) {
                                JOptionPane.showMessageDialog(null, "Passwords do not match", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                userRegText.setText("");
                                passRegText.setText("");
                                confirmRegText.setText("");
                            } else {
                                if (verifyAccount(username)) {
                                    JOptionPane.showMessageDialog(null, "Account already exists", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    userRegText.setText("");
                                    passRegText.setText("");
                                    confirmRegText.setText("");
                                } else {
                                    if (!(username.isBlank() && !(verifyAccount(username)))) { //if username field is not blank and it doesn't exist in the file,
                                        //then append account information into the file
                                        //appendAccount(username, password);
                                        JOptionPane.showMessageDialog(null, "Account Created, Please go back to login page to login", "Confirmation",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        value = true;
                                        userRegText.setText("");
                                        passRegText.setText("");
                                        confirmRegText.setText("");
                                        break;

                                    }
                                }
                            }
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Review entered information", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } while (!value);
                */
            }
            if (e.getSource() == loginPage) {
            	System.out.println("Login Button Pressed");
            	//switchPanel(panelRegister, paneLogin);
                switchPanel("paneLogin");
            	//switchPanel(loginPanelFunction(), registerPanelFunction());
            }
        };
    }
}



