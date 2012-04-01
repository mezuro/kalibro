package org.kalibro.core.persistence.database;

import static org.analizo.AnalizoStub.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.ProjectFixtures.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.*;
import org.kalibro.core.persistence.dao.ModuleResultDao;
import org.powermock.reflect.Whitebox;

public abstract class ModuleResultDatabaseTest extends DatabaseTestCase {

	private ModuleResultDao dao;

	private Date date;
	private Project project;
	private ModuleResult result;

	@Before
	public void setUp() {
		date = new Date();
		project = helloWorld();
		result = helloWorldClassResult(date);
		result.setConfiguration(kalibroConfiguration());
		dao = daoFactory.getModuleResultDao();
		daoFactory.getProjectDao().save(project);
		save();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testSave() {
		result.getMetricResults().iterator().next().addDescendentResult(42.0);
		assertFalse(result.deepEquals(getSavedResult()));

		save();
		assertDeepEquals(result, getSavedResult());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testSaveSpecialDoubleValues() {
		MetricResult firstResult = result.getMetricResults().iterator().next();
		firstResult.addDescendentResult(Double.NaN);
		firstResult.addDescendentResult(Double.NEGATIVE_INFINITY);
		firstResult.addDescendentResult(Double.POSITIVE_INFINITY);

		save();
		assertDeepEquals(result, getSavedResult());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveWithConfiguredResults() {
		save();

		MetricConfiguration badCompoundMetric = newCompoundMetric("bad", "return cbo > 0 ? 1.0 : null;");
		Configuration configuration = simpleConfiguration();
		configuration.addMetricConfiguration(badCompoundMetric);
		daoFactory.getConfigurationDao().save(configuration);

		result = getSavedResult();
		assertTrue(result.getCompoundMetricsWithError().contains(badCompoundMetric));
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
		String moduleName = result.getModule().getName();
		return dao.getModuleResult(projectName, moduleName, date);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testResultHistory() {
		Metric loc = nativeMetric("loc");
		result.getResultFor(loc).addDescendentResult(1.0);
		save();

		incrementDate();
		result.getResultFor(loc).addDescendentResult(2.0);
		dao.save(result, project.getName());

		incrementDate();
		result.getResultFor(loc).addDescendentResult(3.0);
		dao.save(result, project.getName());

		List<ModuleResult> resultHistory = dao.getResultHistory(project.getName(), result.getModule().getName());
		assertEquals(3, resultHistory.size());
		assertDeepEquals(resultHistory.get(0).getResultFor(loc).getDescendentResults(), 1.0);
		assertDeepEquals(resultHistory.get(1).getResultFor(loc).getDescendentResults(), 1.0, 2.0);
		assertDeepEquals(resultHistory.get(2).getResultFor(loc).getDescendentResults(), 1.0, 2.0, 3.0);
	}

	private void incrementDate() {
		date = new Date(date.getTime() + 1);
		Whitebox.setInternalState(result, "date", date);
	}

	private void save() {
		dao.save(result, project.getName());
	}
}