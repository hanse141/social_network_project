import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;

import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Client class that contains GUI, data of open conversation, and sends messages to server
 */

public class Client {
    private static final String HOST = "localhost"; //Host of socket
    private static final int PORT = 4242; //Port of socket

	static Message messageToSend = new Message("", "", "");
	static String editedMessage = "";
	static Socket socket;
	static User user;
	static boolean loggedIn = false;
	
	static final Login login = new Login();
    static final GUI gui = new GUI();
	
    static String commandToSend = "";
    
    static String username = "";
    static String password = "";
    static String recipient = "";
    
    public static void main(String[] args) {
    
    	Object obj = new Object();
    	
    	
        	
        SwingUtilities.invokeLater(login);
    	
        //Connect to server, initialize reader and writer
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
        	Scanner scan = new Scanner(System.in);
        	/*
            ServerConnection serverConnection = new ServerConnection(socket);
            Thread serverThread = new Thread(serverConnection);
            Thread clientThread = new Thread(Thread.currentThread());
            
            serverThread.start();

        	Thread loginThread = new Thread(login);
        	*/
        	String conversations = "";
        	String loginInfo = "";
        	boolean incorrectLogin = false;
        	boolean acceptCommands = true;
        	
        	String lastCommand = "";
        	
        	
        	ServerConnection serverConnection = new ServerConnection(socket);
            new Thread(serverConnection).start();


            //Send and receive commands with server, update GUI accordingly
            commandToSend = "";
            while (gui.isRunning()) {
            	/*
            	if(guiData != null && guiData.length() > 2) {
        			System.out.println(guiData);
        			//clientThread.join();
        			//if(guiData.substring(0, 2).equals("er")) 
        			//	incorrectLogin = true;
        			
        			//if(!incorrectLogin && guiData.substring(0, 2).equals("su")) {
        			//if(!incorrectLogin && guiData.substring(0, 2).equals("gd")) {
        				String[] userInfo = loginInfo.split(" ");
        				user = new User(userInfo[1], userInfo[2]);
        				
        				gui = new GUI();
        	        	SwingUtilities.invokeAndWait(gui);
        				
        				//conversations = guiData;
        				loggedIn = true;
        			//}
        			lastCommand = guiData;
        		}*/
            	
            	if(loggedIn) { 
	            	if(gui.newMessage() != null) {
	    	       		messageToSend = new Message(user.getUsername(), recipient, gui.newMessage());
	    	       		
	    	       		commandToSend = "nm " + messageToSend;
	    	       		//out.write("nm " + messageToSend);
	    	       		//out.println();
	    	       		//out.flush(); //Ensure data is sent to the server
	    	       		
	    	       		gui.resetMessage();
	                }
	            	
	            	if(gui.editMessage() != null) {
	            		//messageToSend = gui.editMessage();
	            		System.out.println("em " + gui.editMessage());
	    	       		//out.write("em " + gui.editMessage());
	    	       		//out.println();
	    	       		//out.flush(); //Ensure data is sent to the server
	    	       		
	            		commandToSend = "em " + gui.editMessage();
	            		
	    	       		gui.resetMessage();
	            	}
	            	
	            	if(gui.deleteMessage() != null) {
	            		messageToSend = gui.deleteMessage();
	    	       		//out.write("dm " + messageToSend);
	    	       		//out.println();
	            		
	            		commandToSend = "dm " + messageToSend;
	    	       		//out.flush(); //Ensure data is sent to the server
	    	       		
	    	       		gui.resetMessage();
	            	}
	    	       	if(gui.loadConversation() != null) {
	                	//messageToSend = 
	                	System.out.println("lc " + gui.loadConversation() + " " + user.getUsername());
	                	//out.write("lc " + gui.loadConversation() + " " + user.getUsername());
	                	commandToSend = "lc " + gui.loadConversation() + " " + user.getUsername();
	                	
	                	gui.resetMessage();
	                }
	                	
	                if(gui.changePass() != null) {
	                	System.out.println("eu " + user.getUsername() + " " + user.getPassword() + " " + gui.changePass());
	                	//out.write("eu " + user.getUsername() + " " + user.getPassword() + " " + gui.changePass());
	                	//out.flush();
	                	
	                	commandToSend = "eu " + user.getUsername() + " " + user.getPassword() + " " + gui.changePass();
	                	
	                	gui.resetMessage();
	                }
	                
	                if(gui.deleteAccount() != null) {
	                	String inputtedPass = gui.deleteAccount();
	                	if(inputtedPass.equals(user.getPassword())) {
	    	           		System.out.println("du " + user.getUsername() + " " + user.getPassword());
	    	           		//out.write("du " + user.getUsername() + " " + inputtedPass);
	    	           		//out.flush();
	                	}
	    	            		
	                	commandToSend = "du " + user.getUsername() + " " + inputtedPass;
	                	
	                	gui.resetMessage();
	                		
	                	gui.close();
	                }
	                if(gui.logoff()) {
	                	commandToSend = "cu " + user.getUsername();
	                	
	                	gui.resetMessage();
	                	gui.close();
	                }
	                
                } else {
                	//System.out.println("Login Check");
                	//loginThread.join();
                	//clientThread.join();
                	if(login.pingServer() != null) {
            			loginInfo = login.pingServer();
            			//System.out.println("Login Info: " + loginInfo);
            			
    	        		//writer.write("lu " + user.getUsername() + " " + user.getPassword());
    	            	//out.write(loginInfo);
            			commandToSend = loginInfo;
            			
            			String[] splitLoginInfo = loginInfo.split(" ");
            			username = splitLoginInfo[1];
            			password = splitLoginInfo[2];
            			//out.flush();
    	            	
            			//writer.write("lu tom asdf");
    	            	//out.println();
    	            	
    	            	//incorrectLogin = false;
    	            	login.resetCredentials();
    	            	//loginThread.join();
            		}
                }

                //Exit condition of loop
                if (!serverConnection.isRunning()) {
                    break;
                }

                //Thread.sleep(500);
                String command = commandToSend;//scan.nextLine();
                
               // command = scan.nextLine();
                
                System.out.println(command);
                if(!command.equals(""))
                	out.println(command);
                if(command.length() > 2 && command.substring(0,2).equals("cu"))
                	Thread.sleep(2000);
                
                
                Thread.sleep(500); //Min time between sending messages
            }
        } catch (ConnectException e) {
        	 gui.running = false;
        	 JOptionPane.showMessageDialog(null, "Cannot Connect to Server. Please check your connection and restart the program.", "Error",
                     JOptionPane.ERROR_MESSAGE);
        	 login.close();
        	 gui.close();
     	} catch (InterruptedException e) {
        	e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
    }
    
