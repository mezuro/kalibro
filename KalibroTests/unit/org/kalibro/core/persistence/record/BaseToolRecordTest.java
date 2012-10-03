package org.kalibro.core.persistence.record;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.MetricCollectorStub;
import org.kalibro.core.concurrent.VoidTask;

public class BaseToolRecordTest extends RecordTest<BaseTool> {

	@Override
	protected BaseTool loadFixture() {
		return new BaseTool(MetricCollectorStub.CLASS_NAME);
	}

	@Test
	public void shouldThrowErrorForCollectorClassNotFound() {
		final BaseToolRecord dto = new BaseToolRecord(loadFixture("inexistent", BaseTool.class));
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				dto.convert();
			}
		}).throwsError().withMessage("Could not find collector class").withCause(ClassNotFoundException.class);
	}
}