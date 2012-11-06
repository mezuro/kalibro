package org.kalibro.desktop.swingextension.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.Button;

public class ConfirmPanel<T> extends EditPanel<T> {

	private EditPanel<T> panel;
	private Button cancelButton, okButton;

	public ConfirmPanel(EditPanel<T> innerPanel) {
		super(innerPanel.getName(), innerPanel);
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		panel = (EditPanel<T>) innerComponents[0];
		cancelButton = new Button("cancel", "Cancel");
		okButton = new Button("ok", "Ok");
	}

	@Override
	protected void buildPanel() {
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		add(buildButtonsPanel(), BorderLayout.SOUTH);
	}

	private JPanel buildButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
		return buttonsPanel;
	}

	@Override
	public T get() {
		return panel.get();
	}

	@Override
	public void set(T value) {
		panel.set(value);
	}

	public void addCancelListener(ActionListener listener) {
		cancelButton.addActionListener(listener);
	}

	public void addOkListener(ActionListener listener) {
		okButton.addActionListener(listener);
	}
}