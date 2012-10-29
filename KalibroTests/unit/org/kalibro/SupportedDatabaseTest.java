package org.kalibro;

import static org.junit.Assert.assertEquals;
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
		return array("Apache Derby", "Mysql", "SQLite")[value.ordinal()];
	}

	@Test
	public void checkJdbcDrivers() {
		assertEquals(org.apache.derby.jdbc.EmbeddedDriver.class.getName(), APACHE_DERBY.getDriverClassName());
		assertEquals(com.mysql.jdbc.Driver.class.getName(), MYSQL.getDriverClassName());
		assertEquals(org.sqlite.JDBC.class.getName(), SQLITE.getDriverClassName());
	}
}