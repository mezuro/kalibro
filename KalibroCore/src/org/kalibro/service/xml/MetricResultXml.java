package org.kalibro.service.xml;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.CompoundMetric;
import org.kalibro.Metric;
import org.kalibro.MetricResult;
import org.kalibro.NativeMetric;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "metricResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricResultXml extends DataTransferObject<MetricResult> {

	private MetricXml<?> metric;

	private Double value;

	private Double weight;

	private RangeXmlRequest range;

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
			range = new RangeXmlRequest(metricResult.getRange());
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

	public List<Double> descendentResults() {
		// TODO Auto-generated method stub
		return null;
	}
}