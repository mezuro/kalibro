package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.Configuration;
import org.kalibro.NativeModuleResult;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.core.persistence.ProcessingDatabaseDao;

/**
 * Context of a {@link Processing}. Contains common data used by {@link ProcessSubtask}s.
 * 
 * @author Carlos Morais
 */
class ProcessContext {

	private Repository repository;
	private Processing processing;
	private Configuration configuration;

	private ProcessingDatabaseDao processingDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;

	private File codeDirectory;
	private Producer<NativeModuleResult> resultProducer;

	ProcessContext(Repository repository) {
		this.repository = repository;
	}

	Repository repository() {
		return repository;
	}

	Processing processing() {
		return processing;
	}

	void setProcessing(Processing processing) {
		this.processing = processing;
	}

	Configuration configuration() {
		return configuration;
	}

	void setConfigurationSnapshot(Configuration configurationSnapshot) {
		this.configuration = configurationSnapshot;
	}

	ProcessingDatabaseDao processingDao() {
		return processingDao;
	}

	void setProcessingDao(ProcessingDatabaseDao processingDao) {
		this.processingDao = processingDao;
	}

	ModuleResultDatabaseDao moduleResultDao() {
		return moduleResultDao;
	}

	void setModuleResultDao(ModuleResultDatabaseDao moduleResultDao) {
		this.moduleResultDao = moduleResultDao;
	}

	MetricResultDatabaseDao metricResultDao() {
		return metricResultDao;
	}

	void setMetricResultDao(MetricResultDatabaseDao metricResultDao) {
		this.metricResultDao = metricResultDao;
	}

	File codeDirectory() {
		return codeDirectory;
	}

	void setCodeDirectory(File codeDirectory) {
		this.codeDirectory = codeDirectory;
	}

	Producer<NativeModuleResult> resultProducer() {
		return resultProducer;
	}

	void setProducer(Producer<NativeModuleResult> producer) {
		this.resultProducer = producer;
	}
}