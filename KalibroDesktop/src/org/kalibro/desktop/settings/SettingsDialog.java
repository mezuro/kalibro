package org.kalibro.desktop.settings;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.Kalibro;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.AbstractDialog;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public class SettingsDialog extends AbstractDialog implements ActionListener {

	private KalibroSettingsPanel settingsPanel;
	private Button cancelButton, okButton;

	public SettingsDialog() {
		super("Kalibro Settings");
		setResizable(false);
		settingsPanel.show(Kalibro.currentSettings());
	}

	@Override
	protected void createComponents() {
		settingsPanel = new KalibroSettingsPanel(this);
		cancelButton = new Button("cancel", "Cancel", this);
		okButton = new Button("ok", "Ok", this);
	}

	@Override
	protected Container buildPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(settingsPanel, BorderLayout.CENTER);
		panel.add(buildSouthPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel buildSouthPanel() {
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(cancelButton);
		southPanel.add(okButton);
		return southPanel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == okButton)
			changeSettings();
		else if (event.getSource() == cancelButton)
			dispose();
	}

	private void changeSettings() {
		try {
			Kalibro.changeSettings(settingsPanel.get());
			dispose();
		} catch (Exception exception) {
			new ErrorDialog(this).show(exception);
		}
	}
}