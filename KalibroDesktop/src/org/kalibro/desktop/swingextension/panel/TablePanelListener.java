package org.kalibro.desktop.swingextension.panel;

public interface TablePanelListener<T> {

	void add();

	void edit(T element);

	void remove(T element);
}