package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleFixtures.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;

public class ModuleNodeTest extends KalibroTestCase {

	private ModuleNode org, analizo, checkstyle;

	@Before
	public void setUp() {
		org = newAnalizoCheckstyleTree();
		analizo = analizoNode();
		checkstyle = checkstyleNode();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void childrenShouldBeEmptyByDefault() {
		assertTrue(new ModuleNode(helloWorldClass()).getChildren().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void toStringShouldBeModule() {
		assertEquals("" + org.getModule(), "" + org);
		assertEquals("" + analizo.getModule(), "" + analizo);
		assertEquals("" + checkstyle.getModule(), "" + checkstyle);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasChildFor() {
		assertTrue(org.hasChildFor(analizo.getModule()));
		assertFalse(analizo.hasChildFor(checkstyle.getModule()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetChildFor() {
		assertDeepEquals(analizo, org.getChildFor(analizo.getModule()));
		assertDeepEquals(checkstyle, org.getChildFor(checkstyle.getModule()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNoChildError() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				checkstyle.getChildFor(analizo.getModule());
			}
		}, "Module org.checkstyle has no child named analizo");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testAddChild() {
		org.addChild(helloWorldLeaf());
		assertTrue(org.hasChildFor(helloWorldClass()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByModule() {
		assertSorted(newNode(SOFTWARE, "G"), newNode(SOFTWARE, "H"),
			newNode(PACKAGE, "E"), newNode(PACKAGE, "F"),
			newNode(CLASS, "C"), newNode(CLASS, "D"),
			newNode(METHOD, "A"), newNode(METHOD, "B"));
	}
}