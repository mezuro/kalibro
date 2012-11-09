package org.kalibro.desktop.swingextension.table;

public interface TablePanelController<T> {

	T add();

	T edit(T element);

	void remove(T element);
}