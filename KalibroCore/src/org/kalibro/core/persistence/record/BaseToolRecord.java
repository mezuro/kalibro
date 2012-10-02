package org.kalibro.core.persistence.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kalibro.BaseTool;
import org.kalibro.dto.DataTransferObject;

@Entity(name = "BaseTool")
@Table(name = "\"BASE_TOOL\"")
public class BaseToolRecord extends DataTransferObject<BaseTool> {

	@Id
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "collector_class", nullable = false, unique = true)
	private String collectorClass;

	public BaseToolRecord() {
		super();
	}

	public BaseToolRecord(String baseToolName) {
		name = baseToolName;
	}

	public BaseToolRecord(BaseTool baseTool) {
		name = baseTool.getName();
		collectorClass = baseTool.getCollectorClassName();
	}

	@Override
	public BaseTool convert() {
		return new BaseTool(collectorClass);
	}

	protected String getName() {
		return name;
	}
}