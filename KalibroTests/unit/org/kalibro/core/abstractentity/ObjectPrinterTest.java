package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectPrinterTest extends PrinterTestCase<Object> {

	@Override
	protected ObjectPrinter createPrinter() {
		return new ObjectPrinter();
	}

	@Test
	public void shouldPrintAnything() {
		assertTrue(printer.canPrint(""));
		assertTrue(printer.canPrint(null));
		assertTrue(printer.canPrint(this));
		assertTrue(printer.canPrint(printer));
	}

	@Test
	public void shouldPrintToString() throws Exception {
		assertEquals(" The answer # is 42", print("The answer", "is 42"));
	}
}