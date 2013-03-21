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
		shouldHaveId();
		assertColumn("moduleResult", Long.class).isRequired();
		assertColumn("configuration", Long.class).isRequired();
		assertColumn("value", Long.class).isRequired();
	}

	@Test
	public void shouldConvertSpecialValuesProperly() {
		assertDoubleEquals(Double.NEGATIVE_INFINITY, new DescendantResultRecord(Double.NEGATIVE_INFINITY).convert());
		assertDoubleEquals(Double.NaN, new DescendantResultRecord(Double.NaN).convert());
		assertDoubleEquals(Double.POSITIVE_INFINITY, new DescendantResultRecord(Double.POSITIVE_INFINITY).convert());
	}
}