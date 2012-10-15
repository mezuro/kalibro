package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.*;
import org.kalibro.dto.DataTransferObject;

@Entity(name = "Module")
@Table(name = "\"MODULE\"")
@PrimaryKey(columns = {@Column(name = "project"), @Column(name = "date"), @Column(name = "name")})
public class ModuleRecord extends DataTransferObject<ModuleNode> {

	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name = "project", nullable = false, referencedColumnName = "project"),
		@JoinColumn(name = "date", nullable = false, referencedColumnName = "date")})
	private ProcessingRecord projectResult;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(nullable = false)
	private String granularity;

	@ManyToOne(optional = true)
	@JoinColumns({
		@JoinColumn(insertable = false, name = "project", referencedColumnName = "project", updatable = false),
		@JoinColumn(insertable = false, name = "date", referencedColumnName = "date", updatable = false),
		@JoinColumn(name = "parent", referencedColumnName = "name")})
	private ModuleRecord parent;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
	private Collection<ModuleRecord> children;

	public ModuleRecord() {
		super();
	}

	public ModuleRecord(Module module) {
		this(new ModuleNode(module), new ProcessingRecord(), new ModuleRecord());
	}

	public ModuleRecord(ModuleNode moduleNode, RepositoryResult repositoryResult) {
		initialize(moduleNode, new ProcessingRecord(repositoryResult), null);
	}

	public ModuleRecord(ModuleNode moduleNode, ProcessingRecord projectResult, ModuleRecord parent) {
		initialize(moduleNode, projectResult, parent);
	}

	private void initialize(ModuleNode moduleNode, ProcessingRecord result, ModuleRecord parentModule) {
		projectResult = result;
		parent = parentModule;
		name = moduleNode.getModule().getLongName();
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

	protected ModuleResult convertIntoModuleResult() {
		return new ModuleResult(convertIntoModule(), projectResult.getDate());
	}

	private Module convertIntoModule() {
		return new Module(Granularity.valueOf(granularity), name);
	}

	protected boolean isRoot() {
		return parent == null;
	}

	protected Collection<ModuleRecord> getChildren() {
		return children;
	}
}