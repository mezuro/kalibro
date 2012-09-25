package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.ProjectState.*;

import org.junit.Test;
import org.kalibro.tests.EnumerationTest;

public class ProjectStateTest extends EnumerationTest<ProjectState> {

	@Override
	protected Class<ProjectState> enumerationClass() {
		return ProjectState.class;
	}

	@Test
	public void shouldRetrieveIfIsTemporary() {
		assertFalse(NEW.isTemporary());
		assertTrue(LOADING.isTemporary());
		assertTrue(COLLECTING.isTemporary());
		assertTrue(ANALYZING.isTemporary());
		assertFalse(READY.isTemporary());
		assertFalse(ERROR.isTemporary());
	}

	@Test
	public void shouldProvideExplainingMessage() {
		String projectName = "HelloWorld-1.0";
		assertEquals("Project HelloWorld-1.0 was not processed", NEW.getMessage(projectName));
		assertEquals("Loading HelloWorld-1.0 from repository", LOADING.getMessage(projectName));
		assertEquals("Collecting metric values for HelloWorld-1.0", COLLECTING.getMessage(projectName));
		assertEquals("Processing metric results for HelloWorld-1.0", ANALYZING.getMessage(projectName));
		assertEquals("Processing of HelloWorld-1.0 done", READY.getMessage(projectName));
		assertEquals("Error while processing HelloWorld-1.0", ERROR.getMessage(projectName));
	}
}