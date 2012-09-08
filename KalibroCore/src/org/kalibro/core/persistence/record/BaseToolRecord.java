package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.*;

import org.kalibro.KalibroError;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;

@Entity(name = "BaseTool")
@Table(name = "BASE_TOOL")
public class BaseToolRecord implements DataTransferObject<BaseTool> {

	@Id
	@Column(name = "name", nullable = false)
	private String name;

	@Column
	private String description;

	@Column(name = "collector_class", nullable = false, unique = true)
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
		convertCollectorClass(baseTool);
		convertSupportedMetrics(baseTool);
		return baseTool;
	}

	private void convertCollectorClass(BaseTool baseTool) {
		try {
			baseTool.setCollectorClass((Class<? extends MetricCollector>) Class.forName(collectorClass));
		} catch (ClassNotFoundException exception) {
			throw new KalibroError("Could not find collector class", exception);
		}
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