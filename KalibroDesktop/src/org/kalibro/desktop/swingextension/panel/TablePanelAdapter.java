package org.kalibro.desktop.swingextension.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.core.reflection.MethodReflector;

class TablePanelAdapter<T> implements ActionListener {

	private TablePanel<T> panel;
	private TablePanelListener<T> listener;

	TablePanelAdapter(TablePanel<T> panel, TablePanelListener<T> listener) {
		this.panel = panel;
		this.listener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String name = ((Component) event.getSource()).getName();
		new MethodReflector(getClass()).invoke(this, name);
	}

	void add() {
		listener.add();
	}

	void edit() {
		listener.edit(panel.table.getSelected());
	}

	void remove() {
		listener.remove(panel.table.getSelected());
	}
}