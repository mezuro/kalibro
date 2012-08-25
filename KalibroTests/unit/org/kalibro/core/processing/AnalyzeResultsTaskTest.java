package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ModuleNodeFixtures.helloWorldRoot;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.ProjectResultFixtures.newHelloWorldResult;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.Kalibro;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class AnalyzeResultsTaskTest extends TestCase {

	private ProjectResult projectResult;
	private AnalyzeResultsTask analyzeTask;

	@Before
	public void setUp() {
		projectResult = newHelloWorldResult();
		projectResult.setSourceTree(null);
		mockKalibro();
		analyzeTask = new AnalyzeResultsTask(projectResult, newHelloWorldResultMap(projectResult.getDate()));
	}

	private void mockKalibro() {
		mockStatic(Kalibro.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskState() {
		assertEquals(ProjectState.ANALYZING, analyzeTask.getTaskState());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetSourceTreeOnProjectResult() {
		analyzeTask.performAndGetResult();
		assertDeepEquals(helloWorldRoot(), projectResult.getSourceTree());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnResults() {
		Collection<ModuleResult> expected = newHelloWorldResults(projectResult.getDate());
		Collection<ModuleResult> actual = analyzeTask.performAndGetResult();
		assertDeepEquals(new HashSet<ModuleResult>(expected), new HashSet<ModuleResult>(actual));
	}
}