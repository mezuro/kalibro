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
		result.setStateTime(ResultState.LOADING, 0);
		result.setStateTime(ResultState.COLLECTING, 0);
		result.setStateTime(ResultState.ANALYZING, 0);
		result.setSourceTree(newHelloWorldRoot());
		return result;
	}

	private ProjectResultFixtures() {
		return;
	}
}