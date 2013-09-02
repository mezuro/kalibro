package org.kalibro.core.processing;

import java.util.Random;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Metric.class, MetricResult.class, ModuleResult.class, ModuleResultConfigurer.class})
public class ModuleResultConfigurerTest extends UnitTest {

	private static final Random RANDOM = new Random();
	private static final Long PROCESSING_ID = RANDOM.nextLong();
	private static final Long MODULE_RESULT_ID = RANDOM.nextLong();
	private static final Long CONFIGURATION_ID = RANDOM.nextLong();

	private Processing processing;
	private ModuleResult moduleResult;
	private Configuration configuration;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private CompoundResultCalculator compoundCalculator;

	private ModuleResultConfigurer configurer;

	@Before
	public void setUp() throws Exception {
		mockEntities();
		metricResultDao = mock(MetricResultDatabaseDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		compoundCalculator = mock(CompoundResultCalculator.class);
		whenNew(CompoundResultCalculator.class)
			.withArguments(moduleResult, configuration).thenReturn(compoundCalculator);

		configurer = new ModuleResultConfigurer(processing, configuration, metricResultDao, moduleResultDao);
	}

	private void mockEntities() {
		processing = mock(Processing.class);
		moduleResult = mock(ModuleResult.class);
		configuration = mock(Configuration.class);
		when(processing.getId()).thenReturn(PROCESSING_ID);
		when(moduleResult.getId()).thenReturn(MODULE_RESULT_ID);
		when(configuration.getId()).thenReturn(CONFIGURATION_ID);
	}

	@Test
	public void shouldSaveCompoundResults() {
		MetricResult compoundResult = mock(MetricResult.class);
		when(compoundCalculator.calculateCompoundResults()).thenReturn(list(compoundResult));
		when(metricResultDao.save(compoundResult, MODULE_RESULT_ID)).thenReturn(compoundResult);

		configurer.configure(moduleResult);
		verify(moduleResult).addMetricResult(compoundResult);
		verify(metricResultDao).save(compoundResult, MODULE_RESULT_ID);
	}

	@Test
	public void shouldUpdateGradeAfterAddingCompoundResults() throws Exception {
		MetricResult nativeResult1 = mockMetricResult(null, null);
		MetricResult nativeResult2 = mockMetricResult(10.0, 1.0);
		MetricResult compoundResult = mockMetricResult(7.0, 2.0);
		SortedSet<MetricResult> metricResults = sortedSet(nativeResult1, nativeResult2, compoundResult);
		when(compoundCalculator.calculateCompoundResults()).thenReturn(list(compoundResult));
		when(metricResultDao.save(compoundResult, MODULE_RESULT_ID)).thenReturn(compoundResult);
		when(moduleResult.getMetricResults()).thenReturn(metricResults);

		configurer.configure(moduleResult);
		InOrder order = Mockito.inOrder(moduleResult, moduleResultDao);
		order.verify(moduleResult).addMetricResult(compoundResult);
		order.verify(moduleResult).getMetricResults();
		order.verify(moduleResult).setGrade(8.0);
		order.verify(moduleResultDao).save(moduleResult, PROCESSING_ID);
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
	public void shouldSetAsRoootIfHasNoParent() {
		when(moduleResult.hasParent()).thenReturn(false);

		configurer.configure(moduleResult);
		verify(processing).setResultsRoot(moduleResult);
	}
}