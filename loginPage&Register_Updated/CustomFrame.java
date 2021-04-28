import java.awt.HeadlessException;
import javax.swing.JFrame;

//by Haoxi Wu, edited by Marina Beshay
public class CustomFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public CustomFrame(String title) throws HeadlessException {
		super(title);
		setSize(500, 500);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);  //gently closes program*
		//TODO

		setResizable(true);


	}


}
