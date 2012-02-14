package org.kalibro.core.model;

import static org.kalibro.core.model.ModuleNodeFixtures.*;

import java.util.Date;

public class ProjectResultFixtures {

	public static ProjectResult helloWorldResult() {
		return helloWorldResult(new Date());
	}

	public static ProjectResult helloWorldResult(Date date) {
		ProjectResult result = new ProjectResult(ProjectFixtures.helloWorld());
		result.setDate(date);
		result.setSourceTree(helloWorldTree());
		return result;
	}
}