package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ModuleNodeFixtures.helloWorldRoot;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.ProjectResultFixtures.newHelloWorldResult;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.ProjectResult;
import org.kalibro.ProjectState;
import org.kalibro.tests.UnitTest;

public class AnalyzeResultsTaskTest extends UnitTest {

	private ProjectResult projectResult;
	private AnalyzeResultsTask analyzeTask;

	@Before
	public void setUp() {
		projectResult = newHelloWorldResult();
		projectResult.setSourceTree(null);
		analyzeTask = new AnalyzeResultsTask(projectResult, newHelloWorldResultMap(projectResult.getDate()));
	}

	@Test
	public void checkTaskState() {
		assertEquals(ProjectState.ANALYZING, analyzeTask.getTaskState());
	}

	@Test
	public void shouldSetSourceTreeOnProjectResult() {
		analyzeTask.compute();
		assertDeepEquals(helloWorldRoot(), projectResult.getSourceTree());
	}

	@Test
	public void shouldReturnResults() {
		Collection<ModuleResult> expected = newHelloWorldResults(projectResult.getDate());
		Collection<ModuleResult> actual = analyzeTask.compute();
		assertDeepEquals(new HashSet<ModuleResult>(expected), new HashSet<ModuleResult>(actual));
	}
}