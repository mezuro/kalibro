package org.kalibro.core.processing;

import org.kalibro.Configuration;
import org.kalibro.NativeModuleResult;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.DatabaseDaoFactory;

/**
 * Performs a historic {@link Processing} for a {@link Repository} according to its {@link Configuration}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public class HistoricProcessTask extends ProcessTask {

	public HistoricProcessTask(Repository repository) {
		super(repository);
	}

	@Override
	public void perform() {
		HistoricLoadingTask historicLoadingTask = new HistoricLoadingTask();
		do {
			daoFactory = new DatabaseDaoFactory();
			processing = daoFactory.createProcessingDao().createProcessingFor(repository);
			resultProducer = new Producer<NativeModuleResult>();
			historicLoadingTask.prepare(this).execute();
			new CollectingTask().prepare(this).executeInBackground();
			ProcessSubtask analyzingTask = new AnalyzingTask().prepare(this);
			analyzingTask.execute();
		} while (! historicLoadingTask.finishedHistoricProcessing());
	}
}
