package org.kalibro.desktop.settings;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.DatabaseSettings;
import org.kalibro.SupportedDatabase;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.PasswordField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class DatabaseSettingsPanelTest extends UnitTest {

	private DatabaseSettings settings;

	private DatabaseSettingsPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		settings = new DatabaseSettings();
		panel = new DatabaseSettingsPanel();
		finder = new ComponentFinder(panel);
	}

	@Test
	public void shouldGet() {
		databaseTypeField().set(settings.getDatabaseType());
		jdbcUrlField().set(settings.getJdbcUrl());
		usernameField().set(settings.getUsername());
		passwordField().set(settings.getPassword());
		assertDeepEquals(settings, panel.get());
	}

	@Test
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