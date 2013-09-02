package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.SupportedDatabase.*;

import org.junit.Test;
import org.kalibro.tests.EnumerationTest;

public class SupportedDatabaseTest extends EnumerationTest<SupportedDatabase> {

	@Override
	protected Class<SupportedDatabase> enumerationClass() {
		return SupportedDatabase.class;
	}

	@Override
	protected String expectedText(SupportedDatabase value) {
		return array("Mysql", "PostgreSQL")[value.ordinal()];
	}

	@Test
	public void checkJdbcDrivers() {
		assertEquals(com.mysql.jdbc.Driver.class.getName(), MYSQL.getDriverClassName());
		assertEquals(org.postgresql.Driver.class.getName(), POSTGRESQL.getDriverClassName());
	}
}