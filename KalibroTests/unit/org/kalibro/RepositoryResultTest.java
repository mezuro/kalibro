package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.ModuleNodeFixtures.helloWorldRoot;
import static org.kalibro.ProjectFixtures.helloWorld;
import static org.kalibro.ProjectResultFixtures.newHelloWorldResult;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class RepositoryResultTest extends UnitTest {

	private Date date;
	private RepositoryResult result;

	@Before
	public void setUp() {
		date = new Date();
		result = newHelloWorldResult(date);
	}

	@Test
	public void checkAttributes() {
		assertDeepEquals(helloWorld(), result.getProject());
		assertSame(date, result.getDate());
		assertEquals(0, result.getLoadTime().longValue());
		assertEquals(0, result.getCollectTime().longValue());
		assertEquals(0, result.getAnalysisTime().longValue());
		assertDeepEquals(helloWorldRoot(), result.getSourceTree());
	}

	@Test
	public void shouldSortByProjectThenDate() {
		RepositoryResult result3 = new RepositoryResult(helloWorld());
		RepositoryResult result4 = new RepositoryResult(helloWorld());

		RepositoryResult result1 = new RepositoryResult(new Project());
		RepositoryResult result2 = new RepositoryResult(new Project());

		assertSorted(result1, result2, result3, result4);
	}

	@Test
	public void shouldRetrieveIfIsProcessed() {
		assertTrue(result.isProcessed());
		assertFalse(new RepositoryResult(helloWorld()).isProcessed());
	}

	@Test
	public void shouldValidateProjectProcessedOnRetrievingProcessData() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				new RepositoryResult(helloWorld()).getSourceTree();
			}
		}).throwsException().withMessage("Project not yet processed: " + result.getProject().getName());
	}
}