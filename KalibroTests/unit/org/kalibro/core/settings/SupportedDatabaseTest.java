package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SupportedDatabase.*;

import org.junit.Test;
import org.kalibro.EnumerationTestCase;

public class SupportedDatabaseTest extends EnumerationTestCase<SupportedDatabase> {

	@Override
	protected Class<SupportedDatabase> enumerationClass() {
		return SupportedDatabase.class;
	}

	@Override
	protected String expectedText(SupportedDatabase value) {
		return (value == APACHE_DERBY) ? "Apache Derby" : super.expectedText(value);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkJdbcDrivers() {
		assertEquals(org.apache.derby.jdbc.EmbeddedDriver.class.getName(), APACHE_DERBY.getDriverClassName());
		assertEquals(com.mysql.jdbc.Driver.class.getName(), MYSQL.getDriverClassName());
	}
}