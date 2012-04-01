package org.kalibro.desktop.configuration;

import java.awt.Component;

import javax.swing.JDesktopPane;
import javax.swing.event.MenuEvent;

import org.kalibro.desktop.swingextension.menu.AbstractMenu;
import org.kalibro.desktop.swingextension.menu.ReflectionMenuItem;

public class ConfigurationMenu extends AbstractMenu {

	private JDesktopPane desktopPane;
	private ConfigurationController controller;

	private ReflectionMenuItem newItem, openItem, deleteItem, saveItem, saveAsItem, closeItem;

	public ConfigurationMenu(JDesktopPane desktopPane) {
		super("configuration", "Configuration", 'C', desktopPane);
	}

	@Override
	protected void createItems(Component... innerComponents) {
		desktopPane = (JDesktopPane) innerComponents[0];
		controller = new ConfigurationController(desktopPane);
		newItem = new ReflectionMenuItem("new", "New", 'N', controller, "newConfiguration");
		openItem = new ReflectionMenuItem("open", "Open", 'O', controller, "open");
		deleteItem = new ReflectionMenuItem("delete", "Delete", 'D', controller, "delete");
		saveItem = new ReflectionMenuItem("save", "Save", 'S', controller, "save");
		saveAsItem = new ReflectionMenuItem("saveAs", "Save as...", 'a', controller, "saveAs");
		closeItem = new ReflectionMenuItem("close", "Close", 'l', controller, "close");
	}

	@Override
	protected void buildMenu() {
		add(newItem);
		add(openItem);
		add(deleteItem);
		addSeparator();
		add(saveItem);
		add(saveAsItem);
		add(closeItem);
	}

	@Override
	public void menuSelected(MenuEvent event) {
		boolean hasSelectedConfiguration = desktopPane.getSelectedFrame() instanceof ConfigurationFrame;
		saveItem.setEnabled(hasSelectedConfiguration);
		saveAsItem.setEnabled(hasSelectedConfiguration);
		closeItem.setEnabled(hasSelectedConfiguration);
	}
}