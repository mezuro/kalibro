package org.kalibro.desktop.swingextension.menu;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public abstract class AbstractMenu extends JMenu implements MenuListener {

	public AbstractMenu(String name, String text, char mnemonic) {
		super(text);
		setName(name);
		setMnemonic(mnemonic);
		createItems();
		buildMenu();
		addMenuListener(this);
	}

	protected abstract void createItems();

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