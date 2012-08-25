package org.kalibro.desktop;

import java.awt.Component;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.MenuEvent;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.desktop.swingextension.menu.AbstractMenu;
import org.kalibro.desktop.swingextension.menu.ReflectionMenuItem;

public abstract class CrudMenu<T extends AbstractEntity<T>> extends AbstractMenu {

	protected String entityName;
	protected JDesktopPane desktopPane;
	protected CrudController<T> controller;

	private ReflectionMenuItem newItem, openItem, deleteItem, saveItem, saveAsItem, closeItem;

	public CrudMenu(JDesktopPane desktopPane, String entityName) {
		super(entityName.toLowerCase(), entityName, entityName.charAt(0), desktopPane);
		this.entityName = entityName;
	}

	@Override
	protected void createItems(Component... innerComponents) {
		desktopPane = (JDesktopPane) innerComponents[0];
		initializeController();
		newItem = new ReflectionMenuItem("new", "New", 'N', controller, "newEntity");
		openItem = new ReflectionMenuItem("open", "Open", 'O', controller, "open");
		deleteItem = new ReflectionMenuItem("delete", "Delete", 'D', controller, "delete");
		saveItem = new ReflectionMenuItem("save", "Save", 'S', controller, "save");
		saveAsItem = new ReflectionMenuItem("saveAs", "Save as...", 'a', controller, "saveAs");
		closeItem = new ReflectionMenuItem("close", "Close", 'l', controller, "close");
	}

	protected abstract void initializeController();

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
		boolean hasSelectedEntity = isEntityFrame(desktopPane.getSelectedFrame());
		saveItem.setEnabled(hasSelectedEntity);
		saveAsItem.setEnabled(hasSelectedEntity);
		closeItem.setEnabled(hasSelectedEntity);
	}

	protected abstract boolean isEntityFrame(JInternalFrame frame);
}