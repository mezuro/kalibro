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
		this("");
	}

	public ReadingGroup(String name) {
		setId(null);
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

	public void setId(Long id) {
		this.id = id;
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
		for (Reading reading : readings)
			reading.setGroup(this);
		return new TreeSet<Reading>(readings);
	}

	public void setReadings(SortedSet<Reading> readings) {
		this.readings = readings;
	}

	public void addReading(Reading reading) {
		for (Reading each : readings)
			reading.assertNoConflictWith(each);
		reading.setGroup(this);
		readings.add(reading);
	}

	void removeReading(Reading reading) {
		readings.remove(reading);
		reading.setGroup(null);
	}

	public void save() {
		if (name.trim().isEmpty())
			throw new KalibroException("Reading group requires name.");
		id = dao().save(this);
		readings = DaoFactory.getReadingDao().readingsOf(id);
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		deleted();
	}

	private void deleted() {
		for (Reading reading : readings)
			reading.deleted();
		id = null;
	}

	@Override
	public String toString() {
		return name;
	}
}