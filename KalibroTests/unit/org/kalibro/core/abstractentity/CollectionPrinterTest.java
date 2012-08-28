package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class CollectionPrinterTest extends PrinterTestCase<Collection<?>> {

	@Override
	protected CollectionPrinter createPrinter() {
		return new CollectionPrinter();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintCollections() {
		assertTrue(printer.canPrint(new HashSet<CollectionPrinter>()));
		assertTrue(printer.canPrint(new ArrayList<CollectionPrinterTest>()));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsYaml() throws Exception {
		assertEquals(" [] # empty collection", print(new ArrayList<String>(), "empty collection"));

		Collection<Object> collection = new ArrayList<Object>();
		collection.add(new ArrayList<String>());
		collection.add(Arrays.asList("cat", "dog", "pig"));
		String printed = print(collection, "strange collection");
		assertEquals(loadResource("collection.printer.test"), printed);

		assertEquals(collection, new Yaml().load(printed));
	}
}