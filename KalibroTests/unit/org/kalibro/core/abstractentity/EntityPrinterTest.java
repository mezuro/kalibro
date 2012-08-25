package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntityPrinterTest extends PrinterTestCase<AbstractEntity<?>> {

	@Override
	protected Printer<AbstractEntity<?>> createPrinter() {
		return new EntityPrinter();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintEntities() {
		assertTrue(printer.canPrint(new Person()));
		assertTrue(printer.canPrint(new Programmer()));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsYaml() throws Exception {
		Programmer programmer = loadFixture("programmer-carlos", Programmer.class);
		assertEquals(loadResource("programmer-carlos.yml").replace("---", ""), print(programmer, ""));
	}
}