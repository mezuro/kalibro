package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.ModuleResult;
import org.kalibro.ProjectResult;
import org.kalibro.ProjectResultFixtures;
import org.kalibro.core.persistence.record.MetricResultRecord;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MetricResultRecord.class, ModuleResultDatabaseDao.class})
public class ModuleResultDatabaseDaoTest extends UnitTest {

	private static final Date DATE = new Date();
	private static final String MODULE_NAME = "ModuleResultDatabaseDaoTest module";
	private static final String PROJECT_NAME = "ModuleResultDatabaseDaoTest project";
	private static final String QUERY = "SELECT result FROM MetricResult result " +
		"WHERE result.module.projectResult.project.name = :projectName AND result.module.name = :moduleName";

	private ModuleResult moduleResult;
	private ProjectResult projectResult;
	private Configuration configuration;
	private List<MetricResultRecord> records;

	private RecordManager recordManager;
	private TypedQuery<MetricResultRecord> query;

	private ModuleResultDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		projectResult = ProjectResultFixtures.newHelloWorldResult(DATE);
		projectResult.getProject().setName(PROJECT_NAME);
		mockRecords();
		recordManager = mock(RecordManager.class);
		dao = spy(new ModuleResultDatabaseDao(recordManager));
		mockQueries();
	}

	private void mockRecords() {
		moduleResult = mock(ModuleResult.class);
		configuration = mock(Configuration.class);
		records = mock(List.class);
		mockStatic(MetricResultRecord.class);
		when(MetricResultRecord.createRecords(moduleResult, projectResult)).thenReturn(records);
		when(MetricResultRecord.convertIntoModuleResults(records)).thenReturn(asList(moduleResult));
	}

	private void mockQueries() throws Exception {
		query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery(anyString());
		when(query.getResultList()).thenReturn(records);

		ProjectDatabaseDao projDao = mock(ProjectDatabaseDao.class);
		ConfigurationDatabaseDao configDao = mock(ConfigurationDatabaseDao.class);
		whenNew(ProjectDatabaseDao.class).withArguments(recordManager).thenReturn(projDao);
		when(projDao.getByName(PROJECT_NAME)).thenReturn(projectResult.getProject());
		whenNew(ConfigurationDatabaseDao.class).withArguments(recordManager).thenReturn(configDao);
		when(configDao.configurationOf(projectResult.getProject().getId())).thenReturn(configuration);
	}

	@Test
	public void testSave() {
		dao.save(moduleResult, projectResult);
		Mockito.verify(recordManager).saveAll(records);
	}

	@Test
	public void testGetModuleResult() {
		assertSame(moduleResult, dao.getModuleResult(PROJECT_NAME, MODULE_NAME, DATE));

		Mockito.verify(dao).createRecordQuery(QUERY + " AND result.module.projectResult.date = :date");
		Mockito.verify(query).setParameter("projectName", PROJECT_NAME);
		Mockito.verify(query).setParameter("moduleName", MODULE_NAME);
		Mockito.verify(query).setParameter("date", DATE.getTime());
	}

	@Test
	public void testGetResultHistory() {
		List<ModuleResult> resultHistory = dao.getResultHistory(PROJECT_NAME, MODULE_NAME);
		assertEquals(1, resultHistory.size());
		assertSame(moduleResult, resultHistory.get(0));

		Mockito.verify(dao).createRecordQuery(QUERY + " ORDER BY result.module.projectResult.date");
		Mockito.verify(query).setParameter("projectName", PROJECT_NAME);
		Mockito.verify(query).setParameter("moduleName", MODULE_NAME);
	}

	@Test
	public void shouldConfigureModuleResult() {
		dao.getModuleResult(PROJECT_NAME, MODULE_NAME, DATE);
		Mockito.verify(moduleResult).setConfiguration(configuration);
	}

	@Test
	public void shouldConfigureResultHistory() {
		dao.getResultHistory(PROJECT_NAME, MODULE_NAME);
		Mockito.verify(moduleResult).setConfiguration(configuration);
	}
}