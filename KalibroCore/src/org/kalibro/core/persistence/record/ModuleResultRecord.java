package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dto.ModuleResultDto;

@Entity(name = "Module")
@Table(name = "\"MODULE\"")
@PrimaryKey(columns = {@Column(name = "project"), @Column(name = "date"), @Column(name = "name")})
public class ModuleResultRecord extends ModuleResultDto {

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
	private ModuleResultRecord parent;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
	private Collection<ModuleResultRecord> children;

	public ModuleResultRecord() {
		super();
	}

	public ModuleResultRecord(Module module) {
		this(new ModuleNode(module), new ProcessingRecord(), new ModuleResultRecord());
	}

	public ModuleResultRecord(ModuleNode moduleNode, RepositoryResult repositoryResult) {
		initialize(moduleNode, new ProcessingRecord(repositoryResult), null);
	}

	public ModuleResultRecord(ModuleNode moduleNode, ProcessingRecord projectResult, ModuleResultRecord parent) {
		initialize(moduleNode, projectResult, parent);
	}

	public ModuleResultRecord(Long id) {
		// TODO Auto-generated constructor stub
	}

	private void initialize(ModuleNode moduleNode, ProcessingRecord result, ModuleResultRecord parentModule) {
		projectResult = result;
		parent = parentModule;
		name = moduleNode.getModule().getLongName();
		granularity = moduleNode.getModule().getGranularity().name();
		initializeChildren(moduleNode);
	}

	private void initializeChildren(ModuleNode moduleNode) {
		children = new ArrayList<ModuleResultRecord>(moduleNode.getChildren().size());
		for (ModuleNode child : moduleNode.getChildren())
			children.add(new ModuleResultRecord(child, projectResult, this));
	}

	@Override
	public ModuleNode convert() {
		ModuleNode moduleNode = new ModuleNode(convertIntoModule());
		convertChildren(moduleNode);
		return moduleNode;
	}

	private void convertChildren(ModuleNode moduleNode) {
		for (ModuleResultRecord child : children)
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

	protected Collection<ModuleResultRecord> getChildren() {
		return children;
	}

	@Override
	public Long id() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Module module() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double grade() {
		// TODO Auto-generated method stub
		return null;
	}
}