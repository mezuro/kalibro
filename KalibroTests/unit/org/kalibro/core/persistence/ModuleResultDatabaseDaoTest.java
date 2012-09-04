package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.ProjectResultFixtures;
import org.kalibro.core.persistence.entities.MetricResultRecord;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MetricResultRecord.class, ModuleResultDatabaseDao.class})
public class ModuleResultDatabaseDaoTest extends TestCase {

	private static final Date DATE = new Date();
	private static final String MODULE_NAME = "ModuleResultDatabaseDaoTest module";
	private static final String PROJECT_NAME = "ModuleResultDatabaseDaoTest project";
	private static final String QUERY = "SELECT result FROM MetricResult result " +
		"WHERE result.module.projectResult.project.name = :projectName AND result.module.name = :moduleName";

	private ModuleResult moduleResult;
	private ProjectResult projectResult;
	private Configuration configuration;
	private List<MetricResultRecord> records;

	private DatabaseManager databaseManager;
	private Query<MetricResultRecord> query;

	private ModuleResultDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		projectResult = ProjectResultFixtures.newHelloWorldResult(DATE);
		projectResult.getProject().setName(PROJECT_NAME);
		mockRecords();
		databaseManager = mock(DatabaseManager.class);
		dao = spy(new ModuleResultDatabaseDao(databaseManager));
		mockQueries();
	}

	private void mockRecords() {
		moduleResult = mock(ModuleResult.class);
		configuration = mock(Configuration.class);
		records = mock(List.class);
		mockStatic(MetricResultRecord.class);
		when(MetricResultRecord.createRecords(moduleResult, projectResult)).thenReturn(records);
		when(MetricResultRecord.convertIntoModuleResults(records)).thenReturn(Arrays.asList(moduleResult));
	}

	private void mockQueries() throws Exception {
		query = mock(Query.class);
		doReturn(query).when(dao).createRecordQuery(anyString());
		when(query.getResultList()).thenReturn(records);

		ConfigurationDatabaseDao configDao = mock(ConfigurationDatabaseDao.class);
		whenNew(ConfigurationDatabaseDao.class).withArguments(databaseManager).thenReturn(configDao);
		when(configDao.getConfigurationFor(PROJECT_NAME)).thenReturn(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(moduleResult, projectResult);
		Mockito.verify(databaseManager).save(records);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetModuleResult() {
		assertSame(moduleResult, dao.getModuleResult(PROJECT_NAME, MODULE_NAME, DATE));

		Mockito.verify(dao).createRecordQuery(QUERY + " AND result.module.projectResult.date = :date");
		Mockito.verify(query).setParameter("projectName", PROJECT_NAME);
		Mockito.verify(query).setParameter("moduleName", MODULE_NAME);
		Mockito.verify(query).setParameter("date", DATE.getTime());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetResultHistory() {
		List<ModuleResult> resultHistory = dao.getResultHistory(PROJECT_NAME, MODULE_NAME);
		assertEquals(1, resultHistory.size());
		assertSame(moduleResult, resultHistory.get(0));

		Mockito.verify(dao).createRecordQuery(QUERY + " ORDER BY result.module.projectResult.date");
		Mockito.verify(query).setParameter("projectName", PROJECT_NAME);
		Mockito.verify(query).setParameter("moduleName", MODULE_NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfigureModuleResult() {
		dao.getModuleResult(PROJECT_NAME, MODULE_NAME, DATE);
		Mockito.verify(moduleResult).setConfiguration(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfigureResultHistory() {
		dao.getResultHistory(PROJECT_NAME, MODULE_NAME);
		Mockito.verify(moduleResult).setConfiguration(configuration);
	}
}