	import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * GUI for displaying and sending messages
 *
 * Sources:
 * https://docs.oracle.com/javase/7/docs/api/javax/swing/JTextArea.html
 * https://docs.oracle.com/javase/7/docs/api/javax/swing/JScrollPane.html
 * https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html
 * https://stackoverflow.com/questions/16493222/how-to-have-multiple-clients-connect-to-a-server-java
 * https://stackoverflow.com/questions/12865803/how-do-i-refresh-a-gui-in-java#
 * https://docs.oracle.com/javase/7/docs/api/javax/swing/JEditorPane.html
 */

public class GUI extends JComponent implements Runnable {
	Image image;
	Graphics2D graphics2D;
	
	JFrame frame = new JFrame("GUI");
	
	JTextArea messageText;  //Message text
	JTextField newPassText; 
	
	JButton sendButton;  //Send Button
	
	JButton settingsButton;  //Go to settings
	JButton returnButton;  //Return to conversations
	JButton passwordButton;  //Password confirm button
	JButton deleteAccountButton;  //Delete Account button
	
	JPanel historyPanel = new JPanel();  //History Panel
	JPanel leftContainer = new JPanel();  //Container Panel
	JPanel conversationPanel = new JPanel();  //Conversation Panel
	JPanel settingsPanel = new JPanel();  //Settings Panel

	CardLayout layoutContainer = new CardLayout();  //Card Layout
	
	//JTextArea historyPanel = new JTextArea();
	ArrayList<JPanel> messageHistory = new ArrayList<JPanel>();  //Stores messages
	ArrayList<JButton> conversationList = new ArrayList<JButton>();  //Stores conversations
	
	GUI gui;
	
	//Fields passed to Client
	String messageToSend = "";
	Message messageToEdit;
	String editedText = "";
	Message messageToDelete;
	String conversationToLoad = "";
	String newPass = "";
	String deletedAccountPass = "";
	boolean closeUser = false;
	
	String user = "";
	
	boolean running = true;
	
	ArrayList<Message> messages = new ArrayList<Message>();
	ArrayList<String> conversations = new ArrayList<String>();
	//ActionListener actionListener = new ActionListener() {
       
	protected void swapPanel(String panelName) {
		switch(panelName) {
			case "Settings":
				layoutContainer.next(leftContainer);
				break;
			case "Conversations":
				layoutContainer.previous(leftContainer);
				break;
		}
	}
	
	public String newMessage() {	
		if(messageToSend != null && !(messageToSend.equals(""))) {
			return messageToSend;
		}

		return null;
	}
	
	public String editMessage() {
		if((messageToEdit != null && !(messageToEdit.getSender().equals("")) &&
				editedText != null)) {
			return editedText + " " + messageToEdit;
		}
		
		return null;
	}
	
	public Message deleteMessage() {
		if(messageToDelete != null && !(messageToDelete.getSender().equals(""))) {
			return messageToDelete;
		}
		
		return null;
	}
	
	public String loadConversation() {
		if(conversationToLoad != null && !(conversationToLoad.equals(""))) {
			return conversationToLoad;
		}
		
		return null;
	}
	
	public String changePass() {
		if(newPass != null && !(newPass.equals(""))) {
			return newPass;
		}
		
		return null;
	}
	
	public String deleteAccount() {
		if(deletedAccountPass != null && !(deletedAccountPass.equals(""))) {
			return deletedAccountPass;
		}
		
		return null;
	}
	
	public void resetMessage() {
		messageToSend = "";
		editedText = "";
		messageToEdit = new Message("", "", "");
		messageToDelete = new Message("", "", "");
		conversationToLoad = "";
		newPass = "";
		deletedAccountPass = "";
	}
	
	public boolean logoff() {
		return closeUser;
	}
	
