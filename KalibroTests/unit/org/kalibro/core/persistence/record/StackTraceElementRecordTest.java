package org.kalibro.core.persistence.record;

public class StackTraceElementRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("throwable", ThrowableRecord.class);
		shouldHaveId();
		assertColumn("declaringClass", String.class).isRequired();
		assertColumn("methodName", String.class).isRequired();
		assertColumn("fileName", String.class).isRequired();
		assertColumn("lineNumber", Integer.class).isRequired();
	}
}