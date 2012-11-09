package org.kalibro.desktop.swingextension.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

class TableModel<T> extends AbstractTableModel {

	private static final int SCROLL_MARGIN = 10;

	private Class<T> rowClass;
	private List<Column> columns;
	private List<T> data;

	TableModel(Class<T> rowClass) {
		this.rowClass = rowClass;
		columns = new ArrayList<Column>();
		data = new ArrayList<T>();
	}

	Column addColumn(String... fieldPath) {
		Column column = new Column(rowClass, fieldPath);
		columns.add(column);
		return column;
	}

	List<T> getData() {
		return new ArrayList<T>(data);
	}

	void setData(Collection<T> data) {
		this.data = new ArrayList<T>(data);
		fireTableDataChanged();
	}

	T getElementAt(int row) {
		return data.get(row);
	}

	void add(T element) {
		data.add(element);
		fireTableDataChanged();
	}

	void replace(T oldElement, T newElement) {
		data.set(data.indexOf(oldElement), newElement);
		fireTableDataChanged();
	}

	void remove(T element) {
		data.remove(element);
		fireTableDataChanged();
	}

	int getWidth() {
		int width = SCROLL_MARGIN;
		for (Column column : columns)
			width += column.getWidth();
		return width;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public String getColumnName(int column) {
		return columns.get(column).getTitle();
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return columns.get(column).getColumnClass();
	}

	@Override
	public Object getValueAt(int row, int column) {
		return columns.get(column).getValue(getElementAt(row));
	}

	void updateColumnModel(TableColumnModel columnModel) {
		for (int i = 0; i < columnModel.getColumnCount(); i++)
			columns.get(i).update(columnModel.getColumn(i));
	}
}