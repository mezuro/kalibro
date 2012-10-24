package org.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kalibro.MetricCollector;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.concurrent.Writer;

/**
 * Metric collector for Checkstyle.
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
public class CheckstyleMetricCollector implements MetricCollector {

	private String description;

	public CheckstyleMetricCollector() throws IOException {
		description = IOUtils.toString(getClass().getResourceAsStream("description"));
	}

	@Override
	public String name() {
		return "Checkstyle";
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		return new HashSet<NativeMetric>(CheckstyleMetric.supportedMetrics());
	}

	@Override
	public void collectMetrics(
		File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception {
		Checker checker = new Checker();
		checker.setModuleClassLoader(Checker.class.getClassLoader());
		checker.addListener(new CheckstyleListener(codeDirectory, wantedMetrics, resultWriter));
		checker.configure(CheckstyleConfiguration.checkerConfiguration(wantedMetrics));
		checker.process(listJavaFiles(codeDirectory));
		checker.destroy();
	}

	private List<File> listJavaFiles(File codeDirectory) {
		return (List<File>) FileUtils.listFiles(codeDirectory, new String[]{"java"}, true);
	}
}