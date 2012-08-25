package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class MapPrinterTest extends PrinterTestCase<Map<?, ?>> {

	@Override
	protected MapPrinter createPrinter() {
		return new MapPrinter();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintMaps() {
		assertTrue(printer.canPrint(new HashMap<String, Object>()));
		assertTrue(printer.canPrint(new TreeMap<MapPrinter, MapPrinterTest>()));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsYaml() throws Exception {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("empty", newMap());
		map.put("pets", newMap("c->cat", "d->dog", "p->pig"));
		map.put("vehicles", newMap("b->bus", "c->car"));

		assertEquals(" {} # empty map", print(newMap(), "empty map"));
		assertEquals(loadResource("map.printer.test"), print(map, "strange map"));
	}

	private Map<String, String> newMap(String... mappings) {
		Map<String, String> map = new TreeMap<String, String>();
		for (String mapping : mappings)
			map.put(mapping.replaceAll("->.*", ""), mapping.replaceAll(".*->", ""));
		return map;
	}
}