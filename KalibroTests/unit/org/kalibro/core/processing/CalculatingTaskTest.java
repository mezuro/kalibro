package org.kalibro.core.processing;

import java.util.Random;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.persistence.ConfigurationDatabaseDao;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Metric.class, MetricResult.class, ModuleResult.class, CalculatingTask.class})
public class CalculatingTaskTest extends UnitTest {

	private Processing processing;
	private ModuleResult moduleResult;
	private Configuration configuration;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private CompoundResultCalculator compoundCalculator;

	private CalculatingTask calculatingTask;

	@Before
	public void setUp() throws Exception {
		compoundCalculator = mock(CompoundResultCalculator.class);

		calculatingTask = new CalculatingTask(mockContext());
		whenNew(CompoundResultCalculator.class).withArguments(moduleResult, configuration)
			.thenReturn(compoundCalculator);
	}

	private ProcessContext mockContext() {
		mockEntities();
		mockDaos();
		ProcessContext context = mock(ProcessContext.class);
		when(context.moduleResultDao()).thenReturn(moduleResultDao);
		when(context.metricResultDao()).thenReturn(metricResultDao);
		when(moduleResultDao.getResultsOfProcessing(processing.getId())).thenReturn(list(moduleResult));
		when(context.processing()).thenReturn(processing);
		when(context.configurationDao()).thenReturn(configurationDao);
		return context;
	}

	private void mockEntities() {
		Random random = new Random();
		processing = mock(Processing.class);
		moduleResult = mock(ModuleResult.class);
		configuration = mock(Configuration.class);
		when(processing.getId()).thenReturn(random.nextLong());
		when(moduleResult.getId()).thenReturn(random.nextLong());
		when(configuration.getId()).thenReturn(random.nextLong());
	}

	private void mockDaos() {
		metricResultDao = mock(MetricResultDatabaseDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		ConfigurationDatabaseDao configurationDao = mock(ConfigurationDatabaseDao.class);
		when(configurationDao.snapshotFor(processing.getId())).thenReturn(configuration);
	}

	@Test
	public void shouldSaveCompoundResults() throws Throwable {
		MetricResult compoundResult = mock(MetricResult.class);
		when(compoundCalculator.calculateCompoundResults()).thenReturn(list(compoundResult));
		when(metricResultDao.save(compoundResult, moduleResult.getId())).thenReturn(compoundResult);

		calculatingTask.perform();
		verify(moduleResult).addMetricResult(compoundResult);
		verify(metricResultDao).save(compoundResult, moduleResult.getId());
	}

	@Test
	public void shouldUpdateGradeAfterAddingCompoundResults() throws Throwable {
		MetricResult nativeResult1 = mockMetricResult(null, null);
		MetricResult nativeResult2 = mockMetricResult(10.0, 1.0);
		MetricResult compoundResult = mockMetricResult(7.0, 2.0);
		SortedSet<MetricResult> metricResults = sortedSet(nativeResult1, nativeResult2, compoundResult);
		when(compoundCalculator.calculateCompoundResults()).thenReturn(list(compoundResult));
		when(metricResultDao.save(compoundResult, moduleResult.getId())).thenReturn(compoundResult);
		when(moduleResult.getMetricResults()).thenReturn(metricResults);

		calculatingTask.perform();
		InOrder order = Mockito.inOrder(moduleResult, moduleResultDao);
		order.verify(moduleResult).addMetricResult(compoundResult);
		order.verify(moduleResult).getMetricResults();
		order.verify(moduleResult).setGrade(8.0);
		order.verify(moduleResultDao).save(moduleResult, processing.getId());
	}

	private MetricResult mockMetricResult(Double grade, Double weight) throws Exception {
		MetricResult metricResult = mock(MetricResult.class);
		when(metricResult.hasGrade()).thenReturn(grade != null);
		when(metricResult.getGrade()).thenReturn(grade);
		when(metricResult.getWeight()).thenReturn(weight);
		when(metricResult, Comparable.class.getMethods()[0]).withArguments(any()).thenReturn(1);
		return metricResult;
	}

	@Test
	public void shouldSetAsRoootIfHasNoParent() throws Throwable {
		when(moduleResult.hasParent()).thenReturn(false);

		calculatingTask.perform();
		verify(processing).setResultsRoot(moduleResult);
	}
}