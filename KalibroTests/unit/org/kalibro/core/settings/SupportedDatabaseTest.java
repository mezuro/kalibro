package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SupportedDatabase.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class SupportedDatabaseTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() {
		SupportedDatabase.values();
		SupportedDatabase.valueOf("MYSQL");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() {
		assertEquals("Apache Derby", "" + APACHE_DERBY);
		assertEquals("Mysql", "" + MYSQL);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkJdbcDrivers() {
		assertEquals(org.apache.derby.jdbc.EmbeddedDriver.class.getName(), APACHE_DERBY.getDriverClassName());
		assertEquals(com.mysql.jdbc.Driver.class.getName(), MYSQL.getDriverClassName());
	}
}