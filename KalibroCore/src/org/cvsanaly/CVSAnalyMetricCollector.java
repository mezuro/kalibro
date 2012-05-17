package org.cvsanaly;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.cvsanaly.entities.MetricResult;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;


public class CVSAnalyMetricCollector implements MetricCollector {

	private static final String CVSANALY2_COMMAND_LINE = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d ";

	@Override
	public BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("CVSAnaly");
		baseTool.setCollectorClass(CVSAnalyMetricCollector.class);
		for (CVSAnalyMetric metric : CVSAnalyMetric.values())
			baseTool.addSupportedMetric(metric.getNativeMetric());
		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception {
		File tempFile = File.createTempFile("kalibro-cvsanaly-db", ".sqlite");
		CommandTask executor = new CommandTask(CVSANALY2_COMMAND_LINE + tempFile.getAbsolutePath(), codeDirectory);
		executor.executeAndWait();
		
		List<MetricResult> entites = getMetricResults(tempFile);
		
		tempFile.delete();
		return null;
	}
	
	private List<MetricResult> getMetricResults(File database) {
		Map<String, String> persistenceProperties = getPersistenceProperties(database);
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("cvsanaly", persistenceProperties);
		EntityManager entityManager =  factory.createEntityManager();
		
		List<MetricResult> result = entityManager.createNamedQuery("getAllMetricResults").getResultList();
		
		entityManager.close();
		return result;
	}
	
	private Map<String, String> getPersistenceProperties(File database) {
		Map<String, String> persistenceProperties = new HashMap<String, String>();
		persistenceProperties.put(DDL_GENERATION, NONE);
		persistenceProperties.put(JDBC_DRIVER, org.sqlite.JDBC.class.getName());
		persistenceProperties.put(JDBC_URL, "jdbc:sqlite:" + database.toURI().toString());
		return persistenceProperties;
	}

}
