package org.kalibro.desktop.swingextension.menu;

import javax.swing.JMenu;

public abstract class AbstractMenu extends JMenu {

	public AbstractMenu(String name, String text, char mnemonic) {
		super(text);
		setName(name);
		setMnemonic(mnemonic);
		createItems();
		buildMenu();
	}

	protected abstract void createItems();

	protected abstract void buildMenu();
}