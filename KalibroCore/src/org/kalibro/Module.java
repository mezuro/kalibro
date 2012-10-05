package org.kalibro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

/**
 * Represents a piece of software, in the level of abstraction determined by its {@link Granularity}.
 * 
 * @author Carlos Morais
 */
@SortingFields({"granularity", "name"})
public class Module extends AbstractEntity<Module> {

	@IdentityField
	private String[] name;

	private Granularity granularity;

	/**
	 * Creates a new Module with the specified granularity and complete name.<br/>
	 * For example, the following code creates a module representing this very class:<br/>
	 * {@code new Module(Granularity.CLASS, "org", "kalibro", "Module")}'
	 * 
	 * @param granularity See {@link Granularity}.
	 * @param name Complete name of the module, with separated packages/directories.
	 */
	public Module(Granularity granularity, String... name) {
		this.granularity = granularity;
		this.name = name;
	}

	public String[] getName() {
		return name;
	}

	public String getShortName() {
		return name[name.length - 1];
	}

	public String getLongName() {
		String longName = "";
		for (String namePart : name)
			longName += "." + namePart;
		return longName.substring(1);
	}

	public Granularity getGranularity() {
		return granularity;
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
		return name.length > 1;
	}

	private Module inferParent() {
		return new Module(granularity.inferParentGranularity(), Arrays.copyOf(name, name.length - 1));
	}

	@Override
	public String toString() {
		return getShortName();
	}
}