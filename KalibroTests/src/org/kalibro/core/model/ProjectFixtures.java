package org.kalibro.core.model;

import static org.kalibro.core.model.RepositoryFixtures.*;

import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.model.enums.RepositoryType;

public final class ProjectFixtures {

	public static Project helloWorld() {
		Project project = new Project();
		project.setName("HelloWorld-1.0");
		project.setLicense("Creative Commons");
		project.setRepository(helloWorldRepository(RepositoryType.LOCAL_DIRECTORY));
		project.setConfigurationName(ConfigurationFixtures.simpleConfiguration().getName());
		project.setState(ProjectState.READY);
		return project;
	}

	private ProjectFixtures() {
		// Utility class
	}
}