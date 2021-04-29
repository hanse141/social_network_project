import java.util.ArrayList;
import java.util.Collections;

/**
 * User object class
 */

public class User {
    private final String username; //Username of user
    private final String password; //Password of user
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
        Collections.sort(conversations);

        String[] chats = new String[conversations.size()];

        for (int i = 0; i < conversations.size(); i++) {
            chats[i] = conversations.get(i).getChat();
        }

        return chats;
    }
}
