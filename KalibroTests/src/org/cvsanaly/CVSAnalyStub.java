package org.cvsanaly;

import java.util.*;

import org.cvsanaly.entities.Commit;
import org.cvsanaly.entities.MetricResult;
import org.cvsanaly.entities.RepositoryFile;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

public final class CVSAnalyStub {

	private static Map<CVSAnalyMetric, Double> helloWorldResultMap = generateHelloWorldResultMap();
	private static Map<CVSAnalyMetric, Double> byeWorldResultMap = generateByeWorldResultMap();
	private static Set<NativeModuleResult> result = generateResult();
	
	private CVSAnalyStub() { }


	private static Map<CVSAnalyMetric, Double> generateHelloWorldResultMap() {
		helloWorldResultMap = new HashMap<CVSAnalyMetric, Double>(CVSAnalyMetric.values().length);
		helloWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_BLANK_LINES, 0.0);
		helloWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_COMMENTED_LINES, 0.0);
		helloWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_COMMENTS, 0.0);
		helloWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_FUNCTIONS, 0.0);
		helloWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_LINES_OF_CODE, 13.0);
		helloWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE, 10.0);
		helloWorldResultMap.put(CVSAnalyMetric.MAXIMUM_CYCLOMATIC_COMPLEXITY, 0.0);
		helloWorldResultMap.put(CVSAnalyMetric.AVERAGE_CYCLOMATIC_COMPLEXITY, 0.0);
		helloWorldResultMap.put(CVSAnalyMetric.HALSTEAD_VOLUME, 0.0);
		return helloWorldResultMap;
	}
	
	private static Map<CVSAnalyMetric, Double> generateByeWorldResultMap() {
		byeWorldResultMap = new HashMap<CVSAnalyMetric, Double>(CVSAnalyMetric.values().length);
		byeWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_BLANK_LINES, 0.0);
		byeWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_COMMENTED_LINES, 0.0);
		byeWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_COMMENTS, 0.0);
		byeWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_FUNCTIONS, 0.0);
		byeWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_LINES_OF_CODE, 8.0);
		byeWorldResultMap.put(CVSAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE, 6.0);
		byeWorldResultMap.put(CVSAnalyMetric.MAXIMUM_CYCLOMATIC_COMPLEXITY, 0.0);
		byeWorldResultMap.put(CVSAnalyMetric.AVERAGE_CYCLOMATIC_COMPLEXITY, 0.0);
		byeWorldResultMap.put(CVSAnalyMetric.HALSTEAD_VOLUME, 0.0);
		return byeWorldResultMap;
	}
	
	private static Set<NativeModuleResult> generateResult() {		
		result = new HashSet<NativeModuleResult>();
		NativeModuleResult helloWorldResult = new NativeModuleResult(new Module(Granularity.CLASS, "HelloWorld.java"));
		for (Map.Entry<CVSAnalyMetric, Double> entry : helloWorldResultMap.entrySet())
			helloWorldResult.addMetricResult(
				new NativeMetricResult(entry.getKey().getNativeMetric(), entry.getValue()));
		
		NativeModuleResult byeWorldResult = new NativeModuleResult(new Module(Granularity.CLASS, "ByeWorld.java"));
		for (Map.Entry<CVSAnalyMetric, Double> entry : byeWorldResultMap.entrySet())
			byeWorldResult.addMetricResult(
				new NativeMetricResult(entry.getKey().getNativeMetric(), entry.getValue()));
		
		result.addAll(Arrays.asList(helloWorldResult, byeWorldResult));
		return result;
	}

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
		List<MetricResult> entity = new LinkedList<MetricResult>();

		Commit firstCommit = createCommit(0, 1);
		Commit head = createCommit(1, 2);

		RepositoryFile helloWorldFile = createRepositoryFile(0, "HelloWorld.java");
		RepositoryFile byeWorldFile = createRepositoryFile(1, "ByeWorld.java");

		entity.addAll(Arrays.asList(createMetricResult(0, firstCommit, helloWorldFile, 2, 2),
			createMetricResult(1, head, helloWorldFile, 10, 13),
			createMetricResult(2, firstCommit, byeWorldFile, 6, 8)));

		return entity;
	}

	private static RepositoryFile createRepositoryFile(int id, String filename) {
		RepositoryFile helloWorldFile = new RepositoryFile();
		helloWorldFile.setId(id);
		helloWorldFile.setFilename(filename);
		return helloWorldFile;
	}

	private static Commit createCommit(int id, int revisionTimestamp) {
		Commit firstCommit = new Commit();
		firstCommit.setId(id);
		firstCommit.setDate(new Date(revisionTimestamp));
		return firstCommit;
	}

	private static MetricResult createMetricResult(int id, Commit commit, RepositoryFile repo, int sloc, int loc) {
		MetricResult newMetricResult = new MetricResult();
		newMetricResult.setId(id);
		newMetricResult.setFile(repo);
		newMetricResult.setCommit(commit);
		newMetricResult.setNumberOfSourceLinesOfCode(sloc);
		newMetricResult.setNumberOfLinesOfCode(loc);
		return newMetricResult;
	}

	public static Set<NativeModuleResult> results() {
		return result;
	}

}
