package org.kalibro;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.Print;
import org.kalibro.core.abstractentity.SortingFields;

/**
 * Abstract representation of source code metrics.
 * 
 * @author Carlos Morais.
 */
@SortingFields({"compound", "scope", "name"})
public abstract class Metric extends AbstractEntity<Metric> {

	private final boolean compound;

	@Print(skip = true)
	private Long id;

	@IdentityField
	private String name;

	private Granularity scope;
	private String description;

	protected Metric(boolean compound, String name, Granularity scope) {
		this.compound = compound;
		setName(name);
		setScope(scope);
		setDescription("");
	}

	public boolean isCompound() {
		return compound;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public Granularity getScope() {
		return scope;
	}

	protected void setScope(Granularity scope) {
		this.scope = scope;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return name;
	}
}