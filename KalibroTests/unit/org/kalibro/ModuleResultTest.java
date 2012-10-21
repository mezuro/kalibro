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
		assertNull(result.getId());
		assertSame(module, result.getModule());
		assertDoubleEquals(Double.NaN, result.getGrade());
		assertTrue(result.hasParent());
		assertSame(parent, result.getParent());
		assertTrue(result.getChildren().isEmpty());

		result = new ModuleResult();
		assertNull(result.getModule());
		assertFalse(result.hasParent());
	}

	@Test
	public void shouldSetParentOnGettingChildren() {
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
	public void shouldSetParentOnAddChild() {
		ModuleResult child = new ModuleResult(null, new Module(Granularity.METHOD, "getParent"));
		result.addChild(child);
		assertDeepEquals(asSet(child), result.getChildren());
		assertSame(result, child.getParent());
	}

	@Test
	public void toStringShouldPrintModule() {
		assertEquals(result.getModule().toString(), "" + result);
	}
}