package org.kalibro.core.model;

import java.util.Date;

import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;
import org.kalibro.core.model.enums.Granularity;

@SortingMethods({"getProject", "getDate"})
public class ProjectResult extends AbstractEntity<ProjectResult> {

	@IdentityField
	private Project project;

	@IdentityField
	private Date date;

	private Long loadTime;
	private Long analysisTime;
	private ModuleNode sourceTree;

	public ProjectResult(Project project) {
		setProject(project);
		setDate(new Date());
		setLoadTime(0L);
		setAnalysisTime(0L);
		setSourceTree(new ModuleNode(new Module(Granularity.APPLICATION, project.getName())));
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
		return loadTime;
	}

	public void setLoadTime(Long loadTime) {
		this.loadTime = loadTime;
	}

	public Long getAnalysisTime() {
		return analysisTime;
	}

	public void setAnalysisTime(Long analysisTime) {
		this.analysisTime = analysisTime;
	}

	public ModuleNode getSourceTree() {
		return sourceTree;
	}

	public void setSourceTree(ModuleNode sourceTree) {
		this.sourceTree = sourceTree;
	}
}