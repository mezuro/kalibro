package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class ArrayPrinterTest extends PrinterTestCase<Object[]> {

	private Object[] sample;

	@Override
	protected ArrayPrinter createPrinter() {
		return new ArrayPrinter();
	}

	@Before
	public void setUp() {
		sample = new Object[2];
		sample[0] = new Object[0];
		sample[1] = new String[]{"cat", "dog", "pig"};
	}

	@Test
	public void shouldPrintArrays() {
		assertTrue(printer.canPrint(new Object[0]));
		assertTrue(printer.canPrint(new String[1]));
		assertTrue(printer.canPrint(new Test[2]));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test
	public void shouldPrintAsYaml() throws Exception {
		assertEquals(" [] # empty array", print(new Object[0], "empty array"));
		assertEquals(loadResource("collection.printer.test"), print(sample, "strange collection"));
	}

	@Test
	public void shouldBeLoadableAsYaml() throws Exception {
		assertArrayEquals((String[]) sample[1], new Yaml().loadAs(print(sample[1], ""), String[].class));
	}
}