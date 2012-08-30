package org.kalibro.core.model;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.core.model.enums.Granularity;

@SortingFields({"compound", "scope", "name"})
public abstract class Metric extends AbstractEntity<Metric> {

	private final boolean compound;

	@IdentityField
	private String name;

	private Granularity scope;
	private String description;

	protected Metric(boolean compound, String name, Granularity scope) {
		this(compound, name, scope, "");
	}

	protected Metric(boolean compound, String name, Granularity scope, String description) {
		setName(name);
		setScope(scope);
		setDescription(description);
		this.compound = compound;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isCompound() {
		return compound;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Granularity getScope() {
		return scope;
	}

	public void setScope(Granularity scope) {
		this.scope = scope;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}