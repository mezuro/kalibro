package org.kalibro.core.processing;

import java.util.Map;

import org.kalibro.Kalibro;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ModuleResultDao;

class AnalyzeResultsTask extends ProcessProjectSubtask<Object> {

	private Map<Module, ModuleResult> resultMap;

	protected AnalyzeResultsTask(ProjectResult projectResult, Map<Module, ModuleResult> resultMap) {
		super(projectResult);
		this.resultMap = resultMap;
	}

	@Override
	protected ProjectState getTaskState() {
		return ProjectState.ANALYZING;
	}

	@Override
	public Object performAndGetResult() {
		new SourceTreeBuilder(projectResult).buildSourceTree(resultMap.keySet());
		new ResultsAggregator(projectResult, resultMap).aggregate();
		Kalibro.getProjectResultDao().save(projectResult);
		saveModuleResults();
		return null;
	}

	private void saveModuleResults() {
		ModuleResultDao moduleResultDao = Kalibro.getModuleResultDao();
		for (ModuleResult moduleResult : resultMap.values())
			moduleResultDao.save(moduleResult, project.getName());
	}
}