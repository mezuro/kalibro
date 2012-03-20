package org.kalibro.core.persistence.dao;

import java.util.Date;

import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;

@SortingMethods({"getProjectName", "getModuleName", "getDate"})
public class ModuleResultKey extends AbstractEntity<ModuleResultKey> {

	@IdentityField
	private String projectName;

	@IdentityField
	private String moduleName;

	@IdentityField
	private Date date;

	protected ModuleResultKey(String projectName, String moduleName, Date date) {
		this.projectName = projectName;
		this.moduleName = moduleName;
		this.date = date;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public Date getDate() {
		return date;
	}
}