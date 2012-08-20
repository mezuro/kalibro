package org.kalibro.core.model;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingMethods;
import org.kalibro.core.model.enums.Granularity;

@SortingMethods({"getCompound", "getScope", "getName"})
public abstract class Metric extends AbstractEntity<Metric> {

	@IdentityField
	private String name;

	private Granularity scope;
	private String description;

	public Metric(String name, Granularity scope) {
		this(name, scope, "");
	}

	public Metric(String name, Granularity scope, String description) {
		setName(name);
		setScope(scope);
		setDescription(description);
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean getCompound() {
		return isCompound();
	}

	public abstract boolean isCompound();

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