package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.ConfigurationFixtures.*;
import static org.kalibro.MetricFixtures.analizoMetric;
import static org.kalibro.ModuleResultFixtures.newHelloWorldClassResult;
import static org.kalibro.ProjectResultFixtures.newHelloWorldResult;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.*;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.tests.AcceptanceTest;
import org.powermock.reflect.Whitebox;

public class ModuleResultDatabaseTest extends AcceptanceTest {

	private ModuleResultDao dao;

	private Date date;
	private Project project;
	private ModuleResult moduleResult;
	private Processing processing;

	@Before
	public void setUp() {
		date = new Date();
		processing = newHelloWorldResult(date);
		project = processing.getProject();
		moduleResult = newHelloWorldClassResult(date);
		moduleResult.setConfiguration(kalibroConfiguration());
		dao = DaoFactory.getModuleResultDao();
		DaoFactory.getConfigurationDao().save(kalibroConfiguration());
		DaoFactory.getProjectDao().save(project);
		DaoFactory.getProjectResultDao().save(processing);
		save();
	}

	@Test
	public void testSave() {
		moduleResult.getMetricResults().iterator().next().addDescendentResult(42.0);
		assertFalse(moduleResult.deepEquals(getSavedResult()));

		save();
		assertDeepEquals(moduleResult, getSavedResult());
	}

	@Test
	public void testSaveSpecialDoubleValues() {
		MetricResult firstResult = moduleResult.getMetricResults().iterator().next();
		firstResult.addDescendentResult(Double.NaN);
		firstResult.addDescendentResult(Double.NEGATIVE_INFINITY);
		firstResult.addDescendentResult(Double.POSITIVE_INFINITY);

		save();
		assertDeepEquals(moduleResult, getSavedResult());
	}

	@Test
	public void shouldRetrieveWithConfiguredResults() {
		save();

		MetricConfiguration badCompoundMetric = newCompoundMetric("bad", "return cbo > 0 ? 1.0 : null;");
		Configuration configuration = newConfiguration("cbo");
		configuration.addMetricConfiguration(badCompoundMetric);

		ConfigurationDao configurationDao = DaoFactory.getConfigurationDao();
		configurationDao.save(configuration);

		moduleResult = getSavedResult();
		assertTrue(moduleResult.getCompoundMetricsWithError().contains(badCompoundMetric.getMetric()));
	}

	private MetricConfiguration newCompoundMetric(String code, String script) {
		CompoundMetric compoundMetric = new CompoundMetric();
		compoundMetric.setName(code);
		compoundMetric.setScript(script);
		MetricConfiguration configuration = new MetricConfiguration(compoundMetric);
		configuration.setCode(code);
		return configuration;
	}

	private ModuleResult getSavedResult() {
		String projectName = project.getName();
		String moduleName = moduleResult.getModule().getLongName();
		return dao.getModuleResult(projectName, moduleName, date);
	}

	@Test
	public void testResultHistory() {
		Metric loc = analizoMetric("loc");
		moduleResult.getResultFor(loc).addDescendentResult(1.0);
		save();

		incrementDate();
		moduleResult.getResultFor(loc).addDescendentResult(2.0);
		save();

		incrementDate();
		moduleResult.getResultFor(loc).addDescendentResult(3.0);
		save();

		List<ModuleResult> resultHistory = dao.getResultHistory(project.getName(), moduleResult.getModule()
			.getLongName());
		assertEquals(3, resultHistory.size());
		assertDeepEquals(asList(1.0), resultHistory.get(0).getResultFor(loc).getDescendentResults());
		assertDeepEquals(asList(1.0, 2.0), resultHistory.get(1).getResultFor(loc).getDescendentResults());
		assertDeepEquals(asList(1.0, 2.0, 3.0), resultHistory.get(2).getResultFor(loc).getDescendentResults());
	}

	private void incrementDate() {
		date = new Date(date.getTime() + 1);
		Whitebox.setInternalState(moduleResult, "date", date);
		processing.setDate(date);
		DaoFactory.getProjectResultDao().save(processing);
	}

	private void save() {
		((ModuleResultDatabaseDao) dao).save(moduleResult, processing);
	}
}