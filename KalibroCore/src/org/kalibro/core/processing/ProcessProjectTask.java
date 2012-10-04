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
		Processing processing = new LoadSourceTask(project).executeSubTask();
		Map<Module, ModuleResult> resultMap = new CollectMetricsTask(processing).executeSubTask();
		Collection<ModuleResult> moduleResults = new AnalyzeResultsTask(processing, resultMap).executeSubTask();

		DaoFactory.getProjectResultDao().save(processing);
		saveModuleResults(moduleResults, processing);
		project.setState(ProcessState.READY);
		DaoFactory.getProjectDao().save(project);
	}

	private void saveModuleResults(Collection<ModuleResult> moduleResults, Processing processing) {
		ModuleResultDatabaseDao moduleResultDao = (ModuleResultDatabaseDao) DaoFactory.getModuleResultDao();
		for (ModuleResult moduleResult : moduleResults)
			moduleResultDao.save(moduleResult, processing);
	}

	private void reportError(Throwable error) {
		project.setError(error);
		DaoFactory.getProjectDao().save(project);
	}
}