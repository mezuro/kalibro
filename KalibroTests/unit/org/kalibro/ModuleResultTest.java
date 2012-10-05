package org.kalibro;

import static org.junit.Assert.*;

import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ModuleResultTest extends UnitTest {

	private Module module;
	private ModuleResult parent, result;

	@Before
	public void setUp() {
		parent = mock(ModuleResult.class);
		module = new Module(Granularity.CLASS, "ModuleResult");
		result = new ModuleResult(parent, module);
	}

	@Test
	public void checkConstruction() {
		assertSame(module, result.getModule());
		assertSame(parent, result.getParent());
		assertTrue(result.getChildren().isEmpty());
	}

	@Test
	public void shouldSetParentOnChildren() {
		ModuleResult child = new ModuleResult(null, new Module(Granularity.METHOD, "getParent"));
		result.setChildren(asSortedSet(child));
		assertDeepEquals(asSet(child), result.getChildren());
		assertSame(result, child.getParent());
	}

	@Test
	public void shouldSetChildrenWithoutTouchingThem() {
		// required for lazy loading
		SortedSet<ModuleResult> children = mock(SortedSet.class);
		result.setChildren(children);
		verifyZeroInteractions(children);
	}

	@Test
	public void toStringShouldPrintModule() {
		assertEquals(result.getModule().toString(), "" + result);
	}
}