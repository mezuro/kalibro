package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class StringPrinterTest extends PrinterTestCase<String> {

	@Override
	protected StringPrinter createPrinter() {
		return new StringPrinter();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintStrings() {
		assertTrue(printer.canPrint(""));
		assertTrue(printer.canPrint("" + this));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsYaml() throws Exception {
		String simpleString = "My string";
		String specialString = "\tMy\n\"special\"\nstring";
		assertEquals(" \"My string\" # simple string", print(simpleString, "simple string"));
		assertEquals(" \"\\tMy\\n\\\"special\\\"\\nstring\"", print(specialString, ""));

		assertEquals(specialString, new Yaml().load(print(specialString, "")));
	}
}