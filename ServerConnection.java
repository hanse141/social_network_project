import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * ServerConnection Thread class to be used in Client
 * <p>
 * List of commands and formatting:
 * Send gui data:  gd Chats<[chat1, chat2...]> Messages<[Message<..1>, Message<..2>...]>
 * Send error:     er message
 * Send success:   su message
 */

public class ServerConnection implements Runnable {
    private final BufferedReader in; //BufferedReader to read server commands
    private String lastGuiCommand; //Last gd command sent by server
    private String lastCommand; //Last er or su command sent by server
    private boolean receivedGuiCommand; //If a new gd command has been sent by server
    private boolean receivedCommand; //If a new er or su command has been sent by server
    private boolean isRunning; //If the Socket connection exists

    /**
     * Constructs a newly allocated ServerConnection with the specified socket and instantiates fields to defaults
     *
     * @param socket Client socket
     * @throws IOException from getInputStream()
     */
    public ServerConnection(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        lastGuiCommand = "";
        lastCommand = "";
        receivedGuiCommand = true;
        receivedCommand = true;
        isRunning = true;
    }

    /**
     * Returns the lastGuiCommand of this ServerConnection
     *
     * @return Last gd command sent by server
     */
    public String getLastGuiCommand() {
        return lastGuiCommand;
    }

    /**
     * Returns the lastCommand of this ServerConnection
     *
     * @return Last er or su command sent by server
     */
    public String getLastCommand() {
        return lastCommand;
    }

    /**
     * Returns the receivedGuiCommand of this ServerConnection
     *
     * @return If a new gd command has been sent by server
     */
    public boolean isReceivedGuiCommand() {
        return receivedGuiCommand;
    }

    /**
     * Returns the receivedCommand of this ServerConnection
     *
     * @return If a new er or su command has been sent by server
     */
    public boolean isReceivedCommand() {
        return receivedCommand;
    }

    /**
     * Returns the isRunning of this ServerConnection
     *
     * @return If the Socket connection exists
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Sets the receivedGuiCommand of this ServerConnection
     *
     * @param receivedGuiCommand If a new gd command has been sent by server
     */
    public void setReceivedGuiCommand(boolean receivedGuiCommand) {
        this.receivedGuiCommand = receivedGuiCommand;
    }

    /**
     * Sets the receivedCommand of this ServerConnection
     *
     * @param receivedCommand If a new er or su command has been sent by server
     */
    public void setReceivedCommand(boolean receivedCommand) {
        this.receivedCommand = receivedCommand;
    }

    /**
     * Thread run method
     */
    @Override
    public void run() {

        try {
            String command;
            while (isRunning) {

                command = in.readLine();

                if (command.startsWith("gd")) {
                    receivedGuiCommand = true;
                    lastGuiCommand = command;

                } else {
                    receivedCommand = true;
                    lastCommand = command;

                    isRunning = !command.substring(3).equals("Goodbye.");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
