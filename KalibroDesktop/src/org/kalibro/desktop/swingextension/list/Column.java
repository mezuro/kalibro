package org.kalibro.desktop.swingextension.list;

import javax.swing.table.TableColumn;

import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;
import org.kalibro.desktop.swingextension.renderer.TableRenderer;

public class Column {

	private int width;
	private String title;
	private Class<?> columnClass;
	private TableRenderer renderer;

	public Column(String title, Class<?> columnClass, int charsWidth) {
		this(title, columnClass, charsWidth, new DefaultRenderer());
	}

	public Column(String title, Class<?> columnClass, int charsWidth, TableRenderer renderer) {
		this.title = title;
		this.width = new StringField("", charsWidth).getPreferredSize().width;
		this.renderer = renderer;
		this.columnClass = columnClass;
	}

	public String getTitle() {
		return title;
	}

	public Class<?> getColumnClass() {
		return columnClass;
	}

	public int getPreferredWidth() {
		return width;
	}

	public void updateTableColumn(TableColumn column) {
		column.setPreferredWidth(width);
		column.setCellRenderer(renderer);
	}
}