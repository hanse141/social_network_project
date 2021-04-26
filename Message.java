import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Message object class
 */

public class Message {
    private final String sender; //Sender of message
    private final String receiver; //Receiver of message
    private String timeStamp; //Time message was sent
    private String content; //Content of message

    /**
     * Constructs a newly allocated Message object and instantiates the fields to the specified parameters
     *
     * @param sender   Sender of message
     * @param receiver Receiver of message
     * @param content  Content of message
     */
    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        this.content = content;
    }

    /**
     * Constructs a newly allocated Message object from a string representation of a Message object
     *
     * @param message String representation of this Message
     */
    public Message(String message) {
        int senderIndex = message.indexOf("sender=");
        int receiverIndex = message.indexOf("receiver=");
        int timeStampIndex = message.indexOf("timeStamp=");
        int contentIndex = message.indexOf("content=");

        this.sender = message.substring(senderIndex + 7, receiverIndex - 2);
        this.receiver = message.substring(receiverIndex + 9, timeStampIndex - 2);
        this.timeStamp = message.substring(timeStampIndex + 10, contentIndex - 2);
        this.content = message.substring(contentIndex + 8, message.length() - 1);
    }

    /**
     * Returns the sender of this Message
     *
     * @return Sender of message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the receiver of this Message
     *
     * @return Receiver of message
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Returns the timeStamp of this Message
     *
     * @return Time message was sent
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Returns the content of this Message
     *
     * @return Content of message
     */
    public String getContent() {
        return content;
    }

    /**
     * Edits the content of this Message to the parameter and updates the timeStamp
     *
     * @param content Content of message
     */
    public void edit(String content) {
        this.content = content;
        this.timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    }

    /**
     * Returns a string representation of this Message
     *
     * @return String representation of this Message
     */
    @Override
    public String toString() {
        return "Message<sender=" + sender + ", receiver=" + receiver +
                ", timeStamp=" + timeStamp + ", content=" + content + '>';
    }

    /**
     * Compares two Messages and returns true if they are equal
     *
     * @param o Object to compare
     * @return Boolean of if Messages are equal
     */
    @Override
    public boolean equals(Object o) {
        boolean isEqual;

        if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            Message message = (Message) o;
            isEqual = Objects.equals(sender, message.sender)
                    && Objects.equals(receiver, message.receiver)
                    && Objects.equals(timeStamp, message.timeStamp)
                    && Objects.equals(content, message.content);
        }

        return isEqual;
    }
}
