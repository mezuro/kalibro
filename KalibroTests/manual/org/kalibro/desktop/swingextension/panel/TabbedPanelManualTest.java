package org.kalibro.desktop.swingextension.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Label;

public class TabbedPanelManualTest implements TabbedPanelListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("TabbedPanel", createPanel()).setVisible(true);
	}

	private static TabbedPanel createPanel() {
		TabbedPanel panel = new TabbedPanel();
		panel.addTab("Configuration", newPanel("configuration"));
		panel.addTab("Metric", newPanel("metric"));
		panel.addTab("Range", newPanel("range"));
		panel.addPanelListener(new TabbedPanelManualTest());
		return panel;
	}

	private static JPanel newPanel(String name) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new Label("This is the " + name + " panel"), BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(300, 100));
		panel.setName(name);
		return panel;
	}

	@Override
	public void tabChanged(Component lastTab, Component newTab) {
		System.out.println(lastTab.getName() + " -> " + newTab.getName());
	}
}