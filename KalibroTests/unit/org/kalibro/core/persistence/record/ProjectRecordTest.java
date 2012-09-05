package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.ConfigurationFixtures.kalibroConfiguration;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.kalibro.DtoTestCase;
import org.kalibro.KalibroException;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Project;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProjectRecord.class)
public class ProjectRecordTest extends DtoTestCase<Project, ProjectRecord> {

	@Before
	public void setUp() throws Exception {
		whenNew(Configuration.class).withNoArguments().thenReturn(kalibroConfiguration());
	}

	@Override
	protected ProjectRecord newDtoUsingDefaultConstructor() {
		return new ProjectRecord();
	}

	@Override
	protected Collection<Project> entitiesForTestingConversion() {
		Project normal = helloWorld();
		Project withError = newHelloWorld();
		withError.setError(new KalibroException("ProjectRecordTest", new Exception()));
		return Arrays.asList(normal, withError);
	}

	@Override
	protected ProjectRecord createDto(Project project) {
		return new ProjectRecord(project, 42L);
	}
}