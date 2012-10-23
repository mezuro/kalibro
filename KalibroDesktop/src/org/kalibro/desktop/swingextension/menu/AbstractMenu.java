package org.kalibro.desktop.swingextension.menu;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public abstract class AbstractMenu extends JMenu implements MenuListener {

	public AbstractMenu(String name, String text, char mnemonic, Component... innerComponents) {
		super(text);
		setName(name);
		setMnemonic(mnemonic);
		createItems(innerComponents);
		buildMenu();
		addMenuListener(this);
	}

	protected abstract void createItems(Component... innerComponents);

	protected abstract void buildMenu();

	@Override
	public void menuSelected(MenuEvent event) {
		return;
	}

	@Override
	public void menuDeselected(MenuEvent event) {
		return;
	}

	@Override
	public void menuCanceled(MenuEvent event) {
		return;
	}
}