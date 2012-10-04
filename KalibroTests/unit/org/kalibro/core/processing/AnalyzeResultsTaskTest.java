package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;
import static org.kalibro.ModuleNodeFixtures.helloWorldRoot;
import static org.kalibro.ModuleResultFixtures.*;
import static org.kalibro.ProjectResultFixtures.newHelloWorldResult;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.Processing;
import org.kalibro.ProcessState;
import org.kalibro.tests.UnitTest;

public class AnalyzeResultsTaskTest extends UnitTest {

	private Processing processing;
	private AnalyzeResultsTask analyzeTask;

	@Before
	public void setUp() {
		processing = newHelloWorldResult();
		processing.setSourceTree(null);
		analyzeTask = new AnalyzeResultsTask(processing, newHelloWorldResultMap(processing.getDate()));
	}

	@Test
	public void checkTaskState() {
		assertEquals(ProcessState.ANALYZING, analyzeTask.getTaskState());
	}

	@Test
	public void shouldSetSourceTreeOnProjectResult() {
		analyzeTask.compute();
		assertDeepEquals(helloWorldRoot(), processing.getSourceTree());
	}

	@Test
	public void shouldReturnResults() {
		Collection<ModuleResult> expected = newHelloWorldResults(processing.getDate());
		Collection<ModuleResult> actual = analyzeTask.compute();
		assertDeepEquals(new HashSet<ModuleResult>(expected), new HashSet<ModuleResult>(actual));
	}
}