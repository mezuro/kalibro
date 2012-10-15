package org.kalibro;

import java.io.File;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;

/**
 * A base tool to provide metric results for Kalibro.
 * 
 * @author Carlos Morais
 */
@SortingFields("name")
public class BaseTool extends AbstractEntity<BaseTool> {

	public static SortedSet<String> allNames() {
		return dao().allNames();
	}

	public static BaseTool get(String baseToolName) {
		return dao().get(baseToolName);
	}

	private static BaseToolDao dao() {
		return DaoFactory.getBaseToolDao();
	}

	@IdentityField
	private String name;

	private String description;
	private String collectorClassName;
	private Set<NativeMetric> supportedMetrics;

	/** Should NOT be used. Only for frameworks. */
	protected BaseTool() {
		super();
	}

	public BaseTool(String name, String description, Set<NativeMetric> supportedMetrics, String collectorClassName) {
		this.name = name;
		this.description = description;
		this.supportedMetrics = supportedMetrics;
		this.collectorClassName = collectorClassName;
	}

	public BaseTool(String collectorClassName) {
		this.collectorClassName = collectorClassName;
		MetricCollector collector = createMetricCollector();
		name = collector.name();
		description = collector.description();
		supportedMetrics = collector.supportedMetrics();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public SortedSet<NativeMetric> getSupportedMetrics() {
		return new TreeSet<NativeMetric>(supportedMetrics);
	}

	public NativeMetric getSupportedMetric(String metricName) {
		for (NativeMetric metric : supportedMetrics)
			if (metric.getName().equals(metricName))
				return metric;
		throw new KalibroException("Base tool '" + name + "' does not support metric: " + metricName);
	}

	public String getCollectorClassName() {
		return collectorClassName;
	}

	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> wantedMetrics)
		throws Exception {
		return createMetricCollector().collectMetrics(codeDirectory, wantedMetrics);
	}

	private MetricCollector createMetricCollector() {
		try {
			return (MetricCollector) Class.forName(collectorClassName).newInstance();
		} catch (Exception exception) {
			throw new KalibroException("Could not create metric collector: " + collectorClassName, exception);
		}
	}

	@Override
	public String toString() {
		return name;
	}
}