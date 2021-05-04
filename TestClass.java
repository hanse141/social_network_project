import java.util.ArrayList;

public class TestClass {
	public static void main(String[] args) {
		String testMessage1 = "gd Chats<[andrew]> Messages<[Message<sender=andrew, receiver=tom, timeStamp=yyyy/MM/dd HH:mm:ss, content=yo>, Message<sender=tom, receiver=andrew, timeStamp=2021/05/01 16:18:35, content=adfadfsadf>]>";
		String testMessage2 = "gd Chats<[tom]> Messages<[Message<sender=andrew, receiver=tom, timeStamp=yyyy/MM/dd HH:mm:ss, content=yo>]>";
		convertToMessages(testMessage1);
		System.out.println();
		convertToMessages(testMessage2);
	}
	 public static ArrayList<Message> convertToMessages(String input) {
	    	try {
	    		System.out.println(input);
	    		
	    		String chats = input.substring(input.indexOf("Chats"), input.indexOf("Messages") - 1);
	    		String messages = input.substring(input.indexOf("Messages"), input.length());
	    	
	    		//ArrayList<String> messageList = new ArrayList<String>();
	    		ArrayList<Message> messageList = new ArrayList<Message>();
	    		
	    		int numMessages = countMessageNumber(messages);
	    		
	    		String currentString = messages;
	    		
	    		//Parse Strings
	    		for(int i = 0; i < numMessages; i++) {
	    			int index = currentString.indexOf(">") + 1;
	    			
	    			messageList.add(new Message(currentString.substring(currentString.indexOf("Message<"), index)));
	    			currentString = currentString.substring(index, currentString.length());
	    		}
	    		
	    		//Parse Messages in Parsed Strings
	    		//for(String message : messageList) {
	    			
	    			/*
	    			String sender = "";
	    			String receiver = "";
	    			String content = "";
	    			
	    			for(int i = 0; i < 4; i++) {
	    				int index;
	    				
	    				switch(i) {
	    					case 0:
	    						index = message.indexOf(", receiver=");
	    						sender = message.substring(message.indexOf("sender=") + 7, index);
	    						break;
	    					case 1:
	    						index = message.indexOf(", receiver=");
	    						break;
	    					case 2:
	    						break;
	    				}
	    			}
	    		}
	    		
	    		for(Message m : messageList) {
	    			System.out.println(m.toString());
	    		}*/
	    		
	    		return messageList;
	    	} catch (IndexOutOfBoundsException e) {
	    		e.printStackTrace();
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
}
