package org.kalibro.core.model;

import java.util.*;

import org.kalibro.KalibroException;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;
import org.kalibro.core.processing.ScriptValidator;

@SortingMethods("getName")
public class Configuration extends AbstractEntity<Configuration> {

	@IdentityField
	private String name;

	private String description;
	private Map<Metric, MetricConfiguration> metricConfigurations;

	public Configuration() {
		setName("");
		setDescription("");
		metricConfigurations = new TreeMap<Metric, MetricConfiguration>();
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

	public Collection<MetricConfiguration> getMetricConfigurations() {
		return metricConfigurations.values();
	}

	public boolean contains(Metric metric) {
		return metricConfigurations.containsKey(metric);
	}

	public SortedSet<CompoundMetric> getCompoundMetrics() {
		SortedSet<CompoundMetric> compoundMetrics = new TreeSet<CompoundMetric>();
		for (Metric metric : metricConfigurations.keySet())
			if (metric.isCompound())
				compoundMetrics.add((CompoundMetric) metric);
		return compoundMetrics;
	}

	public Map<String, Set<NativeMetric>> getNativeMetrics() {
		Map<String, Set<NativeMetric>> nativeMetrics = new HashMap<String, Set<NativeMetric>>();
		for (Metric metric : metricConfigurations.keySet())
			if (!metric.isCompound())
				putNativeMetric((NativeMetric) metric, nativeMetrics);
		return nativeMetrics;
	}

	private void putNativeMetric(NativeMetric metric, Map<String, Set<NativeMetric>> nativeMetrics) {
		String origin = metric.getOrigin();
		if (!nativeMetrics.containsKey(origin))
			nativeMetrics.put(origin, new HashSet<NativeMetric>());
		nativeMetrics.get(origin).add(metric);
	}

	public MetricConfiguration getConfigurationFor(Metric metric) {
		if (!contains(metric))
			throw new KalibroException("No configuration found for metric: " + metric);
		return metricConfigurations.get(metric);
	}

	public void addMetricConfiguration(MetricConfiguration newMetricConfiguration) {
		for (MetricConfiguration metricConfiguration : metricConfigurations.values())
			metricConfiguration.assertNoConflictWith(newMetricConfiguration);
		new ScriptValidator(this).validateScriptOf(newMetricConfiguration);
		metricConfigurations.put(newMetricConfiguration.getMetric(), newMetricConfiguration);
	}

	public void replaceMetricConfiguration(Metric metric, MetricConfiguration newMetricConfiguration) {
		MetricConfiguration oldConfiguration = getConfigurationFor(metric);
		removeMetric(metric);
		try {
			addMetricConfiguration(newMetricConfiguration);
		} catch (KalibroException exception) {
			addMetricConfiguration(oldConfiguration);
			throw exception;
		}
	}

	public boolean removeMetric(Metric metric) {
		return metricConfigurations.remove(metric) != null;
	}
}