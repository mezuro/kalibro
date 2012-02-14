package org.kalibro.desktop.old.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableDefinition {

	private List<Object> columnNames;
	private List<Class<?>> columnClasses;
	private List<List<Object>> data;

	public TableDefinition(Object... columnNames) {
		this(Arrays.asList(columnNames));
	}

	public TableDefinition(List<Object> columnNames) {
		this.columnNames = columnNames;
		this.data = new ArrayList<List<Object>>();
	}

	public void columnClasses(Class<?>... classes) {
		columnClasses = Arrays.asList(classes);
	}

	public void addRow(Object... objects) {
		addRow(Arrays.asList(objects));
	}

	public void addRow(List<Object> record) {
		data.add(record);
	}

	public void clearData() {
		data.clear();
	}

	public int columnCount() {
		return columnNames.size();
	}

	public String columnName(int column) {
		return columnNames.get(column).toString();
	}

	public Class<?> columnClass(int column) {
		int lastClassIndex = columnClasses.size() - 1;
		if (column > lastClassIndex)
			return columnClasses.get(lastClassIndex);
		return columnClasses.get(column);
	}

	public int rowCount() {
		return data.size();
	}

	public Object valueAt(int row, int column) {
		if (row < 0 || column < 0 || row >= data.size() || column >= columnNames.size())
			return null;
		return data.get(row).get(column);
	}
}