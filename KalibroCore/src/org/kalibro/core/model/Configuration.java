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

	private SortedSet<CompoundMetric> compoundMetrics;
	private Map<String, Set<NativeMetric>> nativeMetrics;
	private Map<String, MetricConfiguration> metricConfigurations;

	public Configuration() {
		setName("");
		setDescription("");
		compoundMetrics = new TreeSet<CompoundMetric>();
		nativeMetrics = new TreeMap<String, Set<NativeMetric>>();
		metricConfigurations = new TreeMap<String, MetricConfiguration>();
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

	public boolean containsMetric(String metricName) {
		return metricConfigurations.containsKey(metricName);
	}

	public SortedSet<CompoundMetric> getCompoundMetrics() {
		return compoundMetrics;
	}

	public Map<String, Set<NativeMetric>> getNativeMetrics() {
		return nativeMetrics;
	}

	public MetricConfiguration getConfigurationFor(String metricName) {
		if (! containsMetric(metricName))
			throw new KalibroException("No configuration found for metric: " + metricName);
		return metricConfigurations.get(metricName);
	}

	public void addMetricConfiguration(MetricConfiguration metricConfiguration) {
		for (MetricConfiguration configuration : metricConfigurations.values())
			configuration.assertNoConflictWith(metricConfiguration);
		new ScriptValidator(this).validateScriptOf(metricConfiguration);
		Metric metric = metricConfiguration.getMetric();
		addMetric(metric);
		metricConfigurations.put(metric.getName(), metricConfiguration);
	}

	private void addMetric(Metric metric) {
		if (metric.isCompound())
			compoundMetrics.add((CompoundMetric) metric);
		else
			addNativeMetric((NativeMetric) metric);
	}

	private void addNativeMetric(NativeMetric metric) {
		String origin = metric.getOrigin();
		if (! nativeMetrics.containsKey(origin))
			nativeMetrics.put(origin, new TreeSet<NativeMetric>());
		nativeMetrics.get(origin).add(metric);
	}

	public void replaceMetricConfiguration(String metricName, MetricConfiguration newMetricConfiguration) {
		MetricConfiguration oldConfiguration = getConfigurationFor(metricName);
		removeMetric(metricName);
		try {
			addMetricConfiguration(newMetricConfiguration);
		} catch (KalibroException exception) {
			addMetricConfiguration(oldConfiguration);
			throw exception;
		}
	}

	public void removeMetric(String metricName) {
		removeMetric(getConfigurationFor(metricName).getMetric());
		metricConfigurations.remove(metricName);
	}

	private void removeMetric(Metric metric) {
		if (metric.isCompound())
			compoundMetrics.remove(metric);
		else
			removeNativeMetric((NativeMetric) metric);
	}

	private void removeNativeMetric(NativeMetric metric) {
		String origin = metric.getOrigin();
		nativeMetrics.get(origin).remove(metric);
		if (nativeMetrics.get(origin).isEmpty())
			nativeMetrics.remove(origin);
	}
}