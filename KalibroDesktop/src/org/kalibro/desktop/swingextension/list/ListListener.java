package org.kalibro.desktop.swingextension.list;

public interface ListListener<T> {

	void doubleClicked(T row);

	void selected(T row);

	void selectionCleared();
}