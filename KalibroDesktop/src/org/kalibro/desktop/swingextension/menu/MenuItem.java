package org.kalibro.desktop.swingextension.menu;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class MenuItem extends JMenuItem {

	public MenuItem(String name, String text, int mnemonic, ActionListener... listeners) {
		super(text);
		setName(name);
		setMnemonic(mnemonic);
		for (ActionListener listener : listeners)
			addActionListener(listener);
	}
}