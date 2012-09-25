package org.kalibro.core.model;

import static org.kalibro.core.model.ProjectFixtures.PROJECT_NAME;

import org.kalibro.Granularity;
import org.kalibro.Module;

public final class ModuleFixtures {

	private static Module helloWorldClass = newHelloWorldClass();
	private static Module helloWorldApplication = newHelloWorldApplication();

	public static Module helloWorldClass() {
		return helloWorldClass;
	}

	public static Module newHelloWorldClass() {
		return new Module(Granularity.CLASS, "HelloWorld");
	}

	public static Module helloWorldApplication() {
		return helloWorldApplication;
	}

	public static Module newHelloWorldApplication() {
		return new Module(Granularity.SOFTWARE, PROJECT_NAME);
	}

	private ModuleFixtures() {
		return;
	}
}