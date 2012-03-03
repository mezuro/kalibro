package org.kalibro.desktop.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;

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
		settings = new DatabaseSettings(databaseSettingsMap());
		panel = new DatabaseSettingsPanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShow() {
		panel.show(settings);
		assertEquals(settings.getDatabaseType(), databaseTypeField().getValue());
		assertEquals(settings.getJdbcUrl(), jdbcUrlField().getValue());
		assertEquals(settings.getUsername(), usernameField().getValue());
		assertEquals(settings.getPassword(), passwordField().getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		databaseTypeField().setValue(settings.getDatabaseType());
		jdbcUrlField().setValue(settings.getJdbcUrl());
		usernameField().setValue(settings.getUsername());
		passwordField().setValue(settings.getPassword());
		assertDeepEquals(settings, panel.get());
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