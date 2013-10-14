package org.kalibro.core.processing;

import java.io.File;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.*;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({KalibroSettings.class, PreparingTask.class})
public class PreparingTaskTest extends UnitTest {

	private Project project;
	private Repository repository;

	private File loadDirectory;
	private Processing processing;
	private ProcessContext context;
	private Configuration configuration;
	private ProcessingDatabaseDao processingDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;

	@Before
	public void setUp() throws Throwable {
		mockProject();
		mockRepository();
		mockLoadDirectory();
		mockDaosAndProcessing();
		context = mock(ProcessContext.class);
		when(context.repository()).thenReturn(repository);
		new PreparingTask(context).perform();
	}

	private void mockProject() {
		project = mock(Project.class);
		when(project.getId()).thenReturn(new Random().nextLong());
		when(project.getName()).thenReturn("MY PROJECT_NAME");
	}

	private void mockRepository() {
		repository = mock(Repository.class);
		when(repository.getProject()).thenReturn(project);
		when(repository.getId()).thenReturn(new Random().nextLong());
		when(repository.getName()).thenReturn("MY REPOSITORY_NAME");
	}

	private void mockLoadDirectory() {
		loadDirectory = new File("/");
		ServerSettings serverSettings = mock(ServerSettings.class);
		KalibroSettings kalibroSettings = mock(KalibroSettings.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(kalibroSettings);
		when(kalibroSettings.getServerSettings()).thenReturn(serverSettings);
		when(serverSettings.getLoadDirectory()).thenReturn(loadDirectory);
	}

	private void mockDaosAndProcessing() throws Exception {
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		processing = mock(Processing.class);
		configuration = mock(Configuration.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		metricResultDao = mock(MetricResultDatabaseDao.class);
		ConfigurationDatabaseDao configurationDao = mock(ConfigurationDatabaseDao.class);

		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		when(daoFactory.createModuleResultDao()).thenReturn(moduleResultDao);
		when(daoFactory.createMetricResultDao()).thenReturn(metricResultDao);
		when(daoFactory.createConfigurationDao()).thenReturn(configurationDao);
		when(processingDao.createProcessingFor(repository)).thenReturn(processing);
		when(processing.getId()).thenReturn(new Random().nextLong());
		when(configurationDao.snapshotFor(processing.getId())).thenReturn(configuration);
	}

	@Test
	public void shouldCreateProcessing() {
		verify(context).setProcessing(processing);
	}

	@Test
	public void shouldCreateConfigurationSnapshot() {
		verify(context).setConfigurationSnapshot(configuration);
	}

	@Test
	public void shouldEstablishCodeDirectory() {
		File projectDirectory = new File(loadDirectory, "MyProjectName-" + project.getId());
		File repositoryDirectory = new File(projectDirectory, "MyRepositoryName-" + repository.getId());
		verify(context).setCodeDirectory(repositoryDirectory);
	}

	@Test
	public void shouldCreateResultProducer() {
		verify(context).setProducer(any(Producer.class));
	}

	@Test
	public void shouldCreateProcessingDao() {
		verify(context).setProcessingDao(processingDao);
	}

	@Test
	public void shouldCreateModuleResultDao() {
		verify(context).setModuleResultDao(moduleResultDao);
	}

	@Test
	public void shouldCreateMetricResultDao() {
		verify(context).setMetricResultDao(metricResultDao);
	}
}