	public void updateMessages(ArrayList<Message> msgs) {
		if(msgs == null)
			return;
		
		//historyPanel.setText("");
		historyPanel.removeAll();
		messageHistory.clear();
		
		messages = msgs;
		
		for(int i = 0; i < messages.size(); i++) {
    		Message message = messages.get(i);
    		JPanel messagePanel = new JPanel();
    		
    		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
    		
    		//Text Area implementation
			JTextArea currMessageText = new JTextArea();
			
			currMessageText.setEditable(false);
	    	currMessageText.setLineWrap(true);
			
	    	currMessageText.setText(message.getTimeStamp() + " " + message.getSender() + ": " + message.getContent());
    		//currMessageText.setText(message.getSender() + ": " + message.getContent());
    		
    		messagePanel.add(currMessageText);
    		
    		
    		JPanel sidePanel = new JPanel();
    		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
    		
    		//Standalone Timestamp
    		/*
    		JLabel timeStamp = new JLabel();
    		timeStamp.setText(message.getTimeStamp());
    		sidePanel.add(timeStamp);
    		*/
    		
    		//Only appears if the sender is the same as the current user
    		if(message.getSender().equals(user)) {
	    		//Edit Button
    			JButton editButton = new JButton("Edit");
    			
    			editButton.addActionListener((e -> {
    				editedText = messageText.getText();
    				messageToEdit = message;
    				messageText.setText("");
    			}));
    			
    			sidePanel.add(editButton);
    			
    			//Delete Button
    			JButton deleteButton = new JButton("Delete");
	    		
	    		deleteButton.addActionListener((e -> {
	    			messageToDelete = message;
	    			messageText.setText("");
	    			
	    			for(int j = 0; j < messageHistory.size(); j++) {
	    				JTextArea currTextArea = (JTextArea)messageHistory.get(j).getComponent(0);
	    				if(currTextArea.getText() == message.getSender() + ": " + message.getContent());
	    					messageHistory.remove(messageHistory.get(j));
	    					break;
	    			}
	    			
	    			historyPanel.repaint();
	    			historyPanel.revalidate();
	    		}));	
	    		
	    		sidePanel.add(deleteButton);
    		}
    		
    		messagePanel.add(sidePanel);
    		
    		messageHistory.add(messagePanel);
    	}

		
		for(JPanel j : messageHistory) {
    		historyPanel.add(j);
    	}
		
		
		//for(JEditorPane j : messageHistory) {
    	//	historyPanel.add(j);
    	//}
		
		/*
		for(int i = 0; i < messages.size(); i++) {
    		JLabel label = new JLabel();
    		label.setText(messages.get(i).getContent());
    		messageHistory.add(label);
    	}

		
		for(JLabel j : messageHistory) {
    		historyPanel.add(j);
    	}
		*/
		
		historyPanel.repaint();
		historyPanel.revalidate();
	}
	
	public void updateConversations(ArrayList<String> convos) {
		if(convos == null)
			return;
		
		
		//conversationList.clear();
		
		conversations = convos;
		
		for(String c : conversations) {
			if(!c.equals(user)) {			
				if(conversationList.size() == 0) {
					JButton conversationButton = new JButton(c);
					conversationButton.addActionListener(e -> {
						conversationToLoad = c;
					});
					
					conversationList.add(conversationButton);
					conversationPanel.add(conversationButton);
				} else {
					for(int i = 0; i < conversationList.size(); i++) {
						//Checks if conversation already exists
						if(conversationList.get(i).getText().equals(c))
							break;
						
						if(i == conversationList.size() - 1) {
							JButton conversationButton = new JButton(c);
							conversationButton.addActionListener(e -> {
								conversationToLoad = c;
								
								conversationList.add(conversationButton);
								conversationPanel.add(conversationButton);
							});
						}
					}
				}
			}
		}
		
		conversationPanel.repaint();
		conversationPanel.revalidate();
		//conversationPanel.updateUI();
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public boolean isRunning() {
		//System.out.println(running);
		return running;
	}
	
	public void close() {
		running = false;
		frame.dispose();
	}
	
	public void run() {	
		System.out.println("Launched GUI");
    	
    	Container content = frame.getContentPane();
    	frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {
				closeUser = true;
			}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
    	});
    	
    	content.setLayout(new BorderLayout());
    	gui = new GUI();  //GUI object
    	content.add(gui, BorderLayout.CENTER);
    	
