package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageDialog {

	private Component parent;
	private String title;

	public MessageDialog(Component parent, String title) {
		this.parent = parent;
		this.title = title;
	}

	public void show(String message) {
		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
	}
}