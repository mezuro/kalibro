package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.persistence.database.ConfigurationDatabaseDao;
import org.kalibro.core.persistence.database.DatabaseManager;
import org.kalibro.core.persistence.database.ModuleResultDatabaseDao;
import org.kalibro.core.persistence.database.Query;
import org.kalibro.core.persistence.database.entities.MetricResultRecord;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MetricResultRecord.class, ModuleResultDatabaseDao.class})
public class ModuleResultDatabaseDaoTest extends KalibroTestCase {

	private static final String QUERY = "SELECT result FROM MetricResult result " +
		"WHERE result.module.projectResult.project.name = :projectName AND result.module.name = :moduleName";

	private Date date;
	private String moduleName;
	private String projectName;
	private ModuleResult moduleResult;
	private Configuration configuration;
	private List<MetricResultRecord> records;

	private DatabaseManager databaseManager;
	private Query<MetricResultRecord> query;

	private ModuleResultDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		date = new Date();
		moduleName = "My module";
		projectName = "My project";
		mockRecords();
		databaseManager = PowerMockito.mock(DatabaseManager.class);
		dao = PowerMockito.spy(new ModuleResultDatabaseDao(databaseManager));
		mockQueries();
	}

	private void mockRecords() {
		moduleResult = PowerMockito.mock(ModuleResult.class);
		configuration = PowerMockito.mock(Configuration.class);
		records = PowerMockito.mock(List.class);
		PowerMockito.mockStatic(MetricResultRecord.class);
		PowerMockito.when(MetricResultRecord.createRecords(moduleResult, projectName, date)).thenReturn(records);
		PowerMockito.when(MetricResultRecord.convertIntoModuleResults(records)).thenReturn(Arrays.asList(moduleResult));
	}

	private void mockQueries() throws Exception {
		query = PowerMockito.mock(Query.class);
		PowerMockito.doReturn(query).when(dao).createRecordQuery(anyString());
		PowerMockito.when(query.getResultList()).thenReturn(records);

		ConfigurationDatabaseDao configDao = PowerMockito.mock(ConfigurationDatabaseDao.class);
		PowerMockito.whenNew(ConfigurationDatabaseDao.class).withArguments(databaseManager).thenReturn(configDao);
		PowerMockito.when(configDao.getConfigurationFor(projectName)).thenReturn(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(moduleResult, projectName, date);
		Mockito.verify(databaseManager).save(records);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetModuleResult() {
		assertSame(moduleResult, dao.getModuleResult(projectName, moduleName, date));

		Mockito.verify(dao).createRecordQuery(QUERY + " AND result.module.projectResult.date = :date");
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("moduleName", moduleName);
		Mockito.verify(query).setParameter("date", date.getTime());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetResultHistory() {
		List<ModuleResult> resultHistory = dao.getResultHistory(projectName, moduleName);
		assertEquals(1, resultHistory.size());
		assertSame(moduleResult, resultHistory.get(0));

		Mockito.verify(dao).createRecordQuery(QUERY + " ORDER BY result.module.projectResult.date");
		Mockito.verify(query).setParameter("projectName", projectName);
		Mockito.verify(query).setParameter("moduleName", moduleName);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfigureModuleResult() {
		dao.getModuleResult(projectName, moduleName, date);
		Mockito.verify(moduleResult).setConfiguration(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfigureResultHistory() {
		dao.getResultHistory(projectName, moduleName);
		Mockito.verify(moduleResult).setConfiguration(configuration);
	}
}