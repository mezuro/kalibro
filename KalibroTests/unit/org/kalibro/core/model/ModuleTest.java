package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.Granularity;

public class ModuleTest extends KalibroTestCase {

	private Module org, kalibro, core, model, module;

	@Before
	public void setUp() {
		org = newModule(PACKAGE, "org");
		kalibro = newModule(PACKAGE, "org", "kalibro");
		core = newModule(PACKAGE, "org", "kalibro", "core");
		model = newModule(PACKAGE, "org.kalibro.core.model");
		module = newModule(CLASS, "org.kalibro.core.model.Module");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void toStringShouldBeShortName() {
		assertEquals(org.getShortName(), "" + org);
		assertEquals(kalibro.getShortName(), "" + kalibro);
		assertEquals(core.getShortName(), "" + core);
		assertEquals(model.getShortName(), "" + model);
		assertEquals(module.getShortName(), "" + module);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shortNameShouldBeLastName() {
		assertEquals("org", org.getShortName());
		assertEquals("kalibro", kalibro.getShortName());
		assertEquals("core", core.getShortName());
		assertEquals("model", model.getShortName());
		assertEquals("Module", module.getShortName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveLongName() {
		assertEquals("org", org.getName());
		assertEquals("org.kalibro", kalibro.getName());
		assertEquals("org.kalibro.core", core.getName());
		assertEquals("org.kalibro.core.model", model.getName());
		assertEquals("org.kalibro.core.model.Module", module.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInferAncestry() {
		assertTrue(org.inferAncestry().isEmpty());
		assertDeepList(kalibro.inferAncestry(), org);
		assertDeepList(core.inferAncestry(), org, kalibro);
		assertDeepList(model.inferAncestry(), org, kalibro, core);
		assertDeepList(module.inferAncestry(), org, kalibro, core, model);
	}

	@Test(timeout = UNIT_TIMEOUT)
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