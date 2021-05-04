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
        if (conversations.size() == 1) {
            this.openChat = conversation.getChat();
        }
    }

    /**
     * Deletes the Conversation parameter in conversations
     *
     * @param conversation Conversation object to delete
     */
    public void deleteConversation(Conversation conversation) {
        conversations.remove(conversation);
        if (conversations.size() == 0) {
            this.openChat = null;
        }
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
     * Process the commands sent to a user while they were offline
     *
     * @param commands ArrayList of commands to process
     */
    public void processHolds(ArrayList<String> commands) {
        String[] fields;
        Message message;
        String chatName;
        Conversation conversation;

        for (String command : commands) {

            String direction = command.substring(0, 2);
            command = command.substring(3);

            switch (direction) {

                case "nm":
                    message = new Message(command);
                    if (message.getReceiver().contains(" ")) { //Group chat
                        this.getConversation(message.getReceiver()).addMessage(message);
                    } else {
                        this.getConversation(message.getSender()).addMessage(message);
                    }
                    break;

                case "em":
                    int index = command.indexOf("Message<sender=");
                    String content = command.substring(0, index - 1);
                    message = new Message(command.substring(index));
                    if (message.getReceiver().contains(" ")) { //Group chat
                        this.getConversation(message.getReceiver()).editMessage(message, content);
                    } else {
                        this.getConversation(message.getSender()).editMessage(message, content);
                    }
                    break;

                case "dm":
                    message = new Message(command);
                    if (message.getReceiver().contains(" ")) { //Group chat
                        this.getConversation(message.getReceiver()).deleteMessage(message);
                    } else {
                        this.getConversation(message.getSender()).deleteMessage(message);
                    }
                    break;

                case "nc":
                    fields = command.split(" ");
                    this.addConversation(new Conversation(fields[1]));
                    break;

                case "ng":
                    index = command.indexOf("  ");
                    chatName = command.substring(0, index);
                    fields = command.substring(index + 2).split(" ");

                    conversation = new Conversation(chatName, new String[0]);
                    for (String participant : fields) {

                        if (ClientHandler.doesUserExist(participant)) {
                            conversation.addParticipant(participant);
                        }
                    }
                    this.addConversation(new Conversation(conversation.getChat(), conversation.getParticipants()));
                    break;

                case "ag":
                    index = command.indexOf("  ");
                    chatName = command.substring(0, index + 1);
                    int messageIndex = command.indexOf("Message<sender=");
                    String[] participants = null;
                    fields = command.substring(index + 2).split(" ");

                    //If this is user being added
                    if (this.getUsername().equals(fields[0])) {

                        for (int i = 0; i < Server.groups.size(); i++) {
                            if (Server.groups.get(i).startsWith(chatName)) {
                                participants = Server.groups.get(i).substring(index + 2).split(" ");
                            }
                        }

                        conversation = new Conversation(chatName, participants);
                        ArrayList<Message> messages = new ArrayList<Message>();

                        if (messageIndex > 0) {
                            String[] messageStrings = command.substring(messageIndex).split("(?=Message<sender=)");
                            for (String messageString : messageStrings) {
                                messages.add(new Message(messageString));
                            }
                            conversation.setMessages(messages);
                        }

                        this.addConversation(conversation);

                    } else { //If this is participant updating participants
                        this.getConversation(chatName).addParticipant(fields[0]);
                    }
                    break;

                case "lg":
                    index = command.indexOf("  ");
                    fields = new String[2];
                    fields[0] = command.substring(0, index + 1);
                    fields[1] = command.substring(index + 2);
                    this.getConversation(fields[0]).setParticipants(ClientHandler.getParticipants(fields[0]));
                    break;
            }
        }
    }
}
