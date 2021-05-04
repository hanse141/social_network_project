import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 * ServerConnection Thread class to be used in Client
 * <p>
 * List of commands and formatting:
 * Send gui data:  gd Chats<[chat1, chat2...]> Messages<[Message<..1>, Message<..2>...]>
 * Send error:     er message
 * Send success:   su message
 */

public class ServerConnection implements Runnable {
    private final BufferedReader in; //BufferedReader to read server commands
    private boolean isRunning; //If the Socket connection exists

    private GUI gui;
    private Login login;
    
    private String recipient = "";

    private boolean convoInitialzed = false;
    /**
     * Constructs a newly allocated ServerConnection with the specified socket and instantiates fields to defaults
     *
     * @param socket Client socket
     * @throws IOException from getInputStream()
     */
    public ServerConnection(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        isRunning = true;
        
        gui = Client.gui;
        login = Client.login;
    }

    /**
     * Returns the isRunning of this ServerConnection
     *
     * @return If the Socket connection exists
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Thread run method
     */
    @Override
    public void run() {
        Object obj = new Object(); //Object to synchronize code

        try {
            String command;
            while (isRunning) {

                command = in.readLine();

                //TODO: process commands from the server
                System.out.println("Received from server: " + command);
                

        		//System.out.println(guiData);
        		if(command != null) {
        			switch(command.substring(0, 2)) {
        				case "gd":
        					if(Client.isLoggedIn()) {
        	        			//guiThread.join();
        	        				Client.gui.updateMessages(convertToMessages(command, false));
        	        				
        	        				ArrayList<String> conversations = convertToConversations(command);
        	        				
        	        				for(String s: conversations) {
        	        					System.out.println(s);
        	        				}
        	        				
        	        				if(conversations.size() > 0 && !conversations.get(0).equals("")) {
        	        					Client.gui.updateConversations(conversations);
        	        					
        	        					if(command.substring(0, 2).equals("lc"))
        	        						Client.setRecipient(conversations.get(0));
        	        				}
        	        				//Client.resetCommand();
        	        			} else {
        	        				String[] userInfo = command.split(" ");
        	        				User user = new User(userInfo[1], userInfo[2]);
        	        				
        	        				System.out.println(user.toString());
        	        				
        	        				Client.setUser();
        	        				
        	        				Client.login.close();
        	        				
        	        	        	SwingUtilities.invokeAndWait(Client.gui);
        	        				
        	        				//conversations = guiData;
        	        				Client.setLogin(true);
        	        				
        	        				Client.gui.updateMessages(convertToMessages(command, false));
        	        				
        	        				ArrayList<String> conversations = convertToConversations(command);
        	        				
        	        				if(conversations.size() > 0 && !conversations.get(0).equals("")) {
        	        					Client.gui.updateConversations(conversations);
        	        					Client.setRecipient(conversations.get(0));
        	        					System.out.println(conversations.get(0));
        	        				}
        	        				//Client.resetCommand();
        	        			}
        					break;
        				case "er":
        					JOptionPane.showMessageDialog(null, command.substring(3), "Error",
      							  JOptionPane.ERROR_MESSAGE);
        					break;
        				case "su":
        					JOptionPane.showMessageDialog(null, command.substring(3), "Success",
        							  JOptionPane.INFORMATION_MESSAGE);
        					break;
        				case "xc":
        					String chatName = command.substring(3, command.indexOf("Message<") - 1);        					
        					String messages = command.substring(command.indexOf("Message<"));
        					
        					Conversation.export(messages, chatName);
        					
        					
        					
        					
        					//ArrayList<Message> messages = convertToMessages(command.substring(0, command.indexOf(0)), true);
        					
        					//File fileToWrite = new File(chatName + ".csv");
        					
        					//try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileToWrite));) {
        					//	fileToWrite.createNewFile();
        					//	
        					//	for(Message m: messages) {
        					//		bw.write("\"" + m.toString() + "\",");
        					//		bw.newLine();
        					//	}
        					//	bw.close();
        					//}
        					
        					break;
        			}
        		}
        		Client.resetCommand();
        		
                isRunning = !command.substring(3).equals("Goodbye.");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
        }
    public static ArrayList<Message> convertToMessages(String input, boolean export) {
    	try {
    		//System.out.println(input);
    		
    		String messages;
    		if(export)
    			messages = input;
    		else
    			messages = input.substring(input.indexOf("Messages"), input.length());
    	
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
    
    public static ArrayList<String> convertToConversations(String input) {
    	try {
    		ArrayList<String> conversations = new ArrayList<String>();
    		
    		System.out.println("Conversation Input: " + input);
    		System.out.println(input.substring(input.indexOf("Chats"), input.indexOf("Messages<") - 1));
    		String[] chats = input.substring(input.indexOf("Chats") + 7, input.indexOf("Messages<") - 3).split(",");
    		
    		for(String c : chats) {
    			c.replaceAll(" ", "");
    			System.out.println(c.indexOf(" "));
    			System.out.println(c.trim());
    			conversations.add(c.trim());
    		}
    		return conversations;
    		
    		//ArrayList<Message> messages = convertToMessages(input.substring(input.indexOf("Messages"), input.length()));
    		
    		
    		
      	} catch (IndexOutOfBoundsException e) {
    		System.out.println("Out of Bounds");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
}
