package org.kalibro.desktop.swingextension.menu;

import java.awt.Component;

import javax.swing.JMenu;

public abstract class AbstractMenu extends JMenu {

	public AbstractMenu(String name, String text, char mnemonic, Component... innerComponents) {
		super(text);
		setName(name);
		setMnemonic(mnemonic);
		createItems(innerComponents);
		buildMenu();
	}

	protected abstract void createItems(Component... innerComponents);

	protected abstract void buildMenu();
}