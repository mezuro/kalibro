package org.kalibro.desktop.swingextension.panel;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class GridBagPanelBuilder {

	private JPanel panel;
	private Insets insets;
	private int gridX, gridY, lineJump;

	public GridBagPanelBuilder() {
		this(new JPanel());
	}

	public GridBagPanelBuilder(JPanel panel) {
		this(panel, 5);
	}

	public GridBagPanelBuilder(JPanel panel, int gap) {
		this.panel = panel;
		panel.removeAll();
		panel.setLayout(new GridBagLayout());
		insets = new Insets(gap, gap, gap, gap);
		gridY = 0;
		resetLine();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void addSimpleLine(JComponent... components) {
		for (JComponent component : components)
			add(component);
		newLine();
	}

	public void add(JComponent component) {
		add(component, 1);
	}

	public void add(JComponent component, int width) {
		add(component, width, 1);
	}

	public void add(JComponent component, int width, int height) {
		add(component, width, height, true);
	}

	public void add(JComponent component, int width, int height, boolean jumpLines) {
		add(component, width, height, jumpLines, 0);
	}

	public void add(JComponent component, double weight) {
		add(component, 1, 1, true, weight);
	}

	private void add(JComponent component, int gridWidth, int gridHeight, boolean jumpLines, double weight) {
		panel.add(component, getConstraints(gridWidth, gridHeight, weight));
		gridX += gridWidth;
		if (jumpLines && gridHeight > lineJump)
			lineJump = gridHeight;
	}

	public void newLine() {
		gridY += lineJump;
		resetLine();
	}

	private void resetLine() {
		gridX = 0;
		lineJump = 0;
	}

	private GridBagConstraints getConstraints(int gridWidth, int gridHeight, double weight) {
		return new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weight, weight, CENTER, BOTH, insets, 0, 0);
	}
}