package org.kalibro.desktop.settings;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.settings.DatabaseSettings;
import org.kalibro.core.settings.SupportedDatabase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.PasswordField;
import org.kalibro.desktop.swingextension.field.StringField;

public class DatabaseSettingsPanelTest extends KalibroTestCase {

	private DatabaseSettings settings;

	private DatabaseSettingsPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		settings = new DatabaseSettings();
		panel = new DatabaseSettingsPanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		databaseTypeField().set(settings.getDatabaseType());
		jdbcUrlField().set(settings.getJdbcUrl());
		usernameField().set(settings.getUsername());
		passwordField().set(settings.getPassword());
		assertDeepEquals(settings, panel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		panel.set(settings);
		assertEquals(settings.getDatabaseType(), databaseTypeField().get());
		assertEquals(settings.getJdbcUrl(), jdbcUrlField().get());
		assertEquals(settings.getUsername(), usernameField().get());
		assertEquals(settings.getPassword(), passwordField().get());
	}

	private ChoiceField<SupportedDatabase> databaseTypeField() {
		return finder.find("databaseType", ChoiceField.class);
	}

	private PasswordField passwordField() {
		return finder.find("password", PasswordField.class);
	}

	private StringField jdbcUrlField() {
		return finder.find("jdbcUrl", StringField.class);
	}

	private StringField usernameField() {
		return finder.find("username", StringField.class);
	}
}