package org.kalibro.core.persistence.record;

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
@Table(name = "\"metric_configuration\"")
public class MetricConfigurationRecord extends MetricConfigurationDto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"configuration\"", nullable = false)
	private Long configuration;

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

	@Column(name = "\"reading_group\"")
	private Long readingGroup;

	public MetricConfigurationRecord() {
		super();
	}

	public MetricConfigurationRecord(MetricConfiguration metricConfiguration) {
		this(metricConfiguration, null);
	}

	public MetricConfigurationRecord(MetricConfiguration entity, Long configurationId) {
		id = entity.getId();
		configuration = configurationId;
		code = entity.getCode();
		weight = Double.doubleToLongBits(entity.getWeight());
		aggregationForm = entity.getAggregationForm().name();
		setMetric(entity.getMetric(), entity.getBaseTool());
		readingGroup = entity.hasReadingGroup() ? entity.getReadingGroup().getId() : null;
	}

	private void setMetric(Metric metric, BaseTool baseTool) {
		compound = metric.isCompound();
		metricName = metric.getName();
		metricScope = metric.getScope().name();
		metricDescription = metric.getDescription();
		metricOrigin = compound ? ((CompoundMetric) metric).getScript() : baseTool.getName();
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

	@Override
	public Long readingGroupId() {
		return readingGroup;
	}
}