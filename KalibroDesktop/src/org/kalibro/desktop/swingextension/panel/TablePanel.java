package org.kalibro.desktop.swingextension.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.table.Table;
import org.kalibro.desktop.swingextension.table.TableListener;

public class TablePanel<T> extends EditPanel<Collection<T>> implements ActionListener, TableListener<T> {

	Table<T> table;

	private Button addButton;
	private Button editButton;
	private Button removeButton;

	public TablePanel(Table<T> table) {
		super(table.getName(), table);
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		table = (Table<T>) innerComponents[0];
		table.addTableListener(this);
		addButton = new Button("add", "Add");
		editButton = new Button("edit", "Edit");
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
		editButton.doClick();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		table.remove(table.getSelected());
	}

	public void addTablePanelListener(TablePanelListener<T> listener) {
		TablePanelAdapter<T> adapter = new TablePanelAdapter<T>(this, listener);
		addButton.addActionListener(adapter);
		editButton.addActionListener(adapter);
		removeButton.addActionListener(adapter);
	}
}