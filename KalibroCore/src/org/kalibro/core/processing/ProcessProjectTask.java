package org.kalibro.core.processing;

import java.util.Collection;
import java.util.Map;

import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.dao.DaoFactory;

public class ProcessProjectTask extends VoidTask {

	private Project project;

	public ProcessProjectTask(String projectName) {
		project = DaoFactory.getProjectDao().getProject(projectName);
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

		DaoFactory.getProjectResultDao().save(projectResult);
		saveModuleResults(moduleResults, projectResult);
		project.setState(ProjectState.READY);
		DaoFactory.getProjectDao().save(project);
	}

	private void saveModuleResults(Collection<ModuleResult> moduleResults, ProjectResult projectResult) {
		ModuleResultDatabaseDao moduleResultDao = (ModuleResultDatabaseDao) DaoFactory.getModuleResultDao();
		for (ModuleResult moduleResult : moduleResults)
			moduleResultDao.save(moduleResult, projectResult);
	}

	private void reportError(Throwable error) {
		project.setError(error);
		DaoFactory.getProjectDao().save(project);
	}
}