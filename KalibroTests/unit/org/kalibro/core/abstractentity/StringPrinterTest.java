package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class StringPrinterTest extends PrinterTestCase<String> {

	private static final String STRING = "\tMy\n\"special\"\nstring";

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
	public void shouldExcapeSpecialCharacters() throws Exception {
		assertEquals(" \"\\tMy\\n\\\"special\\\"\\nstring\" # special string", print(STRING, "special string"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeLoadableAsYaml() throws Exception {
		assertEquals(STRING, new Yaml().load(print(STRING, "")));
	}
}