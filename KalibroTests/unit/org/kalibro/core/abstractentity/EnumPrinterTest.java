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

	@Test
	public void shouldPrintEnums() {
		assertTrue(printer.canPrint(Environment.TEST));
		assertTrue(printer.canPrint(MYSQL));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test
	public void shouldPrintName() throws Exception {
		assertEquals(" MYSQL # database type", print(MYSQL, "database type"));
	}

	@Test
	public void shouldBeLoadableAsYaml() throws Exception {
		String printed = print(APACHE_DERBY, "");
		assertEquals(APACHE_DERBY, new Yaml().loadAs(printed, SupportedDatabase.class));
	}
}