package org.kalibro.core.persistence.record;

import static org.kalibro.ProjectResultFixtures.helloWorldResult;

import java.util.ArrayList;

import org.junit.Test;
import org.kalibro.Processing;
import org.kalibro.core.concurrent.VoidTask;
import org.powermock.reflect.Whitebox;

public class ProjectResultRecordTest extends RecordTest<Processing> {

	@Override
	protected Processing loadFixture() {
		return helloWorldResult();
	}

	@Test
	public void checkRootNotFoundError() {
		final ProjectResultRecord record = new ProjectResultRecord(helloWorldResult());
		Whitebox.setInternalState(record, "sourceTree", new ArrayList<ModuleRecord>());
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				record.convert();
			}
		}).throwsException().withMessage("No source tree root found in result for project: HelloWorld-1.0");
	}
}