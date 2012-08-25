package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.Environment;

public class EnumPrinterTest extends PrinterTestCase<Enum<?>> {

	@Override
	protected EnumPrinter createPrinter() {
		return new EnumPrinter();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintEnums() {
		assertTrue(printer.canPrint(Environment.TEST));
		assertTrue(printer.canPrint(SupportedDatabase.MYSQL));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintName() throws Exception {
		assertEquals(" MYSQL # database type", print(SupportedDatabase.MYSQL, "database type"));
	}
}