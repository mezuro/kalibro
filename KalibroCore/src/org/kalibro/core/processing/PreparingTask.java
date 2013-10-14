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
		Repository repository = context.repository();
		Project project = repository.getProject();
		File loadDirectory = KalibroSettings.load().getServerSettings().getLoadDirectory();
		File projectDirectory = newFile(loadDirectory, project.getName(), project.getId());
		File repositoryDirectory = newFile(projectDirectory, repository.getName(), repository.getId());
		context.setCodeDirectory(repositoryDirectory);
	}

	private File newFile(File parent, String name, Long id) {
		return new File(parent, Identifier.fromText(name).asClassName() + "-" + id);
	}

	private void createResultProducer() {
		context.setProducer(new Producer<NativeModuleResult>());
	}

	private void prepareDatabase() {
		DatabaseDaoFactory daoFactory = new DatabaseDaoFactory();
		ProcessingDatabaseDao processingDao = daoFactory.createProcessingDao();
		Processing processing = processingDao.createProcessingFor(context.repository());
		ConfigurationDatabaseDao configurationDao = daoFactory.createConfigurationDao();
		context.setProcessing(processing);
		context.setProcessingDao(processingDao);
		context.setModuleResultDao(daoFactory.createModuleResultDao());
		context.setMetricResultDao(daoFactory.createMetricResultDao());
		context.setConfigurationSnapshot(configurationDao.snapshotFor(processing.getId()));
	}
}