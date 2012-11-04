package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.ProcessState.*;

import java.util.Date;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProcessingTest extends UnitTest {

	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());
	private static final Date DATE = new Date(REPOSITORY_ID);

	private Repository repository;
	private Processing processing;

	private ProcessingDao dao;

	@Before
	public void setUp() {
		mockDao();
		repository = mock(Repository.class);
		when(repository.getId()).thenReturn(REPOSITORY_ID);
		processing = new Processing(repository);
	}

	private void mockDao() {
		dao = mock(ProcessingDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProcessingDao()).thenReturn(dao);
	}

	@Test
	public void shouldAnswerIfHasProcessing() {
		when(dao.hasProcessing(REPOSITORY_ID)).thenReturn(true);
		assertTrue(Processing.hasProcessing(repository));

		when(repository.getId()).thenReturn(-1L);
		assertFalse(Processing.hasProcessing(repository));

		verify(repository, times(2)).assertSaved();
	}

	@Test
	public void shouldAnswerIfHasReadyProcessing() {
		when(dao.hasReadyProcessing(REPOSITORY_ID)).thenReturn(true);
		assertTrue(Processing.hasReadyProcessing(repository));

		when(repository.getId()).thenReturn(-1L);
		assertFalse(Processing.hasReadyProcessing(repository));

		verify(repository, times(2)).assertSaved();
	}

	@Test
	public void shouldAnswerIfHasProcessingAfterDate() {
		when(dao.hasProcessingAfter(DATE, REPOSITORY_ID)).thenReturn(true);
		assertTrue(Processing.hasProcessingAfter(DATE, repository));

		when(repository.getId()).thenReturn(-1L);
		assertFalse(Processing.hasProcessingAfter(DATE, repository));

		verify(repository, times(2)).assertSaved();
	}

	@Test
	public void shouldAnswerIfHasProcessingBeforeDate() {
		when(dao.hasProcessingBefore(DATE, REPOSITORY_ID)).thenReturn(true);
		assertTrue(Processing.hasProcessingBefore(DATE, repository));

		when(repository.getId()).thenReturn(-1L);
		assertFalse(Processing.hasProcessingBefore(DATE, repository));

		verify(repository, times(2)).assertSaved();
	}

	@Test
	public void shouldGetLastProcessingState() {
		when(dao.lastProcessingState(REPOSITORY_ID)).thenReturn(ANALYZING);
		assertEquals(ANALYZING, Processing.lastProcessingState(repository));
		verify(repository).assertSaved();
	}

	@Test
	public void shouldGetLastReadyProcessing() {
		when(dao.lastReadyProcessing(REPOSITORY_ID)).thenReturn(processing);
		assertSame(processing, Processing.lastReadyProcessing(repository));
		verify(repository).assertSaved();
	}

	@Test
	public void shouldGetFirstProcessing() {
		when(dao.firstProcessing(REPOSITORY_ID)).thenReturn(processing);
		assertSame(processing, Processing.firstProcessing(repository));
		verify(repository).assertSaved();
	}

	@Test
	public void shouldGetLastProcessing() {
		when(dao.lastProcessing(REPOSITORY_ID)).thenReturn(processing);
		assertSame(processing, Processing.lastProcessing(repository));
		verify(repository).assertSaved();
	}

	@Test
	public void shouldGetFirstProcessingAfterDate() {
		when(dao.firstProcessingAfter(DATE, REPOSITORY_ID)).thenReturn(processing);
		assertSame(processing, Processing.firstProcessingAfter(DATE, repository));
		verify(repository).assertSaved();
	}

	@Test
	public void shouldGetLastProcessingBeforeDate() {
		when(dao.lastProcessingBefore(DATE, REPOSITORY_ID)).thenReturn(processing);
		assertSame(processing, Processing.lastProcessingBefore(DATE, repository));
		verify(repository).assertSaved();
	}

	@Test
	public void shouldSortByRepositoryThenDate() {
		Processing first = withRepositoryDate("A", 3);
		Processing second = withRepositoryDate("A", 4);
		Processing third = withRepositoryDate("Z", 1);
		Processing fourth = withRepositoryDate("Z", 2);
		assertSorted(first, second, third, fourth);
	}

	private Processing withRepositoryDate(String repositoryName, long date) {
		Processing other = new Processing(new Repository(repositoryName, null, ""));
		other.setDate(new Date(date));
		return other;
	}

	@Test
	public void shouldIdentifyByRepositoryAndDate() {
		Processing other = new Processing(null);
		assertFalse(other.equals(processing));

		other = new Processing(repository);
		other.setDate(new Date(0));
		assertFalse(other.equals(processing));

		other.setDate(processing.getDate());
		assertEquals(processing, other);
	}

	@Test
	public void checkConstruction() {
		assertNull(processing.getId());
		assertSame(repository, processing.getRepository());
		assertEquals(new Date().getTime(), processing.getDate().getTime(), 100);
		assertEquals(LOADING, processing.getState());
		for (ProcessState state : ProcessState.values())
			assertNull(processing.getStateTime(state));
		assertNull(processing.getResultsRoot());
	}

	@Test
	public void shouldBeInErrorStateAfterSettingError() {
		Throwable error = mock(Throwable.class);
		processing.setError(error);
		assertSame(error, processing.getError());
		assertEquals(ERROR, processing.getState());
	}

	@Test
	public void shouldGetStateMessage() {
		String name = "ProcessingTest repository complete name";
		when(repository.getCompleteName()).thenReturn(name);

		assertEquals(LOADING.getMessage(name), processing.getStateMessage());
		processing.setState(ANALYZING);
		assertEquals(ANALYZING.getMessage(name), processing.getStateMessage());
	}

	@Test
	public void shouldGetStateWhenErrorOcurred() {
		assertNull(processing.getStateWhenErrorOcurred());
		processing.setState(ANALYZING);
		processing.setError(mock(Throwable.class));
		assertEquals(ANALYZING, processing.getStateWhenErrorOcurred());
	}

	@Test
	public void shouldNotAllowErrorStateWithoutError() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				processing.setState(ERROR);
			}
		}).throwsException().withMessage("Use setError(Throwable) to put repository in error state");
	}

	@Test
	public void shouldSetStateTimes() {
		processing.setStateTime(LOADING, 6);
		processing.setStateTime(COLLECTING, 28);
		processing.setStateTime(ANALYZING, 496);
		assertEquals(6, processing.getStateTime(LOADING).longValue());
		assertEquals(28, processing.getStateTime(COLLECTING).longValue());
		assertEquals(496, processing.getStateTime(ANALYZING).longValue());
	}

	@Test
	public void shouldSetResultsRoot() {
		ModuleResult resultsRoot = mock(ModuleResult.class);
		processing.setResultsRoot(resultsRoot);
		assertSame(resultsRoot, processing.getResultsRoot());
	}
}