import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class that contains GUI, data of open conversation, and sends messages to server
 * <p>
 * List of commands and formatting:
 * Send gui data:  gd Chats<[chat1, chat2...]> Messages<[Message<..1>, Message<..2>...]>
 * Send error:     er message
 */

public class Client {
    public static void main(String[] args) throws IOException {

        //Connect to server, initialize reader and writer
        try (Scanner scan = new Scanner(System.in);
             Socket socket = new Socket("localhost", 4242);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

            while (true) {

                System.out.println("Command to send to server:");
                String command = scan.nextLine();

                writer.write(command);
                writer.println();
                writer.flush(); //Ensure data is sent to the server

                String guiData = reader.readLine();
                System.out.println(guiData);
            }
        } catch (ConnectException e) {
            System.out.println("Server is not online.");
        }
    }
}
