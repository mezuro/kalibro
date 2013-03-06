package org.kalibro.core.processing;

import java.awt.Color;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.tests.UnitTest;

public class ModuleResultConfigurerTest extends UnitTest {

	@Test
	public void shouldUpdateGrade() {
		assertDoubleEquals(Double.NaN, result.getGrade());
		assertDoubleEquals(10.0, configurer.calculateGrade());

		MetricConfiguration cboConfiguration = configuration.getConfigurationFor(cbo);
		cboConfiguration.addRange(rangeThatGradesCboWith(7.0));
		cboConfiguration.setWeight(2.0);
		assertDoubleEquals(8.0, configurer.calculateGrade());
	}

	private Range rangeThatGradesCboWith(Double grade) {
		Range range = new Range(CBO, CBO + 1.0);
		range.setReading(new Reading("name", grade, Color.WHITE));
		return range;
	}
}