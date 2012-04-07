package org.kalibro.core.persistence.database.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.core.model.*;
import org.kalibro.core.util.DataTransferObject;

@Entity(name = "MetricResult")
@PrimaryKey(columns = {
	@Column(name = "projectName"),
	@Column(name = "date"),
	@Column(name = "moduleName"),
	@Column(name = "metricName")})
public class MetricResultRecord implements DataTransferObject<MetricResult> {

	public static List<MetricResultRecord> createRecords(ModuleResult moduleResult, String projectName) {
		Date date = moduleResult.getDate();
		List<MetricResultRecord> records = new ArrayList<MetricResultRecord>();
		for (MetricResult metricResult : moduleResult.getMetricResults())
			if (!metricResult.getMetric().isCompound())
				records.add(new MetricResultRecord(metricResult, moduleResult.getModule(), projectName, date));
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
		@JoinColumn(name = "projectName", nullable = false, referencedColumnName = "projectName"),
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
	private List<Long> descendentResults;

	public MetricResultRecord() {
		super();
	}

	public MetricResultRecord(MetricResult metricResult, Module module, String projectName, Date date) {
		initializeModule(module, projectName, date);
		metric = new NativeMetricRecord((NativeMetric) metricResult.getMetric());
		value = Double.doubleToLongBits(metricResult.getValue());
		initializeDescendentResults(metricResult);
	}

	private void initializeModule(Module entity, String projectName, Date date) {
		ModuleNode moduleNode = new ModuleNode(entity);
		module = new ModuleRecord(moduleNode, projectName, date);
	}

	private void initializeDescendentResults(MetricResult metricResult) {
		descendentResults = new ArrayList<Long>();
		for (Double result : metricResult.getDescendentResults())
			descendentResults.add(Double.doubleToLongBits(result));
	}

	@Override
	public MetricResult convert() {
		MetricResult metricResult = new MetricResult(metric.convert(), Double.longBitsToDouble(value));
		convertResults(metricResult);
		return metricResult;
	}

	private void convertResults(MetricResult metricResult) {
		for (Long result : descendentResults)
			metricResult.addDescendentResult(Double.longBitsToDouble(result));
	}
}