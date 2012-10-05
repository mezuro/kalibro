package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class MetricResultTest extends UnitTest {

	private static final Double VALUE = mock(Double.class);

	private Metric metric;
	private MetricConfiguration configuration;

	private MetricResult result;

	@Before
	public void setUp() {
		metric = mock(Metric.class);
		configuration = mock(MetricConfiguration.class);
		when(configuration.getMetric()).thenReturn(metric);
		result = new MetricResult(configuration, VALUE);
	}

	@Test
	public void checkNormalConstruction() {
		assertSame(configuration.getMetric(), result.getMetric());
		assertSame(VALUE, result.getValue());
		assertFalse(result.hasError());
		assertTrue(result.getDescendentResults().isEmpty());
	}

	@Test
	public void checkCompoundMetricWithErrorConstruction() {
		metric = mock(CompoundMetric.class);
		Throwable error = mock(Throwable.class);
		result = new MetricResult((CompoundMetric) metric, error);
		assertSame(metric, result.getMetric());
		assertDoubleEquals(Double.NaN, result.getValue());
		assertTrue(result.hasError());
		assertSame(error, result.getError());
		assertNull(result.getDescendentResults());
	}

	@Test
	public void shouldGetAggregatedValue() {
		assertSame(VALUE, result.getAggregatedValue());

		result = new MetricResult(configuration, Double.NaN);
		result.addDescendentResult(1.0);
		result.addDescendentResult(2.0);

		when(configuration.getAggregationForm()).thenReturn(Statistic.AVERAGE);
		assertDoubleEquals(1.5, result.getAggregatedValue());

		when(configuration.getAggregationForm()).thenReturn(Statistic.SUM);
		assertDoubleEquals(3.0, result.getAggregatedValue());
	}

	@Test
	public void shouldAnswerIfHasGrade() {
		assertFalse(result.hasGrade());
		mockGrade();
		assertTrue(result.hasGrade());
	}

	@Test
	public void shouldGetGrade() {
		Double grade = mockGrade();
		assertSame(grade, result.getGrade());
	}

	private Double mockGrade() {
		Range range = mock(Range.class);
		Double grade = mock(Double.class);
		Reading reading = mock(Reading.class);
		when(configuration.getRangeFor(VALUE)).thenReturn(range);
		when(range.getReading()).thenReturn(reading);
		when(reading.getGrade()).thenReturn(grade);
		return grade;
	}

	@Test
	public void shouldGetWeight() {
		Double weight = mock(Double.class);
		when(configuration.getWeight()).thenReturn(weight);
		assertSame(weight, result.getWeight());
	}
}