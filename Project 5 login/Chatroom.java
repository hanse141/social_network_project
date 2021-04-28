package com.java.chatroom;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Chatroom {
	public static void main(String[] args) {
		JFrame frame = new CustomFrame("Chatroom");
		JPanel login = new CustomPanel();
		frame.add(login);  // frame add panel
		frame.setVisible(true);  // frame
	}
}
