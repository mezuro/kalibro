package org.kalibro.core.persistence.record;

public class ThrowableRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("throwableClass", String.class).isRequired();
		assertColumn("detailMessage", String.class).isNullable();
		assertOneToMany("stackTrace").cascades().isMappedBy("throwable");
		shouldHaveError("cause");
	}
}