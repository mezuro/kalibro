package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class MapPrinterTest extends PrinterTestCase<Map<?, ?>> {

	private Map<String, Object> sample;

	@Override
	protected MapPrinter createPrinter() {
		return new MapPrinter();
	}

	@Before
	public void setUp() {
		sample = new TreeMap<String, Object>();
		sample.put("empty", newMap());
		sample.put("pets", newMap("c->cat", "d->dog", "p->pig"));
		sample.put("vehicles", newMap("b->bus", "c->car"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintMaps() {
		assertTrue(printer.canPrint(sample));
		assertTrue(printer.canPrint(new HashMap<MapPrinter, MapPrinterTest>()));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsYaml() throws Exception {
		assertEquals(" {} # empty map", print(newMap(), "empty map"));
		assertEquals(loadResource("map.printer.test"), print(sample, "strange map"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeLoadableAsYaml() throws Exception {
		assertEquals(sample, new Yaml().load(print(sample, "")));
	}

	private Map<String, String> newMap(String... mappings) {
		Map<String, String> map = new TreeMap<String, String>();
		for (String mapping : mappings)
			map.put(mapping.replaceAll("->.*", ""), mapping.replaceAll(".*->", ""));
		return map;
	}
}