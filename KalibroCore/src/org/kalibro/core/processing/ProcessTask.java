package org.kalibro.core.processing;

import java.io.File;
import java.util.Set;

import org.kalibro.NativeModuleResult;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.DatabaseDaoFactory;

public class ProcessTask extends VoidTask {

	private Processing processing;

	public ProcessTask(Repository repository) {
		processing = new DatabaseDaoFactory().createProcessingDao().createProcessingFor(repository);
	}

	@Override
	protected void perform() {
		File codeDirectory = new LoadSourceTask(processing).execute();
		Set<NativeModuleResult> results = new CollectMetricsTask(processing, codeDirectory).execute();
		new AnalyzeResultsTask(processing, results).execute();
	}
}