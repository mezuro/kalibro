package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class MapEqualityTest extends UnitTest {

	private static final boolean DEEP = new Random().nextBoolean();

	private MapEquality equality;

	@Before
	public void setUp() {
		equality = new MapEquality(DEEP);
		mockStatic(Equality.class);
		when(Equality.areEqual(any(), any(), eq(DEEP))).thenAnswer(new EqualArgumentsAnswer());
	}

	@Test
	public void shouldEvaluateAnyTypeOfMap() {
		assertTrue(equality.canEvaluate(new HashMap<Object, String>()));
		assertTrue(equality.canEvaluate(new TreeMap<String, Integer>()));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
	}

	@Test
	public void mapsShouldHaveSameKeySet() {
		assertFalse(equality.equals(newMap("c->cat", "p->pig"), newMap("c->cat", "d->dog")));
	}

	@Test
	public void mappingsShouldBeEqual() {
		assertFalse(equality.equals(newMap("c->cat", "p->pig"), newMap("c->car", "p->pig")));
		assertFalse(equality.equals(newMap("c->cat", "d->dog"), newMap("c->cat", "d->dot")));
	}

	@Test
	public void mappingsShouldBeDeepEqualIfDeep() {
		Map<String, String> map1 = newMap("d->dog", "p->pig");
		Map<String, String> map2 = newMap("d->dog", "p->pig");
		assertTrue(equality.equals(map1, map2));
		verifyStatic();
		Equality.areEqual(map1.keySet(), map2.keySet(), DEEP);
		verifyStatic();
		Equality.areEqual("pig", "pig", DEEP);
	}

	private Map<String, String> newMap(String... mappings) {
		Map<String, String> map = new HashMap<String, String>();
		for (String mapping : mappings)
			map.put(mapping.replaceAll("->.*", ""), mapping.replaceAll(".*->", ""));
		return map;
	}
}