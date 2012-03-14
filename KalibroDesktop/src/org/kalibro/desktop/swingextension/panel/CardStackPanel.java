package org.kalibro.desktop.swingextension.panel;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.Stack;

import javax.swing.JPanel;

public class CardStackPanel extends JPanel {

	private Stack<Component> stack;

	public CardStackPanel() {
		super(new CardLayout());
		stack = new Stack<Component>();
	}

	public void push(Component component) {
		String name = component.getName();
		adjustSize(component);
		add(component, name);
		stack.push(component);
		show(component);
	}

	public void pop() {
		Component top = stack.pop();
		if (!stack.isEmpty()) {
			Component previous = stack.peek();
			adjustSize(previous);
			show(previous);
		}
		remove(top);
	}

	private void show(Component component) {
		CardLayout layout = (CardLayout) getLayout();
		layout.show(this, component.getName());
	}

	private void adjustSize(Component component) {
		setPreferredSize(component.getPreferredSize());
		setMinimumSize(component.getMinimumSize());
		setSize(component.getSize());
		repaint();
	}
}