package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.KalibroException;
import org.kalibro.Processing;
import org.kalibro.ProcessState;
import org.kalibro.dto.DataTransferObject;

@Entity(name = "Processing")
@Table(name = "\"PROJECT_RESULT\"")
@PrimaryKey(columns = {@Column(name = "project"), @Column(name = "date")})
public class ProjectResultRecord extends DataTransferObject<Processing> {

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "processing", orphanRemoval = true)
	private Collection<ModuleRecord> sourceTree;

	public ProjectResultRecord() {
		super();
	}

	public ProjectResultRecord(Processing processing) {
		project = new ProjectRecord(processing.getProject(), null);
		date = processing.getDate().getTime();
		if (processing.isProcessed()) {
			loadTime = processing.getLoadTime();
			collectTime = processing.getCollectTime();
			analysisTime = processing.getAnalysisTime();
			initializeSourceTree(processing);
		}
	}

	private void initializeSourceTree(Processing processing) {
		sourceTree = new ArrayList<ModuleRecord>();
		ModuleRecord root = new ModuleRecord(processing.getSourceTree(), this, null);
		addToSourceTree(root);
	}

	private void addToSourceTree(ModuleRecord node) {
		sourceTree.add(node);
		for (ModuleRecord child : node.getChildren())
			addToSourceTree(child);
	}

	@Override
	public Processing convert() {
		Processing processing = new Processing(project.convert());
		processing.setDate(new Date(date));
		processing.setStateTime(ProcessState.LOADING, loadTime);
		processing.setStateTime(ProcessState.COLLECTING, collectTime);
		processing.setStateTime(ProcessState.ANALYZING, analysisTime);
		convertSourceTree(processing);
		return processing;
	}

	private void convertSourceTree(Processing processing) {
		for (ModuleRecord node : sourceTree)
			if (node.isRoot()) {
				processing.setSourceTree(node.convert());
				return;
			}
		String projectName = processing.getProject().getName();
		throw new KalibroException("No source tree root found in result for project: " + projectName);
	}

	protected Date getDate() {
		return new Date(date);
	}
}