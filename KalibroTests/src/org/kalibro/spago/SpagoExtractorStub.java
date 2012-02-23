package org.kalibro.spago;

import it.eng.spago4q.bo.ProjectDetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

class SpagoExtractorStub extends SpagoExtractor {

	private ProjectDetail projectDetail;

	protected SpagoExtractorStub(String xmlFileName) throws IOException {
		String detail = IOUtils.toString(getClass().getResourceAsStream(xmlFileName));
		projectDetail = new ProjectDetail(1, "HelloWorld-1.0R1", detail);
	}

	@Override
	protected String readOperationParameterValue(String parameterName) {
		return "file:///";
	}

	@Override
	public ArrayList<ProjectDetail> getProjectList() {
		return new ArrayList<ProjectDetail>(Arrays.asList(projectDetail));
	}
}