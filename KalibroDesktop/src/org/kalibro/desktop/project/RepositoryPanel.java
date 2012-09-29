package org.kalibro.desktop.project;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.DaoFactory;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.PasswordField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class RepositoryPanel extends EditPanel<Repository> implements ActionListener {

	private ChoiceField<RepositoryType> typeField;
	private StringField addressField;

	private Label usernameLabel, passwordLabel;
	private StringField usernameField;
	private PasswordField passwordField;

	public RepositoryPanel() {
		super("repository");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		typeField = new ChoiceField<RepositoryType>("type", DaoFactory.getProjectDao().getSupportedRepositoryTypes());
		addressField = new StringField("address", 20);
		usernameLabel = new Label("Username:");
		usernameField = new StringField("username", 10);
		passwordLabel = new Label("Password:");
		passwordField = new PasswordField("password", 10);
		typeField.addActionListener(this);
	}

	@Override
	protected void buildPanel() {
		setBorder(new TitledBorder("Repository"));
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Label("Type:"), typeField);
		builder.add(new Label("Address:"));
		builder.add(addressField, 3);
		builder.newLine();
		builder.addSimpleLine(usernameLabel, usernameField, passwordLabel, passwordField);
	}

	@Override
	public Repository get() {
		return new Repository(typeField.get(), addressField.get(), usernameField.get(), passwordField.get());
	}

	@Override
	public void set(Repository repository) {
		typeField.set(repository.getType());
		addressField.set(repository.getAddress());
		usernameField.setText(repository.getUsername());
		passwordField.set(repository.getPassword());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		boolean authenticationSupported = typeField.get().supportsAuthentication();
		updateAuthenticationField(usernameLabel, authenticationSupported);
		updateAuthenticationField(usernameField, authenticationSupported);
		updateAuthenticationField(passwordLabel, authenticationSupported);
		updateAuthenticationField(passwordField, authenticationSupported);
	}

	private void updateAuthenticationField(JComponent authenticationComponent, boolean authenticationSupported) {
		authenticationComponent.setEnabled(authenticationSupported);
		if (!authenticationSupported && authenticationComponent instanceof JTextField)
			((JTextField) authenticationComponent).setText("");
	}
}