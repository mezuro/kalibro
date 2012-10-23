package org.kalibro.desktop.swingextension.list;

public abstract class ListAdapter<T> implements ListListener<T> {

	@Override
	public void doubleClicked(T row) {
		return;
	}

	@Override
	public void selected(T row) {
		return;
	}

	@Override
	public void selectionCleared() {
		return;
	}
}