package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.ResultState.*;

import org.junit.Test;
import org.kalibro.tests.EnumerationTest;

public class ResultStateTest extends EnumerationTest<ResultState> {

	@Override
	protected Class<ResultState> enumerationClass() {
		return ResultState.class;
	}

	@Test
	public void shouldAnswerIfIsTemporary() {
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
		assertEquals("Repository HelloWorld-1.0 was not processed", NEW.getMessage(projectName));
		assertEquals("Loading HelloWorld-1.0 from repository", LOADING.getMessage(projectName));
		assertEquals("Collecting metric values for HelloWorld-1.0", COLLECTING.getMessage(projectName));
		assertEquals("Processing metric results for HelloWorld-1.0", ANALYZING.getMessage(projectName));
		assertEquals("Processing of HelloWorld-1.0 done", READY.getMessage(projectName));
		assertEquals("Error while processing HelloWorld-1.0", ERROR.getMessage(projectName));
	}
}