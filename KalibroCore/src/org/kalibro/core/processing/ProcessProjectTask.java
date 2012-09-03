package org.kalibro.core.processing;

import java.util.Collection;
import java.util.Map;

import org.kalibro.core.Kalibro;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

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
		Collection<ModuleResult> moduleResults = new AnalyzeResultsTask(projectResult, resultMap).execute();

		Kalibro.getProjectResultDao().save(projectResult);
		saveModuleResults(moduleResults, projectResult);
		project.setState(ProjectState.READY);
		Kalibro.getProjectDao().save(project);
	}

	private void saveModuleResults(Collection<ModuleResult> moduleResults, ProjectResult projectResult) {
		ModuleResultDatabaseDao moduleResultDao = (ModuleResultDatabaseDao) Kalibro.getModuleResultDao();
		for (ModuleResult moduleResult : moduleResults)
			moduleResultDao.save(moduleResult, projectResult);
	}

	private void reportError(Throwable error) {
		project.setError(error);
		Kalibro.getProjectDao().save(project);
	}
}