package org.kalibro.core.persistence.record;

import javax.persistence.Id;

import org.junit.Test;

public class StackTraceElementRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		verifyComposedId();
		assertManyToOne("throwable", ThrowableRecord.class).isEager().isRequired();
		assertColumn("index", Integer.class).isRequired();
		assertColumn("declaringClass", String.class).isRequired();
		assertColumn("methodName", String.class).isRequired();
		assertColumn("fileName", String.class).isNullable();
		assertColumn("lineNumber", Integer.class).isNullable();
	}

	private void verifyComposedId() {
		annotation("throwable", Id.class);
		annotation("index", Id.class);
	}

	@Test
	public void shouldAddElementToStackTrace() {
		StackTraceElement[] stackTrace = new StackTraceElement[1];
		((StackTraceElementRecord) dto).addTo(stackTrace);
		assertDeepEquals(entity, stackTrace[0]);
	}
}