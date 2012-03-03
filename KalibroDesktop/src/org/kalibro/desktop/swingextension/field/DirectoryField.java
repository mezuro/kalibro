package org.kalibro.desktop.swingextension.field;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.dialog.FileChooser;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class DirectoryField extends EditPanel<File> implements ActionListener {

	private StringField pathField;
	private Button browseButton;
	private FileChooser chooser;

	private File directory;

	public DirectoryField(String name) {
		super(name);
		chooser = new FileChooser(this);
	}

	@Override
	protected void createComponents() {
		browseButton = new Button("browse", "Browse", this);
		pathField = new StringField("path", 20);
		browseButton.addFocusListener(new FocusListener());
		pathField.addFocusListener(new FocusListener());
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this, 0);
		builder.add(pathField, 1.0);
		builder.add(new Label("  "));
		builder.add(browseButton);
	}

	@Override
	public File get() {
		return directory;
	}

	@Override
	public void set(File value) {
		if (value.exists() && value.isDirectory()) {
			directory = value;
			pathField.setText(value.getAbsolutePath());
		} else {
			new ErrorDialog(this).show("\"" + value + "\" is not a valid directory");
			pathField.requestFocus();
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (chooser.chooseDirectoryToOpen())
			set(chooser.getChosenFile());
	}

	private class FocusListener extends FocusAdapter {

		@Override
		public void focusLost(FocusEvent event) {
			if (reallyLostFocus(event))
				set(new File(pathField.getText()));
		}

		private boolean reallyLostFocus(FocusEvent event) {
			Component focusOwner = event.getOppositeComponent();
			boolean focusStillHere = focusOwner == browseButton || focusOwner == pathField;
			return !(event.isTemporary() || focusStillHere);
		}
	}
}