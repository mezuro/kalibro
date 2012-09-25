package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;
import static org.kalibro.ModuleFixtures.helloWorldClass;
import static org.kalibro.ModuleNodeFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class ModuleNodeTest extends UnitTest {

	private ModuleNode org, analizo, checkstyle;

	@Before
	public void setUp() {
		org = newAnalizoCheckstyleTree();
		analizo = analizoNode();
		checkstyle = checkstyleNode();
	}

	@Test
	public void childrenShouldBeEmptyByDefault() {
		assertTrue(new ModuleNode(helloWorldClass()).getChildren().isEmpty());
	}

	@Test
	public void toStringShouldBeModule() {
		assertEquals("" + org.getModule(), "" + org);
		assertEquals("" + analizo.getModule(), "" + analizo);
		assertEquals("" + checkstyle.getModule(), "" + checkstyle);
	}

	@Test
	public void testHasChildFor() {
		assertTrue(org.hasChildFor(analizo.getModule()));
		assertFalse(analizo.hasChildFor(checkstyle.getModule()));
	}

	@Test
	public void testGetChildFor() {
		assertDeepEquals(analizo, org.getChildFor(analizo.getModule()));
		assertDeepEquals(checkstyle, org.getChildFor(checkstyle.getModule()));
	}

	@Test
	public void checkNoChildError() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				checkstyle.getChildFor(analizo.getModule());
			}
		}).throwsException().withMessage("Module org.checkstyle has no child named analizo");
	}

	@Test
	public void testAddChild() {
		org.addChild(helloWorldLeaf());
		assertTrue(org.hasChildFor(helloWorldClass()));
	}

	@Test
	public void shouldSortByModule() {
		assertSorted(newNode(SOFTWARE, "G"), newNode(SOFTWARE, "H"),
			newNode(PACKAGE, "E"), newNode(PACKAGE, "F"),
			newNode(CLASS, "C"), newNode(CLASS, "D"),
			newNode(METHOD, "A"), newNode(METHOD, "B"));
	}
}