package org.kalibro.desktop.swingextension.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.kalibro.desktop.swingextension.list.TableModel;

public abstract class TableRenderer extends Renderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
		int row, int column) {
		TableModel<?> model = (TableModel<?>) table.getModel();
		Object context = model.getObjectAt(table.convertRowIndexToModel(row));
		Component component = render(value, context);
		changeBackgroundIfSelected(component, isSelected);
		return component;
	}

	protected abstract Component render(Object value, Object context);
}