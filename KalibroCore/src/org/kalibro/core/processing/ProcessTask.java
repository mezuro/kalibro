package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.Configuration;
import org.kalibro.NativeModuleResult;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.DatabaseDaoFactory;

/**
 * Performs a {@link Processing} for a {@link Repository} according to its {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public class ProcessTask extends VoidTask {

	Processing processing;

	public ProcessTask(Repository repository) {
		processing = new DatabaseDaoFactory().createProcessingDao().createProcessingFor(repository);
	}

	@Override
	protected void perform() {
		File codeDirectory = new LoadSourceTask(processing).execute();
		Producer<NativeModuleResult> resultProducer = new Producer<NativeModuleResult>();
		new CollectMetricsTask(processing, codeDirectory, resultProducer).execute();
		new AnalyzeResultsTask(processing, resultProducer).execute();
	}
}