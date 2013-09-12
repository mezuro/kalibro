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
		assertTrue(BUILDING.isTemporary());
		assertTrue(AGGREGATING.isTemporary());
		assertTrue(CALCULATING.isTemporary());
		assertFalse(READY.isTemporary());
		assertFalse(ERROR.isTemporary());
	}

	@Test
	public void shouldProvideExplainingMessage() {
		String projectName = "HelloWorld-1.0";
		assertEquals("Loading HelloWorld-1.0 from repository", LOADING.getMessage(projectName));
		assertEquals("Collecting metric values for HelloWorld-1.0", COLLECTING.getMessage(projectName));
		assertEquals("Building source tree of HelloWorld-1.0", BUILDING.getMessage(projectName));
		assertEquals("Aggregating metric results for HelloWorld-1.0", AGGREGATING.getMessage(projectName));
		assertEquals("Calculating compound metric results and grades of HelloWorld-1.0",
			CALCULATING.getMessage(projectName));
		assertEquals("Processing of HelloWorld-1.0 done", READY.getMessage(projectName));
		assertEquals("Error while processing HelloWorld-1.0", ERROR.getMessage(projectName));
	}

	@Test
	public void shouldGetNextState() {
		assertEquals(COLLECTING, LOADING.nextState());
		assertEquals(BUILDING, COLLECTING.nextState());
		assertEquals(AGGREGATING, BUILDING.nextState());
		assertEquals(CALCULATING, AGGREGATING.nextState());
		assertEquals(READY, CALCULATING.nextState());
		assertEquals(ERROR, READY.nextState());
		assertEquals(LOADING, ERROR.nextState());
	}
}