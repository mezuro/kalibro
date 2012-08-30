package org.kalibro;

import java.io.File;
import java.util.*;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.Print;

public class ReadingGroup extends AbstractEntity<ReadingGroup> {

	private static Long nextId = 1L;
	private static Map<Long, ReadingGroup> groups = new HashMap<Long, ReadingGroup>();

	public static List<ReadingGroup> all() {
		return new ArrayList<ReadingGroup>(groups.values());
	}

	public static ReadingGroup importFrom(File file) {
		return importFrom(file, ReadingGroup.class);
	}

	@IdentityField
	@Print(skip = true)
	private Long id;

	private String name = "";
	private String description = "";
	private Collection<Reading> readings = new ArrayList<Reading>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Reading> getReadings() {
		return new ArrayList<Reading>(readings);
	}

	public void add(Reading reading) {
		for (Reading each : readings)
			each.assertNoConflictWith(reading);
		readings.add(reading);
	}

	public void save() {
		id = nextId++;
		groups.put(id, this);
	}

	public boolean isSaved() {
		return id != null && groups.containsKey(id) && groups.get(id).deepEquals(this);
	}

	public void delete() {
		groups.remove(id);
	}
}