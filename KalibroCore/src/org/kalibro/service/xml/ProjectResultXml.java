package org.kalibro.service.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.RepositoryResult;
import org.kalibro.ProcessState;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "repositoryResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectResultXml extends DataTransferObject<RepositoryResult> {

	private ProjectXmlResponse project;
	private Date date;
	private Long loadTime;
	private Long collectTime;
	private Long analysisTime;
	private ModuleNodeXml sourceTree;

	public ProjectResultXml() {
		super();
	}

	public ProjectResultXml(RepositoryResult repositoryResult) {
		project = new ProjectXmlResponse(repositoryResult.getProject());
		date = repositoryResult.getDate();
		loadTime = repositoryResult.getLoadTime();
		collectTime = repositoryResult.getCollectTime();
		analysisTime = repositoryResult.getAnalysisTime();
		sourceTree = new ModuleNodeXml(repositoryResult.getSourceTree());
	}

	@Override
	public RepositoryResult convert() {
		RepositoryResult repositoryResult = new RepositoryResult(project.convert());
		repositoryResult.setDate(date);
		repositoryResult.setStateTime(ProcessState.LOADING, loadTime);
		repositoryResult.setStateTime(ProcessState.COLLECTING, collectTime);
		repositoryResult.setStateTime(ProcessState.ANALYZING, analysisTime);
		repositoryResult.setSourceTree(sourceTree.convert());
		return repositoryResult;
	}
}