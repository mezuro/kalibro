package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.*;
import org.kalibro.dto.DataTransferObject;

@Entity(name = "MetricResult")
@Table(name = "\"METRIC_RESULT\"")
@PrimaryKey(columns = {
	@Column(name = "project"),
	@Column(name = "date"),
	@Column(name = "moduleName"),
	@Column(name = "metricName")})
public class MetricResultRecord extends DataTransferObject<MetricResult> {

	public static List<MetricResultRecord> createRecords(ModuleResult moduleResult, RepositoryResult repositoryResult) {
		List<MetricResultRecord> records = new ArrayList<MetricResultRecord>();
		for (MetricResult metricResult : moduleResult.getMetricResults())
			if (!metricResult.getMetric().isCompound())
				records.add(new MetricResultRecord(metricResult, moduleResult.getModule(), repositoryResult));
		return records;
	}

	public static List<ModuleResult> convertIntoModuleResults(List<MetricResultRecord> metricResults) {
		List<ModuleResult> moduleResults = new ArrayList<ModuleResult>();
		for (MetricResultRecord metricResult : metricResults)
			getCurrentResult(metricResult, moduleResults).addMetricResult(metricResult.convert());
		return moduleResults;
	}

	private static ModuleResult getCurrentResult(MetricResultRecord metricResult, List<ModuleResult> results) {
		ModuleResult moduleResult = metricResult.module.convertIntoModuleResult();
		if (!results.contains(moduleResult))
			results.add(moduleResult);
		return results.get(results.size() - 1);
	}

	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name = "project", nullable = false, referencedColumnName = "project"),
		@JoinColumn(name = "date", nullable = false, referencedColumnName = "date"),
		@JoinColumn(name = "moduleName", nullable = false, referencedColumnName = "name")})
	private ModuleRecord module;

	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name = "metricName", nullable = false, referencedColumnName = "name"),
		@JoinColumn(name = "metricOrigin", nullable = false, referencedColumnName = "origin")})
	private NativeMetricRecord metric;

	@Column(nullable = false)
	private Long value;

	@OrderColumn
	@ElementCollection
	private List<Long> descendantResults;

	public MetricResultRecord() {
		super();
	}

	public MetricResultRecord(MetricResult metricResult) {
		metric = new NativeMetricRecord((NativeMetric) metricResult.getMetric());
		value = Double.doubleToLongBits(metricResult.getValue());
		initializedescendantResults(metricResult);
	}

	public MetricResultRecord(MetricResult metricResult, Module module, RepositoryResult repositoryResult) {
		this(metricResult);
		initializeModule(module, repositoryResult);
	}

	private void initializeModule(Module entity, RepositoryResult repositoryResult) {
		ModuleNode moduleNode = new ModuleNode(entity);
		module = new ModuleRecord(moduleNode, repositoryResult);
	}

	private void initializedescendantResults(MetricResult metricResult) {
		descendantResults = new ArrayList<Long>();
		for (Double result : metricResult.getdescendantResults())
			descendantResults.add(Double.doubleToLongBits(result));
	}

	@Override
	public MetricResult convert() {
		MetricResult metricResult = new MetricResult(metric.convert(), Double.longBitsToDouble(value));
		convertResults(metricResult);
		return metricResult;
	}

	private void convertResults(MetricResult metricResult) {
		for (Long result : descendantResults)
			metricResult.adddescendantResult(Double.longBitsToDouble(result));
	}
}