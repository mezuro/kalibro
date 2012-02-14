package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;
import java.util.Collection;

import javax.swing.JOptionPane;

public class ChoiceDialog<T> {

	private String title;
	private Component parent;

	public ChoiceDialog(String title, Component parent) {
		this.title = title;
		this.parent = parent;
	}

	public T choose(String message, Collection<T> options) {
		return doChoose(message, options.toArray());
	}

	public T choose(String message, T... options) {
		return doChoose(message, options);
	}

	private T doChoose(String message, Object... options) {
		int messageType = JOptionPane.PLAIN_MESSAGE;
		return (T) JOptionPane.showInputDialog(parent, message, title, messageType, null, options, options[0]);
	}
}