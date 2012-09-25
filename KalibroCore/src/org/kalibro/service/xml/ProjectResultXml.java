package org.kalibro.service.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.ProjectResult;
import org.kalibro.ProjectState;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "projectResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectResultXml extends DataTransferObject<ProjectResult> {

	private ProjectXml project;
	private Date date;
	private Long loadTime;
	private Long collectTime;
	private Long analysisTime;
	private ModuleNodeXml sourceTree;

	public ProjectResultXml() {
		super();
	}

	public ProjectResultXml(ProjectResult projectResult) {
		project = new ProjectXml(projectResult.getProject());
		date = projectResult.getDate();
		loadTime = projectResult.getLoadTime();
		collectTime = projectResult.getCollectTime();
		analysisTime = projectResult.getAnalysisTime();
		sourceTree = new ModuleNodeXml(projectResult.getSourceTree());
	}

	@Override
	public ProjectResult convert() {
		ProjectResult projectResult = new ProjectResult(project.convert());
		projectResult.setDate(date);
		projectResult.setStateTime(ProjectState.LOADING, loadTime);
		projectResult.setStateTime(ProjectState.COLLECTING, collectTime);
		projectResult.setStateTime(ProjectState.ANALYZING, analysisTime);
		projectResult.setSourceTree(sourceTree.convert());
		return projectResult;
	}
}