import java.io.*;
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
 * New conversation:       nc <chat> <...> <username>
 * Delete conversation:    dc <chat> <username>
 * Login user:             lu <username> <password>
 * New user:               nu <username> <password>
 * Close user:             cu <username>
 * Edit user:              eu <username> <password> <newPassword>
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

        ArrayList<String> logins = importLogins();
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
                        //New conversation: nc <chat> <...> <username>
                        //Find user by username, add Conversation to conversations, set openChat to conversation
                        //If user doesn't exist, error code and message is sent to client

                        fields = command.split(" ");
                        senderIndex = findUser(fields[1], users);
                        receiverIndex = findUser(fields[0], users);

                        if (receiverIndex == -1) {
                            sendError("Error: User is not online or username does not exist.", writer);

                        } else if (new ArrayList<>(Arrays.asList(users.get(findUser(fields[1],
                                users)).getChats())).contains(fields[0])) {
                            sendError("Error: Chat already exists.", writer);

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

                    case "lu":
                        //Login user: lu <username> <password>
                        //retrieve conversations from file, send list of chats to client, set openChat to most recent,
                        //send list of messages from openChat to client

                        //TODO: Fix NullPointerException when dedicated user folder is not created before calling lu

                        fields = command.split(" ");

                        //Check that login information is correct
                        if (findUser(fields[0], users) != -1) {
                            sendError("Error: User is already logged in.", writer);
                        } else if (logins.contains(command)) {
                           users.add(loadUser(fields[0], fields[1]));
                           sendConfirmation(writer);

                            for (int i = 0; i < users.size(); i++) {
                                System.out.println(users.get(i).getUsername());
                            }


                        } else {
                            sendError("Error: Username/Password is not correct.", writer);
                        }

                        break;

                    case "nu":
                        //New user: nu <username> <password>
                        //Create new user object and keep it in users array

                        fields = command.split(" ");

                        //Check that username is available
                        if (checkLogins(fields[0], logins)) {
                            sendError("Error: Username taken.", writer);
                        } else {
                            //users.add(new User(fields[0], fields[1]));
                            addLogin(fields[0], fields[1]);
                            logins.add(fields[0] + " " + fields[1]);

                            sendConfirmation(writer);
                        }

                        break;

                    case "cu":
                        //Close user: cu <username>
                        //Rewrite all User data to file, delete User from users

                        closeUser(users.get(findUser(command, users)));
                        users.remove(findUser(command, users));
                        sendConfirmation(writer);

                        break;

                    case "eu":
                        //Edit user: eu <username> <password> <newPassword>
                        //Change the user's password to the new password

                        fields = command.split(" ");

                        //User must enter the logged in and have the correct current password
                        if (findUser(fields[0], users) == -1) {
                            sendError("Error: Wrong username.", writer);
                        } else if (!users.get(findUser(fields[0], users)).getPassword().equals(fields[1])) {
                            sendError("Error: Wrong password.", writer);
                        } else {
                            users.get(findUser(fields[0], users)).setPassword(fields[2]);

                            for (int i = 0; i < logins.size(); i++) {
                                if (logins.get(i).substring(0, logins.get(i).indexOf(" ")).equals(fields[0])) {
                                    logins.remove(i);
                                    logins.add(fields[0] + " " + fields[2]);
                                }
                            }

                            replaceLogin(logins);

                            sendConfirmation(writer);
                        }

                        break;

                    case "xc":
                        //Export conversation: <chat> <username> <filename>
                        //Export all Conversation data to file

                        break;
                }
            }

        } catch (Exception e) {
            System.out.println("Error in Server Class");
            e.printStackTrace();
        }
    }


    /**
     * Offloads user conversation data to file system
     *
     * @param user Complete User object
     */
    public static void closeUser(User user) {

        ArrayList<Conversation> conversations = user.getConversations();

        for (Conversation conversation : conversations) {

            File userFile = new File("src/Server/" + user.getUsername() +
                    '/' + conversation.getChat() + ".txt");

            userFile.getParentFile().mkdirs();
            if (!userFile.exists()) {
                try {
                    userFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try (FileOutputStream fos = new FileOutputStream(userFile, false);
                 PrintWriter pw = new PrintWriter(fos)) {

                pw.println(conversation.getLastModified());

                ArrayList<Message> messages = conversation.getMessages();

                for (Message message : messages) {
                    pw.println(message.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads user conversation data from file system and returns complete user object
     *
     * @param username Username of user
     * @param password Password of user
     * @return Complete User object
     */
    public static User loadUser(String username, String password) {

        File userFile = new File("src/Server/" + username);
        File[] conversations = userFile.listFiles();

        User user = new User(username, password);

        for (File conversation : conversations) {

            try (FileReader fr = new FileReader(conversation);
                BufferedReader bfr = new BufferedReader(fr)) {

                String chat = conversation.getName();
                chat = chat.substring(0, chat.length() - 4);

                ArrayList<Message> messages = new ArrayList<>();

                String line = bfr.readLine();
                long lastModified = Long.parseLong(line);

                line = bfr.readLine();
                while (line != null) {
                    messages.add(new Message(line));
                    line = bfr.readLine();
                }

                user.addConversation(new Conversation(chat, lastModified, messages));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (user.getChats().length > 0) {
            user.setOpenChat(user.getChats()[0]);
        }

        return user;
    }

    /**
     * Imports all usernames and passwords to ArrayList of logins (from main)
     */
    public static ArrayList<String> importLogins() {
        try (FileReader fr = new FileReader("src/Server/logins.txt");
            BufferedReader bfr = new BufferedReader(fr)) {

            ArrayList<String> logins = new ArrayList<>();

            String line = bfr.readLine();
            while (line != null) {
                logins.add(line);
                line = bfr.readLine();
            }

            return logins;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds login information to ArrayList of logins (from main)
     *
     * @param username Username of user
     * @param password Password of user
     */
    public static void addLogin(String username, String password) {
        try (FileOutputStream fos = new FileOutputStream("src/Server/logins.txt", true);
             PrintWriter pw = new PrintWriter(fos)) {

            pw.println(username + " " + password);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Replace the logins file with current logins ArrayList
     *
     * @param logins ArrayList of logins (from main)
     */
    public static void replaceLogin(ArrayList<String> logins) {
        try (FileOutputStream fos = new FileOutputStream("src/Server/logins.txt", false);
             PrintWriter pw = new PrintWriter(fos)) {
            for (int i = 0; i < logins.size(); i++) {
                pw.println(logins.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the logins contains the specified username
     *
     * @param username Username of user
     * @param logins   ArrayList of logins (from main)
     * @return The index of the user in logins
     */
    public static boolean checkLogins(String username, ArrayList<String> logins) {
        boolean bool = false;

        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).contains(username)) {
                bool = true;
            }
        }

        return bool;
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
