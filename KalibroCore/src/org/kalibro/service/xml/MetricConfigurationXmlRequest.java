package org.kalibro.service.xml;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.*;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.dto.MetricConfigurationDto;

@XmlRootElement(name = "metricConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricConfigurationXmlRequest extends MetricConfigurationDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String code;

	@XmlElement(required = true)
	private MetricXmlRequest metric;

	@XmlElement
	private String baseToolName;

	@XmlElement(required = true)
	private Double weight;

	@XmlElement(required = true)
	private Statistic aggregationForm;

	@XmlElement
	private Long readingGroupId;

	@XmlElement(name = "range")
	private List<RangeXmlRequest> ranges;

	public MetricConfigurationXmlRequest() {
		super();
	}

	public MetricConfigurationXmlRequest(MetricConfiguration metricConfiguration) {
		id = metricConfiguration.getId();
		code = metricConfiguration.getCode();
		metric = new MetricXmlRequest(metricConfiguration.getMetric());
		if (!metric.compound())
			baseToolName = metricConfiguration.getBaseTool().getName();
		weight = metricConfiguration.getWeight();
		aggregationForm = metricConfiguration.getAggregationForm();
		setReadingGroup(metricConfiguration.getReadingGroup());
		ranges = createDtos(metricConfiguration.getRanges(), RangeXmlRequest.class);
	}

	private void setReadingGroup(ReadingGroup readingGroup) {
		if (readingGroup != null)
			readingGroupId = readingGroup.getId();
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
	public Metric metric() {
		return metric.convert();
	}

	@Override
	public String baseToolName() {
		return baseToolName;
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
		return readingGroupId == null ? null :
			(ReadingGroup) DaoLazyLoader.createProxy(ReadingGroupDao.class, "get", readingGroupId);
	}

	@Override
	public SortedSet<Range> ranges() {
		return ranges == null ? new TreeSet<Range>() : toSortedSet(ranges);
	}
}