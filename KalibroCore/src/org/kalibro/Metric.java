package org.kalibro;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.Print;
import org.kalibro.core.abstractentity.SortingFields;

/**
 * Abstract representation of source code metric.
 * 
 * @author Carlos Morais.
 */
@SortingFields({"compound", "scope", "name"})
public abstract class Metric extends AbstractEntity<Metric> {

	@Print(skip = true)
	private final boolean compound;

	@IdentityField
	@Print(order = 1)
	private String name;

	@Print(order = 2)
	private Granularity scope;

	@Print(order = 3)
	private String description;

	protected Metric(boolean compound, String name, Granularity scope) {
		this.compound = compound;
		setName(name);
		setScope(scope);
		setDescription("");
	}

	public final boolean isCompound() {
		return compound;
	}

	public final String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public final Granularity getScope() {
		return scope;
	}

	protected void setScope(Granularity scope) {
		this.scope = scope;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return name;
	}
}