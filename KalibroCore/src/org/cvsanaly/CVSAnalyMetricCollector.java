package org.cvsanaly;

import java.io.File;
import java.util.*;

import org.cvsanaly.entities.MetricResult;
import org.cvsanaly.entities.RepositoryFile;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Granularity;

public class CVSAnalyMetricCollector implements MetricCollector {

	private static final String CVSANALY2_COMMAND_LINE = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d ";

	@Override
	public BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("CVSAnaly");
		baseTool.setCollectorClass(CVSAnalyMetricCollector.class);
		for (CVSAnalyMetric metric : CVSAnalyMetric.values())
			baseTool.addSupportedMetric(metric.getNativeMetric());
		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception {
		//TODO Only collect the given metrics
		File tempFile = File.createTempFile("kalibro-cvsanaly-db", ".sqlite");
		CommandTask executor = new CommandTask(CVSANALY2_COMMAND_LINE + tempFile.getAbsolutePath(), codeDirectory);
		executor.executeAndWait();
		
		CVSAnalyDatabaseFetcher databaseFetcher = new CVSAnalyDatabaseFetcher(tempFile);
		List<MetricResult> entities = databaseFetcher.getMetricResults();
	
		Set<NativeModuleResult> result = convertEntityToNativeModuleResult(entities);
		
		tempFile.delete();
		return result;
	}

	private Set<NativeModuleResult> convertEntityToNativeModuleResult(List<MetricResult> entities) throws Exception {
		Set<NativeModuleResult> result = new HashSet<NativeModuleResult>();
		Map<String, Module> modules = new HashMap<String, Module>();
		
		List<MetricResult> filteredEntities = filterOlderRevisio1ns(entities);
		
		for (MetricResult entity: filteredEntities) {
			//TODO Modify CVSAnaly to get information about path
			String filename = entity.getFile().getFilename();
			Module module = findModule(modules, filename);
			NativeModuleResult nativeModuleResult = new NativeModuleResult(module);
			extractMetrics(entity, nativeModuleResult);
			
			result.add(nativeModuleResult);
		}
		return result;
	}

	private List<MetricResult> filterOlderRevisio1ns(List<MetricResult> entities) {
		// TODO Optimize this
		Map<RepositoryFile, MetricResult> result = new HashMap<RepositoryFile, MetricResult>();
		
		for (MetricResult entity: entities) {
			RepositoryFile file = entity.getFile();
			if (!result.containsKey(file) || 
				result.get(file).getCommit().getDate().before(entity.getCommit().getDate()))
			result.put(file, entity);
		}
		return new ArrayList<MetricResult>(result.values());
	}

	private void extractMetrics(MetricResult entity, NativeModuleResult nativeModuleResult) throws Exception {
		for (CVSAnalyMetric metric : CVSAnalyMetric.values()) {
			nativeModuleResult.addMetricResult(new NativeMetricResult(metric.getNativeMetric(), 
				metric.getMetricValue(entity)));
		}
	}

	private Module findModule(Map<String, Module> modules, String filename) {
		Module module;
		if (modules.containsKey(filename)) {
			module = modules.get(filename);
		} else  {
			module = new Module(Granularity.CLASS, filename);
			modules.put(filename, module);
		}
		
		return module;
	}
	
}
