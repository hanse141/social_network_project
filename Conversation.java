import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Conversation object class
 */

public class Conversation implements Comparable<Conversation> {
    private final String chat; //Name of chat (receiver or group)
    private String[] participants; //List of usernames of participants (null if not group chat)
    private long lastModified; //Time in milliseconds since epoch of last modification
    private boolean hidden; //If the user has deleted the conversation
    private ArrayList<Message> messages; //ArrayList of messages


    /**
     * Constructs a newly allocated Conversation object and instantiates its chat to the parameter
     *
     * @param chat Name of chat
     */
    public Conversation(String chat) {
        this.chat = chat;
        this.participants = null;
        this.lastModified = new Date().getTime();
        this.hidden = false;
        this.messages = new ArrayList<Message>();
    }

    /**
     * Constructs a newly allocated Conversation object and instantiates its fields to the specified parameters
     *
     * @param conversation Conversation object to copy
     */
    public Conversation(Conversation conversation) {
        this.chat = conversation.getChat();
        this.participants = conversation.getParticipants();
        this.lastModified = conversation.getLastModified();
        this.hidden = conversation.isHidden();
        this.messages = (ArrayList<Message>) conversation.getMessages().clone();
    }

    /**
     * Constructs a newly allocated Conversation object and instantiates its chat to the parameter
     *
     * @param chat         Name of chat
     * @param participants List of usernames of participants
     */
    public Conversation(String chat, String[] participants) {
        if (chat.charAt(chat.length() - 1) == ' ') {
            this.chat = chat;
        } else {
            this.chat = chat + ' ';
        }
        this.participants = participants;
        this.lastModified = new Date().getTime();
        this.hidden = false;
        this.messages = new ArrayList<Message>();
    }

    /**
     * Constructs a newly allocated Conversation object and instantiates its fields to the specified parameters
     *
     * @param chat         Name of chat
     * @param participants List of usernames of participants
     * @param lastModified Time in milliseconds since epoch of last modification
     * @param hidden       If the user has deleted the conversation
     * @param messages     ArrayList of messages
     */
    public Conversation(String chat, String[] participants, long lastModified, boolean hidden, ArrayList<Message> messages) {
        this.chat = chat;
        this.participants = participants;
        this.lastModified = lastModified;
        this.hidden = hidden;
        this.messages = messages;
    }

    /**
     * Returns the chat of this Conversation
     *
     * @return Name of chat
     */
    public String getChat() {
        return chat;
    }

    /**
     * Returns the participants of this Conversation
     *
     * @return List of usernames of participants
     */
    public String[] getParticipants() {
        return participants;
    }

    /**
     * Returns the lastModified of this Conversation
     *
     * @return Time in milliseconds since epoch of last modification
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Returns the hidden of this Conversation
     *
     * @return If the user has deleted the conversation
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets the hidden of this Conversation
     *
     * @param hidden If the user has deleted the conversation
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Sets the lastModified of this Conversation to the current time
     */
    public void setLastModified() {
        lastModified = new Date().getTime();
    }

    /**
     * Returns the messages of this Conversation
     *
     * @return ArrayList of messages
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants.clone();
    }

    /**
     * Sets the messages of this Conversation
     *
     * @param messages Arraylist of messages
     */
    public void setMessages(ArrayList<Message> messages) {
        this.messages = (ArrayList<Message>) messages.clone();
    }

    /**
     * Adds a Message to messages
     *
     * @param message Message object to add
     */
    public void addMessage(Message message) {
        messages.add(message);
        this.setHidden(false);
        setLastModified();
    }

    /**
     * Edits the Message parameter in messages with the content parameter
     *
     * @param message Message object to edit
     * @param content Content of message
     */
    public void editMessage(Message message, String content) {
        messages.get(messages.indexOf(message)).edit(content);

        setLastModified();
    }

    /**
     * Deletes the Message parameter in messages
     *
     * @param message Message object to delete
     */
    public void deleteMessage(Message message) {
        messages.remove(message);
        setLastModified();
    }

    /**
     * Adds a participant to this conversation
     *
     * @param username Username of user
     * @return If participant was added successfully, false indicates user is already added
     */
    public boolean addParticipant(String username) {
        String[] newParticipants = new String[participants.length + 1];
        int i;

        for (i = 0; i < participants.length; i++) {
            if (participants[i] == username) {
                return false;
            }
            newParticipants[i] = participants[i];
        }

        newParticipants[i] = username;

        this.setParticipants(newParticipants);
        return true;
    }

    /**
     * Creates list of string representation messages of the last 10 messages from this conversation
     *
     * @return List of string representation of messages
     */
    public String[] getVisibleMessages() {
        int size = messages.size();
        int index;

        if (size >= 20) {
            index = size - 20;
        } else {
            index = 0;
        }

        String[] visibleMessages = new String[size - index];

        for (int i = index; i < size; i++) {
            visibleMessages[i - index] = messages.get(i).toString();
        }

        return visibleMessages;
    }

    /**
     * Compares two messages for use in the Collections.sort() method
     *
     * @param conversation Conversation to compare
     * @return -1 if conversation's lastModified is smaller, 1 if it is larger, and 0 if it is equal
     */
    @Override
    public int compareTo(Conversation conversation) {
        return Long.compare(conversation.getLastModified(), this.getLastModified());
    }

    /**
     * Exports a Conversation to the specified filename
     *
     * @param messageCommand String containing conversation info as <chat> Message<>Message<>...
     * @param filename       Name of file
     */
    public static void export(String messageCommand, String filename) {
        String chatName;
        String messageString;

        if (messageCommand.contains("  ")) {
            int index = messageCommand.indexOf("  ");
            chatName = messageCommand.substring(0, index + 1);
            messageString = messageCommand.substring(index + 2);

        } else {
            chatName = messageCommand.split(" ")[0];
            messageString = messageCommand.substring(chatName.length() + 1);
        }

        String[] messageStrings = messageString.split("(?=Message<sender=)");

        try (FileOutputStream fos = new FileOutputStream(filename, false);
             PrintWriter pw = new PrintWriter(fos)) {

            pw.println(chatName);

            for (String messageAsString : messageStrings) {
                Message message = new Message(messageAsString);
                pw.println(message.getTimeStamp() + ' ' + message.getSender() + ": " + message.getContent() + ',');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
