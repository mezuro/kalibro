package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.KalibroSettings;
import org.kalibro.NativeModuleResult;
import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.*;

class ProcessContext {

	private Repository repository;

	private File codeDirectory;
	private Producer<NativeModuleResult> resultProducer;

	private ProcessingDatabaseDao processingDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;
	private ConfigurationDatabaseDao configurationDao;

	ProcessContext(Repository repository) {
		this.repository = repository;
		establishCodeDirectory();
		createResultProducer();
		createDaos();

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

	private void createDaos() {
		DatabaseDaoFactory daoFactory = new DatabaseDaoFactory();
		processingDao = daoFactory.createProcessingDao();
		moduleResultDao = daoFactory.createModuleResultDao();
		metricResultDao = daoFactory.createMetricResultDao();
		configurationDao = daoFactory.createConfigurationDao();
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