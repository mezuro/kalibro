package org.kalibro.desktop.swingextension.panel;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class AbstractPanel<T> extends JPanel {

	protected AbstractPanel(String name) {
		super();
		setName(name);
		createComponents();
		buildPanel();
		adjustSize();
	}

	protected abstract void createComponents();

	protected abstract void buildPanel();

	public void setWidth(int newWidth) {
		setPreferredSize(new Dimension(newWidth, getPreferredSize().height));
		adjustSize();
	}

	public void adjustSize() {
		setMinimumSize(getPreferredSize());
		setSize(getPreferredSize());
	}

	public abstract T get();
}