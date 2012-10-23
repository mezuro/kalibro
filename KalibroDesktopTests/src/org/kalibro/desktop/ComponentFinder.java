package org.kalibro.desktop;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;

import org.junit.Assert;

public class ComponentFinder {

	private Component rootComponent;

	public ComponentFinder(Component rootComponent) {
		this.rootComponent = rootComponent;
	}

	public Component find(String name) {
		return find(name, JComponent.class);
	}

	public <T extends Component> T find(String name, Class<T> componentClass) {
		return find(name, componentClass, rootComponent);
	}

	private <T extends Component> T find(String name, Class<T> componentClass, Component component) {
		if (name.equals(component.getName()) && componentClass.isInstance(component))
			return (T) component;
		if (component instanceof Container)
			return findInChildren(name, componentClass, (Container) component);
		throw new ComponentNotFoundException(name, componentClass);
	}

	private <T extends Component> T findInChildren(String name, Class<T> componentClass, Container container) {
		for (Component child : container.getComponents())
			try {
				return find(name, componentClass, child);
			} catch (ComponentNotFoundException exception) {
				continue;
			}
		throw new ComponentNotFoundException(name, componentClass);
	}

	public <T extends Component> void assertNotPresent(String name) {
		try {
			find(name);
			Assert.fail("Component '" + name + "' should not be present");
		} catch (ComponentNotFoundException exception) {
			return;
		}
	}
}