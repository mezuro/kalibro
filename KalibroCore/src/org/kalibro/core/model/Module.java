package org.kalibro.core.model;

import java.util.ArrayList;
import java.util.List;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.core.model.enums.Granularity;

/**
 * Represent a piece of software, in one level of abstraction, determined by its {@link Granularity}.
 * 
 * @author Carlos Morais
 */
@SortingFields({"granularity", "name"})
public class Module extends AbstractEntity<Module> {

	@IdentityField
	protected String name;

	protected Granularity granularity;

	/**
	 * Creates a new Module. <br/>
	 * For example, the following code initializes the class org.junit.Assert: <br/>
	 * {@code new Module(Granularity.CLASS, "org", "junit", "Assert")}'
	 * 
	 * @param granularity See {@link Granularity}.
	 * @param name Complete name of the module, with separated packages.
	 */
	public Module(Granularity granularity, String... name) {
		setGranularity(granularity);
		setName(name[0]);
		for (int i = 1; i < name.length; i++)
			this.name += "." + name[i];
	}

	@Override
	public String toString() {
		return getShortName();
	}

	public String getShortName() {
		return name.substring(name.lastIndexOf('.') + 1);
	}

	public List<Module> inferAncestry() {
		List<Module> ancestry = new ArrayList<Module>();
		if (hasParent()) {
			Module parent = inferParent();
			ancestry.addAll(parent.inferAncestry());
			ancestry.add(parent);
		}
		return ancestry;
	}

	private boolean hasParent() {
		return name.contains(".");
	}

	private Module inferParent() {
		String parentName = name.substring(0, name.lastIndexOf('.'));
		return new Module(granularity.inferParentGranularity(), parentName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Granularity getGranularity() {
		return granularity;
	}

	public void setGranularity(Granularity granularity) {
		this.granularity = granularity;
	}
}