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
	private DatabaseDaoFactory daoFactory;
	private Producer<NativeModuleResult> resultProducer;

	ProcessContext(Repository repository) {
		this.repository = repository;
		this.daoFactory = new DatabaseDaoFactory();
		this.resultProducer = new Producer<NativeModuleResult>();
		establishCodeDirectory();

	}

	private void establishCodeDirectory() {
		Project project = repository.getProject();
		File loadDirectory = KalibroSettings.load().getServerSettings().getLoadDirectory();
		File projectDirectory = new File(loadDirectory, project.getName() + "-" + project.getId());
		codeDirectory = new File(projectDirectory, repository.getName() + "-" + repository.getId());
	}

	File codeDirectory() {
		return codeDirectory;
	}

	Producer<NativeModuleResult> resultProducer() {
		return resultProducer;
	}

	ConfigurationDatabaseDao createConfigurationDao() {
		return daoFactory.createConfigurationDao();
	}

	ProcessingDatabaseDao createProcessingDao() {
		return daoFactory.createProcessingDao();
	}

	ModuleResultDatabaseDao createModuleResultDao() {
		return daoFactory.createModuleResultDao();
	}

	MetricResultDatabaseDao createMetricResultDao() {
		return daoFactory.createMetricResultDao();
	}
}