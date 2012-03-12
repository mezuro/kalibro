package org.kalibro.desktop.configuration;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JPanel;

import org.kalibro.core.util.Identifier;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.panel.AbstractPanel;

public final class ConfigurationControllerManualTest extends AbstractPanel<String> implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ConfigurationController", new ConfigurationControllerManualTest()).setVisible(true);
	}

	private JDesktopPane desktopPane;
	private ConfigurationController controller;

	private ConfigurationControllerManualTest() {
		super("");
		controller = new ConfigurationController(desktopPane);
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		desktopPane = new JDesktopPane();
		desktopPane.setPreferredSize(new Dimension(1024, 708));
	}

	@Override
	protected void buildPanel() {
		setLayout(new BorderLayout());
		add(desktopPane, BorderLayout.CENTER);
		add(createButtonsPanel("New", "Open", "Delete", "Save", "Save as", "Close"), BorderLayout.SOUTH);
	}

	private JPanel createButtonsPanel(String... titles) {
		JPanel buttonsPanel = new JPanel();
		for (String title : titles)
			buttonsPanel.add(new Button("", title, this));
		return buttonsPanel;
	}

	@Override
	public String get() {
		return "";
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String buttonTitle = ((Button) event.getSource()).getText();
		try {
			invokeControllerMethod(buttonTitle);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void invokeControllerMethod(String buttonTitle) throws Exception {
		String methodName = Identifier.fromText(buttonTitle).asVariable();
		if (methodName.equals("new"))
			methodName = "newConfiguration";
		ConfigurationController.class.getMethod(methodName).invoke(controller);
	}
}