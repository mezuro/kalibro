package org.kalibro.spago;

import it.eng.spago4q.bo.ProjectDetail;
import it.eng.spago4q.extractors.bo.GenericItem;
import it.eng.spago4q.extractors.bo.GenericItemInterface;
import it.eng.spago4q.extractors.qualipso.AbstractQualipsoExtractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.core.util.StackTracePrinter;

public class SpagoExtractor extends AbstractQualipsoExtractor {

	private KalibroClientForSpago client;

	@Override
	protected void setUp() {
		return;
	}

	@Override
	protected void tearDown() {
		return;
	}

	@Override
	protected List<GenericItemInterface> extract() {
		try {
			return doExtract();
		} catch (Exception exception) {
			return Arrays.asList(getErrorItem(exception));
		}
	}

	private GenericItemInterface getErrorItem(Exception exception) {
		GenericItem genericItem = new GenericItem();
		genericItem.setValue("Resource", "Extraction failed.");
		genericItem.setValue("Metric", new StackTracePrinter(1000).printStackTrace(exception));
		genericItem.setValue("Value", "0.0");
		return genericItem;
	}

	private List<GenericItemInterface> doExtract() throws Exception {
		String serviceAddress = readOperationParameterValue("kalibro_endpoint");
		client = new KalibroClientForSpago(serviceAddress);
		List<GenericItemInterface> items = new ArrayList<GenericItemInterface>();
		for (Object projectDetail : getProjectList())
			items.addAll(extract((ProjectDetail) projectDetail));
		return items;
	}

	private List<GenericItem> extract(ProjectDetail projectDetail) throws Exception {
		SpagoRequestParser parser = new SpagoRequestParser(projectDetail.getDetail());
		if (!parser.shouldIncludeProject())
			return new ArrayList<GenericItem>();
		return extractMetrics(parser.getProject());
	}

	private List<GenericItem> extractMetrics(Project project) {
		List<GenericItem> items = new ArrayList<GenericItem>();
		String projectName = project.getName();
		if (client.hasResultsFor(projectName)) {
			ModuleResult moduleResult = client.getLastApplicationResult(projectName);
			for (MetricResult metricResult : moduleResult.getMetricResults())
				items.add(extractMetricResult("" + project, metricResult));
		}
		client.saveAndProcess(project);
		return items;
	}

	private GenericItem extractMetricResult(String projectName, MetricResult metricResult) {
		GenericItem genericItem = new GenericItem();
		genericItem.setValue("Resource", projectName);
		genericItem.setValue("Metric", metricResult.getMetric().getName());
		genericItem.setValue("Value", "" + extractResult(metricResult));
		return genericItem;
	}

	private Double extractResult(MetricResult metricResult) {
		Double value = metricResult.getValue();
		return value.isNaN() ? metricResult.getStatistic(Statistic.AVERAGE) : value;
	}
}