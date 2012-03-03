package org.kalibro.desktop.settings;

import javax.swing.border.TitledBorder;

import org.kalibro.core.settings.DatabaseSettings;
import org.kalibro.core.settings.SupportedDatabase;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.PasswordField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class DatabaseSettingsPanel extends EditPanel<DatabaseSettings> {

	private ChoiceField<SupportedDatabase> databaseTypeField;
	private StringField jdbcUrlField, usernameField;
	private PasswordField passwordField;

	public DatabaseSettingsPanel() {
		super("databaseSettings");
	}

	@Override
	protected void createComponents() {
		databaseTypeField = new ChoiceField<SupportedDatabase>("databaseType", SupportedDatabase.values());
		jdbcUrlField = new StringField("jdbcUrl", 25);
		usernameField = new StringField("username", 25);
		passwordField = new PasswordField("password", 25);
	}

	@Override
	protected void buildPanel() {
		setBorder(new TitledBorder("Database"));
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Label("Database type:"), databaseTypeField);
		builder.addSimpleLine(new Label("JDBC URL:"), jdbcUrlField);
		builder.addSimpleLine(new Label("Username:"), usernameField);
		builder.addSimpleLine(new Label("Password:"), passwordField);
	}

	@Override
	public void show(DatabaseSettings settings) {
		databaseTypeField.set(settings.getDatabaseType());
		jdbcUrlField.setText(settings.getJdbcUrl());
		usernameField.setText(settings.getUsername());
		passwordField.setText(settings.getPassword());
	}

	@Override
	public DatabaseSettings get() {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDatabaseType(databaseTypeField.get());
		settings.setJdbcUrl(jdbcUrlField.getText());
		settings.setUsername(usernameField.getText());
		settings.setPassword(passwordField.get());
		return settings;
	}
}