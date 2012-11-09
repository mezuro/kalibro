package org.kalibro.desktop.swingextension.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.kalibro.desktop.swingextension.Renderer;

public abstract class ColumnRenderer extends Renderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
		int row, int column) {
		TableModel<?> model = (TableModel<?>) table.getModel();
		Object context = model.getElementAt(table.convertRowIndexToModel(row));
		Component component = render(value, context);
		setSelectionBackground(component, isSelected);
		return component;
	}

	protected abstract Component render(Object value, Object context);
}