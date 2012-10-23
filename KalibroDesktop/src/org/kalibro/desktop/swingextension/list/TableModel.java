package org.kalibro.desktop.swingextension.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

public abstract class TableModel<T> extends AbstractTableModel {

	protected List<T> data;
	protected List<Column> columns;

	public TableModel() {
		data = new ArrayList<T>();
		columns = new ArrayList<Column>();
	}

	protected List<T> getData() {
		return data;
	}

	protected void setData(Collection<T> data) {
		this.data = new ArrayList<T>(data);
		fireTableDataChanged();
	}

	public T getObjectAt(int row) {
		return data.get(row);
	}

	protected void remove(T object) {
		data.remove(object);
		fireTableDataChanged();
	}

	public void addColumn(Column column) {
		columns.add(column);
		fireTableStructureChanged();
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
	public int getRowCount() {
		return data.size();
	}

	protected int getPreferredWidth() {
		int width = 10;
		for (Column column : columns)
			width += column.getPreferredWidth();
		return width;
	}

	protected void updateColumnModel(TableColumnModel columnModel) {
		for (int i = 0; i < columnModel.getColumnCount(); i++)
			columns.get(i).updateTableColumn(columnModel.getColumn(i));
	}
}