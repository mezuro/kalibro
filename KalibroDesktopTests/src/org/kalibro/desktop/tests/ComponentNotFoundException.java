package org.kalibro.desktop.tests;

class ComponentNotFoundException extends RuntimeException {

	public ComponentNotFoundException(String name, Class<?> componentClass) {
		super("Component '" + name + "' (" + componentClass.getName() + ") not found.");
	}
}