package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.KalibroException;
import org.kalibro.RepositoryResult;
import org.kalibro.ProcessState;
import org.kalibro.dto.DataTransferObject;

@Entity(name = "RepositoryResult")
@Table(name = "\"PROJECT_RESULT\"")
@PrimaryKey(columns = {@Column(name = "project"), @Column(name = "date")})
public class ProjectResultRecord extends DataTransferObject<RepositoryResult> {

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "repositoryResult", orphanRemoval = true)
	private Collection<ModuleRecord> sourceTree;

	public ProjectResultRecord() {
		super();
	}

	public ProjectResultRecord(RepositoryResult repositoryResult) {
		project = new ProjectRecord(repositoryResult.getProject(), null);
		date = repositoryResult.getDate().getTime();
		if (repositoryResult.isProcessed()) {
			loadTime = repositoryResult.getLoadTime();
			collectTime = repositoryResult.getCollectTime();
			analysisTime = repositoryResult.getAnalysisTime();
			initializeSourceTree(repositoryResult);
		}
	}

	private void initializeSourceTree(RepositoryResult repositoryResult) {
		sourceTree = new ArrayList<ModuleRecord>();
		ModuleRecord root = new ModuleRecord(repositoryResult.getSourceTree(), this, null);
		addToSourceTree(root);
	}

	private void addToSourceTree(ModuleRecord node) {
		sourceTree.add(node);
		for (ModuleRecord child : node.getChildren())
			addToSourceTree(child);
	}

	@Override
	public RepositoryResult convert() {
		RepositoryResult repositoryResult = new RepositoryResult(project.convert());
		repositoryResult.setDate(new Date(date));
		repositoryResult.setStateTime(ProcessState.LOADING, loadTime);
		repositoryResult.setStateTime(ProcessState.COLLECTING, collectTime);
		repositoryResult.setStateTime(ProcessState.ANALYZING, analysisTime);
		convertSourceTree(repositoryResult);
		return repositoryResult;
	}

	private void convertSourceTree(RepositoryResult repositoryResult) {
		for (ModuleRecord node : sourceTree)
			if (node.isRoot()) {
				repositoryResult.setSourceTree(node.convert());
				return;
			}
		String projectName = repositoryResult.getProject().getName();
		throw new KalibroException("No source tree root found in result for project: " + projectName);
	}

	protected Date getDate() {
		return new Date(date);
	}
}