import java.util.ArrayList;

/**
 * User object class
 */

public class User {
    private final String username; //Username of user
    private final String password; //Password of user
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
     * Returns the conversations of this User
     *
     * @return ArrayList of conversations
     */
    public ArrayList<Conversation> getConversations() {
        return conversations;
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
}
