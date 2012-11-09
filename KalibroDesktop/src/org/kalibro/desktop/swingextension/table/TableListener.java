package org.kalibro.desktop.swingextension.table;

public interface TableListener<T> {

	void doubleClicked(T element);

	void selected(T element);

	void selectionCleared();
}