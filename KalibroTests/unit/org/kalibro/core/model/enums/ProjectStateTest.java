package org.kalibro.core.model.enums;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.ProjectState.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class ProjectStateTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() {
		ProjectState.values();
		ProjectState.valueOf("NEW");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() {
		assertEquals("New", "" + NEW);
		assertEquals("Loading", "" + LOADING);
		assertEquals("Analyzing", "" + ANALYZING);
		assertEquals("Ready", "" + READY);
		assertEquals("Error", "" + ERROR);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testIsTemporary() {
		assertFalse(NEW.isTemporary());
		assertTrue(LOADING.isTemporary());
		assertTrue(ANALYZING.isTemporary());
		assertFalse(READY.isTemporary());
		assertFalse(ERROR.isTemporary());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testMessage() {
		String projectName = "HelloWorld-1.0";
		assertEquals("Project HelloWorld-1.0 was not processed", NEW.getMessage(projectName));
		assertEquals("Loading HelloWorld-1.0 from repository", LOADING.getMessage(projectName));
		assertEquals("Calculating metric results for HelloWorld-1.0", ANALYZING.getMessage(projectName));
		assertEquals("Processing of HelloWorld-1.0 done", READY.getMessage(projectName));
		assertEquals("Error while processing HelloWorld-1.0", ERROR.getMessage(projectName));
	}
}