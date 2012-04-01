package org.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class CheckstyleMetricCollector implements MetricCollector {

	@Override
	public BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("Checkstyle");
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			baseTool.addSupportedMetric(metric.getNativeMetric());
		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception {
		Configuration configuration = CheckstyleConfiguration.checkerConfiguration(metrics);
		CheckstyleOutputParser parser = new CheckstyleOutputParser(codeDirectory, metrics);
		runCheckstyle(listFiles(codeDirectory), configuration, parser);
		return parser.getResults();
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