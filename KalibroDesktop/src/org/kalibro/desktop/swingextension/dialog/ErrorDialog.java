package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorDialog {

	private Component parent;

	public ErrorDialog(Component parent) {
		this.parent = parent;
	}

	public void show(Exception error) {
		show(error.getMessage());
	}

	public void show(String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}