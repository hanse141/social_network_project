import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;

//by Haoxi Wu, edited by Marina Beshay
public class CustomButton extends JButton {

	private static final long serialVersionUID = 1L;

	public CustomButton(String text) {
		super(text);
		setForeground(new Color(255, 255, 255));
		setContentAreaFilled(false);
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(3, 169, 244));

		g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 20, 20);

		super.paintComponent(g);
	}

	public void paintBorder(Graphics g) {

		g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 20, 20);
	}
}
