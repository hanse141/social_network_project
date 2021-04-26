import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server class that contains data of connected users, receives messages, sends open conversation to client
 */
public class Server {
    public static void main(String[] args) {

        ArrayList<User> users = new ArrayList<>();

        //Wait for client connection, initialize reader and writer
        try (ServerSocket serverSocket = new ServerSocket(4242);
             Socket socket = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

            System.out.println("Connected");

            String message = reader.readLine();

            Message m = new Message(message);
            System.out.println(m.toString());

            writer.write(message);
            writer.println();
            writer.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

