package org.kalibro;

import java.io.File;
import java.util.*;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.Print;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.core.processing.ScriptValidator;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;

/**
 * A configuration is a set of {@link MetricConfiguration} used to evaluate and compare source code bases.
 * 
 * @author Carlos Morais
 */
@SortingFields("name")
public class Configuration extends AbstractEntity<Configuration> {

	public static Configuration importFrom(File file) {
		return importFrom(file, Configuration.class);
	}

	public static SortedSet<Configuration> all() {
		return dao().all();
	}

	private static ConfigurationDao dao() {
		return DaoFactory.getConfigurationDao();
	}

	@Print(skip = true)
	private Long id;

	@IdentityField
	private String name;

	private String description;
	private Set<MetricConfiguration> metricConfigurations;

	public Configuration() {
		this("");
	}

	public Configuration(String name) {
		setId(null);
		setName(name);
		setDescription("");
		setMetricConfigurations(new TreeSet<MetricConfiguration>());
	}

	public Long getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
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

	public SortedSet<MetricConfiguration> getMetricConfigurations() {
		for (MetricConfiguration each : metricConfigurations)
			each.setConfiguration(this);
		return new TreeSet<MetricConfiguration>(metricConfigurations);
	}

	public void setMetricConfigurations(SortedSet<MetricConfiguration> metricConfigurations) {
		this.metricConfigurations = metricConfigurations;
	}

	public void addMetricConfiguration(MetricConfiguration metricConfiguration) {
		for (MetricConfiguration each : metricConfigurations)
			metricConfiguration.assertNoConflictWith(each);
		metricConfiguration.setConfiguration(this);
		metricConfigurations.add(metricConfiguration);
	}

	public void removeMetricConfiguration(MetricConfiguration metricConfiguration) {
		metricConfigurations.remove(metricConfiguration);
		metricConfiguration.setConfiguration(null);
	}

	public void validateScripts() {
		ScriptValidator.validate(this);
	}

	public SortedSet<CompoundMetric> getCompoundMetrics() {
		SortedSet<CompoundMetric> compoundMetrics = new TreeSet<CompoundMetric>();
		for (MetricConfiguration each : metricConfigurations)
			if (each.getMetric().isCompound())
				compoundMetrics.add((CompoundMetric) each.getMetric());
		return compoundMetrics;
	}

	public Map<String, Set<NativeMetric>> getNativeMetrics() {
		Map<String, Set<NativeMetric>> nativeMetrics = new HashMap<String, Set<NativeMetric>>();
		for (MetricConfiguration each : metricConfigurations)
			if (!each.getMetric().isCompound())
				addNativeMetricTo(nativeMetrics, (NativeMetric) each.getMetric());
		return nativeMetrics;
	}

	private void addNativeMetricTo(Map<String, Set<NativeMetric>> nativeMetrics, NativeMetric nativeMetric) {
		String origin = nativeMetric.getOrigin();
		if (!nativeMetrics.containsKey(origin))
			nativeMetrics.put(origin, new HashSet<NativeMetric>());
		nativeMetrics.get(origin).add(nativeMetric);
	}

	public boolean containsMetric(Metric metric) {
		return findConfigurationFor(metric) != null;
	}

	public MetricConfiguration getConfigurationFor(Metric metric) {
		MetricConfiguration metricConfiguration = findConfigurationFor(metric);
		if (metricConfiguration == null)
			throw new KalibroException("No configuration found for metric: " + metric);
		return metricConfiguration;
	}

	private MetricConfiguration findConfigurationFor(Metric metric) {
		for (MetricConfiguration metricConfiguration : metricConfigurations)
			if (metricConfiguration.getMetric().equals(metric))
				return metricConfiguration;
		return null;
	}

	public void save() {
		if (name.trim().isEmpty())
			throw new KalibroException("Configuration requires name.");
		id = dao().save(this);
		metricConfigurations = DaoFactory.getMetricConfigurationDao().metricConfigurationsOf(id);
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		for (MetricConfiguration metricConfiguration : metricConfigurations)
			metricConfiguration.setId(null);
		id = null;
	}

	@Override
	public String toString() {
		return name;
	}
}