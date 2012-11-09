package org.kalibro.desktop.swingextension.table;

import javax.swing.table.TableColumn;

import org.kalibro.core.Identifier;
import org.kalibro.core.reflection.FieldReflector;
import org.kalibro.desktop.swingextension.field.StringField;

public class Column {

	private static final int DEFAULT_WIDTH = 50;

	private String[] fieldPath;

	private ColumnRenderer columnRenderer;
	private Class<?> columnClass;
	private Integer columnWidth;
	private String columnTitle;

	Column(Class<?> rowClass, String... fieldPath) {
		this.fieldPath = fieldPath;
		runIntoFields(rowClass);
		columnRenderer = new DefaultRenderer();
		columnWidth = DEFAULT_WIDTH;
	}

	private void runIntoFields(Class<?> rowClass) {
		columnClass = rowClass;
		columnTitle = "";
		for (String field : fieldPath) {
			columnClass = new FieldReflector(columnClass).getFieldType(field);
			columnTitle += Identifier.fromVariable(field).asClassName();
		}
		columnTitle = Identifier.fromClassName(columnTitle).asText();
	}

	public Column renderedBy(ColumnRenderer renderer) {
		columnRenderer = renderer;
		return this;
	}

	public Column withWidth(int charsWidth) {
		columnWidth = new StringField("", charsWidth).getPreferredSize().width;
		return this;
	}

	public Column titled(String title) {
		columnTitle = title;
		return this;
	}

	int getWidth() {
		return columnWidth;
	}

	String getTitle() {
		return columnTitle;
	}

	Class<?> getColumnClass() {
		return columnClass;
	}

	Object getValue(Object row) {
		Object value = row;
		for (String field : fieldPath)
			value = new FieldReflector(value).get(field);
		return value;
	}

	void update(TableColumn column) {
		column.setPreferredWidth(columnWidth);
		column.setCellRenderer(columnRenderer);
	}
}