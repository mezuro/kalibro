package org.kalibro.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;

import org.kalibro.desktop.configuration.ConfigurationController;
import org.kalibro.desktop.configuration.ConfigurationFrame;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.menu.AbstractMenu;
import org.kalibro.desktop.swingextension.menu.MenuItem;

public class ConfigurationMenu extends AbstractMenu implements ActionListener {

	private JDesktopPane desktopPane;
	private ConfigurationController controller;

	private MenuItem newItem, openItem, deleteItem, saveItem, saveAsItem, closeItem;

	public ConfigurationMenu(JDesktopPane desktopPane) {
		super("configuration", "Configuration", 'C');
		this.desktopPane = desktopPane;
		controller = new ConfigurationController(desktopPane);
	}

	@Override
	protected void createItems() {
		newItem = new MenuItem("newConfiguration", "New", 'N', this);
		openItem = new MenuItem("open", "Open", 'O', this);
		deleteItem = new MenuItem("delete", "Delete", 'D', this);
		saveItem = new MenuItem("save", "Save", 'S', this);
		saveAsItem = new MenuItem("saveAs", "Save as...", 'a', this);
		closeItem = new MenuItem("close", "Close", 'l', this);
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
	public void actionPerformed(ActionEvent event) {
		MenuItem source = (MenuItem) event.getSource();
		String methodName = source.getName();
		try {
			ConfigurationController.class.getMethod(methodName).invoke(controller);
		} catch (Exception exception) {
			new ErrorDialog(desktopPane).show(exception.getCause());
		}
	}

	private void checkItems() {
		// TODO listen when selected frame changes and do this
		boolean hasSelectedConfiguration = desktopPane.getSelectedFrame() instanceof ConfigurationFrame;
		saveItem.setEnabled(hasSelectedConfiguration);
		saveAsItem.setEnabled(hasSelectedConfiguration);
		closeItem.setEnabled(hasSelectedConfiguration);
	}
}