package org.kalibro.core.model;

import java.util.*;

import org.kalibro.KalibroException;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.Ignore;
import org.kalibro.core.abstractentity.SortingMethods;
import org.kalibro.core.processing.ScriptValidator;

@SortingMethods("getName")
public class Configuration extends AbstractEntity<Configuration> {

	private Long id;

	@IdentityField
	private String name;

	private String description;

	private SortedSet<CompoundMetric> compoundMetrics;
	private Map<String, Set<NativeMetric>> nativeMetrics;
	private Map<String, MetricConfiguration> metricConfigurations;

	@Ignore
	private ScriptValidator validator;

	public Configuration() {
		setId(null);
		setName("");
		setDescription("");
		compoundMetrics = new TreeSet<CompoundMetric>();
		nativeMetrics = new TreeMap<String, Set<NativeMetric>>();
		metricConfigurations = new TreeMap<String, MetricConfiguration>();
		validator = new ScriptValidator();
	}

	@Override
	public String toString() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		if (!containsMetric(metricName))
			throw new KalibroException("No configuration found for metric: " + metricName);
		return metricConfigurations.get(metricName);
	}

	public void addMetricConfiguration(MetricConfiguration metricConfiguration) {
		for (MetricConfiguration configuration : metricConfigurations.values())
			configuration.assertNoConflictWith(metricConfiguration);
		validator.add(metricConfiguration);
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
		if (!nativeMetrics.containsKey(origin))
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
		MetricConfiguration metricConfiguration = getConfigurationFor(metricName);
		validator.remove(metricConfiguration);
		removeMetric(metricConfiguration.getMetric());
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