package org.kalibro.core.persistence.database.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.KalibroException;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.util.DataTransferObject;

@Entity(name = "ProjectResult")
@PrimaryKey(columns = {@Column(name = "projectName"), @Column(name = "date")})
public class ProjectResultRecord implements DataTransferObject<ProjectResult> {

	@ManyToOne(optional = false)
	@JoinColumn(name = "projectName", nullable = false, referencedColumnName = "name")
	private ProjectRecord project;

	@Column(name = "date", nullable = false)
	private Long date;

	@Column(nullable = false)
	private Long loadTime;

	@Column(nullable = false)
	private Long analysisTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "projectResult", orphanRemoval = true)
	private Collection<ModuleRecord> sourceTree;

	public ProjectResultRecord() {
		super();
	}

	public ProjectResultRecord(ProjectResult projectResult) {
		project = new ProjectRecord(projectResult.getProject());
		date = projectResult.getDate().getTime();
		loadTime = projectResult.getLoadTime();
		analysisTime = projectResult.getAnalysisTime();
		initializeSourceTree(projectResult);
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
		projectResult.setLoadTime(loadTime);
		projectResult.setAnalysisTime(analysisTime);
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