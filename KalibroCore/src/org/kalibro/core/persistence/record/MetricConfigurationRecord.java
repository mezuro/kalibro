package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.kalibro.*;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.MetricConfigurationDto;

/**
 * Java Persistence API entity for {@link MetricConfiguration}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "MetricConfiguration")
@Table(name = "\"METRIC_CONFIGURATION\"")
public class MetricConfigurationRecord extends MetricConfigurationDto {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"configuration\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ConfigurationRecord configuration;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"reading_group\"", referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ReadingGroupRecord readingGroup;

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "configuration", orphanRemoval = true)
	private Collection<RangeRecord> ranges;

	public MetricConfigurationRecord() {
		super();
	}

	public MetricConfigurationRecord(Long id) {
		this.id = id;
	}

	public MetricConfigurationRecord(MetricConfiguration metricConfiguration) {
		this(metricConfiguration, (Long) null);
	}

	public MetricConfigurationRecord(MetricConfiguration metricConfiguration, Long configurationId) {
		this(metricConfiguration, new ConfigurationRecord(configurationId));
	}

	public MetricConfigurationRecord(MetricConfiguration metricConfiguration, ConfigurationRecord configurationRecord) {
		this(metricConfiguration.getId());
		configuration = configurationRecord;
		code = metricConfiguration.getCode();
		weight = Double.doubleToLongBits(metricConfiguration.getWeight());
		aggregationForm = metricConfiguration.getAggregationForm().name();
		setReadingGroup(metricConfiguration.getReadingGroup());
		setMetric(metricConfiguration.getMetric(), metricConfiguration.getBaseTool());
		setRanges(metricConfiguration.getRanges());
	}

	private void setReadingGroup(ReadingGroup readingGroup) {
		if (readingGroup != null)
			this.readingGroup = new ReadingGroupRecord(readingGroup.getId());
	}

	private void setMetric(Metric metric, BaseTool baseTool) {
		compound = metric.isCompound();
		metricName = metric.getName();
		metricScope = metric.getScope().name();
		metricDescription = metric.getDescription();
		metricOrigin = compound ? ((CompoundMetric) metric).getScript() : baseTool.getName();
	}

	private void setRanges(Collection<Range> ranges) {
		this.ranges = new ArrayList<RangeRecord>();
		for (Range range : ranges)
			this.ranges.add(new RangeRecord(range, this));
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