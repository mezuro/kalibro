package org.kalibro.service.xml;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.*;
import org.kalibro.dto.MetricConfigurationDto;

/**
 * XML element for {@link MetricConfiguration} snapshots.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "metricConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricConfigurationSnapshotXml extends MetricConfigurationDto {

	@XmlElement
	private String code;

	@XmlElement
	private MetricXmlResponse metric;

	@XmlElement
	private Double weight;

	@XmlElement
	private Statistic aggregationForm;

	@XmlElement(name = "range")
	private List<RangeSnapshotXml> ranges;

	public MetricConfigurationSnapshotXml() {
		super();
	}

	public MetricConfigurationSnapshotXml(MetricConfiguration metricConfiguration) {
		code = metricConfiguration.getCode();
		metric = new MetricXmlResponse(metricConfiguration.getMetric());
		weight = metricConfiguration.getWeight();
		aggregationForm = metricConfiguration.getAggregationForm();
		ranges = createDtos(metricConfiguration.getRanges(), RangeSnapshotXml.class);
	}

	@Override
	public Long id() {
		return null;
	}

	@Override
	public String code() {
		return code;
	}

	@Override
	public Metric metric() {
		return metric.convert();
	}

	@Override
	public BaseTool baseTool() {
		return null;
	}

	@Override
	public Double weight() {
		return weight;
	}

	@Override
	public Statistic aggregationForm() {
		return aggregationForm;
	}

	@Override
	public ReadingGroup readingGroup() {
		return null;
	}

	@Override
	public SortedSet<Range> ranges() {
		return ranges == null ? new TreeSet<Range>() : toSortedSet(ranges);
	}
}