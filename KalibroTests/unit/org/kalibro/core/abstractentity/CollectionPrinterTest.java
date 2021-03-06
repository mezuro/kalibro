package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class CollectionPrinterTest extends PrinterTestCase<Collection<?>> {

	private Collection<Object> sample;

	@Override
	protected CollectionPrinter createPrinter() {
		return new CollectionPrinter();
	}

	@Before
	public void setUp() {
		sample = new ArrayList<Object>();
		sample.add(new ArrayList<String>());
		sample.add(list("cat", "dog", "pig"));
	}

	@Test
	public void shouldPrintCollections() {
		assertTrue(printer.canPrint(new HashSet<CollectionPrinter>()));
		assertTrue(printer.canPrint(new ArrayList<CollectionPrinterTest>()));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test
	public void shouldPrintAsYaml() throws Exception {
		assertEquals(" [] # empty collection", print(new ArrayList<String>(), "empty collection"));
		assertEquals(loadResource("collection.printer.test"), print(sample, "strange collection"));
	}

	@Test
	public void shouldBeLoadableAsYaml() throws Exception {
		assertEquals(sample, new Yaml().load(print(sample, "")));
	}
}