    	//Main Frame
    	frame.setSize(600, 400);
    	frame.setLocationRelativeTo(null);
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setResizable(false);
    	frame.setVisible(true);
    	
    	//Left Container
    	leftContainer.setPreferredSize(new Dimension(100, 400));
    	leftContainer.setLayout(layoutContainer);
    	
    	content.add(leftContainer);
    	
    	//Conversation selection panel
    	conversationPanel = new JPanel();
    	conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.Y_AXIS));
    	conversationPanel.setPreferredSize(new Dimension(100, 400));
    	
    	settingsButton = new JButton("Settings");
    	settingsButton.addActionListener(e -> {
    		System.out.println("Settings Button Pressed");
    		swapPanel("Settings");
    	});
    	
    	conversationPanel.add(settingsButton);
    	
    	JScrollPane conversationContentPanel = new JScrollPane(conversationPanel, 
			      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	
    	conversationContentPanel.setPreferredSize(conversationPanel.getPreferredSize());
    	
    	leftContainer.add(conversationContentPanel, BorderLayout.WEST);
    	
    	//Settings Panel
    	//settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
    	settingsPanel.setLayout(new FlowLayout());
    	settingsPanel.setPreferredSize(new Dimension(100, 400));
    	
    	//Return Button
    	returnButton = new JButton("Back");
    	returnButton.addActionListener(e -> {
    		System.out.println("Settings Button");
    		swapPanel("Conversations");
    	});
    	
    	settingsPanel.add(returnButton);
    	
    	newPassText = new JTextField(10);
    	newPassText.setEditable(true);
    	settingsPanel.add(newPassText);
    	
    	passwordButton = new JButton("Change Password");
    	passwordButton.addActionListener(e -> {
    		String pass = newPassText.getText();
    		
    		if(!pass.isBlank() && !(pass.contains(" ")))
    			newPass = pass;
    		
    		newPassText.setText("");
    	});
    	settingsPanel.add(passwordButton);
    	
    	JTextArea notice = new JTextArea("Please enter your password in the above field to delete your account.");
    	notice.setEditable(false);
    	notice.setLineWrap(true);
    	settingsPanel.add(notice);
    	
    	deleteAccountButton = new JButton("Delete Account");
    	deleteAccountButton.addActionListener(e -> {
    		System.out.println("Account Delete Button Pressed");
    		
    		String pass = newPassText.getText();
    		
    		if(!pass.isBlank() && !(pass.contains(" ")))
    			deletedAccountPass = pass;
    		
    		newPassText.setText("");
    	});
    	
    	settingsPanel.add(deleteAccountButton);
    	
    	leftContainer.add(settingsPanel, BorderLayout.WEST);
    	//Previous messages panel
    	historyPanel = new JPanel();
    	historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
    	
    	//historyPanel.setEditable(false);
    	//historyPanel.setLineWrap(true);
    	
    	JScrollPane historyContentPanel = new JScrollPane(historyPanel,
    													  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    													  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	
    	historyContentPanel.setPreferredSize(new Dimension(450, 200));
    	//historyContentPanel.setMaximumSize(new Dimension(400, historyContentPanel.getPreferredSize().height));
    	
    	content.add(historyContentPanel, BorderLayout.EAST);
    	
    	//historyPanel.setPreferredSize(new Dimension(450, 200));
    	//content.add(historyPanel, BorderLayout.NORTH);
    	
    	//Send Message Panel
    	sendButton = new JButton("Send");
    	
    	sendButton.addActionListener(e -> {
    		messageToSend = messageText.getText();
    		messageText.setText("");
    		//System.out.println(messageToSend);
    	});	
    	
    	messageText = new JTextArea();
    	messageText.setLineWrap(true);
    	
    	JPanel messagePanel = new JPanel();
    	JScrollPane messageContentPanel = new JScrollPane(messageText, 
    												      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    													  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	
    	messageContentPanel.setPreferredSize(new Dimension(450, 50));
    	
    	messagePanel.add(messageContentPanel);
    	messagePanel.add(sendButton);
    	content.add(messagePanel, BorderLayout.SOUTH);
    }
}