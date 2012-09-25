package org.kalibro.core.model;

import static org.kalibro.core.model.ConfigurationFixtures.CONFIGURATION_NAME;
import static org.kalibro.core.model.RepositoryFixtures.newHelloWorldRepository;

import org.kalibro.Project;
import org.kalibro.ProjectState;
import org.kalibro.RepositoryType;

public final class ProjectFixtures {

	public static final String PROJECT_NAME = "HelloWorld-1.0";

	private static Project helloWorld = newHelloWorld();

	public static Project helloWorld() {
		return helloWorld;
	}

	public static Project newHelloWorld() {
		Project project = new Project();
		project.setName(PROJECT_NAME);
		project.setLicense("Creative Commons");
		project.setRepository(newHelloWorldRepository(RepositoryType.LOCAL_DIRECTORY));
		project.setConfigurationName(CONFIGURATION_NAME);
		project.setState(ProjectState.READY);
		return project;
	}

	private ProjectFixtures() {
		return;
	}
}