package org.kalibro;

import static org.kalibro.ModuleNodeFixtures.newHelloWorldRoot;
import static org.kalibro.ProjectFixtures.newHelloWorld;

import java.util.Date;

public final class ProjectResultFixtures {

	private static ProjectResult helloWorldResult = newHelloWorldResult();

	public static ProjectResult helloWorldResult() {
		return helloWorldResult;
	}

	public static ProjectResult newHelloWorldResult() {
		return newHelloWorldResult(new Date());
	}

	public static ProjectResult newHelloWorldResult(Date date) {
		ProjectResult result = new ProjectResult(newHelloWorld());
		result.setDate(date);
		result.setStateTime(ProjectState.LOADING, 0);
		result.setStateTime(ProjectState.COLLECTING, 0);
		result.setStateTime(ProjectState.ANALYZING, 0);
		result.setSourceTree(newHelloWorldRoot());
		return result;
	}

	private ProjectResultFixtures() {
		return;
	}
}