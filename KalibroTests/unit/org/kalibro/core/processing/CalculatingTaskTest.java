package org.kalibro.core.processing;

import java.util.Random;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.MetricResult;
import org.kalibro.ModuleResult;
import org.kalibro.Processing;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CalculatingTask.class, ModuleResult.class})
public class CalculatingTaskTest extends UnitTest {

	private Processing processing;
	private ModuleResult moduleResult;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private CompoundResultCalculator compoundCalculator;

	private CalculatingTask calculatingTask;

	@Before
	public void setUp() throws Exception {
		ProcessContext context = mock(ProcessContext.class);
		mockEntities(context);
		mockDatabaseDaos(context);
		mockCompoundCalculator(context);
		calculatingTask = new CalculatingTask(context);
	}

	private void mockEntities(ProcessContext context) {
		Random random = new Random();
		processing = mock(Processing.class);
		moduleResult = mock(ModuleResult.class);
		when(processing.getId()).thenReturn(random.nextLong());
		when(moduleResult.getId()).thenReturn(random.nextLong());
		when(context.processing()).thenReturn(processing);
	}

	private void mockDatabaseDaos(ProcessContext context) {
		metricResultDao = mock(MetricResultDatabaseDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		when(context.moduleResultDao()).thenReturn(moduleResultDao);
		when(context.metricResultDao()).thenReturn(metricResultDao);
		when(moduleResultDao.getResultsOfProcessing(processing.getId())).thenReturn(list(moduleResult));
	}

	private void mockCompoundCalculator(ProcessContext context) throws Exception {
		Configuration configuration = mock(Configuration.class);
		compoundCalculator = mock(CompoundResultCalculator.class);
		when(context.configuration()).thenReturn(configuration);
		whenNew(CompoundResultCalculator.class).withArguments(moduleResult, configuration)
			.thenReturn(compoundCalculator);
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