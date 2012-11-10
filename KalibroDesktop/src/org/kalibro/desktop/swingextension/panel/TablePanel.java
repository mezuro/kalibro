package org.kalibro.desktop.swingextension.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.table.Table;
import org.kalibro.desktop.swingextension.table.TableListener;

public class TablePanel<T> extends EditPanel<Collection<T>> implements TableListener<T> {

	private TablePanelController<T> controller;

	private Table<T> table;
	private Button addButton;
	private Button editButton;
	private Button removeButton;

	public TablePanel(TablePanelController<T> controller, Table<T> table) {
		super(table.getName(), table);
		this.controller = controller;
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		table = (Table<T>) innerComponents[0];
		table.addTableListener(this);
		addButton = new Button("add", "Add", this, "add");
		editButton = new Button("edit", "Edit", this, "edit");
		removeButton = new Button("remove", "Remove", this, "remove");
	}

	@Override
	protected void buildPanel() {
		setLayout(new BorderLayout());
		add(table, BorderLayout.CENTER);
		add(southPanel(), BorderLayout.SOUTH);
		enableButtons(false);
	}

	private Component southPanel() {
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPanel.add(addButton);
		southPanel.add(editButton);
		southPanel.add(removeButton);
		return southPanel;
	}

	private void enableButtons(boolean enable) {
		editButton.setEnabled(enable);
		removeButton.setEnabled(enable);
	}

	@Override
	public List<T> get() {
		return table.get();
	}

	@Override
	public void set(Collection<T> data) {
		table.set(data);
	}

	@Override
	public void selected(T element) {
		enableButtons(true);
	}

	@Override
	public void selectionCleared() {
		enableButtons(false);
	}

	@Override
	public void doubleClicked(T element) {
		edit();
	}

	void add() {
		T newElement = controller.add();
		if (newElement != null)
			table.add(newElement);
	}

	void edit() {
		T oldElement = table.getSelected();
		T newElement = controller.edit(oldElement);
		if (newElement != null)
			table.replace(oldElement, newElement);
	}

	void remove() {
		T element = table.getSelected();
		controller.remove(element);
		table.remove(element);
	}
}