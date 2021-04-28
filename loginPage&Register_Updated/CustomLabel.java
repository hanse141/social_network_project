import java.awt.Color;

import javax.swing.JLabel;

//by Haoxi Wu, edited by Marina Beshay
public class CustomLabel extends JLabel {

    private static final long serialVersionUID = 1L;

    public CustomLabel(String text) {
        super(text);
        setForeground(new Color(3, 3, 1, 255));
    }
}
