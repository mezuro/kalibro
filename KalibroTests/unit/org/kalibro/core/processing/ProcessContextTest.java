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
	private DatabaseDaoFactory daoFactory;

	private ProcessContext context;

	@Before
	public void setUp() throws Exception {
		mockProject();
		mockRepository();
		mockLoadDirectory();
		mockDaoFactory();
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

	private void mockDaoFactory() throws Exception {
		daoFactory = mock(DatabaseDaoFactory.class);
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
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
	public void shouldCreateConfigurationDao() {
		ConfigurationDatabaseDao configurationDao = mock(ConfigurationDatabaseDao.class);
		when(daoFactory.createConfigurationDao()).thenReturn(configurationDao);
		assertSame(configurationDao, context.createConfigurationDao());
	}

	@Test
	public void shouldCreateProcessingDao() {
		ProcessingDatabaseDao processingDao = mock(ProcessingDatabaseDao.class);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		assertSame(processingDao, context.createProcessingDao());
	}

	@Test
	public void shouldCreateModuleResultDao() {
		ModuleResultDatabaseDao moduleResultDao = mock(ModuleResultDatabaseDao.class);
		when(daoFactory.createModuleResultDao()).thenReturn(moduleResultDao);
		assertSame(moduleResultDao, context.createModuleResultDao());
	}

	@Test
	public void shouldCreateMetricResultDao() {
		MetricResultDatabaseDao metricResultDao = mock(MetricResultDatabaseDao.class);
		when(daoFactory.createMetricResultDao()).thenReturn(metricResultDao);
		assertSame(metricResultDao, context.createMetricResultDao());
	}
}