    public static boolean isLoggedIn() {
    	return loggedIn;
    }
    
    public static void setUser() {
    	user = new User(username, password);
    }
    
    public static void setLogin(boolean l) {
    	loggedIn = l;
    }
    
    public static void resetCommand() {
    	commandToSend = "";
    }
    
    public static void setRecipient(String r) {
    	recipient = r;
    	System.out.println("Recipient: " + recipient);
    }
    
    public static ArrayList<Message> convertToMessages(String input) {
    	try {
    		//System.out.println(input);
    		
    		String chats = input.substring(input.indexOf("Chats"), input.indexOf("Messages") - 1);
    		String messages = input.substring(input.indexOf("Messages"), input.length());
    	
    		ArrayList<Message> messageList = new ArrayList<Message>();
    		
    		int numMessages = countMessageNumber(messages);
    		
    		String currentString = messages;
    		
    		//Parse Strings
    		for(int i = 0; i < numMessages; i++) {
    			int index = currentString.indexOf(">") + 1;
    			
    			messageList.add(new Message(currentString.substring(currentString.indexOf("Message<"), index)));
    			currentString = currentString.substring(index, currentString.length());
    		}
    		
    		return messageList;
    	} catch (IndexOutOfBoundsException e) {
    		System.out.println("Out of Bounds");
    	} catch (Exception e) {
    		System.out.println("Invalid String Format");
    	}
    	
    	return null;
  	}
    
    public static int countMessageNumber(String messages) {
    	if(messages.indexOf("Message<") != -1) {
    		return 1 + countMessageNumber(messages.substring(messages.indexOf("Message<") + 8));
    	}
    	return 0;
    }
    
    public static ArrayList<Conversation> convertToConversations(String input) {
    	try {
    		//System.out.println(input);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
}
