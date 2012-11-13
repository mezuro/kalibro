package org.kalibro.desktop;

import org.kalibro.desktop.settings.SettingsController;
import org.kalibro.desktop.swingextension.menu.AbstractMenu;
import org.kalibro.desktop.swingextension.menu.MenuItem;

class KalibroMenu extends AbstractMenu {

	private MenuItem settingsItem, exitItem;

	KalibroMenu() {
		super("kalibro", "Kalibro", 'K');
		createItems();
		buildMenu();
	}

	private void createItems() {
		settingsItem = new MenuItem("settings", "Settings", 'S', SettingsController.class, "editSettings");
		exitItem = new MenuItem("exit", "Exit", 'x', System.class, "exit", 0);
	}

	private void buildMenu() {
		add(settingsItem);
		addSeparator();
		add(exitItem);
	}
}