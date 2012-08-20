package org.kalibro.core.persistence.dao;

import java.util.Date;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingMethods;
import org.kalibro.core.model.ProjectResult;

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