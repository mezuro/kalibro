package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class InputDialog {

	private String title;
	private Component parent;

	private String input;

	public InputDialog(String title, Component parent) {
		this.title = title;
		this.parent = parent;
	}

	public boolean userTyped(String message) {
		input = JOptionPane.showInputDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
		return ! (input == null || input.trim().isEmpty());
	}

	public String getInput() {
		return input;
	}
}