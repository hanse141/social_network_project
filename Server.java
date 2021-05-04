import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class that contains data of connected users, receives messages, sends open conversation to client
 */

public class Server {
    protected static final ArrayList<String> logins = importLogins(); //Login data of all users
    protected static final ArrayList<String> groups = importGroups(); //All group chats data
    protected static final ArrayList<User> users = new ArrayList<>(); //User objects of online users
    protected static final ArrayList<ClientHandler> clients = new ArrayList<>(); //ClientHandler objects of online users
    private static final ExecutorService pool = Executors.newFixedThreadPool(4); //Pool of threads
    private static final int PORT = 4242; //Port of server socket

    public static void main(String[] args) {

        //Wait for client connection, initialize reader and writer
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {

                Socket client = serverSocket.accept();
                System.out.println("Client connected.");

                ClientHandler clientThread = new ClientHandler(client);
                clients.add(clientThread);

                pool.execute(clientThread);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imports all group chat data to ArrayList of groups
     */
    public static ArrayList<String> importGroups() {
        ArrayList<String> groups = new ArrayList<>();

        try (FileReader fr = new FileReader("src/Server/groups.txt");
             BufferedReader bfr = new BufferedReader(fr)) {

            String line = bfr.readLine();
            while (line != null) {
                groups.add(line);
                line = bfr.readLine();
            }

            return groups;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return groups;
    }

    /**
     * Imports all usernames and passwords to ArrayList of logins
     */
    public static ArrayList<String> importLogins() {
        ArrayList<String> logins = new ArrayList<>();

        try (FileReader fr = new FileReader("src/Server/logins.txt");
             BufferedReader bfr = new BufferedReader(fr)) {

            String line = bfr.readLine();
            while (line != null) {
                logins.add(line);
                line = bfr.readLine();
            }

            return logins;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return logins;
    }
}
