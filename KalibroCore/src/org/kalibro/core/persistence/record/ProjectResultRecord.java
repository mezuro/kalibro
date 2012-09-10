package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.KalibroException;
import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;

@Entity(name = "ProjectResult")
@PrimaryKey(columns = {@Column(name = "project"), @Column(name = "date")})
public class ProjectResultRecord extends DataTransferObject<ProjectResult> {

	@ManyToOne(optional = false)
	@JoinColumn(name = "project", nullable = false, referencedColumnName = "id")
	private ProjectRecord project;

	@Column(name = "date", nullable = false)
	private Long date;

	@Column(nullable = false)
	private Long loadTime;

	@Column(nullable = false)
	private Long collectTime;

	@Column(nullable = false)
	private Long analysisTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "projectResult", orphanRemoval = true)
	private Collection<ModuleRecord> sourceTree;

	public ProjectResultRecord() {
		super();
	}

	public ProjectResultRecord(ProjectResult projectResult) {
		project = new ProjectRecord(projectResult.getProject(), null);
		date = projectResult.getDate().getTime();
		if (projectResult.isProcessed()) {
			loadTime = projectResult.getLoadTime();
			collectTime = projectResult.getCollectTime();
			analysisTime = projectResult.getAnalysisTime();
			initializeSourceTree(projectResult);
		}
	}

	private void initializeSourceTree(ProjectResult projectResult) {
		sourceTree = new ArrayList<ModuleRecord>();
		ModuleRecord root = new ModuleRecord(projectResult.getSourceTree(), this, null);
		addToSourceTree(root);
	}

	private void addToSourceTree(ModuleRecord node) {
		sourceTree.add(node);
		for (ModuleRecord child : node.getChildren())
			addToSourceTree(child);
	}

	@Override
	public ProjectResult convert() {
		ProjectResult projectResult = new ProjectResult(project.convert());
		projectResult.setDate(new Date(date));
		projectResult.setStateTime(ProjectState.LOADING, loadTime);
		projectResult.setStateTime(ProjectState.COLLECTING, collectTime);
		projectResult.setStateTime(ProjectState.ANALYZING, analysisTime);
		convertSourceTree(projectResult);
		return projectResult;
	}

	private void convertSourceTree(ProjectResult projectResult) {
		for (ModuleRecord node : sourceTree)
			if (node.isRoot()) {
				projectResult.setSourceTree(node.convert());
				return;
			}
		String projectName = projectResult.getProject().getName();
		throw new KalibroException("No source tree root found in result for project: " + projectName);
	}

	protected Date getDate() {
		return new Date(date);
	}
}