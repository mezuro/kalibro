package org.kalibro.desktop.swingextension.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.Field;

public class EditDialog<T> extends AbstractDialog implements ActionListener {

	private Field<T> field;
	private Button cancelButton, okButton;

	public EditDialog(String title, Field<T> field) {
		super(title);
		this.field = field;
		addField();
	}

	@Override
	protected void createComponents() {
		cancelButton = new Button("cancel", "Cancel", this);
		okButton = new Button("ok", "Ok");
	}

	@Override
	protected Container buildPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createButtonsPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel createButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
		return buttonsPanel;
	}

	private void addField() {
		getContentPane().add((Component) field, BorderLayout.CENTER);
		adjustSize();
	}

	public void addListener(EditDialogListener<T> listener) {
		okButton.addActionListener(new OkAdapter(listener));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		dispose();
	}

	private void showError(Exception error) {
		new ErrorDialog(this).show(error);
	}

	private class OkAdapter implements ActionListener {

		private EditDialogListener<T> listener;

		protected OkAdapter(EditDialogListener<T> listener) {
			this.listener = listener;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				if (listener.dialogConfirm(field.get()))
					dispose();
			} catch (Exception exception) {
				showError(exception);
			}
		}
	}
}