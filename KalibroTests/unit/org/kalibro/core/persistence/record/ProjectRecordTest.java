package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.ProjectFixtures.newHelloWorld;

import org.kalibro.KalibroException;
import org.kalibro.core.model.Project;

public class ProjectRecordTest extends RecordTest<Project> {

	@Override
	protected Project loadFixture() {
		Project fixture = newHelloWorld();
		fixture.setError(new KalibroException("ProjectRecordTest", new Exception()));
		return fixture;
	}
}