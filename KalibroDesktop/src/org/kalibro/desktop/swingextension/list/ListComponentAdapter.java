package org.kalibro.desktop.swingextension.list;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListComponentAdapter<T> extends MouseAdapter implements ListSelectionListener {

	private ListListener<T> listener;
	private ListComponent<T> component;

	protected ListComponentAdapter(ListListener<T> listener, ListComponent<T> component) {
		this.listener = listener;
		this.component = component;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() > 1)
			listener.doubleClicked(component.getSelected());
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting())
			return;
		if (component.hasSelection())
			listener.selected(component.getSelected());
		else
			listener.selectionCleared();
	}
}