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

	Repository repository;
	Processing processing;
	Configuration configuration;

	ProcessingDatabaseDao processingDao;
	ModuleResultDatabaseDao moduleResultDao;
	MetricResultDatabaseDao metricResultDao;

	File codeDirectory;
	Producer<NativeModuleResult> resultProducer;

	ProcessContext(Repository repository) {
		this.repository = repository;
	}
}