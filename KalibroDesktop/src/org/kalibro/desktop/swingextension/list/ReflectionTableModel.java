package org.kalibro.desktop.swingextension.list;

public class ReflectionTableModel<T> extends TableModel<T> {

	private Class<T> dataClass;

	public ReflectionTableModel(Class<T> dataClass) {
		super();
		this.dataClass = dataClass;
	}

	@Override
	public void addColumn(Column column) {
		if (! (column instanceof ReflectionColumn))
			throw new IllegalArgumentException("All columns of ReflectionTableModel should be ReflectionColumn");
		super.addColumn(column);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return getColumn(column).getColumnClass(dataClass);
	}

	@Override
	public Object getValueAt(int row, int column) {
		return getColumn(column).getValue(getObjectAt(row));
	}

	private ReflectionColumn getColumn(int index) {
		return (ReflectionColumn) columns.get(index);
	}
}