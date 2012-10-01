package org.kalibro;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

/**
 * A base tool to provide metric results for Kalibro.
 * 
 * @author Carlos Morais
 */
@SortingFields("name")
public class BaseTool extends AbstractEntity<BaseTool> {

	@IdentityField
	private String name;

	private String description;
	private Set<NativeMetric> supportedMetrics;

	private Class<? extends MetricCollector> collectorClass;

	public BaseTool(String name) {
		this(name, "");
	}

	public BaseTool(String name, String description) {
		setName(name);
		setDescription(description);
		supportedMetrics = new TreeSet<NativeMetric>();
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
		return supportedMetrics;
	}

	public void addSupportedMetric(NativeMetric supportedMetric) {
		supportedMetric.setOrigin(this);
		supportedMetrics.add(supportedMetric);
	}

	public void setSupportedMetrics(Collection<NativeMetric> supportedMetrics) {
		this.supportedMetrics = new TreeSet<NativeMetric>();
		for (NativeMetric supportedMetric : supportedMetrics)
			addSupportedMetric(supportedMetric);
	}

	public Class<? extends MetricCollector> getCollectorClass() {
		return collectorClass;
	}

	public void setCollectorClass(Class<? extends MetricCollector> collectorClass) {
		this.collectorClass = collectorClass;
	}

	public MetricCollector createMetricCollector() {
		try {
			return collectorClass.newInstance();
		} catch (Exception exception) {
			throw new KalibroException("Could not create metric collector of base tool '" + name + "'", exception);
		}
	}

	public static SortedSet<BaseTool> all() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCollectorClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}
}