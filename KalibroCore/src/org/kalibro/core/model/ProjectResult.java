package org.kalibro.core.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.kalibro.KalibroException;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;
import org.kalibro.core.model.enums.ProjectState;

@SortingMethods({"getProject", "getDate"})
public class ProjectResult extends AbstractEntity<ProjectResult> {

	@IdentityField
	private Project project;

	@IdentityField
	private Date date;

	private Map<ProjectState, Long> stateTimes;
	private ModuleNode sourceTree;

	public ProjectResult(Project project) {
		setProject(project);
		setDate(new Date());
		stateTimes = new HashMap<ProjectState, Long>();
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getLoadTime() {
		assertProcessed();
		return stateTimes.get(ProjectState.LOADING);
	}

	public Long getCollectTime() {
		assertProcessed();
		return stateTimes.get(ProjectState.COLLECTING);
	}

	public Long getAnalysisTime() {
		assertProcessed();
		return stateTimes.get(ProjectState.ANALYZING);
	}

	public void setStateTime(ProjectState state, long time) {
		stateTimes.put(state, time);
	}

	public ModuleNode getSourceTree() {
		assertProcessed();
		return sourceTree;
	}

	public void setSourceTree(ModuleNode sourceTree) {
		this.sourceTree = sourceTree;
	}

	private void assertProcessed() {
		if (sourceTree == null)
			throw new KalibroException("Project not yet processed: " + project.getName());
	}
}