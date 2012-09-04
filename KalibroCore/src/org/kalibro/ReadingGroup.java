package org.kalibro;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.Print;
import org.kalibro.core.persistence.dao.DaoFactory;

public class ReadingGroup extends AbstractEntity<ReadingGroup> {

	public static List<ReadingGroup> all() {
		return DaoFactory.getReadingGroupDao().all();
	}

	public static ReadingGroup importFrom(File file) {
		return importFrom(file, ReadingGroup.class);
	}

	@IdentityField
	@Print(skip = true)
	private Long id;

	private String name;
	private String description;
	private Collection<Reading> readings;

	public ReadingGroup() {
		setName("");
		setDescription("");
		readings = new ArrayList<Reading>();
	}

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
		DaoFactory.getReadingGroupDao().save(this);
	}

	public void delete() {
		DaoFactory.getReadingGroupDao().delete(this);
	}
}