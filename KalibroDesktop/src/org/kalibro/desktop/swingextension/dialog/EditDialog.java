package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.desktop.swingextension.panel.ConfirmPanel;
import org.kalibro.desktop.swingextension.panel.EditPanel;

public class EditDialog<T> extends AbstractDialog implements ActionListener {

	private ConfirmPanel<T> confirmPanel;

	public EditDialog(Window owner, String title, EditPanel<T> editPanel) {
		super(owner, title, editPanel);
		setName(editPanel.getName());
		confirmPanel.addCancelListener(this);
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		confirmPanel = new ConfirmPanel<T>((EditPanel<T>) innerComponents[0]);
	}

	@Override
	protected Container buildPanel() {
		return confirmPanel;
	}

	public void addListener(EditDialogListener<T> listener) {
		confirmPanel.addOkListener(new OkAdapter(listener));
	}

	public void edit(T value) {
		confirmPanel.set(value);
		setVisible(true);
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
				if (listener.dialogConfirm(confirmPanel.get()))
					dispose();
			} catch (Exception exception) {
				showError(exception);
			}
		}
	}
}