package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.*;

/**
 * Context of a {@link Processing}. Contains common data used by {@link ProcessSubtask}s.
 * 
 * @author Carlos Morais
 */
class ProcessContext {

	private Repository repository;

	private File codeDirectory;
	private Producer<NativeModuleResult> resultProducer;

	private Processing processing;
	private ProcessingDatabaseDao processingDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;
	private ConfigurationDatabaseDao configurationDao;

	ProcessContext(Repository repository) {
		this.repository = repository;
		establishCodeDirectory();
		createResultProducer();
		createDaosAndProcessing();
	}

	private void establishCodeDirectory() {
		Project project = repository.getProject();
		File loadDirectory = KalibroSettings.load().getServerSettings().getLoadDirectory();
		File projectDirectory = new File(loadDirectory, project.getName() + "-" + project.getId());
		codeDirectory = new File(projectDirectory, repository.getName() + "-" + repository.getId());
	}

	private void createResultProducer() {
		this.resultProducer = new Producer<NativeModuleResult>();
	}

	private void createDaosAndProcessing() {
		DatabaseDaoFactory daoFactory = new DatabaseDaoFactory();
		processingDao = daoFactory.createProcessingDao();
		moduleResultDao = daoFactory.createModuleResultDao();
		metricResultDao = daoFactory.createMetricResultDao();
		configurationDao = daoFactory.createConfigurationDao();
		processing = processingDao.createProcessingFor(repository);
	}

	Repository repository() {
		return repository;
	}

	Processing processing() {
		return processing;
	}

	File codeDirectory() {
		return codeDirectory;
	}

	Producer<NativeModuleResult> resultProducer() {
		return resultProducer;
	}

	ProcessingDatabaseDao processingDao() {
		return processingDao;
	}

	ModuleResultDatabaseDao moduleResultDao() {
		return moduleResultDao;
	}

	MetricResultDatabaseDao metricResultDao() {
		return metricResultDao;
	}

	ConfigurationDatabaseDao configurationDao() {
		return configurationDao;
	}
}