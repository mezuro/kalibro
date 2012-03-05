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
		add(component, name);
		show(name);
		stack.push(component);
		repaint();
	}

	public void pop() {
		remove(stack.pop());
		if (!stack.isEmpty())
			show(stack.peek().getName());
	}

	private void show(String name) {
		CardLayout layout = (CardLayout) getLayout();
		layout.show(this, name);
	}
}