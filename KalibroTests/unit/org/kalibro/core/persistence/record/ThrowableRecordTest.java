package org.kalibro.core.persistence.record;

public class ThrowableRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("targetString", String.class).isRequired();
		assertColumn("message", String.class).isNullable();
		shouldHaveError("cause");
		assertOneToMany("stackTrace").cascades().isMappedBy("throwable");
	}
}