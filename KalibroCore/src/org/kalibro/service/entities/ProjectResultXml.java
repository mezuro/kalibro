package org.kalibro.service.entities;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.util.DataTransferObject;

@XmlRootElement(name = "ProjectResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectResultXml implements DataTransferObject<ProjectResult> {

	private ProjectXml project;
	private Date date;
	private Long loadTime;
	private Long analysisTime;
	private ModuleNodeXml sourceTree;

	public ProjectResultXml() {
		super();
	}

	public ProjectResultXml(ProjectResult projectResult) {
		project = new ProjectXml(projectResult.getProject());
		date = projectResult.getDate();
		loadTime = projectResult.getLoadTime();
		analysisTime = projectResult.getAnalysisTime();
		sourceTree = new ModuleNodeXml(projectResult.getSourceTree());
	}

	@Override
	public ProjectResult convert() {
		ProjectResult projectResult = new ProjectResult(project.convert());
		projectResult.setDate(date);
		projectResult.setLoadTime(loadTime);
		projectResult.setAnalysisTime(analysisTime);
		projectResult.setSourceTree(sourceTree.convert());
		return projectResult;
	}
}