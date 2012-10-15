package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.kalibro.*;
import org.kalibro.dto.DataTransferObject;

@Entity(name = "MetricConfiguration")
@Table(name = "\"METRIC_CONFIGURATION\"")
public class MetricConfigurationRecord extends DataTransferObject<MetricConfiguration> {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"configuration\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ConfigurationRecord configuration;

	@Column(name = "metricName", nullable = false)
	@SuppressWarnings("unused" /* used by JPA */)
	private String metricName;

//	@ManyToOne
//	@JoinColumns({
//		@JoinColumn(insertable = false, name = "metricName", referencedColumnName = "name", updatable = false),
//		@JoinColumn(name = "metricOrigin", referencedColumnName = "origin")})
//	private NativeMetricRecord nativeMetric;
//
//	@OneToOne(cascade = CascadeType.ALL, mappedBy = "metricConfiguration", orphanRemoval = true)
//	@JoinColumns({
//		@JoinColumn(insertable = false, name = "metricName", referencedColumnName = "name", updatable = false),
//		@JoinColumn(name = "configuration", referencedColumnName = "configuration")})
//	private CompoundMetricRecord compoundMetric;

	@Column(nullable = false)
	private String code;

	@Column(nullable = false)
	private Long weight;

	@Column(nullable = false)
	private String aggregationForm;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "metricConfiguration", orphanRemoval = true)
	private Collection<RangeRecord> ranges;

	public MetricConfigurationRecord() {
		super();
	}

	public MetricConfigurationRecord(MetricConfiguration metricConfiguration) {
		this(metricConfiguration, (Long) null);
	}

	public MetricConfigurationRecord(MetricConfiguration metricConfiguration, Long configurationId) {
		this(metricConfiguration, new ConfigurationRecord(configurationId));
	}

	public MetricConfigurationRecord(MetricConfiguration metricConfiguration, ConfigurationRecord configuration) {
		this.configuration = configuration;
		initializeMetric(metricConfiguration);
		id = metricConfiguration.getId();
		code = metricConfiguration.getCode();
		weight = Double.doubleToLongBits(metricConfiguration.getWeight());
		aggregationForm = metricConfiguration.getAggregationForm().name();
		initializeRanges(metricConfiguration);
	}

	public MetricConfigurationRecord(Long configurationId) {
		// TODO Auto-generated constructor stub
	}

	private void initializeMetric(MetricConfiguration metricConfiguration) {
		Metric metric = metricConfiguration.getMetric();
		metricName = metric.getName();
		if (metric.isCompound())
			compoundMetric = new CompoundMetricRecord((CompoundMetric) metric, this);
		else
			nativeMetric = new NativeMetricRecord((NativeMetric) metric);
	}

	private void initializeRanges(MetricConfiguration metricConfiguration) {
		ranges = new ArrayList<RangeRecord>();
		for (Range range : metricConfiguration.getRanges())
			ranges.add(new RangeRecord(range, this));
	}

	@Override
	public MetricConfiguration convert() {
		MetricConfiguration metricConfiguration = new MetricConfiguration();
		metricConfiguration.setCode(code);
		metricConfiguration.setWeight(Double.longBitsToDouble(weight));
		metricConfiguration.setAggregationForm(Statistic.valueOf(aggregationForm));
		convertRanges(metricConfiguration);
		return metricConfiguration;
	}

	private Metric convertMetric() {
		return (nativeMetric == null) ? compoundMetric.convert() : nativeMetric.convert();
	}

	private void convertRanges(MetricConfiguration metricConfiguration) {
		for (RangeRecord range : ranges)
			metricConfiguration.addRange(range.convert());
	}

	public Long id() {
		return id;
	}
}