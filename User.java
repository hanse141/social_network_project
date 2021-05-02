import java.util.ArrayList;
import java.util.Collections;

/**
 * User object class
 */

public class User {
    private final String username; //Username of user
    private String password; //Password of user
    private String openChat; //Chat that user currently has visible
    private final ArrayList<Conversation> conversations; //ArrayList of conversations

    /**
     * Constructs a newly allocated User object and instantiates the fields to the specified parameters
     *
     * @param username Username of user
     * @param password Password of user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.openChat = null;
        this.conversations = new ArrayList<Conversation>();
    }

    /**
     * Returns the username of this User
     *
     * @return Username of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of this User
     *
     * @return Password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the openChat of this User
     *
     * @return Chat that user currently has visible
     */
    public String getOpenChat() {
        return openChat;
    }

    /**
     * Returns the conversations of this User
     *
     * @return ArrayList of conversations
     */
    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    /**
     * Sets the password of this User
     *
     * @param password The new password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the openChat of this User
     *
     * @param openChat Chat that user currently has visible
     */
    public void setOpenChat(String openChat) {
        this.openChat = openChat;
    }

    /**
     * Adds a Conversation to conversations
     *
     * @param conversation Conversation object to add
     */
    public void addConversation(Conversation conversation) {
        conversations.add(conversation);
    }

    /**
     * Deletes the Conversation parameter in conversations
     *
     * @param conversation Conversation object to delete
     */
    public void deleteConversation(Conversation conversation) {
        conversations.remove(conversation);
    }

    /**
     * Finds and returns the conversation with the specified chat
     *
     * @param chat Name of chat
     * @return The conversation with this chat
     */
    public Conversation getConversation(String chat) {
        Conversation conversation = null;

        for (Conversation c : conversations) {
            if (c.getChat().equals(chat)) {
                conversation = c;
                break;
            }
        }

        return conversation;
    }

    /**
     * Returns this user's open conversation
     *
     * @return The open conversation
     */
    public Conversation getOpenConversation() {
        return getConversation(openChat);
    }

    /**
     * Creates list of chats from this user's conversations in order of lastModified
     *
     * @return List of chats
     */
    public String[] getChats() {

        if (conversations.size() == 0) {
            return new String[0];

        } else {
            Collections.sort(conversations);

            ArrayList<String> chats = new ArrayList<>();

            for (int i = 0; i < conversations.size(); i++) {
                if (!conversations.get(i).isHidden()) {
                    chats.add(conversations.get(i).getChat());
                }
            }

            String[] chatArr = new String[chats.size()];
            chatArr = chats.toArray(chatArr);

            return chatArr;
        }
    }

    /**
     * @param commands
     */
    public void processHolds(ArrayList<String> commands) {
        String[] fields;
        Message message;

        for (String command : commands) {

            String direction = command.substring(0, 2);
            command = command.substring(3);

            switch (direction) {

                case "nm":
                    message = new Message(command);
                    this.getConversation(message.getSender()).addMessage(message);
                    break;

                case "em":
                    int index = command.indexOf("Message<sender=");
                    String content = command.substring(0, index - 1);
                    message = new Message(command.substring(index));
                    this.getConversation(message.getSender()).editMessage(message, content);
                    break;

                case "dm":
                    message = new Message(command);
                    this.getConversation(message.getSender()).deleteMessage(message);
                    break;

                case "nc":
                    fields = command.split(" ");
                    this.addConversation(new Conversation(fields[1]));
                    break;
            }
        }
    }
}
