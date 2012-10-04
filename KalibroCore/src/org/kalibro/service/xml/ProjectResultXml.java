package org.kalibro.service.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.RepositoryResult;
import org.kalibro.ResultState;
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
		repositoryResult.setStateTime(ResultState.LOADING, loadTime);
		repositoryResult.setStateTime(ResultState.COLLECTING, collectTime);
		repositoryResult.setStateTime(ResultState.ANALYZING, analysisTime);
		repositoryResult.setSourceTree(sourceTree.convert());
		return repositoryResult;
	}
}