package org.kalibro;

import static org.kalibro.ModuleNodeFixtures.newHelloWorldRoot;
import static org.kalibro.ProjectFixtures.newHelloWorld;

import java.util.Date;

public final class ProjectResultFixtures {

	private static Processing helloWorldResult = newHelloWorldResult();

	public static Processing helloWorldResult() {
		return helloWorldResult;
	}

	public static Processing newHelloWorldResult() {
		return newHelloWorldResult(new Date());
	}

	public static Processing newHelloWorldResult(Date date) {
		Processing result = new Processing(newHelloWorld());
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