package org.kalibro;

import static org.kalibro.ModuleNodeFixtures.newHelloWorldRoot;
import static org.kalibro.ProjectFixtures.newHelloWorld;

import java.util.Date;

public final class ProjectResultFixtures {

	private static RepositoryResult helloWorldResult = newHelloWorldResult();

	public static RepositoryResult helloWorldResult() {
		return helloWorldResult;
	}

	public static RepositoryResult newHelloWorldResult() {
		return newHelloWorldResult(new Date());
	}

	public static RepositoryResult newHelloWorldResult(Date date) {
		RepositoryResult result = new RepositoryResult(newHelloWorld());
		result.setDate(date);
		result.setStateTime(ProcessState.LOADING, 0);
		result.setStateTime(ProcessState.COLLECTING, 0);
		result.setStateTime(ProcessState.ANALYZING, 0);
		result.setSourceTree(newHelloWorldRoot());
		return result;
	}

	private ProjectResultFixtures() {
		return;
	}
}