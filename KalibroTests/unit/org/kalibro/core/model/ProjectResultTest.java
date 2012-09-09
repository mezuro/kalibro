package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleNodeFixtures.helloWorldRoot;
import static org.kalibro.core.model.ProjectFixtures.helloWorld;
import static org.kalibro.core.model.ProjectResultFixtures.newHelloWorldResult;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;

public class ProjectResultTest extends TestCase {

	private Date date;
	private ProjectResult result;

	@Before
	public void setUp() {
		date = new Date();
		result = newHelloWorldResult(date);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAttributes() {
		assertDeepEquals(helloWorld(), result.getProject());
		assertSame(date, result.getDate());
		assertEquals(0, result.getLoadTime().longValue());
		assertEquals(0, result.getCollectTime().longValue());
		assertEquals(0, result.getAnalysisTime().longValue());
		assertDeepEquals(helloWorldRoot(), result.getSourceTree());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByProjectThenDate() {
		ProjectResult result3 = new ProjectResult(helloWorld());
		ProjectResult result4 = new ProjectResult(helloWorld());

		ProjectResult result1 = new ProjectResult(new Project());
		ProjectResult result2 = new ProjectResult(new Project());

		assertSorted(result1, result2, result3, result4);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveIfIsProcessed() {
		assertTrue(result.isProcessed());
		assertFalse(new ProjectResult(helloWorld()).isProcessed());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateProjectProcessedOnRetrievingProcessData() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new ProjectResult(helloWorld()).getSourceTree();
			}
		}, "Project not yet processed: " + result.getProject().getName());
	}
}