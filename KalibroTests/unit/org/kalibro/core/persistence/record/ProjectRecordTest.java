package org.kalibro.core.persistence.record;

import static org.kalibro.ProjectFixtures.newHelloWorld;

import org.kalibro.KalibroException;
import org.kalibro.Project;

public class ProjectRecordTest extends RecordTest<Project> {

	@Override
	protected Project loadFixture() {
		Project fixture = newHelloWorld();
		fixture.setError(new KalibroException("ProjectRecordTest", new Exception()));
		return fixture;
	}
}