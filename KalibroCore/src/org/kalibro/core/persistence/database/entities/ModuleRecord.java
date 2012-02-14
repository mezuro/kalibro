package org.kalibro.core.persistence.database.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.util.DataTransferObject;

@Entity(name = "Module")
@PrimaryKey(columns = {@Column(name = "projectName"), @Column(name = "date"), @Column(name = "name")})
public class ModuleRecord implements DataTransferObject<ModuleNode> {

	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name = "projectName", nullable = false, referencedColumnName = "projectName"),
		@JoinColumn(name = "date", nullable = false, referencedColumnName = "date")})
	private ProjectResultRecord projectResult;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(nullable = false)
	private String granularity;

	@ManyToOne(optional = true)
	@JoinColumns({
		@JoinColumn(insertable = false, name = "projectName", referencedColumnName = "projectName", updatable = false),
		@JoinColumn(insertable = false, name = "date", referencedColumnName = "date", updatable = false),
		@JoinColumn(name = "parent", referencedColumnName = "name")})
	private ModuleRecord parent;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
	private Collection<ModuleRecord> children;

	public ModuleRecord() {
		super();
	}

	public ModuleRecord(ModuleNode moduleNode, ProjectResultRecord projectResult, ModuleRecord parent) {
		initialize(moduleNode, projectResult, parent);
	}

	public ModuleRecord(ModuleNode moduleNode, String projectName, Date date) {
		initialize(moduleNode, createProjectResult(projectName, date), null);
	}

	private ProjectResultRecord createProjectResult(String projectName, Date date) {
		Project project = new Project();
		project.setName(projectName);
		ProjectResult entity = new ProjectResult(project);
		entity.setDate(date);
		return new ProjectResultRecord(entity);
	}

	private void initialize(ModuleNode moduleNode, ProjectResultRecord result, ModuleRecord parentModule) {
		projectResult = result;
		parent = parentModule;
		name = moduleNode.getModule().getName();
		granularity = moduleNode.getModule().getGranularity().name();
		initializeChildren(moduleNode);
	}

	private void initializeChildren(ModuleNode moduleNode) {
		children = new ArrayList<ModuleRecord>(moduleNode.getChildren().size());
		for (ModuleNode child : moduleNode.getChildren())
			children.add(new ModuleRecord(child, projectResult, this));
	}

	@Override
	public ModuleNode convert() {
		ModuleNode moduleNode = new ModuleNode(convertIntoModule());
		convertChildren(moduleNode);
		return moduleNode;
	}

	private void convertChildren(ModuleNode moduleNode) {
		for (ModuleRecord child : children)
			moduleNode.addChild(child.convert());
	}

	public ModuleResult convertIntoModuleResult() {
		return new ModuleResult(convertIntoModule(), projectResult.getDate());
	}

	private Module convertIntoModule() {
		return new Module(Granularity.valueOf(granularity), name);
	}

	public boolean isRoot() {
		return parent == null;
	}

	public Collection<ModuleRecord> getChildren() {
		return children;
	}
}