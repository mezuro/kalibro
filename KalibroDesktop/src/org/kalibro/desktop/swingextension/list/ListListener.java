package org.kalibro.desktop.swingextension.list;

public interface ListListener<T> {

	public void doubleClicked(T row);

	public void selected(T row);

	public void selectionCleared();
}