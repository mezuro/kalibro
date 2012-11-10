package org.kalibro.desktop;

import java.awt.Component;

import org.kalibro.desktop.settings.SettingsController;
import org.kalibro.desktop.swingextension.menu.AbstractMenu;
import org.kalibro.desktop.swingextension.menu.MenuItem;

class KalibroMenu extends AbstractMenu {

	private MenuItem settingsItem, exitItem;

	protected KalibroMenu() {
		super("kalibro", "Kalibro", 'K');
	}

	@Override
	protected void createItems(Component... innerComponents) {
		settingsItem = new MenuItem("settings", "Settings", 'S', SettingsController.class, "editSettings");
		exitItem = new MenuItem("exit", "Exit", 'x', System.class, "exit", 0);
	}

	@Override
	protected void buildMenu() {
		add(settingsItem);
		addSeparator();
		add(exitItem);
	}
}