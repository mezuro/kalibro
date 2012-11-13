package org.kalibro.desktop;

import javax.swing.event.MenuEvent;

import org.kalibro.core.Identifier;
import org.kalibro.desktop.swingextension.menu.AbstractMenu;
import org.kalibro.desktop.swingextension.menu.MenuItem;

class CrudMenu extends AbstractMenu {

	private MenuItem newItem, openItem, deleteItem, saveItem, closeItem;

	private CrudController<?> controller;

	CrudMenu(CrudController<?> controller) {
		this.controller = controller;
		Identifier className = controller.getClassName();
		setName(className.asVariable());
		setText(className.asText());
		setMnemonic(getText().charAt(0));
		createItems();
		buildMenu();
	}

	private void createItems() {
		newItem = new MenuItem("new", "New", 'N', controller, "create");
		openItem = new MenuItem("open", "Open", 'O', controller, "open");
		deleteItem = new MenuItem("delete", "Delete", 'D', controller, "delete");
		saveItem = new MenuItem("save", "Save", 'S', controller, "save");
		closeItem = new MenuItem("close", "Close", 'C', controller, "close");
	}

	private void buildMenu() {
		add(newItem);
		add(openItem);
		add(deleteItem);
		addSeparator();
		add(saveItem);
		add(closeItem);
	}

	@Override
	public void menuSelected(MenuEvent event) {
		boolean saveAndClose = selectedTabIsCompatible();
		saveItem.setEnabled(saveAndClose);
		closeItem.setEnabled(saveAndClose);
	}

	private boolean selectedTabIsCompatible() {
		return KalibroFrame.getInstance().getSelectedTitle().endsWith(controller.getClassName().asText());
	}
}