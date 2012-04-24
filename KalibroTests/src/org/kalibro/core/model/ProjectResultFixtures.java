package org.kalibro.core.model;

import static org.kalibro.core.model.ModuleNodeFixtures.*;
import static org.kalibro.core.model.ProjectFixtures.*;

import java.util.Date;

import org.kalibro.core.model.enums.ProjectState;

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