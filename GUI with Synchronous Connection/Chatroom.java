import javax.swing.JFrame;
import javax.swing.JPanel;


//by Haoxi Wu, edited by Marina Beshay
public class Chatroom {

	public static void main(String[] args) {
		JFrame frame = new CustomFrame("Instant Messenger");
		JPanel login = new CustomPanel();

		frame.add(login);  // frame add panel

		frame.setVisible(true);  // frame

	}

}
