package org.kalibro.desktop.swingextension.field;

public interface Field<T> {

	public T getValue();

	public void setValue(T value);
}