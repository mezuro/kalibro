package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageDialog {

	private Component parent;

	public MessageDialog(Component parent) {
		this.parent = parent;
	}

	public void show(String message, String title) {
		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
	}
}