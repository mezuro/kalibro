package org.kalibro.desktop.swingextension.panel;

import org.kalibro.desktop.swingextension.field.Field;

public abstract class EditPanel<T> extends AbstractPanel<T> implements Field<T> {

	public EditPanel(String name) {
		super(name);
	}
}