import java.awt.HeadlessException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class CustomFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	public CustomFrame(String title) throws HeadlessException {
		super(title);  
		setSize(300, 200);  
		setDefaultCloseOperation(EXIT_ON_CLOSE);  
		//TODO
		URL url = getClass().getResource("icon.png");
		setIconImage(new ImageIcon(url).getImage());
		setLocationRelativeTo(null);  
		setResizable(false);  
	}
}
