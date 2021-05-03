import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * ClientHandler Thread class to be used in Server
 * <p>
 * List of commands and formatting:
 * New message:            nm Message<>
 * Edit message:           em <content> Message<>
 * Delete message:         dm Message<>
 * Load conversation:      lc <chat> <username>
 * New conversation:       nc <chat> <username>
 * Delete conversation:    dc <chat> <username>
 * Login user:             lu <username> <password>
 * New user:               nu <username> <password>
 * Close user:             cu <username>
 * Edit user:              eu <username> <password> <newPassword>
 * Export conversation:    xc <chat> <username> <filename> (incomplete)
 * <p>
 * Message.toString() ->   Message<sender=s, receiver=r, timeStamp=MM/dd hh:mm aa, content=c>
 */

public class ClientHandler implements Runnable {
    private final BufferedReader in; //BufferedReader to read client commands
    private final PrintWriter out; //PrintWriter to send client commands
    private String username; //Username of user

    /**
     * Constructs a newly allocated ClientHandler with the specified socket and instantiates its fields to defaults
     *
     * @param clientSocket Client socket
     * @throws IOException from getInputStream()
     */
    public ClientHandler(Socket clientSocket) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
        username = null;
    }

    /**
     * Returns the out of this ClientHandler
     *
     * @return Username of user
     */
    public PrintWriter getOut() {
        return out;
    }

    /**
     * Returns the username of this ClientHandler
     *
     * @return Username of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of this ClientHandler
     *
     * @param username Username of user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Thread run method
     */
    @Override
    public void run() {
        String[] fields; //Fields of command specific to case
        int senderIndex; //Index of sender in users
        int receiverIndex; //Index of receiver in users
        Message message; //Message object specific to case
        String command = null; //Command received from client
        String direction = ""; //Two letter direction of command

        while (!direction.equals("cu")) {

            try {
                command = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(command);

            assert command != null;
            direction = command.substring(0, 2);
            command = command.substring(3);


            switch (direction) {

                case "nm":
                    //New message: nm Message<>
                    //Add Message to messages for both clients

                    message = new Message(command);
                    senderIndex = findUser(message.getSender());
                    receiverIndex = findUser(message.getReceiver());

                    Server.users.get(senderIndex).getConversation(message.getReceiver()).addMessage(message);
                    sendGuiData(senderIndex, out);

                    if (receiverIndex != -1) {
                        Server.users.get(receiverIndex).getConversation(message.getSender()).addMessage(message);
                        sendGuiData(receiverIndex, findWriter(message.getReceiver()));

                    } else {
                        holdCommand(direction + ' ' + command, message.getReceiver());
                    }

                    break;

                case "em":
                    //Edit message: em <content> Message<>
                    //Edit Message in messages for both clients

                    int index = command.indexOf("Message<sender=");
                    String content = command.substring(0, index - 1);
                    message = new Message(command.substring(index));
                    senderIndex = findUser(message.getSender());
                    receiverIndex = findUser(message.getReceiver());

                    Server.users.get(senderIndex).getConversation(message.getReceiver()).editMessage(message, content);
                    sendGuiData(senderIndex, out);
                    sendGuiData(receiverIndex, findWriter(message.getReceiver()));

                    if (receiverIndex == -1) {
                        holdCommand(direction + ' ' + command, message.getReceiver());
                    }

                    break;

                case "dm":
                    //Delete message: dm Message<>
                    //Delete Message in messages for both clients

                    message = new Message(command);
                    senderIndex = findUser(message.getSender());
                    receiverIndex = findUser(message.getReceiver());

                    Server.users.get(senderIndex).getConversation(message.getReceiver()).deleteMessage(message);
                    sendGuiData(senderIndex, out);

                    if (receiverIndex != -1) {
                        Server.users.get(receiverIndex).getConversation(message.getSender()).deleteMessage(message);

                    } else {
                        holdCommand(direction + ' ' + command, message.getReceiver());
                    }

                    break;

                case "lc":
                    //Load conversation: lc <chat> <username>
                    //Set openChat to chat

                    fields = command.split(" ");
                    senderIndex = findUser(fields[1]);

                    Server.users.get(senderIndex).setOpenChat(fields[0]);
                    sendGuiData(senderIndex, out);

                    break;

                case "nc":
                    //New conversation: nc <chat> <username>
                    //Sdd Conversation to conversations or set it to unhidden, set openChat to conversation.
                    //If user doesn't exist or chat already exists, send error command to client

                    fields = command.split(" ");
                    senderIndex = findUser(fields[1]);
                    receiverIndex = findUser(fields[0]);
                    boolean userExists = false;

                    for (String login : Server.logins) {
                        String username = login.split(" ")[0];

                        if (username.equals(fields[0])) {
                            userExists = true;
                            break;
                        }
                    }

                    //Check if user exists or chat is hidden
                    if (!userExists) {
                        sendError("Error: User does not exist.", out);

                    } else if (Server.users.get(senderIndex).getConversations().contains(Server.users.get(senderIndex).getConversation(fields[0]))) {

                        if (Server.users.get(senderIndex).getConversation(fields[0]).isHidden()) {
                            Server.users.get(senderIndex).getConversation(fields[0]).setHidden(false);
                            Server.users.get(senderIndex).getConversation(fields[0]).setLastModified();
                            sendGuiData(senderIndex, out);

                        } else {
                            sendError("Error: Chat already exists.", out);
                        }
                    } else {
                        Server.users.get(senderIndex).addConversation(new Conversation(fields[0]));
                        Server.users.get(senderIndex).setOpenChat(Server.users.get(senderIndex).getChats()[0]);
                        sendGuiData(senderIndex, out);

                        if (receiverIndex != -1) {

                            Server.users.get(receiverIndex).addConversation(new Conversation(fields[1]));
                            Server.users.get(receiverIndex).setOpenChat(Server.users.get(receiverIndex).getChats()[0]);
                            sendConfirmation("Chat created.", findWriter(fields[0]));
                            sendGuiData(receiverIndex, findWriter(fields[0]));

                        } else {
                            holdCommand(direction + ' ' + command, fields[0]);
                        }
                    }

                    break;

                case "dc":
                    //Delete conversation: dc <chat> <username>
                    //Hide conversation, set openChat to most recent

                    fields = command.split(" ");
                    senderIndex = findUser(fields[1]);
                    Server.users.get(senderIndex).getConversation(fields[0]).setHidden(true);

                    if (Server.users.get(senderIndex).getChats().length != 0) {
                        Server.users.get(senderIndex).setOpenChat(Server.users.get(senderIndex).getChats()[0]);
                    }
                    sendGuiData(senderIndex, out);

                    break;

                case "lu":
                    //Login user: lu <username> <password>
                    //Retrieve conversations from file, set openChat to most recent

                    fields = command.split(" ");
                    senderIndex = findUser(fields[0]);

                    //Check that login information is correct
                    if (!Server.logins.contains(command)) {
                        sendError("Error: Username/Password is not correct.", out);
                    } else if (senderIndex != -1) {
                        sendError("Error: User already logged in.", out);
                    } else {
                        Server.users.add(loadUser(fields[0], fields[1]));
                        this.setUsername(fields[0]);
                        sendConfirmation("Login successful.", out);
                        sendGuiData(findUser(fields[0]), out);
                    }

                    break;

                case "nu":
                    //New user: nu <username> <password>
                    //Create new user object and keep it in users array

                    fields = command.split(" ");

                    //Check that username is available
                    if (Server.logins.contains(command)) {
                        sendError("Error: Username taken.", out);
                    } else {
                        addLogin(fields[0], fields[1]);
                        Server.logins.add(fields[0] + ' ' + fields[1]);

                        //Create user folder
                        File userFolder = new File("src/Server/" + fields[0]);
                        if (!userFolder.exists()) {
                            userFolder.mkdirs();
                        }

                        sendConfirmation("User created.", out);
                    }

                    break;

                case "cu":
                    //Close user: cu <username>
                    //Rewrite all User data to file, delete User from users

                    senderIndex = findUser(command);

                    closeUser(Server.users.get(senderIndex));
                    Server.users.remove(senderIndex);
                    sendConfirmation("Goodbye.", out);

                    break;

                case "eu":
                    //Edit user: eu <username> <password> <newPassword>
                    //Change the user's password to the new password

                    fields = command.split(" ");

                    //Check that user login is correct
                    if (!Server.logins.contains(fields[0] + ' ' + fields[1])) {
                        sendError("Error: Username/Password is not correct.", out);
                    } else {
                        Server.users.get(findUser(fields[0])).setPassword(fields[2]);

                        Server.logins.remove(fields[0] + ' ' + fields[1]);
                        Server.logins.add(fields[0] + ' ' + fields[2]);

                        updateLogins();
                        sendConfirmation("Password changed.", out);
                    }

                    break;


                case "xc":
                    //Export conversation: <chat> <username> <filename>
                    //Export all Conversation data to file

                    break;
            }
        }
    }

    /**
     * Holds command sent to offline user in file system for processing on login
     *
     * @param command  Command received from client
     * @param username Username of user
     */
    public static void holdCommand(String command, String username) {

        File commandHolds = new File("src/Server/" + username + "/command hold.txt");
        try {
            commandHolds.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream(commandHolds, true);
             PrintWriter pw = new PrintWriter(fos)) {

            pw.println(command);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Offloads user conversation data to file system
     *
     * @param user Complete User object
     */
    public static void closeUser(User user) {

        //Make file storing each conversation in user folder
        ArrayList<Conversation> conversations = user.getConversations();

        for (Conversation conversation : conversations) {

            File userFile = new File("src/Server/" + user.getUsername() + '/' + conversation.getChat() + ".txt");

            userFile.getParentFile().mkdirs();
            if (!userFile.exists()) {
                try {
                    userFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Write messages as strings for each line
            try (FileOutputStream fos = new FileOutputStream(userFile, false);
                 PrintWriter pw = new PrintWriter(fos)) {

                pw.println(conversation.getLastModified());
                pw.println(conversation.isHidden());

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

        //Process commands sent while user was offline
        ArrayList<String> commands = new ArrayList<>();
        File commandHolds = new File("src/Server/" + username + "/command hold.txt");

        if (commandHolds.exists()) {

            try (FileReader fr = new FileReader(commandHolds);
                 BufferedReader bfr = new BufferedReader(fr)) {

                String line = bfr.readLine();
                while (line != null) {
                    commands.add(line);
                    line = bfr.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            commandHolds.delete();
        }

        //Process conversations from file structure
        File userFile = new File("src/Server/" + username);
        User user = new User(username, password);

        if (userFile.exists()) {

            //Save each conversation to a .txt file
            File[] conversations = userFile.listFiles();

            for (File conversation : conversations) {

                try (FileReader fr = new FileReader(conversation);
                     BufferedReader bfr = new BufferedReader(fr)) {

                    String chat = conversation.getName();
                    chat = chat.substring(0, chat.length() - 4);

                    ArrayList<Message> messages = new ArrayList<>();

                    String line = bfr.readLine();
                    long lastModified = Long.parseLong(line);

                    line = bfr.readLine();
                    boolean hidden = Boolean.parseBoolean(line);

                    line = bfr.readLine();
                    while (line != null) {
                        messages.add(new Message(line));
                        line = bfr.readLine();
                    }

                    user.addConversation(new Conversation(chat, lastModified, hidden, messages));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        user.processHolds(commands);

        if (user.getChats().length != 0) {
            user.setOpenChat(user.getChats()[0]);

        }

        return user;
    }

    /**
     * Replace the logins file with current logins ArrayList
     */
    public static void updateLogins() {
        try (FileOutputStream fos = new FileOutputStream("src/Server/logins.txt", false);
             PrintWriter pw = new PrintWriter(fos)) {
            for (int i = 0; i < Server.logins.size(); i++) {
                pw.println(Server.logins.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static PrintWriter findWriter(String username) {
        int index = -1;

        for (int i = 0; i < Server.clients.size(); i++) {
            if (Server.clients.get(i).getUsername().equals(username)) {
                index = i;
                break;
            }
        }

        return Server.clients.get(index).getOut();

    }

    /**
     * Finds the index of the user with the specified username
     *
     * @param username Username of user
     * @return The index of the user in users
     */
    public static int findUser(String username) {
        int index = -1;

        for (int i = 0; i < Server.users.size(); i++) {
            if (Server.users.get(i).getUsername().equals(username)) {
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
     * @param writer    PrintWriter of client
     */
    public static void sendGuiData(int userIndex, PrintWriter writer) {
        //Send gui data: gd Chats<[chat1, chat2...]> Messages<[Message<..1>, Message<..2>...]>
        String[] chats;
        String[] messages;

        try {
            chats = Server.users.get(userIndex).getChats();
        } catch (NullPointerException e) {
            chats = new String[0];
        }
        try {
            messages = Server.users.get(userIndex).getOpenConversation().getVisibleMessages();
        } catch (NullPointerException e) {
            messages = new String[0];
        }

        sendCommand("gd Chats<" + Arrays.toString(chats) + "> Messages<" + Arrays.toString(messages) + '>', writer);
    }

    /**
     * Send command of error back to client
     *
     * @param message Description of error
     * @param writer  PrintWriter of client
     */
    public static void sendError(String message, PrintWriter writer) {
        //Send error: er message
        sendCommand("er " + message, writer);
    }

    /**
     * Send success command back to client
     *
     * @param message Description of success
     * @param writer  PrintWriter of client
     */
    public static void sendConfirmation(String message, PrintWriter writer) {
        //Send success: su message
        sendCommand("su " + message, writer);
    }

    /**
     * Send command to client
     *
     * @param command Command to send client
     * @param writer  PrintWriter of client
     */
    public static void sendCommand(String command, PrintWriter writer) {
        writer.write(command);
        writer.println();
        writer.flush();
    }
}

