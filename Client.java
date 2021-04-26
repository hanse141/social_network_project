import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class that contains GUI, data of open conversation, and sends messages to server
 */
public class Client {
    public static void main(String[] args) throws IOException {

        //Connect to server, initialize reader and writer
        try (Scanner scan = new Scanner(System.in);
             Socket socket = new Socket("localhost", 4242);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

            System.out.println("Who is the sender?");
            String sender = scan.nextLine();
            System.out.println("Who is the receiver?");
            String receiver = scan.nextLine();
            System.out.println("What is the message?");
            String content = scan.nextLine();

            Message message = new Message(sender, receiver, content);

            writer.write(message.toString());
            writer.println();
            writer.flush(); //Ensure data is sent to the server

            String s1 = reader.readLine();
            System.out.printf("Received from server:\n%s\n", s1);
        }
    }
}
