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
		assertEquals(SupportedDatabase.POSTGRESQL, settings.getDatabaseType());
		assertEquals("jdbc:postgresql://localhost:5432/kalibro", settings.getJdbcUrl());
		assertEquals("kalibro", settings.getUsername());
		assertEquals("kalibro", settings.getPassword());
	}

	@Test
	public void shouldPrintDatabaseTypePossibilities() {
		assertTrue(settings.toString().contains(" # Possibilities: MYSQL, POSTGRESQL\n"));
	}
}