package org.kalibro.core.model;

import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;

@SortingMethods("getName")
public class BaseTool extends AbstractEntity<BaseTool> {

	@IdentityField
	private String name;

	private String description;
	private Set<NativeMetric> supportedMetrics;

	private Class<? extends MetricCollector> collectorClass;

	public BaseTool(String name) {
		setName(name);
		setDescription("");
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<NativeMetric> getSupportedMetrics() {
		if (supportedMetrics == null)
			setSupportedMetrics(createMetricCollector().getSupportedMetrics());
		return supportedMetrics;
	}

	public void setSupportedMetrics(Set<NativeMetric> supportedMetrics) {
		for (NativeMetric metric : supportedMetrics)
			metric.setOrigin(name);
		this.supportedMetrics = supportedMetrics;
	}

	public Class<? extends MetricCollector> getCollectorClass() {
		return collectorClass;
	}

	public void setCollectorClassName(String collectorClassName) {
		try {
			setCollectorClass((Class<? extends MetricCollector>) Class.forName(collectorClassName));
		} catch (ClassNotFoundException exception) {
			throw new RuntimeException("Could not find metric collector class of base tool '" + name + "'", exception);
		}
	}

	public void setCollectorClass(Class<? extends MetricCollector> collectorClass) {
		this.collectorClass = collectorClass;
	}

	public MetricCollector createMetricCollector() {
		try {
			return collectorClass.newInstance();
		} catch (Exception exception) {
			throw new RuntimeException("Could not create metric collector of base tool '" + name + "'", exception);
		}
	}
}