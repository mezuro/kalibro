package org.kalibro.core.persistence;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.kalibro.MetricResult;
import org.kalibro.ModuleResult;
import org.kalibro.Statistic;

public class ModuleResultCsvExporter {

	private ModuleResult moduleResult;
	private StringBuffer csvContents;

	public ModuleResultCsvExporter(ModuleResult moduleResult) {
		this.moduleResult = moduleResult;
		createCsvContents();
	}

	public void exportTo(File file) throws IOException {
		FileUtils.writeStringToFile(file, csvContents.toString());
	}

	private void createCsvContents() {
		csvContents = new StringBuffer();
		appendTitles();
		for (MetricResult metricResult : moduleResult.getMetricResults())
			appendResult(metricResult);
	}

	private void appendTitles() {
		appendString("Metric");
		appendString("Value");
		appendString("Range label");
		appendString("Grade");
		appendString("Sample size");
		for (Statistic statistic : Statistic.values())
			appendString(statistic.toString());
		finishLine();
	}

	private void appendResult(MetricResult metricResult) {
		appendString(metricResult.getMetric().getName());
		appendDouble(metricResult.getValue());
		appendString(metricResult.hasRange() ? metricResult.getRange().getLabel() : "");
		appendDouble(metricResult.hasRange() ? metricResult.getGrade() : Double.NaN);
		appendInteger(metricResult.getDescendentResults().size());
		for (Statistic statistic : Statistic.values())
			appendDouble(metricResult.getStatistic(statistic));
		finishLine();
	}

	private void appendString(String string) {
		csvContents.append("\"");
		csvContents.append(string.replace("\"", "\"\""));
		csvContents.append("\",");
	}

	private void appendDouble(Double number) {
		String fieldValue = (number.isInfinite() || number.isNaN()) ? "" : number.toString();
		csvContents.append(fieldValue + ",");
	}

	private void appendInteger(Integer number) {
		csvContents.append(number + ",");
	}

	private void finishLine() {
		deleteLastComma();
		csvContents.append("\n");
	}

	private void deleteLastComma() {
		csvContents.deleteCharAt(csvContents.length() - 1);
	}
}