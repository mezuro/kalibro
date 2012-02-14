package org.kalibro.desktop.swingextension.list;

public interface TablePanelListener<T> {

	public void add();

	public void edit(T object);
}