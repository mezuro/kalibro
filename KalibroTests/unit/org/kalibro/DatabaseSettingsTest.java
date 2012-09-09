package org.kalibro;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DatabaseSettingsTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultSettings() {
		DatabaseSettings settings = new DatabaseSettings();
		assertEquals(SupportedDatabase.MYSQL, settings.getDatabaseType());
		assertEquals("jdbc:mysql://localhost:3306/kalibro", settings.getJdbcUrl());
		assertEquals("kalibro", settings.getUsername());
		assertEquals("kalibro", settings.getPassword());
	}
}