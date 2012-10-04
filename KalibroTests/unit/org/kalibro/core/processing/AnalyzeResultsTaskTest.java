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
import org.kalibro.RepositoryResult;
import org.kalibro.ResultState;
import org.kalibro.tests.UnitTest;

public class AnalyzeResultsTaskTest extends UnitTest {

	private RepositoryResult repositoryResult;
	private AnalyzeResultsTask analyzeTask;

	@Before
	public void setUp() {
		repositoryResult = newHelloWorldResult();
		repositoryResult.setSourceTree(null);
		analyzeTask = new AnalyzeResultsTask(repositoryResult, newHelloWorldResultMap(repositoryResult.getDate()));
	}

	@Test
	public void checkTaskState() {
		assertEquals(ResultState.ANALYZING, analyzeTask.getTaskState());
	}

	@Test
	public void shouldSetSourceTreeOnProjectResult() {
		analyzeTask.compute();
		assertDeepEquals(helloWorldRoot(), repositoryResult.getSourceTree());
	}

	@Test
	public void shouldReturnResults() {
		Collection<ModuleResult> expected = newHelloWorldResults(repositoryResult.getDate());
		Collection<ModuleResult> actual = analyzeTask.compute();
		assertDeepEquals(new HashSet<ModuleResult>(expected), new HashSet<ModuleResult>(actual));
	}
}