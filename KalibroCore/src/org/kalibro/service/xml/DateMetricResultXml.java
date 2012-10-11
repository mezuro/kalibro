package org.kalibro.service.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.MetricResult;

/**
 * XML element for mapping {@link MetricResult} to dates.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "dateMetricResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class DateMetricResultXml {

	@XmlElement
	private Date date;

	@XmlElement
	private MetricResultXml metricResult;

	public DateMetricResultXml() {
		super();
	}

	public DateMetricResultXml(Date date, MetricResult metricResult) {
		this.date = date;
		this.metricResult = new MetricResultXml(metricResult);
	}

	public Date date() {
		return date;
	}

	public MetricResult metricResult() {
		return metricResult.convert();
	}
}