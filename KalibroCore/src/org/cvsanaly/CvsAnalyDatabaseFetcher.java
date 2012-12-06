package org.cvsanaly;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.cvsanaly.entities.MetricResult;

public class CvsAnalyDatabaseFetcher {

	private final File databasePath;

	CvsAnalyDatabaseFetcher(File databasePath) {
		this.databasePath = databasePath;
	}

	public List<MetricResult> getMetricResults() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("CVSAnalY", getPersistenceProperties());
		final EntityManager entityManager = factory.createEntityManager();

		List<MetricResult> result = entityManager.createNamedQuery("getLastMetricResults").getResultList();

		entityManager.close();
		return result;
	}

	public Map<String, String> getPersistenceProperties() {
		Map<String, String> persistenceProperties = new HashMap<String, String>();
		persistenceProperties.put(DDL_GENERATION, NONE);
		persistenceProperties.put(JDBC_DRIVER, org.sqlite.JDBC.class.getName());
		persistenceProperties.put(JDBC_URL, "jdbc:sqlite:" + databasePath.getAbsolutePath());
		return persistenceProperties;
	}
}