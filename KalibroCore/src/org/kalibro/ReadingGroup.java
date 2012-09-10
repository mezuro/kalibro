package org.kalibro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.Print;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.dao.ReadingGroupDao;

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

	public static List<ReadingGroup> all() {
		return dao().all();
	}

	private static ReadingGroupDao dao() {
		return DaoFactory.getReadingGroupDao();
	}

	@Print(skip = true)
	private Long id;

	private String name;
	private String description;
	private List<Reading> readings;

	public ReadingGroup() {
		this("");
	}

	public ReadingGroup(String name) {
		setId(null);
		setName(name);
		setDescription("");
		setReadings(new ArrayList<Reading>());
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

	public List<Reading> getReadings() {
		for (Reading reading : readings)
			reading.setGroup(this);
		return new ArrayList<Reading>(readings);
	}

	public void setReadings(List<Reading> readings) {
		this.readings = readings;
	}

	public void addReading(Reading reading) {
		for (Reading each : readings)
			reading.assertNoConflictWith(each);
		reading.setGroup(this);
		readings.add(reading);
	}

	protected void removeReading(Reading reading) {
		readings.remove(reading);
		reading.setGroup(null);
	}

	public void save() {
		if (name.trim().isEmpty())
			throw new KalibroException("Reading group requires name.");
		dao().save(this);
	}

	public void delete() {
		if (id != null)
			dao().delete(id);
		id = null;
	}
}