package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ModuleTest extends UnitTest {

	private Module org, kalibro, module;

	@Before
	public void setUp() {
		org = module(PACKAGE, "org");
		kalibro = module(PACKAGE, "org", "kalibro");
		module = module(CLASS, "org", "kalibro", "Module");
	}

	@Test
	public void shouldSortByGranularityThenName() {
		assertSorted(
			module(SOFTWARE, "G"), module(SOFTWARE, "H"),
			module(PACKAGE, "E"), module(PACKAGE, "F"),
			module(CLASS, "C"), module(CLASS, "D"),
			module(METHOD, "A"), module(METHOD, "B"));
	}

	@Test
	public void shouldIdentifyByName() {
		assertEquals(org, module(org.getName()));
		assertEquals(kalibro, module(kalibro.getName()));
		assertEquals(module, module(module.getName()));
	}

	private Module module(String... name) {
		return new Module(METHOD, name);
	}

	private Module module(Granularity granularity, String... name) {
		return new Module(granularity, name);
	}

	@Test
	public void checkConstruction() {
		assertArrayEquals(new String[]{"org"}, org.getName());
		assertArrayEquals(new String[]{"org", "kalibro"}, kalibro.getName());
		assertArrayEquals(new String[]{"org", "kalibro", "Module"}, module.getName());
		assertEquals(PACKAGE, org.getGranularity());
		assertEquals(PACKAGE, kalibro.getGranularity());
		assertEquals(CLASS, module.getGranularity());
	}

	@Test
	public void shortNameShouldBeLastName() {
		assertEquals("org", org.getShortName());
		assertEquals("kalibro", kalibro.getShortName());
		assertEquals("Module", module.getShortName());
	}

	@Test
	public void longNameShouldBeDotSeparated() {
		assertEquals("org", org.getLongName());
		assertEquals("org.kalibro", kalibro.getLongName());
		assertEquals("org.kalibro.Module", module.getLongName());
	}

	@Test
	public void shouldInferParent() {
		assertDeepEquals(kalibro, module.inferParent());
		assertDeepEquals(org, kalibro.inferParent());
		assertDeepEquals(new Module(SOFTWARE), org.inferParent());

		assertNull(new Module(SOFTWARE, "any", "name").inferParent());
	}

	@Test
	public void toStringShouldBeShortName() {
		assertEquals(org.getShortName(), "" + org);
		assertEquals(kalibro.getShortName(), "" + kalibro);
		assertEquals(module.getShortName(), "" + module);
	}
}