package org.kalibro.core.persistence.record;

import java.util.Random;

import org.junit.Test;

public class DescendantResultRecordTest extends RecordTest {

	@Override
	protected Double loadFixture() {
		return new Random().nextDouble();
	}

	@Override
	public String entityName() {
		return "DescendantResult";
	}

	@Override
	protected void verifyColumns() {
		assertManyToOne("metricResult", MetricResultRecord.class).isRequired();
		shouldHaveId();
		assertColumn("value", Long.class).isRequired();
	}

	@Test
	public void shouldConvertProperly() {
		assertDoubleEquals(Double.NEGATIVE_INFINITY, new DescendantResultRecord(Double.NEGATIVE_INFINITY).convert());
		assertDoubleEquals(Double.NaN, new DescendantResultRecord(Double.NaN).convert());
		assertDoubleEquals(Double.POSITIVE_INFINITY, new DescendantResultRecord(Double.POSITIVE_INFINITY).convert());
	}
}