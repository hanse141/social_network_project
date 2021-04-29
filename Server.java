import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Server class that contains data of connected users, receives messages, sends open conversation to client
 * <p>
 * List of commands and formatting:
 * New message:            nm Message<>
 * Edit message:           em <content> Message<>
 * Delete message:         dm Message<>
 * Load conversation:      lc <chat> <username>
 * New conversation:       nc <chat> <username>
 * Delete conversation:    dc <chat> <username>
 * New user:               nu <username> <password>
 * Load user:              lu <username> (incomplete)
 * Close user:             cu <username> (incomplete)
 * Export conversation:    xc <chat> <username> <filename> (incomplete)
 * <p>
 * Message.toString() ->   Message<sender=s, receiver=r, timeStamp=yyyy/MM/dd HH:mm:ss, content=c>
 */

public class Server {
    public static void main(String[] args) {
        String[] fields; //Fields of command specific to case
        int senderIndex; //Index of sender in users
        int receiverIndex; //Index of receiver in users
        Message message; //Message object specific to case

        ArrayList<User> users = new ArrayList<>();

        System.out.println("Waiting for client...");

        //Wait for client connection, initialize reader and writer
        try (ServerSocket serverSocket = new ServerSocket(4242);
             Socket socket = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

            System.out.println("Connected.");

            while (true) {

                String command = reader.readLine();
                System.out.println(command);

                String direction = command.substring(0, 2);
                command = command.substring(3);


                //TODO: currently only sends data back to sender, learn how to connect to multiple clients

                switch (direction) {

                    case "nm":
                        //New message: nm Message<>
                        //Convert string to message object, find user by username as sender and receiver,
                        //find conversation by chat as sender and receiver, add Message to messages,
                        //send list of messages to client. Repeat for both sender and receiver.

                        message = new Message(command);
                        senderIndex = findUser(message.getSender(), users);
                        receiverIndex = findUser(message.getReceiver(), users);

                        users.get(senderIndex).getConversation(message.getReceiver()).addMessage(message);
                        users.get(receiverIndex).getConversation(message.getSender()).addMessage(message);

                        sendGuiData(senderIndex, users, writer);

                        break;

                    case "em":
                        //Edit message: em <content> Message<>
                        //Convert string to message object, find user by username as sender and receiver,
                        //find conversation by chat as sender and receiver, edit Message in messages,
                        //send list of messages to client. Repeat for both sender and receiver.

                        int index = command.indexOf("Message<sender=");
                        String content = command.substring(0, index - 1);
                        message = new Message(command.substring(index));
                        senderIndex = findUser(message.getSender(), users);
                        //receiverIndex = findUser(message.getReceiver(), users);

                        users.get(senderIndex).getConversation(message.getReceiver()).editMessage(message, content);
                        //users.get(receiverIndex).getConversation(message.getSender()).editMessage(message, content);

                        //Edit command works on both users with only one edited for some reason

                        sendGuiData(senderIndex, users, writer);

                        break;

                    case "dm":
                        //Delete message: dm Message<>
                        //Convert string to message object, find user by username as sender and receiver,
                        //find conversation by chat as sender and receiver, delete Message in messages,
                        //send list of messages to client. Repeat for both sender and receiver.

                        message = new Message(command);
                        senderIndex = findUser(message.getSender(), users);
                        receiverIndex = findUser(message.getReceiver(), users);

                        users.get(senderIndex).getConversation(message.getReceiver()).deleteMessage(message);
                        users.get(receiverIndex).getConversation(message.getSender()).deleteMessage(message);

                        sendGuiData(senderIndex, users, writer);

                        break;

                    case "lc":
                        //Load conversation: lc <chat> <username>
                        //Find user by username, set openChat to chat

                        fields = command.split(" ");
                        senderIndex = findUser(fields[1], users);

                        users.get(senderIndex).setOpenChat(fields[0]);

                        sendGuiData(senderIndex, users, writer);

                        break;

                    case "nc":
                        //New conversation: nc <chat> <username>
                        //Find user by username, add Conversation to conversations, set openChat to conversation
                        //If user doesn't exist, error code and message is sent to client

                        //TODO: send error if chat already exists

                        fields = command.split(" ");
                        senderIndex = findUser(fields[1], users);
                        receiverIndex = findUser(fields[0], users);

                        if (receiverIndex == -1) {
                            sendError("Error: Username/Chat does not exist", writer);

                        } else {

                            users.get(senderIndex).addConversation(new Conversation(fields[0]));
                            users.get(senderIndex).setOpenChat(users.get(senderIndex).getChats()[0]);

                            users.get(receiverIndex).addConversation(new Conversation(fields[1]));
                            users.get(receiverIndex).setOpenChat(users.get(receiverIndex).getChats()[0]);

                            sendGuiData(senderIndex, users, writer);
                        }

                        break;

                    case "dc":
                        //Delete conversation: dc <chat> <username>
                        //Find user by username, find conversation by chat, delete it,
                        //set openChat to most recent, send list of conversations to client

                        fields = command.split(" ");
                        senderIndex = findUser(fields[1], users);
                        receiverIndex = findUser(fields[0], users);

                        users.get(senderIndex).deleteConversation(users.get(findUser(fields[1], users)).getConversation(fields[0]));
                        users.get(senderIndex).setOpenChat(users.get(senderIndex).getChats()[0]);

                        users.get(receiverIndex).setOpenChat(users.get(receiverIndex).getChats()[0]);

                        sendGuiData(senderIndex, users, writer);

                        break;

                    case "nu":
                        //New user: nu <username> <password>
                        //Create new user object and keep it in users array

                        //TODO: check that username is not already taken, make sure it doesn't have [] characters

                        fields = command.split(" ");
                        users.add(new User(fields[0], fields[1]));

                        sendConfirmation(writer);

                        break;

                    case "lu":
                        //Load user: lu <username>
                        //retrieve conversations from file, send list of chats to client, set openChat to most recent,
                        //send list of messages from openChat to client

                        break;

                    case "cu":
                        //Close user: cu <username>
                        //Rewrite all User data to file, delete User from users

                        break;

                    case "xc":
                        //Export conversation: <chat> <username> <filename>
                        //Export all Conversation data to file

                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Finds the index of the user with the specified username
     *
     * @param username Username of user
     * @param users    ArrayList of users (from main)
     * @return The index of the user in users
     */
    public static int findUser(String username, ArrayList<User> users) {
        int index = -1;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Send command of data used in GUI back to client
     *
     * @param userIndex Index of user in users to send data
     * @param users     ArrayList of users (from main)
     * @param writer    PrintWriter (from main)
     */
    public static void sendGuiData(int userIndex, ArrayList<User> users, PrintWriter writer) {
        //Send gui data: gd Chats<[chat1, chat2...]> Messages<[Message<..1>, Message<..2>...]>

        String[] chats = users.get(userIndex).getChats();
        String[] messages = users.get(userIndex).getOpenConversation().getVisibleMessages();

        writer.write("gd Chats<" + Arrays.toString(chats) + "> Messages<" + Arrays.toString(messages) + '>');
        writer.println();
        writer.flush();
    }

    /**
     * Send command of error back to client
     *
     * @param message Description of error
     * @param writer  PrintWriter (from main)
     */
    public static void sendError(String message, PrintWriter writer) {
        //Send error: er message

        writer.write("er " + message);
        writer.println();
        writer.flush();
    }

    /**
     * Send success message to client
     *
     * @param writer PrintWriter (from main)
     */
    public static void sendConfirmation(PrintWriter writer) {
        writer.write("success");
        writer.println();
        writer.flush();
    }
}

