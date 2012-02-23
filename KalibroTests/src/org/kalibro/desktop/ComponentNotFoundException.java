package org.kalibro.desktop;

class ComponentNotFoundException extends RuntimeException {

	public ComponentNotFoundException(String name, Class<?> componentClass) {
		super("Component '" + name + "' (" + componentClass.getName() + ") not found.");
	}
}