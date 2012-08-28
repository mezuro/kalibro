package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.kalibro.SupportedDatabase.*;

import org.junit.Test;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.Environment;
import org.yaml.snakeyaml.Yaml;

public class EnumPrinterTest extends PrinterTestCase<Enum<?>> {

	@Override
	protected EnumPrinter createPrinter() {
		return new EnumPrinter();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintEnums() {
		assertTrue(printer.canPrint(Environment.TEST));
		assertTrue(printer.canPrint(MYSQL));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsYaml() throws Exception {
		assertEquals(" MYSQL # database type", print(MYSQL, "database type"));
		assertEquals(APACHE_DERBY, new Yaml().loadAs(print(APACHE_DERBY, ""), SupportedDatabase.class));
	}
}