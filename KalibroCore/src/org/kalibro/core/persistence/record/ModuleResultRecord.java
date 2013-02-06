package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.kalibro.Granularity;
import org.kalibro.MetricResult;
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

	private static final String TOKEN = "&#&#&";
	private static final int MAX_PATH_LENGTH = 2000;

	public static String persistedName(String[] moduleName) {
		String name = "";
		for (String namePart : moduleName)
			name += TOKEN + namePart;
		if (moduleName.length > 0)
			name = name.substring(TOKEN.length());
		return name;
	}

	private static ModuleResultRecord parentRecord(ModuleResult moduleResult) {
		if (!moduleResult.hasParent())
			return null;
		return new ModuleResultRecord(moduleResult.getParent().getId());
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"processing\"", nullable = false, referencedColumnName = "\"id\"")
	private ProcessingRecord processing;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(length = MAX_PATH_LENGTH, name = "\"module_name\"", nullable = false)
	private String moduleName;

	@Column(name = "\"module_granularity\"", nullable = false)
	private String moduleGranularity;

	@Column(name = "\"grade\"")
	private Long grade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"parent\"", referencedColumnName = "\"id\"")
	private ModuleResultRecord parent;

	@CascadeOnDelete
	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	private Collection<ModuleResultRecord> children;

	@CascadeOnDelete
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "moduleResult", orphanRemoval = true)
	private Collection<MetricResultRecord> metricResults;

	public ModuleResultRecord() {
		super();
	}

	public ModuleResultRecord(Long id) {
		this.id = id;
	}

	public ModuleResultRecord(ModuleResult moduleResult) {
		this(moduleResult, null);
	}

	public ModuleResultRecord(ModuleResult moduleResult, Long processingId) {
		this(moduleResult, parentRecord(moduleResult), processingId);
	}

	public ModuleResultRecord(ModuleResult moduleResult, ModuleResultRecord parent, Long processingId) {
		this(moduleResult.getModule(), parent, processingId);
		id = moduleResult.getId();
		grade = Double.doubleToLongBits(moduleResult.getGrade());
		setMetricResults(moduleResult.getMetricResults());
	}

	public ModuleResultRecord(Module module, ModuleResultRecord parent, Long processingId) {
		this.parent = parent;
		processing = new ProcessingRecord(processingId);
		moduleName = persistedName(module.getName());
		moduleGranularity = module.getGranularity().name();
	}

	private void setMetricResults(Collection<MetricResult> results) {
		metricResults = new ArrayList<MetricResultRecord>();
		for (MetricResult metricResult : results)
			metricResults.add(new MetricResultRecord(metricResult, this));
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public Module module() {
		return new Module(Granularity.valueOf(moduleGranularity), moduleName.split(TOKEN));
	}

	@Override
	public Double grade() {
		return grade == null ? Double.NaN : Double.longBitsToDouble(grade);
	}

	@Override
	public Long parentId() {
		return parent == null ? null : parent.id();
	}
}