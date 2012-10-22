package org.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.kalibro.MetricCollector;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.concurrent.Writer;

public class CheckstyleMetricCollector implements MetricCollector {

	@Override
	public String name() {
		return "Checkstyle";
	}

	@Override
	public String description() {
		return "";
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		return new HashSet<NativeMetric>(CheckstyleMetric.supportedMetrics());
	}

	@Override
	public void collectMetrics(
		File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception {
		Configuration configuration = CheckstyleConfiguration.checkerConfiguration(wantedMetrics);
		CheckstyleOutputParser parser = new CheckstyleOutputParser(codeDirectory, wantedMetrics);
		runCheckstyle(listFiles(codeDirectory), configuration, parser);
		for (NativeModuleResult result : parser.getResults())
			resultWriter.write(result);
		resultWriter.close();
	}

	private List<File> listFiles(File codeDirectory) {
		return (List<File>) FileUtils.listFiles(codeDirectory, new String[]{"java"}, true);
	}

	private void runCheckstyle(List<File> files, Configuration configuration, AuditListener listener) throws Exception {
		Checker checker = new Checker();
		checker.setModuleClassLoader(Checker.class.getClassLoader());
		checker.addListener(listener);
		checker.configure(configuration);
		checker.process(files);
		checker.destroy();
	}
}