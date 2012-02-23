package org.kalibro.desktop.swingextension.field;

public interface Field<T> {

	T getValue();

	void setValue(T value);
}