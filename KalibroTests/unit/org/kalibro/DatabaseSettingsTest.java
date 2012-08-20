package org.kalibro;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.kalibro.core.Environment;

public class DatabaseSettingsTest extends KalibroTestCase {

	private DatabaseSettings settings = new DatabaseSettings();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultSettings() {
		assertEquals(SupportedDatabase.MYSQL, settings.getDatabaseType());
		assertEquals("jdbc:mysql://localhost:3306/kalibro", settings.getJdbcUrl());
		assertEquals("kalibro", settings.getUsername());
		assertEquals("kalibro", settings.getPassword());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkToPersistenceProperties() {
		Map<String, String> properties = settings.toPersistenceProperties();
		assertEquals(5, properties.size());
		assertEquals(Environment.ddlGeneration(), properties.get(DDL_GENERATION));
		assertEquals(settings.getDatabaseType().getDriverClassName(), properties.get(JDBC_DRIVER));
		assertEquals(settings.getJdbcUrl(), properties.get(JDBC_URL));
		assertEquals(settings.getUsername(), properties.get(JDBC_USER));
		assertEquals(settings.getPassword(), properties.get(JDBC_PASSWORD));
	}
}