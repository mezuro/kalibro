package org.kalibro.core.abstractentity;

import static java.lang.Double.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class DoublePrinterTest extends PrinterTestCase<Double> {

	@Override
	protected DoublePrinter createPrinter() {
		return new DoublePrinter();
	}

	@Test
	public void shouldPrintDoubles() {
		assertTrue(printer.canPrint(42.0));
		assertTrue(printer.canPrint(POSITIVE_INFINITY));
		assertTrue(printer.canPrint(NEGATIVE_INFINITY));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test
	public void shouldPrintInfinities() throws Exception {
		assertEquals(" 42.0", print(42.0, ""));
		assertEquals(" .inf # Infinity", print(POSITIVE_INFINITY, "Infinity"));
		assertEquals(" -.inf", print(NEGATIVE_INFINITY, ""));
	}

	@Test
	public void shouldBeLoadableAsYaml() throws Exception {
		assertEquals(42.0, new Yaml().load(print(42.0, "")));
		assertEquals(POSITIVE_INFINITY, new Yaml().load(print(POSITIVE_INFINITY, "")));
		assertEquals(NEGATIVE_INFINITY, new Yaml().load(print(NEGATIVE_INFINITY, "")));
	}
}