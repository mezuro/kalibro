package org.kalibro.desktop.swingextension.list;

public interface ListComponent<T> {

	public boolean hasSelection();

	public T getSelectedObject();
}