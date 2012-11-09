package org.kalibro.desktop.swingextension.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.panel.EditPanel;

public class TablePanel<T> extends EditPanel<Collection<T>> implements ActionListener, TableListener<T> {

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
		addButton = new Button("add", "Add", this);
		editButton = new Button("edit", "Edit", this);
		removeButton = new Button("remove", "Remove", this);
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
		edit(element);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == addButton)
			add();
		else if (source == editButton)
			edit(table.getSelected());
		else if (source == removeButton)
			remove(table.getSelected());
	}

	private void add() {
		T newElement = controller.add();
		if (newElement != null)
			table.add(newElement);
	}

	private void edit(T oldElement) {
		T newElement = controller.edit(oldElement);
		if (newElement != null)
			table.replace(oldElement, newElement);
	}

	private void remove(T element) {
		controller.remove(element);
		table.remove(element);
	}
}