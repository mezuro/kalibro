package org.kalibro.core.model.enums;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Statistic.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class StatisticTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() {
		assertEquals("Average", "" + AVERAGE);
		assertEquals("Maximum", "" + MAXIMUM);
		assertEquals("Median", "" + MEDIAN);
		assertEquals("Minimum", "" + MINIMUM);
		assertEquals("Standard deviation", "" + STANDARD_DEVIATION);
		assertEquals("Sum", "" + SUM);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCalculate() {
		testCalculate(AVERAGE, Double.NaN, 6.0, 5.0);
		testCalculate(MAXIMUM, Double.NEGATIVE_INFINITY, 10.0, 10.0);
		testCalculate(MEDIAN, Double.NaN, 6.0, 5.0);
		testCalculate(MINIMUM, Double.POSITIVE_INFINITY, 2.0, 0.0);
		testCalculate(STANDARD_DEVIATION, Double.NaN, Math.sqrt(8), Math.sqrt(70.0 / 6));
		testCalculate(SUM, 0.0, 30.0, 30.0);
	}

	private void testCalculate(Statistic statistic, Double resultEmpty, Double result2to10, Double result0to10) {
		assertDoubleEquals(resultEmpty, statistic.calculate(new ArrayList<Double>()));

		List<Double> values = new ArrayList<Double>(Arrays.asList(2.0, 4.0, 6.0, 8.0, 10.0));
		assertDoubleEquals(result2to10, statistic.calculate(values));

		values.add(0.0);
		assertDoubleEquals(result0to10, statistic.calculate(values));
	}
}