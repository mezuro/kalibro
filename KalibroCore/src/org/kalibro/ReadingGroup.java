package org.kalibro;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.core.abstractentity.AbstractEntity;

public class ReadingGroup extends AbstractEntity<ReadingGroup> {

	public static List<ReadingGroup> all() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ReadingGroup importFrom(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	private String name;
	private String description;
	private Collection<Reading> readings;

	public void setName(String name) {
		// TODO Auto-generated method stub
	}

	public void setDescription(String description) {
		// TODO Auto-generated method stub
	}

	public List<Reading> getReadings() {
		// TODO Auto-generated method stub
		return new ArrayList<Reading>(readings);
	}

	public void add(Reading reading) {
		// TODO Auto-generated method stub
	}

	public void save() {
		// TODO Auto-generated method stub
	}

	public boolean isSaved() {
		// TODO Auto-generated method stub
		return false;
	}

	public void delete() {
		// TODO Auto-generated method stub
	}

	public void exportTo(File file) {
		// TODO Auto-generated method stub
	}
}