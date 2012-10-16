package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dto.ModuleResultDto;

/**
 * Java Persistence API entity for {@link ModuleResult}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "ModuleResult")
@Table(name = "\"MODULE_RESULT\"")
public class ModuleResultRecord extends ModuleResultDto {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"processing\"", nullable = false, referencedColumnName = "\"id\"")
	private ProcessingRecord processing;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@OrderColumn(name = "\"index\"", nullable = false)
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> moduleName;

	@Column(name = "\"module_granularity\"", nullable = false)
	private String moduleGranularity;

	@Column(name = "\"grade\"", nullable = false)
	private Long grade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"parent\"", referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ModuleResultRecord parent;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
	private Collection<ModuleResultRecord> children;

	public ModuleResultRecord() {
		super();
	}

	public ModuleResultRecord(Long id) {
		this.id = id;
	}

	public ModuleResultRecord(ModuleResult moduleResult) {
		this(moduleResult, null);
	}

	public ModuleResultRecord(ModuleResult moduleResult, ProcessingRecord processingRecord) {
		this(moduleResult.getId());
		processing = processingRecord;
		moduleName = Arrays.asList(moduleResult.getModule().getName());
		moduleGranularity = moduleResult.getModule().getGranularity().name();
		grade = Double.doubleToLongBits(moduleResult.getGrade());
		setParent(moduleResult.getParent());
		setChildren(moduleResult.getChildren());
	}

	private void setParent(ModuleResult parent) {
		if (parent != null)
			this.parent = new ModuleResultRecord(parent.getId());
	}

	private void setChildren(Collection<ModuleResult> children) {
		this.children = new ArrayList<ModuleResultRecord>();
		for (ModuleResult child : children)
			this.children.add(new ModuleResultRecord(child, processing));
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public Module module() {
		return new Module(Granularity.valueOf(moduleGranularity), moduleName.toArray(new String[0]));
	}

	@Override
	public Double grade() {
		return Double.longBitsToDouble(grade);
	}
}