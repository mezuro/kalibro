package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.BaseToolFixtures.analizoStub;

import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.model.BaseTool;
import org.powermock.reflect.Whitebox;

public class BaseToolRecordTest extends RecordTest<BaseTool> {

	@Override
	protected BaseTool loadFixture() {
		return analizoStub();
	}

	@Test
	public void shouldThrowErrorForCollectorClassNotFound() {
		final BaseToolRecord dto = new BaseToolRecord(analizoStub());
		Whitebox.setInternalState(dto, "collectorClass", "inexistent");
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				dto.convert();
			}
		}).throwsError().withMessage("Could not find collector class").withCause(ClassNotFoundException.class);
	}
}