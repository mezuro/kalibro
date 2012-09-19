package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class ColorPrinterTest extends PrinterTestCase<Color> {

	@Override
	protected ColorPrinter createPrinter() {
		return new ColorPrinter();
	}

	@Test
	public void shouldPrintColors() {
		assertTrue(printer.canPrint(Color.RED));
		assertTrue(printer.canPrint(new Color(42)));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test
	public void shouldPrintAsHexString() throws Exception {
		assertEquals(" 0xff0000 # red", print(Color.RED, "red"));
		assertEquals(" 0x00ff00 # green", print(Color.GREEN, "green"));
		assertEquals(" 0x0000ff # blue", print(Color.BLUE, "blue"));
		assertEquals(" 0x000000 # black", print(Color.BLACK, "black"));
		assertEquals(" 0xffffff # white", print(Color.WHITE, "white"));
	}

	@Test
	public void shouldBeLoadableAsYaml() throws Exception {
		assertEquals(Color.RED, loadFromPrinted(Color.RED));
		assertEquals(Color.GREEN, loadFromPrinted(Color.GREEN));
		assertEquals(Color.BLUE, loadFromPrinted(Color.BLUE));
		assertEquals(Color.BLACK, loadFromPrinted(Color.BLACK));
		assertEquals(Color.WHITE, loadFromPrinted(Color.WHITE));
	}

	private Color loadFromPrinted(Color color) throws Exception {
		return new Yaml().loadAs(print(color, ""), Color.class);
	}
}