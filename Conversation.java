import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Conversation object class
 */

public class Conversation {
    private final String chat; //Name of chat (receiver or group)
    private final ArrayList<Message> messages; //ArrayList of messages

    /**
     * Constructs a newly allocated Conversation object and instantiates its chat to the parameter
     *
     * @param chat Name of chat
     */
    public Conversation(String chat) {
        this.chat = chat;
        this.messages = new ArrayList<Message>();
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
     * Returns the messages of this Conversation
     *
     * @return ArrayList of messages
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }

    /**
     * Adds a Message to messages
     *
     * @param message Message object to add
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Edits the Message parameter in messages with the content parameter
     *
     * @param message Message object to edit
     * @param content Content of message
     */
    public void editMessage(Message message, String content) {
        messages.get(messages.indexOf(message)).edit(content);
    }

    /**
     * Deletes the Message parameter in messages
     *
     * @param message Message object to delete
     */
    public void deleteMessage(Message message) {
        messages.remove(message);
    }

    /**
     * Exports this Conversation to the specified filename
     *
     * @param filename Name of file
     */
    public void export(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename, false);
             PrintWriter pw = new PrintWriter(fos)) {

            pw.println("Conversation<chat=" + chat + ", messages=");

            for (Message message : messages) {
                pw.println(message.toString() + ',');
            }

            pw.println('>');

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
