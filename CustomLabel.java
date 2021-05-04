import java.awt.Color;

import javax.swing.JLabel;

public class CustomLabel extends JLabel{

	private static final long serialVersionUID = 1L;

	public CustomLabel(String text) {
		super(text);
		setForeground(new Color(255, 255, 255));
	}
}
