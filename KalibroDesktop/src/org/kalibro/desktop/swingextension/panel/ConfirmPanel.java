package org.kalibro.desktop.swingextension.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.Button;

public class ConfirmPanel extends JPanel {

	private Button cancelButton, okButton;

	public ConfirmPanel(Component contentPane) {
		super(new BorderLayout());
		add(contentPane, BorderLayout.CENTER);
		add(createButtonsPanel(), BorderLayout.SOUTH);
	}

	private JPanel createButtonsPanel() {
		cancelButton = new Button("cancel", "Cancel");
		okButton = new Button("ok", "Ok");
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
		return buttonsPanel;
	}

	public void addCancelListener(ActionListener listener) {
		cancelButton.addActionListener(listener);
	}

	public void addOkListener(ActionListener listener) {
		okButton.addActionListener(listener);
	}
}