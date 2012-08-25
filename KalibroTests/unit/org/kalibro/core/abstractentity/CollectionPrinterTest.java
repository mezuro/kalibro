package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

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
		Collection<Object> collection = new ArrayList<Object>();
		collection.add(new ArrayList<String>());
		collection.add(Arrays.asList("cat", "dog", "pig"));

		assertEquals(" [] # empty collection", print(new ArrayList<String>(), "empty collection"));
		assertEquals(loadResource("collection.printer.test"), print(collection, "strange collection"));
	}
}