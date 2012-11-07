package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class DatabaseSettingsTest extends UnitTest {

	private DatabaseSettings settings;

	@Before
	public void setUp() {
		settings = new DatabaseSettings();
	}

	@Test
	public void checkConstruction() {
		assertEquals(SupportedDatabase.MYSQL, settings.getDatabaseType());
		assertEquals("jdbc:mysql://localhost:3306/kalibro", settings.getJdbcUrl());
		assertEquals("kalibro", settings.getUsername());
		assertEquals("kalibro", settings.getPassword());
	}

	@Test
	public void shouldPrintDatabaseTypePossibilities() {
		assertTrue(settings.toString().contains(" # Possibilities: APACHE_DERBY, MYSQL, POSTGRESQL, SQLITE\n"));
	}
}