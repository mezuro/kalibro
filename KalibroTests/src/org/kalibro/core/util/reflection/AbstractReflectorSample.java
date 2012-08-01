package org.kalibro.core.util.reflection;

import javax.persistence.Column;

abstract class AbstractReflectorSample {

	@Column(name = "name_column")
	private String name;

	@Column(name = "description_column")
	private String description;

	protected AbstractReflectorSample(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}