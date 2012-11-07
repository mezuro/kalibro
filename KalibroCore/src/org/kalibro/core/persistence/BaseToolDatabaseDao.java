package org.kalibro.core.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.kalibro.BaseTool;
import org.kalibro.KalibroException;
import org.kalibro.MetricCollector;
import org.kalibro.core.Environment;
import org.kalibro.dao.BaseToolDao;

/**
 * Implementation for {@link BaseToolDao} that reads collector classes.
 * 
 * @author Carlos Morais
 */
class BaseToolDatabaseDao implements BaseToolDao {

	private Map<String, MetricCollector> collectors;

	BaseToolDatabaseDao() {
		try {
			createCollectors();
		} catch (IOException exception) {
			throw new KalibroException("Error creating collectors.", exception);
		}
	}

	private void createCollectors() throws IOException {
		collectors = new HashMap<String, MetricCollector>();
		InputStream collectorsStream = getClass().getResourceAsStream("/META-INF/collectors");
		for (Object collectorClassName : IOUtils.readLines(collectorsStream))
			addCollector(collectorClassName.toString());
	}

	private void addCollector(String collectorClassName) throws IOException {
		try {
			MetricCollector collector = (MetricCollector) Class.forName(collectorClassName).newInstance();
			collectors.put(collector.name(), collector);
		} catch (Exception exception) {
			logErrorLoadingCollector(collectorClassName, exception);
		}
	}

	private void logErrorLoadingCollector(String collectorClassName, Exception cause) throws IOException {
		Throwable error = new KalibroException("Could not load collector of class: " + collectorClassName, cause);
		error.printStackTrace(new PrintStream(new File(Environment.logsDirectory(), "collectors.log")));
	}

	@Override
	public SortedSet<String> allNames() {
		return new TreeSet<String>(collectors.keySet());
	}

	@Override
	public BaseTool get(String baseToolName) {
		return new BaseTool(collectors.get(baseToolName).getClass().getName());
	}
}