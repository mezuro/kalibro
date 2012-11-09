package org.kalibro.desktop.swingextension.table;

import java.awt.Dimension;
import java.util.Collection;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.kalibro.desktop.swingextension.field.Field;

public class Table<T> extends JScrollPane implements Field<Collection<T>> {

	private int lines;
	private JTable table;
	private TableModel<T> model;

	public Table(String name, int lines, Class<T> rowClass) {
		super();
		this.lines = lines;
		this.model = new TableModel<T>(rowClass);
		setName(name);
		createTable(name);
	}

	private void createTable(String name) {
		table = new JTable(model);
		table.setName(name);
		table.setShowGrid(true);
		table.setAutoCreateRowSorter(true);
		table.setColumnSelectionAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setViewportView(table);
	}

	public Column addColumn(String... fieldPath) {
		return model.addColumn(fieldPath);
	}

	public void pack() {
		model.fireTableStructureChanged();
		model.updateColumnModel(table.getColumnModel());
		Dimension size = getMinimumSize();
		size.width = model.getWidth();
		size.height = table.getRowHeight() * (lines + 2);
		setMinimumSize(size);
		setPreferredSize(size);
	}

	@Override
	public List<T> get() {
		return model.getData();
	}

	@Override
	public void set(Collection<T> value) {
		model.setData(value);
	}

	public boolean hasSelection() {
		return table.getSelectedRow() != -1;
	}

	public T getSelected() {
		return model.getElementAt(table.convertRowIndexToModel(table.getSelectedRow()));
	}

	public void add(T element) {
		model.add(element);
	}

	public void replace(T oldElement, T newElement) {
		model.replace(oldElement, newElement);
	}

	public void remove(T element) {
		model.remove(element);
	}

	public void addTableListener(TableListener<T> listener) {
		TableAdapter<T> adapter = new TableAdapter<T>(listener, this);
		table.addMouseListener(adapter);
		table.getSelectionModel().addListSelectionListener(adapter);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		table.setEnabled(enabled);
	}
}