package org.kalibro.service.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Processing;
import org.kalibro.ProcessState;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "processing")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectResultXml extends DataTransferObject<Processing> {

	private ProjectXmlResponse project;
	private Date date;
	private Long loadTime;
	private Long collectTime;
	private Long analysisTime;
	private ModuleNodeXml sourceTree;

	public ProjectResultXml() {
		super();
	}

	public ProjectResultXml(Processing processing) {
		project = new ProjectXmlResponse(processing.getProject());
		date = processing.getDate();
		loadTime = processing.getLoadTime();
		collectTime = processing.getCollectTime();
		analysisTime = processing.getAnalysisTime();
		sourceTree = new ModuleNodeXml(processing.getSourceTree());
	}

	@Override
	public Processing convert() {
		Processing processing = new Processing(project.convert());
		processing.setDate(date);
		processing.setStateTime(ProcessState.LOADING, loadTime);
		processing.setStateTime(ProcessState.COLLECTING, collectTime);
		processing.setStateTime(ProcessState.ANALYZING, analysisTime);
		processing.setSourceTree(sourceTree.convert());
		return processing;
	}
}