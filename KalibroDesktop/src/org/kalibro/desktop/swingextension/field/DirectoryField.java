package org.kalibro.desktop.swingextension.field;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.dialog.FileChooser;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class DirectoryField extends EditPanel<File> implements ActionListener, FocusListener {

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
		browseButton.addFocusListener(this);
		pathField.addFocusListener(this);
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this, 0);
		builder.add(pathField, 1.0);
		builder.add(new Label("  "));
		builder.add(browseButton);
	}

	@Override
	public void show(File aDirectory) {
		setDirectory(aDirectory);
	}

	@Override
	public File retrieve() {
		return getDirectory();
	}

	public File getDirectory() {
		return directory;
	}

	@Override
	public void focusGained(FocusEvent event) {
		return;
	}

	@Override
	public void focusLost(FocusEvent event) {
		if (reallyLostFocus(event))
			setDirectory(new File(pathField.getText()));
	}

	private boolean reallyLostFocus(FocusEvent event) {
		Component focusOwner = event.getOppositeComponent();
		boolean focusStillHere = focusOwner == browseButton || focusOwner == pathField;
		return !(event.isTemporary() || focusStillHere);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (chooser.chooseDirectoryToOpen())
			setDirectory(chooser.getChosenFile());
	}

	public void setDirectory(File directory) {
		if (directory.exists() && directory.isDirectory()) {
			this.directory = directory;
			pathField.setText(directory.getAbsolutePath());
		} else {
			new ErrorDialog(this).show("\"" + directory + "\" is not a valid directory");
			pathField.requestFocus();
		}
	}
}