package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.ProcessState.*;

import org.junit.Test;
import org.kalibro.tests.EnumerationTest;

public class ProcessStateTest extends EnumerationTest<ProcessState> {

	@Override
	protected Class<ProcessState> enumerationClass() {
		return ProcessState.class;
	}

	@Test
	public void shouldAnswerIfIsTemporary() {
		assertTrue(LOADING.isTemporary());
		assertTrue(COLLECTING.isTemporary());
		assertTrue(ANALYZING.isTemporary());
		assertFalse(READY.isTemporary());
		assertFalse(ERROR.isTemporary());
	}

	@Test
	public void shouldProvideExplainingMessage() {
		String projectName = "HelloWorld-1.0";
		assertEquals("Loading HelloWorld-1.0 from repository", LOADING.getMessage(projectName));
		assertEquals("Collecting metric values for HelloWorld-1.0", COLLECTING.getMessage(projectName));
		assertEquals("Processing metric results for HelloWorld-1.0", ANALYZING.getMessage(projectName));
		assertEquals("Processing of HelloWorld-1.0 done", READY.getMessage(projectName));
		assertEquals("Error while processing HelloWorld-1.0", ERROR.getMessage(projectName));
	}

	@Test
	public void shouldGetNextState() {
		assertEquals(COLLECTING, LOADING.nextState());
		assertEquals(ANALYZING, COLLECTING.nextState());
		assertEquals(READY, ANALYZING.nextState());
		assertEquals(ERROR, READY.nextState());
		assertEquals(LOADING, ERROR.nextState());
	}
}