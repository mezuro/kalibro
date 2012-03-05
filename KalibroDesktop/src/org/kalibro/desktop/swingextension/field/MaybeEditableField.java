package org.kalibro.desktop.swingextension.field;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class MaybeEditableField<T> extends JPanel implements Field<T> {

	private Field<T> editableField;
	private UneditableField<T> uneditableField;

	public MaybeEditableField(Field<T> editableField) {
		super(new GridLayout());
		this.editableField = editableField;
		String name = editableComponent().getName();
		this.uneditableField = new UneditableField<T>(name);
		setName(name);
		setEditable(true);
	}

	@Override
	public T get() {
		return editableField.get();
	}

	@Override
	public void set(T value) {
		editableField.set(value);
		uneditableField.set(value);
	}

	public boolean isEditable() {
		return getComponent(0) == editableField;
	}

	public void setEditable(boolean editable) {
		removeAll();
		if (editable)
			add(editableComponent());
		else {
			uneditableField.set(editableField.get());
			add(uneditableField);
		}
		repaint();
	}

	private Component editableComponent() {
		return (Component) editableField;
	}
}