package org.kalibro;

import java.io.File;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.Print;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingGroupDao;

/**
 * Interpretations should, naturally, be grouped (see {@link Reading}). ReadingGroup adds name and description to an
 * interpretation group.
 * 
 * @author Carlos Morais
 */
@SortingFields("name")
public class ReadingGroup extends AbstractEntity<ReadingGroup> {

	public static ReadingGroup importFrom(File file) {
		return importFrom(file, ReadingGroup.class);
	}

	public static SortedSet<ReadingGroup> all() {
		return dao().all();
	}

	private static ReadingGroupDao dao() {
		return DaoFactory.getReadingGroupDao();
	}

	@Print(skip = true)
	private Long id;

	@IdentityField
	@Print(order = 1)
	private String name;

	@Print(order = 2)
	private String description;

	@Print(order = 3)
	private Set<Reading> readings;

	public ReadingGroup() {
		this("New group");
	}

	public ReadingGroup(String name) {
		setName(name);
		setDescription("");
		setReadings(new TreeSet<Reading>());
	}

	public Long getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
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

	public SortedSet<Reading> getReadings() {
		TreeSet<Reading> myReadings = new TreeSet<Reading>();
		for (Reading reading : readings) {
			reading.setGroup(this);
			myReadings.add(reading);
		}
		return myReadings;
	}

	public void setReadings(SortedSet<Reading> readings) {
		this.readings = readings;
	}

	public void addReading(Reading reading) {
		for (Reading each : readings)
			each.assertNoConflictWith(reading);
		reading.setGroup(this);
		readings.add(reading);
	}

	public void removeReading(Reading reading) {
		readings = getReadings();
		readings.remove(reading);
		reading.setGroup(null);
	}

	void assertSaved() {
		if (!hasId())
			save();
	}

	public void save() {
		throwExceptionIf(name.trim().isEmpty(), "Reading group requires name.");
		id = dao().save(this);
		for (Reading reading : getReadings())
			reading.save();
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		deleted();
	}

	private void deleted() {
		id = null;
		for (Reading reading : readings)
			reading.deleted();
	}

	@Override
	public void exportTo(File file) {
		super.exportTo(file);
	}

	@Override
	public String toString() {
		return name;
	}
}