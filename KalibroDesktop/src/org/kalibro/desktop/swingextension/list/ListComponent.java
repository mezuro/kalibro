package org.kalibro.desktop.swingextension.list;

public interface ListComponent<T> {

	boolean hasSelection();

	T getSelectedObject();
}