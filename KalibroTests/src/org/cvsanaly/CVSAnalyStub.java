package org.cvsanaly;

import java.util.*;

import org.cvsanaly.entities.Commit;
import org.cvsanaly.entities.MetricResult;
import org.cvsanaly.entities.RepositoryFile;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

public final class CVSAnalyStub {

	private CVSAnalyStub() { }

	public static BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("CVSAnaly");
		baseTool.setCollectorClass(CVSAnalyMetricCollector.class);
		for (NativeMetric metric : getSupportedMetrics())
			baseTool.addSupportedMetric(metric);
		return baseTool;
	}

	public static Set<NativeMetric> getSupportedMetrics() {
		NativeMetric[] metric = {
			new NativeMetric("Number of source lines of code", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of lines of code", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of comments", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of commented lines", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of blank lines", Granularity.CLASS, Language.values()),
			new NativeMetric("Number of functions", Granularity.CLASS, Language.values()),
		};
		return new HashSet<NativeMetric>(Arrays.asList(metric));
	}

	public static List<MetricResult> getExampleEntities() {
		List<MetricResult> result = new LinkedList<MetricResult>();

		Commit firstCommit = createCommit(0, "1");
		Commit head = createCommit(1, "2");

		RepositoryFile helloWorldFile = createRepositoryFile(0, "HelloWorld.java");
		RepositoryFile byeWorldFile = createRepositoryFile(1, "ByeWorld.java");
		
		result.addAll(Arrays.asList(createMetricResult(0, firstCommit, helloWorldFile, 2),
			createMetricResult(1, head, helloWorldFile, 10),
			createMetricResult(2, firstCommit, byeWorldFile, 6)));

		return result;
	}

	private static RepositoryFile createRepositoryFile(int id, String filename) {
		RepositoryFile helloWorldFile = new RepositoryFile();
		helloWorldFile.setId(id);
		helloWorldFile.setFilename(filename);
		return helloWorldFile;
	}

	private static Commit createCommit(int id, String revision) {
		Commit firstCommit = new Commit();
		firstCommit.setId(id);
		firstCommit.setRevision(revision);
		return firstCommit;
	}

	private static MetricResult createMetricResult(int id, Commit commit, RepositoryFile repository, int sloc) {
		MetricResult newMetricResult = new MetricResult();
		newMetricResult.setId(id);
		newMetricResult.setFile(repository);
		newMetricResult.setCommit(commit);
		newMetricResult.setNumberOfSourceCodeLines(sloc);
		return newMetricResult;
	}

	public static Set<NativeModuleResult> getExampleResult() {
		Set<NativeModuleResult> result = new HashSet<NativeModuleResult>();
		NativeModuleResult helloWorldResult = new NativeModuleResult(new Module(Granularity.CLASS, "HelloWorld"));
		helloWorldResult.addMetricResult(
			new NativeMetricResult(CVSAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE.getNativeMetric(), 10.0));

		NativeModuleResult byeWorldResult = new NativeModuleResult(new Module(Granularity.CLASS, "ByeWorld"));
		byeWorldResult.addMetricResult(
			new NativeMetricResult(CVSAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE.getNativeMetric(), 6.0));

		result.addAll(Arrays.asList(helloWorldResult, byeWorldResult));

		return result;
	}

}
