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

		Commit firstCommit = new Commit();
		Commit head = new Commit();

		RepositoryFile helloWorldFile = new RepositoryFile();
		RepositoryFile byeWorldFile = new RepositoryFile();

		MetricResult result1File1 = new MetricResult();
		MetricResult result2File1 = new MetricResult();
		MetricResult result1File2 = new MetricResult();

		firstCommit.setId(0);
		firstCommit.setRevision("1");
		head.setId(1);
		head.setRevision("2");
		
		helloWorldFile.setId(0);
		helloWorldFile.setFilename("HelloWorld.java");
		byeWorldFile.setId(1);
		helloWorldFile.setFilename("ByeWorld.java");
		
		result1File1.setId(0);
		result1File1.setFile(helloWorldFile);
		result1File1.setCommit(firstCommit);
		result1File1.setNumberOfSourceCodeLines(2);
		
		result2File1.setId(1);
		result2File1.setFile(helloWorldFile);
		result2File1.setCommit(head);
		result2File1.setNumberOfSourceCodeLines(10);
		
		result1File2.setId(3);
		result1File2.setFile(byeWorldFile);
		result1File2.setCommit(firstCommit);
		result1File2.setNumberOfSourceCodeLines(6);

		return result;
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
