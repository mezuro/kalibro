package org.kalibro.desktop.swingextension.panel;

public abstract class EditPanel<T> extends AbstractPanel<T> {

	public EditPanel(String name) {
		super(name);
	}

	public abstract void show(T object);
}