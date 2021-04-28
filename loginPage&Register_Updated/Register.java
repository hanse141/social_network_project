import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

//BY: Marina Beshay
public class Register extends JPanel {

    private static final long serialVersionUID = 1L;
    //JPanel register = new JPanel();
    private final JPanel panelRegister = new JPanel();
    JLabel emailLabel = new CustomLabel("Email:"); //email
    JTextField emailText = new JTextField();

    JLabel passLabel = new CustomLabel("Password:"); //password
    JPasswordField passwordText = new JPasswordField();

    JLabel confirmLabel = new CustomLabel("Confirm Password:"); //re-enter password
    JPasswordField confirmPassText = new JPasswordField();

    private final JLabel create_an_account = new CustomLabel("Create an Account:"); //title
    JButton createAccount = new CustomButton("Create Account"); //Create Account button
    JButton loginPage = new CustomButton("Back to Login Page"); //back to main page button

    public Register() { //constructor
        super();
        setLayout(new BorderLayout());
        initRegister();
        confirmation();
    }

    private void initRegister() { //set up page
        panelRegister.setLayout(null);
        panelRegister.setBackground(new Color(30, 209, 233, 101));

        create_an_account.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        create_an_account.setBounds(73, 10, 250, 24);
        panelRegister.add(create_an_account);

        emailLabel.setBounds(24, 50, 80, 24);
        panelRegister.add(emailLabel);

        emailText.setBounds(145, 50, 170, 24);
        panelRegister.add(emailText);

        passLabel.setBounds(24, 80, 80, 24);
        panelRegister.add(passLabel);

        passwordText.setBounds(145, 80, 170, 24);
        panelRegister.add(passwordText);

        confirmLabel.setBounds(24, 120, 120, 24);
        panelRegister.add(confirmLabel);

        confirmPassText.setBounds(145, 120, 170, 24);
        panelRegister.add(confirmPassText);

        createAccount.setForeground(Color.BLACK);
        createAccount.setBounds(180, 250, 170, 24);
        panelRegister.add(createAccount);

        loginPage.setForeground(Color.BLACK);
        loginPage.setBounds(180, 290, 170, 24);
        panelRegister.add(loginPage);


        add(panelRegister, BorderLayout.CENTER);
    }

    boolean value = true;

    private void confirmation() {
        createAccount.addActionListener(e -> {
            do { //do while that loops around whenever there are any errors
                String email = emailText.getText();
                String password = String.valueOf(passwordText.getPassword());
                String rePassword = String.valueOf(confirmPassText.getPassword());

                try {
                    if (email == null || password.equals("") || rePassword.equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter your information", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {

                        if (!(email.contains("@"))) {
                            JOptionPane.showMessageDialog(null, "Invalid Email Address", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            emailText.setText("");
                            passwordText.setText("");
                            confirmPassText.setText("");

                        } else if (!(email.contains("purdue.edu"))) {
                            JOptionPane.showMessageDialog(null, "Must be Purdue Email", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            emailText.setText("");
                            passwordText.setText("");
                            confirmPassText.setText("");
                        } else {
                            if (!(password.equals(rePassword))) {
                                JOptionPane.showMessageDialog(null, "Passwords do not match", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                emailText.setText("");
                                passwordText.setText("");
                                confirmPassText.setText("");
                            } else {
                                if (verifyAccount(email)) {
                                    JOptionPane.showMessageDialog(null, "Account already exists", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    emailText.setText("");
                                    passwordText.setText("");
                                    confirmPassText.setText("");
                                } else {
                                    if (!(email.isBlank() && !(verifyAccount(email)))) { //if email field is not blank and it doesn't exist in the file,
                                        //then append account information into the file
                                        String username = emailText.getText().substring(0, emailText.getText().indexOf("@"));
                                        appendAccount(username, email, password);
                                        JOptionPane.showMessageDialog(null, "Account Created, Please go back to login page to login", "Confirmation",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        value = true;
                                        emailText.setText("");
                                        passwordText.setText("");
                                        confirmPassText.setText("");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Review entered information", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } while (!value);

        });

        loginPage.addActionListener(e -> { //switches panels and goes back to main page
            CustomPanel customPanel = new CustomPanel();
            switchPanel(panelRegister, customPanel);

        });
    }

    protected void switchPanel(JPanel current, JPanel newOne) { //switchPanel function that removes current panel and adds new one
        // it essentially replaces the current window then updates UI
        remove(current);
        add(newOne);
        updateUI();
    }

    protected boolean verifyAccount(String email) { //this checks to see if email exists in the file.
        ArrayList<String> contents = readContentsFromFile();
        for (String line : contents) {
            String[] content = line.split(",");
            if (email.equals(content[1].trim())) {
                return true;
            }
        }
        return false;

    }


    private ArrayList<String> readContentsFromFile() { //reds from the admins.txt to later check to see if email exists
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

    private void appendAccount(String username, String email, String password) { //appends account information in a new line with commas. This function is
        //implemented at the end of the do while loop
        try {
            String information = username + "," + email + "," + password;
            File file = new File("admins.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("\n" + information.trim());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
