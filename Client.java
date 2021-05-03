import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client class that contains GUI, data of open conversation, and sends messages to server
 */

public class Client {
    private static final String HOST = "localhost"; //Host of socket
    private static final int PORT = 4242; //Port of socket

    public static void main(String[] args) {

        //Connect to server, initialize reader and writer
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            ServerConnection serverConnection = new ServerConnection(socket);
            new Thread(serverConnection).start();

            //Send and receive commands with server, update GUI accordingly
            while (true) {

                if (serverConnection.isReceivedGuiCommand()) {

                    //TODO: Process gd command here
                    System.out.println(serverConnection.getLastGuiCommand());

                    serverConnection.setReceivedGuiCommand(false);

                } else if (serverConnection.isReceivedCommand()) {

                    //Exit condition of loop
                    if (!serverConnection.isRunning()) {
                        break;
                    }

                    //TODO: Process er or su command here (sent by nc, lu, cu, eu)
                    System.out.println(serverConnection.getLastCommand());

                    serverConnection.setReceivedCommand(false);
                }

                //TODO: Send commands to server here

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
