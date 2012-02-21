package org.kalibro.desktop.swingextension.list;

import java.awt.Dimension;
import java.util.Collection;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class Table<T> extends JScrollPane implements ListComponent<T> {

	private JTable table;
	private TableModel<T> tableModel;

	public Table(String name, TableModel<T> tableModel, int lines) {
		super();
		this.tableModel = tableModel;
		setName(name);
		createTable(name);
		changeSize(lines);
	}

	private void createTable(String name) {
		table = new JTable(tableModel);
		table.setName(name);
		table.setShowGrid(true);
		table.setAutoCreateRowSorter(true);
		table.setColumnSelectionAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableModel.updateColumnModel(table.getColumnModel());
		setViewportView(table);
	}

	private void changeSize(int lines) {
		Dimension size = getMinimumSize();
		size.width += tableModel.getPreferredWidth();
		size.height = table.getRowHeight() * (lines + 1);
		setMinimumSize(size);
		setPreferredSize(size);
	}

	public List<T> getData() {
		return tableModel.getData();
	}

	public void setData(Collection<T> data) {
		tableModel.setData(data);
	}

	public void remove(T row) {
		tableModel.remove(row);
	}

	@Override
	public boolean hasSelection() {
		return table.getSelectedRow() != -1;
	}

	@Override
	public T getSelectedObject() {
		return tableModel.getObjectAt(table.convertRowIndexToModel(table.getSelectedRow()));
	}

	public void addListListener(ListListener<T> listener) {
		ListComponentAdapter<T> adapter = new ListComponentAdapter<T>(listener, this);
		table.addMouseListener(adapter);
		table.getSelectionModel().addListSelectionListener(adapter);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		table.setEnabled(enabled);
	}
}