package org.kalibro;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.core.abstractentity.AbstractEntity;

public class ReadingGroup extends AbstractEntity<ReadingGroup> {

	public static List<ReadingGroup> all() {
		return new ArrayList<ReadingGroup>();
	}

	public static ReadingGroup importFrom(File file) {
		return importFrom(file, ReadingGroup.class);
	}

	private String name;
	private String description;
	private Collection<Reading> readings;

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
		readings.add(reading);
	}

	public void save() {
		return;
	}

	public boolean isSaved() {
		return false;
	}

	public void delete() {
		return;
	}
}