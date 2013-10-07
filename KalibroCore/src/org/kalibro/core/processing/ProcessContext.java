package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.*;
import org.kalibro.core.Identifier;
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
	private Configuration configuration;
	private ProcessingDatabaseDao processingDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;

	ProcessContext(Repository repository) {
		this.repository = repository;
		establishCodeDirectory();
		createResultProducer();
		prepareDatabase();
	}

	private void establishCodeDirectory() {
		Project project = repository.getProject();
		File loadDirectory = KalibroSettings.load().getServerSettings().getLoadDirectory();
		File projectDirectory = newFile(loadDirectory, project.getName(), project.getId());
		codeDirectory = newFile(projectDirectory, repository.getName(), repository.getId());
	}

	private File newFile(File parent, String name, Long id) {
		return new File(parent, Identifier.fromText(name).asClassName() + "-" + id);
	}

	private void createResultProducer() {
		this.resultProducer = new Producer<NativeModuleResult>();
	}

	private void prepareDatabase() {
		DatabaseDaoFactory daoFactory = new DatabaseDaoFactory();
		ConfigurationDatabaseDao configurationDao = daoFactory.createConfigurationDao();
		processingDao = daoFactory.createProcessingDao();
		moduleResultDao = daoFactory.createModuleResultDao();
		metricResultDao = daoFactory.createMetricResultDao();

		processing = processingDao.createProcessingFor(repository);
		configuration = configurationDao.snapshotFor(processing.getId());
	}

	Repository repository() {
		return repository;
	}

	Processing processing() {
		return processing;
	}

	Configuration configuration() {
		return configuration;
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
}