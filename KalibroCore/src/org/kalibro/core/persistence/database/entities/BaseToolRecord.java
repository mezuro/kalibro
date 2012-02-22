package org.kalibro.core.persistence.database.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.*;

import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.util.DataTransferObject;

@Entity(name = "BaseTool")
public class BaseToolRecord implements DataTransferObject<BaseTool> {

	@Id
	@Column(name = "name", nullable = false)
	private String name;

	@Column
	private String description;

	@Column(nullable = false)
	private String collectorClass;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "origin")
	private Collection<NativeMetricRecord> supportedMetrics;

	public BaseToolRecord() {
		super();
	}

	public BaseToolRecord(String baseToolName) {
		name = baseToolName;
	}

	public BaseToolRecord(BaseTool baseTool) {
		name = baseTool.getName();
		description = baseTool.getDescription();
		collectorClass = baseTool.getCollectorClass().getCanonicalName();
		initializeSupportedMetrics(baseTool);
	}

	private void initializeSupportedMetrics(BaseTool baseTool) {
		supportedMetrics = new ArrayList<NativeMetricRecord>();
		for (NativeMetric supportedMetric : baseTool.getSupportedMetrics())
			supportedMetrics.add(new NativeMetricRecord(supportedMetric, this));
	}

	@Override
	public BaseTool convert() {
		BaseTool baseTool = new BaseTool(name);
		baseTool.setDescription(description);
		baseTool.setCollectorClassName(collectorClass);
		convertSupportedMetrics(baseTool);
		return baseTool;
	}

	private void convertSupportedMetrics(BaseTool baseTool) {
		Set<NativeMetric> metrics = new TreeSet<NativeMetric>();
		for (NativeMetricRecord supportedMetric : supportedMetrics)
			metrics.add(supportedMetric.convert());
		baseTool.setSupportedMetrics(metrics);
	}

	protected String getName() {
		return name;
	}
}