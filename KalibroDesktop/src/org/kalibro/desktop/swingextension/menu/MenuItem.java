package org.kalibro.desktop.swingextension.menu;

import javax.swing.JMenuItem;

import org.kalibro.desktop.swingextension.ReflectiveAction;

public class MenuItem extends JMenuItem {

	public MenuItem(String name, String text, int mnemonic,
		Object target, String methodName, Object... arguments) {
		super();
		setAction(new ReflectiveAction(target, methodName, arguments));
		setName(name);
		setText(text);
		setMnemonic(mnemonic);
	}
}