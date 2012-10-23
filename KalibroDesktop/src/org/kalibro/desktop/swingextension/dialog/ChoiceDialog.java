package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;
import java.util.Collection;

import javax.swing.JOptionPane;

public class ChoiceDialog<T> {

	private String title;
	private Component parent;

	private T choice;

	public ChoiceDialog(String title, Component parent) {
		this.title = title;
		this.parent = parent;
	}

	public boolean choose(String message, T... options) {
		return doChoose(message, options);
	}

	public boolean choose(String message, Collection<T> options) {
		return doChoose(message, options.toArray());
	}

	private boolean doChoose(String message, Object... options) {
		int messageType = JOptionPane.PLAIN_MESSAGE;
		choice = (T) JOptionPane.showInputDialog(parent, message, title, messageType, null, options, options[0]);
		return choice != null;
	}

	public T getChoice() {
		return choice;
	}
}