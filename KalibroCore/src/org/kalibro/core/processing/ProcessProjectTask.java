package org.kalibro.core.processing;

import java.util.Collection;
import java.util.Map;

import org.kalibro.*;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.dao.DaoFactory;

public class ProcessProjectTask extends VoidTask {

	private Project project;

	public ProcessProjectTask(String projectName) {
		project = DaoFactory.getProjectDao().getProject(projectName);
	}

	@Override
	protected void perform() {
		try {
			processProject();
		} catch (Throwable error) {
			reportError(error);
		}
	}

	private void processProject() {
		RepositoryResult repositoryResult = new LoadSourceTask(project).executeSubTask();
		Map<Module, ModuleResult> resultMap = new CollectMetricsTask(repositoryResult).executeSubTask();
		Collection<ModuleResult> moduleResults = new AnalyzeResultsTask(repositoryResult, resultMap).executeSubTask();

		DaoFactory.getProjectResultDao().save(repositoryResult);
		saveModuleResults(moduleResults, repositoryResult);
		project.setState(ResultState.READY);
		DaoFactory.getProjectDao().save(project);
	}

	private void saveModuleResults(Collection<ModuleResult> moduleResults, RepositoryResult repositoryResult) {
		ModuleResultDatabaseDao moduleResultDao = (ModuleResultDatabaseDao) DaoFactory.getModuleResultDao();
		for (ModuleResult moduleResult : moduleResults)
			moduleResultDao.save(moduleResult, repositoryResult);
	}

	private void reportError(Throwable error) {
		project.setError(error);
		DaoFactory.getProjectDao().save(project);
	}
}