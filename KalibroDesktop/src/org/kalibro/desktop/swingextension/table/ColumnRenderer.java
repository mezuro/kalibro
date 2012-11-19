package org.kalibro.desktop.swingextension.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.kalibro.desktop.swingextension.RendererUtil;

public abstract class ColumnRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
		int row, int column) {
		TableModel<?> model = (TableModel<?>) table.getModel();
		Object context = model.getElementAt(table.convertRowIndexToModel(row));
		Component component = render(value, context);
		RendererUtil.setSelectionBackground(component, isSelected);
		return component;
	}

	public abstract Component render(Object value, Object context);
}