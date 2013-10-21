package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
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
		context = new ProcessContext(repository);
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
		mockNativeMetrics();
	}

	private void mockNativeMetrics() {
		Map<BaseTool, Set<NativeMetric>> nativeMetrics = mock(Map.class);
		when(repository.getConfiguration()).thenReturn(configuration);
		when(configuration.getNativeMetrics()).thenReturn(nativeMetrics);
		when(nativeMetrics.isEmpty()).thenReturn(false);
	}

	@Test
	public void shouldCreateProcessing() {
		assertSame(processing, context.processing);
	}

	@Test
	public void shouldCreateConfigurationSnapshot() {
		assertSame(configuration, context.configuration);
	}

	@Test
	public void shouldEstablishCodeDirectory() {
		File projectDirectory = new File(loadDirectory, "MyProjectName-" + project.getId());
		File repositoryDirectory = new File(projectDirectory, "MyRepositoryName-" + repository.getId());
		assertEquals(repositoryDirectory, context.codeDirectory);
	}

	@Test
	public void shouldCreateResultProducer() {
		assertNotNull(context.resultProducer);
	}

	@Test
	public void shouldCreateProcessingDao() {
		assertSame(processingDao, context.processingDao);
	}

	@Test
	public void shouldCreateModuleResultDao() {
		assertSame(moduleResultDao, context.moduleResultDao);
	}

	@Test
	public void shouldCreateMetricResultDao() {
		assertSame(metricResultDao, context.metricResultDao);
	}

	@Test
	public void shouldNotProcessIfConfigurationHasNoNativeMetrics() {
		when(configuration.getNativeMetrics()).thenReturn(new HashMap<BaseTool, Set<NativeMetric>>());
		assertThat(new PreparingTask(context)).throwsException().withMessage(
			"Could not process repository '" + repository.getCompleteName() +
				"' because its configuration has no native metrics.");
	}
}