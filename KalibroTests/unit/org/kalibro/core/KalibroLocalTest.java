package org.kalibro.core;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.database.DatabaseDaoFactory;
import org.kalibro.core.processing.ProcessProjectTask;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KalibroLocal.class, RepositoryType.class})
public class KalibroLocalTest extends KalibroTestCase {

	private static final String PROJECT_NAME = "KalibroLocalTest project";

	private DatabaseDaoFactory daoFactory;

	private KalibroLocal kalibroLocal;

	@Before
	public void setUp() throws Exception {
		mockDaoFactory();
		kalibroLocal = new KalibroLocal();
	}

	private void mockDaoFactory() throws Exception {
		daoFactory = mock(DatabaseDaoFactory.class);
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateDaoFactory() {
		assertSame(daoFactory, kalibroLocal.createDaoFactory());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveSupportedRepositoryTypes() {
		Set<RepositoryType> supportedTypes = mock(Set.class);
		mockStatic(RepositoryType.class);
		when(RepositoryType.supportedTypes()).thenReturn(supportedTypes);
		assertSame(supportedTypes, kalibroLocal.getSupportedRepositoryTypes());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProcessProject() throws Exception {
		ProcessProjectTask task = mockProcessProjectTask(PROJECT_NAME);
		kalibroLocal.processProject(PROJECT_NAME);
		Mockito.verify(task).executeInBackground();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProcessPeriodically() throws Exception {
		ProcessProjectTask task = mockProcessProjectTask(PROJECT_NAME);
		kalibroLocal.processPeriodically(PROJECT_NAME, 42);
		Mockito.verify(task).executePeriodically(42 * Task.DAY);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCancelPreviousPeriodicExecutionIfExistent() throws Exception {
		ProcessProjectTask existent = mockProcessProjectTask(PROJECT_NAME);
		kalibroLocal.processPeriodically(PROJECT_NAME, 42);

		ProcessProjectTask newTask = mockProcessProjectTask(PROJECT_NAME);
		kalibroLocal.processPeriodically(PROJECT_NAME, 84);

		Mockito.verify(existent).cancelPeriodicExecution();
		Mockito.verify(newTask).executePeriodically(84 * Task.DAY);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveProcessPeriod() throws Exception {
		mockProcessProjectTask(PROJECT_NAME);
		kalibroLocal.processPeriodically(PROJECT_NAME, 42);
		assertEquals(42, kalibroLocal.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void processPeriodShouldBeZeroIfNotScheduled() throws Exception {
		mockProcessProjectTask(PROJECT_NAME);
		assertEquals(0, kalibroLocal.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCancelPeriodicProcess() throws Exception {
		ProcessProjectTask task = mockProcessProjectTask(PROJECT_NAME);
		kalibroLocal.processPeriodically(PROJECT_NAME, 42);
		kalibroLocal.cancelPeriodicProcess(PROJECT_NAME);
		Mockito.verify(task).cancelPeriodicExecution();
		assertEquals(0, kalibroLocal.getProcessPeriod(PROJECT_NAME).intValue());
	}

	private ProcessProjectTask mockProcessProjectTask(String projectName) throws Exception {
		ProcessProjectTask task = mock(ProcessProjectTask.class);
		whenNew(ProcessProjectTask.class).withArguments(projectName).thenReturn(task);
		return task;
	}
}