package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class MapEqualityEvaluatorTest extends KalibroTestCase {

	private MapEqualityEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = new MapEqualityEvaluator();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateAnyTypeOfMap() {
		assertTrue(evaluator.canEvaluate(new HashMap<String, Object>()));
		assertTrue(evaluator.canEvaluate(new TreeMap<Object, Integer>()));

		assertFalse(evaluator.canEvaluate(this));
		assertFalse(evaluator.canEvaluate(evaluator));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConsiderKeySet() {
		assertFalse(evaluator.equals(newMap("c", "cat", "d", "dog"), newMap("c", "cat", "p", "pig")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConsiderMapping() {
		assertTrue(evaluator.equals(newMap("c", "car", "f", "fog"), newMap("f", "fog", "c", "car")));
		assertFalse(evaluator.equals(newMap("c", "car", "f", "fog"), newMap("f", "fog", "c", "cat")));
	}

	private Map<String, String> newMap(String... mappings) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < mappings.length; i += 2)
			map.put(mappings[i], mappings[i + 1]);
		return map;
	}
}