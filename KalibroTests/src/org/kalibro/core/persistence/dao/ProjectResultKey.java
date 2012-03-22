package org.kalibro.core.persistence.dao;

import java.util.Date;

import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;

@SortingMethods({"getProjectName", "getDate"})
public class ProjectResultKey extends AbstractEntity<ProjectResultKey> {

	@IdentityField
	private String projectName;

	@IdentityField
	private Date date;

	protected ProjectResultKey(ProjectResult projectResult) {
		this(projectResult.getProject().getName(), projectResult.getDate());
	}

	protected ProjectResultKey(String projectName, Date date) {
		this.projectName = projectName;
		this.date = date;
	}

	public String getProjectName() {
		return projectName;
	}

	public Date getDate() {
		return date;
	}
}