package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageDialog {

	private String title;
	private Component parent;

	public MessageDialog(String title, Component parent) {
		this.title = title;
		this.parent = parent;
	}

	public void show(String message) {
		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
	}
}