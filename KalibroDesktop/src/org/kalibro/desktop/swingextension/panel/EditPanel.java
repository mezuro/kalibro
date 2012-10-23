package org.kalibro.desktop.swingextension.panel;

import java.awt.Component;

import org.kalibro.desktop.swingextension.field.Field;

public abstract class EditPanel<T> extends AbstractPanel<T> implements Field<T> {

	public EditPanel(String name, Component... innerComponents) {
		super(name, innerComponents);
	}
}