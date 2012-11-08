package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class InputDialog {

	private Component parent;
	private String title;

	private String input;

	public InputDialog(Component parent, String title) {
		this.parent = parent;
		this.title = title;
	}

	public boolean userTyped(String message) {
		input = JOptionPane.showInputDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
		return !(input == null || input.trim().isEmpty());
	}

	public String getInput() {
		return input;
	}
}