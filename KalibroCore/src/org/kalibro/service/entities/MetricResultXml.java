package org.kalibro.service.entities;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.NativeMetric;

@XmlRootElement(name = "MetricResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricResultXml extends DataTransferObject<MetricResult> {

	private MetricXml<?> metric;

	private Double value;

	private Double weight;

	private RangeXml range;

	@XmlElement(name = "descendentResult")
	private Collection<Double> descendentResults;

	public MetricResultXml() {
		super();
	}

	public MetricResultXml(MetricResult metricResult) {
		initializeMetric(metricResult);
		value = metricResult.getValue();
		weight = metricResult.getWeight();
		initializeRange(metricResult);
		descendentResults = metricResult.getDescendentResults();
	}

	private void initializeMetric(MetricResult metricResult) {
		Metric theMetric = metricResult.getMetric();
		if (theMetric.isCompound())
			metric = new CompoundMetricXml((CompoundMetric) theMetric);
		else
			metric = new NativeMetricXml((NativeMetric) theMetric);
	}

	private void initializeRange(MetricResult metricResult) {
		if (metricResult.hasRange())
			range = new RangeXml(metricResult.getRange());
	}

	@Override
	public MetricResult convert() {
		MetricResult metricResult = new MetricResult(metric.convert(), value);
		metricResult.setWeight(weight);
		convertRange(metricResult);
		if (descendentResults != null)
			metricResult.addDescendentResults(descendentResults);
		return metricResult;
	}

	private void convertRange(MetricResult metricResult) {
		if (range != null)
			metricResult.setRange(range.convert());
	}
}