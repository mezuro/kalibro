package org.kalibro.desktop.swingextension.panel;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPanel extends JTabbedPane {

	private Component lastTab;

	public TabbedPanel() {
		super();
		setName("tabbedPanel");
	}

	@Override
	public void addTab(String title, Component tab) {
		super.addTab(title, tab);
		adjustSize();
	}

	private void adjustSize() {
		setMinimumSize(getPreferredSize());
		setSize(getPreferredSize());
	}

	public void setTitle(String title, Component tab) {
		int index = Arrays.asList(getComponents()).indexOf(tab);
		super.setTitleAt(index, title);
	}

	public void addPanelListener(final TabbedPanelListener listener) {
		addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent event) {
				listener.tabChanged(lastTab, getSelectedComponent());
			}
		});
	}

	@Override
	protected void fireStateChanged() {
		super.fireStateChanged();
		lastTab = getSelectedComponent();
	}
}