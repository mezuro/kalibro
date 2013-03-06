package org.kalibro.core.persistence.record;

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
@Table(name = "\"module_result\"")
public class ModuleResultRecord extends ModuleResultDto {

	private static final String TOKEN = "###";

	public static String persistedName(String[] moduleName) {
		String name = "";
		for (String namePart : moduleName)
			name += TOKEN + namePart;
		if (moduleName.length > 0)
			name = name.substring(TOKEN.length());
		return name;
	}

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"processing\"", nullable = false)
	private Long processing;

	@Column(name = "\"module_name\"", nullable = false)
	private String moduleName;

	@Column(name = "\"module_granularity\"", nullable = false)
	private String moduleGranularity;

	@Column(name = "\"grade\"")
	private Long grade;

	@Column(name = "\"height\"", nullable = false)
	private Integer height;

	@Column(name = "\"parent\"")
	private Long parent;

	public ModuleResultRecord() {
		super();
	}

	public ModuleResultRecord(ModuleResult moduleResult) {
		this(moduleResult, null);
	}

	public ModuleResultRecord(ModuleResult moduleResult, Long processingId) {
		id = moduleResult.getId();
		processing = processingId;
		setModule(moduleResult.getModule());
		grade = Double.doubleToLongBits(moduleResult.getGrade());
		setParent(moduleResult.getParent());
	}

	private void setModule(Module module) {
		moduleName = persistedName(module.getName());
		moduleGranularity = module.getGranularity().name();
	}

	private void setParent(ModuleResult parent) {
		height = parent == null ? 0 : parent.getHeight() + 1;
		if (parent != null)
			this.parent = parent.getId();
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
	public Integer height() {
		return height;
	}

	@Override
	public Long parentId() {
		return parent;
	}
}