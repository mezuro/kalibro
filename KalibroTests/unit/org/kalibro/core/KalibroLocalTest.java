package org.kalibro.core;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.RepositoryType.*;
import static org.mockito.Matchers.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.database.DatabaseDaoFactory;
import org.kalibro.core.processing.ProcessProjectTask;
import org.kalibro.core.settings.DatabaseSettings;
import org.kalibro.core.settings.KalibroSettings;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, KalibroLocal.class})
public class KalibroLocalTest extends KalibroTestCase {

	private static final String PROJECT_NAME = "KalibroLocalTest project";

	private DatabaseDaoFactory daoFactory;

	private KalibroLocal kalibroLocal;

	@Before
	public void setUp() throws Exception {
		mockSettings();
		mockDaoFactory();
		kalibroLocal = new KalibroLocal();
	}

	private void mockSettings() {
		KalibroSettings settings = PowerMockito.mock(KalibroSettings.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
	}

	private void mockDaoFactory() throws Exception {
		daoFactory = PowerMockito.mock(DatabaseDaoFactory.class);
		PowerMockito.whenNew(DatabaseDaoFactory.class).withParameterTypes(DatabaseSettings.class)
			.withArguments(any(DatabaseSettings.class)).thenReturn(daoFactory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateDaoFactory() {
		assertSame(daoFactory, kalibroLocal.createDaoFactory());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateSupportedRepositoryTypes() throws Exception {
		mockCommandTask();
		kalibroLocal = PowerMockito.spy(kalibroLocal);
		PowerMockito.doThrow(new RuntimeException()).when(kalibroLocal, "validateRepositoryType", GIT);
		Set<RepositoryType> supportedTypes = kalibroLocal.getSupportedRepositoryTypes();
		for (RepositoryType type : RepositoryType.values())
			assertEquals(type != GIT, supportedTypes.contains(type));
	}

	private void mockCommandTask() throws Exception {
		CommandTask commandTask = PowerMockito.mock(CommandTask.class);
		PowerMockito.whenNew(CommandTask.class).withArguments(Matchers.anyString()).thenReturn(commandTask);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProcessProject() throws Exception {
		ProcessProjectTask task = mockProcessProjectTask(PROJECT_NAME);
		kalibroLocal.processProject(PROJECT_NAME);
		Mockito.verify(task).executeInBackground();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProcessPeriodicallyCancelingPreviousPeriodicExecutrion() throws Exception {
		ProcessProjectTask task = mockProcessProjectTask(PROJECT_NAME);
		kalibroLocal.processPeriodically(PROJECT_NAME, 42);

		InOrder order = Mockito.inOrder(task);
		order.verify(task).cancelPeriodicExecution();
		order.verify(task).executePeriodically(42 * Task.DAY);
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
		Mockito.reset(task);

		kalibroLocal.cancelPeriodicProcess(PROJECT_NAME);
		Mockito.verify(task).cancelPeriodicExecution();
	}

	private ProcessProjectTask mockProcessProjectTask(String projectName) throws Exception {
		ProcessProjectTask task = PowerMockito.mock(ProcessProjectTask.class);
		PowerMockito.whenNew(ProcessProjectTask.class).withArguments(projectName).thenReturn(task);
		return task;
	}
}