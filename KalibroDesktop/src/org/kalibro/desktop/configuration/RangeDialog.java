package org.kalibro.desktop.configuration;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.AbstractDialog;

public class RangeDialog extends AbstractDialog {

	private RangePanel rangePanel;
	private Button cancelButton, okButton;

	public RangeDialog() {
		super("Range");
		setResizable(false);
	}

	@Override
	protected void createComponents() {
		rangePanel = new RangePanel();
		cancelButton = new Button("cancel", "Cancel", new CancelListener());
		okButton = new Button("ok", "Ok");
	}

	@Override
	protected Container buildPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(rangePanel, BorderLayout.CENTER);
		panel.add(buttonsPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private Component buttonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
		return buttonsPanel;
	}

	public Range getRange() {
		return rangePanel.retrieve();
	}

	public void setRange(Range range) {
		rangePanel.show(range);
	}

	public void addOkListener(ActionListener listener) {
		okButton.addActionListener(listener);
	}

	private class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			dispose();
		}
	}
}