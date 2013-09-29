package org.kalibro.core.processing;

import static org.junit.Assert.*;

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
@PrepareOnlyThisForTest({KalibroSettings.class, ProcessContext.class})
public class ProcessContextTest extends UnitTest {

	private Project project;
	private Repository repository;

	private File loadDirectory;
	private ProcessingDatabaseDao processingDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;
	private ConfigurationDatabaseDao configurationDao;

	private ProcessContext context;

	@Before
	public void setUp() throws Exception {
		mockProject();
		mockRepository();
		mockLoadDirectory();
		mockDaos();
		context = new ProcessContext(repository);
	}

	private void mockProject() {
		project = mock(Project.class);
		when(project.getId()).thenReturn(new Random().nextLong());
		when(project.getName()).thenReturn("ProcessContextTest PROJECT_NAME");
	}

	private void mockRepository() {
		repository = mock(Repository.class);
		when(repository.getProject()).thenReturn(project);
		when(repository.getId()).thenReturn(new Random().nextLong());
		when(repository.getName()).thenReturn("ProcessContextTest REPOSITORY_NAME");
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

	private void mockDaos() throws Exception {
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		metricResultDao = mock(MetricResultDatabaseDao.class);
		configurationDao = mock(ConfigurationDatabaseDao.class);

		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
		when(daoFactory.createConfigurationDao()).thenReturn(configurationDao);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		when(daoFactory.createModuleResultDao()).thenReturn(moduleResultDao);
		when(daoFactory.createMetricResultDao()).thenReturn(metricResultDao);
	}

	@Test
	public void shouldEstablishCodeDirectory() {
		File projectDirectory = new File(loadDirectory, project.getName() + "-" + project.getId());
		File repositoryDirectory = new File(projectDirectory, repository.getName() + "-" + repository.getId());
		assertEquals(repositoryDirectory, context.codeDirectory());
	}

	@Test
	public void shouldRememberCodeDirectory() {
		File codeDirectory = context.codeDirectory();
		assertSame(codeDirectory, context.codeDirectory());
		assertSame(codeDirectory, context.codeDirectory());
	}

	@Test
	public void shouldCreateResultProducer() {
		assertNotNull(context.resultProducer());
	}

	@Test
	public void shouldRememberResultProducer() {
		Producer<NativeModuleResult> producer = context.resultProducer();
		assertSame(producer, context.resultProducer());
		assertSame(producer, context.resultProducer());
	}

	@Test
	public void shouldCreateAndRememberProcessingDao() {
		assertSame(processingDao, context.processingDao());
		assertSame(processingDao, context.processingDao());
	}

	@Test
	public void shouldCreateAndRememberModuleResultDao() {
		assertSame(moduleResultDao, context.moduleResultDao());
		assertSame(moduleResultDao, context.moduleResultDao());
	}

	@Test
	public void shouldCreateAndRememberMetricResultDao() {
		assertSame(metricResultDao, context.metricResultDao());
		assertSame(metricResultDao, context.metricResultDao());
	}

	@Test
	public void shouldCreateAndRememberConfigurationDao() {
		assertSame(configurationDao, context.configurationDao());
		assertSame(configurationDao, context.configurationDao());
	}
}