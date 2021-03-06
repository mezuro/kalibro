package org.kalibro.core.persistence.record;

public class ThrowableRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("targetString", String.class).isRequired();
		assertColumn("message", String.class).isNullable();
		assertOneToOne("cause", ThrowableRecord.class).isEager().isOptional();
		assertOneToMany("stackTrace").isEager().isMappedBy("throwable");
	}
}