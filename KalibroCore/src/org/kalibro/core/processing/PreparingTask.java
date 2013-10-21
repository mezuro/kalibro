package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.*;
import org.kalibro.core.Identifier;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.ConfigurationDatabaseDao;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ProcessingDatabaseDao;

class PreparingTask extends ProcessSubtask {

	PreparingTask(ProcessContext context) {
		super(context);
	}

	@Override
	protected void perform() throws Throwable {
		establishCodeDirectory();
		createResultProducer();
		prepareDatabase();
	}

	private void establishCodeDirectory() {
		Repository repository = context.repository;
		Project project = repository.getProject();
		File loadDirectory = KalibroSettings.load().getServerSettings().getLoadDirectory();
		File projectDirectory = newFile(loadDirectory, project.getName(), project.getId());
		File repositoryDirectory = newFile(projectDirectory, repository.getName(), repository.getId());
		context.codeDirectory = repositoryDirectory;
	}

	private File newFile(File parent, String name, Long id) {
		return new File(parent, Identifier.fromText(name).asClassName() + "-" + id);
	}

	private void createResultProducer() {
		context.resultProducer = new Producer<NativeModuleResult>();
	}

	private void prepareDatabase() {
		DatabaseDaoFactory daoFactory = new DatabaseDaoFactory();
		ProcessingDatabaseDao processingDao = daoFactory.createProcessingDao();
		Processing processing = processingDao.createProcessingFor(context.repository);
		ConfigurationDatabaseDao configurationDao = daoFactory.createConfigurationDao();
		context.processing = processing;
		context.processingDao = processingDao;
		context.moduleResultDao = daoFactory.createModuleResultDao();
		context.metricResultDao = daoFactory.createMetricResultDao();
		context.configuration = configurationDao.snapshotFor(processing.getId());
		validateConfiguration();
	}

	private void validateConfiguration() {
		Configuration configuration = context.repository.getConfiguration();
		String errorMessage = "Could not process repository '" + context.repository.getCompleteName() +
			"' because its configuration has no native metrics.";
		if (configuration.getNativeMetrics().isEmpty())
			throw new KalibroException(errorMessage);
	}
}