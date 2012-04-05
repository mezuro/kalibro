package org.kalibro.desktop.configuration;

import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.panel.AbstractPanel;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;

public final class MetricControllerManualTest extends AbstractPanel<String> implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("MetricController", new MetricControllerManualTest()).setVisible(true);
	}

	private CardStackPanel cardStack;
	private Button addButton, editButton;

	private MetricConfigurationController controller;

	private MetricControllerManualTest() {
		super("");
		controller = new MetricConfigurationController(newConfiguration("amloc", "cbo", "lcom4"), cardStack);
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		addButton = new Button("add", "Add", this);
		editButton = new Button("edit", "Edit", this);
		cardStack = new CardStackPanel();
	}

	@Override
	protected void buildPanel() {
		setLayout(new BorderLayout());
		add(cardStack, BorderLayout.CENTER);
		add(createButtonsPanel(), BorderLayout.SOUTH);
	}

	private Component createButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(addButton);
		buttonsPanel.add(editButton);
		return buttonsPanel;
	}

	@Override
	public String get() {
		return "";
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == addButton)
			controller.addMetricConfiguration();
		if (event.getSource() == editButton)
			controller.edit(metricConfiguration("amloc"));
	}
}