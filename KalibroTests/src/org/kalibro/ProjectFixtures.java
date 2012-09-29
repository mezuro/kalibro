package org.kalibro;

import static org.kalibro.ConfigurationFixtures.CONFIGURATION_NAME;
import static org.kalibro.RepositoryFixtures.newHelloWorldRepository;

import java.util.Arrays;

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
		project.setMailsToNotify(Arrays.asList("aaa@example.com", "bbb@example.com"));
		project.setConfigurationName(CONFIGURATION_NAME);
		project.setState(ProjectState.READY);
		return project;
	}

	private ProjectFixtures() {
		return;
	}
}