package org.kalibro.core.processing;

import java.util.Collection;
import java.util.Map;

import org.kalibro.*;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.dao.DaoFactory;

public class ProcessTask extends VoidTask {

	private Processing processing;

	public ProcessTask(Repository repository) {
		processing = new DatabaseDaoFactory().createProcessingDao().createProcessingFor(repository);
	}

	@Override
	protected void perform() {
		try {
			process();
		} catch (Throwable error) {
			processing.setError(error);
			saveProcessing();
		}
	}

	private void process() {
		saveProcessing();
		Processing processing = new LoadSourceTask(project).executeSubTask();
		Map<Module, ModuleResult> resultMap = new CollectMetricsTask(processing).executeSubTask();
		Collection<ModuleResult> moduleResults = new AnalyzeResultsTask(processing, resultMap).executeSubTask();

		DaoFactory.getProjectResultDao().save(processing);
		saveModuleResults(moduleResults, processing);
		project.setState(ProcessState.READY);
		DaoFactory.getProjectDao().save(project);
	}

	private void saveProcessing() {
		.save(processing);
	}
}