package org.kalibro.core.processing;

import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.kalibro.Kalibro;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.concurrent.TypedTaskReport;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ModuleResultDao;
import org.kalibro.core.settings.KalibroSettings;

public class AnalyzeProjectExecutor extends ProjectTaskExecutor {

	protected AnalyzeProjectExecutor(ProjectResult projectResult) {
		super(projectResult);
	}

	@Override
	protected ProjectState getTaskState() {
		return ProjectState.ANALYZING;
	}

	@Override
	protected Task getTask() {
		return new AnalyzeProjectTask(projectResult);
	}

	@Override
	protected void setTaskExecutionTime(long executionTime) {
		projectResult.setAnalysisTime(executionTime);
	}

	@Override
	protected void continueProcessing(TaskReport report) {
		Collection<ModuleResult> moduleResults = ((TypedTaskReport<Collection<ModuleResult>>) report).getResult();
		Kalibro.getProjectResultDao().save(projectResult);
		ModuleResultDao moduleResultDao = Kalibro.getModuleResultDao();
		for (ModuleResult moduleResult : moduleResults)
			moduleResultDao.save(moduleResult, getProject().getName(), projectResult.getDate());
		updateProjectState(ProjectState.READY);
		deleteSourcePathIfNecessary();
	}

	private void deleteSourcePathIfNecessary() {
		KalibroSettings settings = Kalibro.currentSettings();
		if (settings.shouldRemoveSources())
			FileUtils.deleteQuietly(settings.getLoadDirectoryFor(getProject()));
	}
}