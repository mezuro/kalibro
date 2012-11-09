package org.kalibro.desktop.swingextension.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class TableAdapter<T> extends MouseAdapter implements ListSelectionListener {

	private TableListener<T> listener;
	private Table<T> table;

	TableAdapter(TableListener<T> listener, Table<T> table) {
		this.listener = listener;
		this.table = table;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() > 1)
			listener.doubleClicked(table.getSelected());
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting())
			return;
		if (table.hasSelection())
			listener.selected(table.getSelected());
		else
			listener.selectionCleared();
	}
}