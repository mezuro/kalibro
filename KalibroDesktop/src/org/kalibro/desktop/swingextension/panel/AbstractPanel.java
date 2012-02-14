package org.kalibro.desktop.swingextension.panel;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class AbstractPanel<T> extends JPanel {

	public AbstractPanel(String name) {
		super();
		setName(name);
		createComponents();
		buildPanel();
		adjustSize();
	}

	public void setWidth(int newWidth) {
		setPreferredSize(new Dimension(newWidth, getPreferredSize().height));
		adjustSize();
	}

	public void adjustSize() {
		setMinimumSize(getPreferredSize());
		setSize(getPreferredSize());
	}

	protected abstract void createComponents();

	protected abstract void buildPanel();

	public abstract T retrieve();
}