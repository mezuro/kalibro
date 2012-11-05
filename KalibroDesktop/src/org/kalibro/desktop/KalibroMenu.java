package org.kalibro.desktop;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.desktop.settings.SettingsController;
import org.kalibro.desktop.swingextension.menu.AbstractMenu;
import org.kalibro.desktop.swingextension.menu.MenuItem;
import org.kalibro.desktop.swingextension.menu.ReflectionMenuItem;

class KalibroMenu extends AbstractMenu implements ActionListener {

	private MenuItem settingsItem, exitItem;

	protected KalibroMenu() {
		super("kalibro", "Kalibro", 'K');
	}

	@Override
	protected void createItems(Component... innerComponents) {
		settingsItem = new ReflectionMenuItem("settings", "Settings", 'S', SettingsController.class, "editSettings");
		exitItem = new MenuItem("exit", "Exit", 'x', this);
	}

	@Override
	protected void buildMenu() {
		add(settingsItem);
		addSeparator();
		add(exitItem);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		System.exit(0);
	}
}