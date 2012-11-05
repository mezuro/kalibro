package org.kalibro.desktop.swingextension.field;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JComboBox;

public class ChoiceField<T> extends JComboBox<T> implements Field<T> {

	public ChoiceField(String name, T... values) {
		this(name, Arrays.asList(values));
	}

	public ChoiceField(String name, Collection<T> values) {
		super(new Vector<T>(values));
		setName(name);
		setSize(new FieldSize(this));
	}

	@Override
	public T get() {
		return (T) super.getSelectedItem();
	}

	@Override
	public void set(T value) {
		super.setSelectedItem(value);
	}
}