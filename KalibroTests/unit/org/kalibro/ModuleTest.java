package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ModuleTest extends UnitTest {

	private Module org, kalibro, core, model, module;

	@Before
	public void setUp() {
		org = newModule(PACKAGE, "org");
		kalibro = newModule(PACKAGE, "org", "kalibro");
		core = newModule(PACKAGE, "org", "kalibro", "core");
		model = newModule(PACKAGE, "org.kalibro.core.model");
		module = newModule(CLASS, "org.kalibro.core.model.Module");
	}

	@Test
	public void toStringShouldBeShortName() {
		assertEquals(org.getShortName(), "" + org);
		assertEquals(kalibro.getShortName(), "" + kalibro);
		assertEquals(core.getShortName(), "" + core);
		assertEquals(model.getShortName(), "" + model);
		assertEquals(module.getShortName(), "" + module);
	}

	@Test
	public void shortNameShouldBeLastName() {
		assertEquals("org", org.getShortName());
		assertEquals("kalibro", kalibro.getShortName());
		assertEquals("core", core.getShortName());
		assertEquals("model", model.getShortName());
		assertEquals("Module", module.getShortName());
	}

	@Test
	public void shouldRetrieveLongName() {
		assertEquals("org", org.getName());
		assertEquals("org.kalibro", kalibro.getName());
		assertEquals("org.kalibro.core", core.getName());
		assertEquals("org.kalibro.core.model", model.getName());
		assertEquals("org.kalibro.core.model.Module", module.getName());
	}

	@Test
	public void shouldInferAncestry() {
		assertTrue(org.inferAncestry().isEmpty());
		assertDeepEquals(asList(org), kalibro.inferAncestry());
		assertDeepEquals(asList(org, kalibro), core.inferAncestry());
		assertDeepEquals(asList(org, kalibro, core), model.inferAncestry());
		assertDeepEquals(asList(org, kalibro, core, model), module.inferAncestry());
	}

	@Test
	public void shouldSortByGranularityThenName() {
		assertSorted(newModule(SOFTWARE, "G"), newModule(SOFTWARE, "H"),
			newModule(PACKAGE, "E"), newModule(PACKAGE, "F"),
			newModule(CLASS, "C"), newModule(CLASS, "D"),
			newModule(METHOD, "A"), newModule(METHOD, "B"));
	}

	private Module newModule(Granularity granularity, String... name) {
		return new Module(granularity, name);
	}
}