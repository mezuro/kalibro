package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.ModuleNodeFixtures.helloWorldRoot;
import static org.kalibro.ProjectFixtures.helloWorld;
import static org.kalibro.ProjectResultFixtures.newHelloWorldResult;
import static org.kalibro.ProcessState.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class ProcessingTest extends UnitTest {

	private Date date;
	private Processing result;

	@Before
	public void setUp() {
		date = new Date();
		result = newHelloWorldResult(date);
	}

	@Test
	public void checkAttributes() {
		assertDeepEquals(helloWorld(), result.getRepository());
		assertSame(date, result.getDate());
		assertEquals(0, result.getLoadTime().longValue());
		assertEquals(0, result.getCollectTime().longValue());
		assertEquals(0, result.getAnalysisTime().longValue());
		assertDeepEquals(helloWorldRoot(), result.getResultsRoot());
		assertEquals(NEW, result.getState());
	}

	@Test
	public void shouldSortByProjectThenDate() {
		Processing result3 = new Processing(helloWorld());
		Processing result4 = new Processing(helloWorld());

		Processing result1 = new Processing(new Project());
		Processing result2 = new Processing(new Project());

		assertSorted(result1, result2, result3, result4);
	}

	@Test
	public void shouldBeInErrorStateAfterSettingError() {
		Throwable error = mock(Throwable.class);
		result.setError(error);
		assertSame(error, result.getError());
		assertEquals(ERROR, result.getState());
	}

	@Test
	public void shouldGetStateMessageFromState() {
		assertEquals(NEW.getMessage(repository.getCompleteName()), result.getStateMessage());
		result.setState(ANALYZING);
		assertEquals(ANALYZING.getMessage(repository.getCompleteName()), result.getStateMessage());
	}

	@Test
	public void shouldGetStateWhenErrorOcurred() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				result.getStateWhenErrorOcurred();
			}
		}).throwsException().withMessage("Repository " + repository.getCompleteName() + " has no error.");
		result.setState(ANALYZING);
		result.setError(mock(Throwable.class));
		assertEquals(ANALYZING, result.getStateWhenErrorOcurred());
	}

	@Test
	public void shouldNotAllowErrorStateWithoutException() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				result.setState(ERROR);
			}
		}).throwsException().withMessage("Use setError(Throwable) to put repository in error state");
	}

	@Test
	public void shouldRetrieveIfIsProcessed() {
		assertTrue(result.isProcessed());
		assertFalse(new Processing(helloWorld()).isProcessed());
	}

	@Test
	public void shouldValidateProjectProcessedOnRetrievingProcessData() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				new Processing(helloWorld()).getResultsRoot();
			}
		}).throwsException().withMessage("Project not yet processed: " + result.getRepository().getName());
	}
}