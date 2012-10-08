package org.kalibro;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class MetricResultTest extends UnitTest {

	private static final Double VALUE = new Random().nextDouble();

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
		assertSame(configuration, result.getConfiguration());
		assertTrue(result.getDescendentResults().isEmpty());
	}

	@Test
	public void checkCompoundMetricWithErrorConstruction() {
		CompoundMetric compoundMetric = mock(CompoundMetric.class);
		Throwable error = mock(Throwable.class);
		result = new MetricResult(compoundMetric, error);
		assertSame(compoundMetric, result.getMetric());
		assertDoubleEquals(Double.NaN, result.getValue());
		assertTrue(result.hasError());
		assertSame(error, result.getError());
		assertNull(result.getConfiguration());
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
	public void shouldGetGradeFromRange() {
		assertFalse(result.hasGrade());

		Double grade = mockGrade();
		assertTrue(result.hasGrade());
		assertSame(grade, result.getGrade());
	}

	private Double mockGrade() {
		Double grade = new Random().nextDouble();
		Range range = new Range(grade - 1.0, grade + 1.0);
		range.setReading(new Reading("label", grade, Color.MAGENTA));
		when(configuration.getRangeFor(VALUE)).thenReturn(range);
		return grade;
	}

	@Test
	public void shouldGetWeight() {
		Double weight = mock(Double.class);
		when(configuration.getWeight()).thenReturn(weight);
		assertSame(weight, result.getWeight());
	}
}