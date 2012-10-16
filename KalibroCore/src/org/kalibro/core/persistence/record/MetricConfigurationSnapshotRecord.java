package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.kalibro.*;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.MetricConfigurationDto;

/**
 * Java Persistence API entity for {@link MetricConfiguration} snapshots.
 * 
 * @author Carlos Morais
 */
@Entity(name = "MetricConfigurationSnapshot")
@Table(name = "\"METRIC_CONFIGURATION_SNAPSHOT\"")
public class MetricConfigurationSnapshotRecord extends MetricConfigurationDto {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"processing\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ProcessingRecord processing;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(name = "\"code\"", nullable = false)
	private String code;

	@Column(name = "\"weight\"", nullable = false)
	private Long weight;

	@Column(name = "\"aggregation_form\"", nullable = false)
	private String aggregationForm;

	@Column(name = "\"compound\"", nullable = false)
	private Boolean compound;

	@Column(name = "\"metric_name\"", nullable = false)
	private String metricName;

	@Column(name = "\"metric_scope\"", nullable = false)
	private String metricScope;

	@Column(name = "\"metric_description\"")
	private String metricDescription;

	@Column(name = "\"metric_origin\"", nullable = false)
	private String metricOrigin;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "configurationSnapshot", orphanRemoval = true)
	private Collection<RangeSnapshotRecord> ranges;

	public MetricConfigurationSnapshotRecord() {
		super();
	}

	public MetricConfigurationSnapshotRecord(Long id) {
		this.id = id;
	}

	public MetricConfigurationSnapshotRecord(MetricConfiguration metricConfiguration) {
		this(metricConfiguration, null);
	}

	public MetricConfigurationSnapshotRecord(MetricConfiguration metricConfiguration, Long processingId) {
		processing = new ProcessingRecord(processingId);
		code = metricConfiguration.getCode();
		weight = Double.doubleToLongBits(metricConfiguration.getWeight());
		aggregationForm = metricConfiguration.getAggregationForm().name();
		setMetric(metricConfiguration.getMetric(), metricConfiguration.getBaseTool());
		setRanges(metricConfiguration.getRanges());
	}

	private void setMetric(Metric metric, BaseTool baseTool) {
		compound = metric.isCompound();
		metricName = metric.getName();
		metricScope = metric.getScope().name();
		metricDescription = metric.getDescription();
		metricOrigin = compound ? ((CompoundMetric) metric).getScript() : baseTool.getName();
	}

	private void setRanges(Collection<Range> ranges) {
		this.ranges = new ArrayList<RangeSnapshotRecord>();
		for (Range range : ranges)
			this.ranges.add(new RangeSnapshotRecord(range, this));
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String code() {
		return code;
	}

	@Override
	public Double weight() {
		return Double.longBitsToDouble(weight);
	}

	@Override
	public Statistic aggregationForm() {
		return Statistic.valueOf(aggregationForm);
	}

	@Override
	public Metric metric() {
		return compound ? compoundMetric() : nativeMetric();
	}

	private CompoundMetric compoundMetric() {
		CompoundMetric metric = new CompoundMetric(metricName);
		metric.setScope(Granularity.valueOf(metricScope));
		metric.setDescription(metricDescription);
		metric.setScript(metricOrigin);
		return metric;
	}

	private NativeMetric nativeMetric() {
		return DaoFactory.getBaseToolDao().get(baseToolName()).getSupportedMetric(metricName);
	}

	@Override
	public String baseToolName() {
		return metricOrigin;
	}
}