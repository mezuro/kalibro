package org.kalibro.core.processing;

import java.util.Map;

import org.kalibro.Kalibro;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;

public class ProcessProjectTask extends Task {

	private Project project;

	public ProcessProjectTask(String projectName) {
		project = Kalibro.getProjectDao().getProject(projectName);
	}

	@Override
	public void perform() {
		try {
			processProject();
		} catch (Throwable error) {
			reportError(error);
		}
	}

	private void processProject() {
		ProjectResult projectResult = new LoadSourceTask(project).execute();
		Map<Module, ModuleResult> resultMap = new CollectMetricsTask(projectResult).execute();
		new AnalyzeResultsTask(projectResult, resultMap).execute();
		project.setState(ProjectState.READY);
		Kalibro.getProjectDao().save(project);
	}

	private void reportError(Throwable error) {
		project.setError(error);
		Kalibro.getProjectDao().save(project);
	}
}