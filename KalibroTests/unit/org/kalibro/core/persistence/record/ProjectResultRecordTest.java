package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ProjectResultFixtures.helloWorldResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.ProjectResult;
import org.powermock.reflect.Whitebox;

public class ProjectResultRecordTest extends DtoTestCase<ProjectResult, ProjectResultRecord> {

	@Override
	protected ProjectResultRecord newDtoUsingDefaultConstructor() {
		return new ProjectResultRecord();
	}

	@Override
	protected Collection<ProjectResult> entitiesForTestingConversion() {
		return Arrays.asList(helloWorldResult());
	}

	@Override
	protected void assertCorrectConversion(ProjectResult original, ProjectResult converted) {
		original.getProject().setConfigurationName("");
		super.assertCorrectConversion(original, converted);
	}

	@Override
	protected ProjectResultRecord createDto(ProjectResult projectResult) {
		return new ProjectResultRecord(projectResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkRootNotFoundError() {
		final ProjectResultRecord record = createDto(helloWorldResult());
		Whitebox.setInternalState(record, "sourceTree", new ArrayList<ModuleRecord>());
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				record.convert();
			}
		}, "No source tree root found in result for project: HelloWorld-1.0");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveDate() {
		ProjectResult result = helloWorldResult();
		ProjectResultRecord record = createDto(result);
		assertEquals(result.getDate(), record.getDate());
	}
}