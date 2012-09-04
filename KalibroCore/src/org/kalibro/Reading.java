package org.kalibro;

import java.awt.Color;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.Ignore;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.core.persistence.dao.DaoFactory;

@SortingFields("grade")
public class Reading extends AbstractEntity<Reading> {

	private String label;
	private Double grade;
	private Color color;

	@Ignore
	private ReadingGroup group;

	public Reading() {
		this("", 0.0, Color.WHITE);
	}

	public Reading(String label, Double grade, Color color) {
		setLabel(label);
		setGrade(grade);
		setColor(color);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getGrade() {
		return grade;
	}

	private void setGrade(Double grade) {
		this.grade = grade;
	}

	public Color getColor() {
		return color;
	}

	private void setColor(Color color) {
		this.color = color;
	}

	protected void setGroup(ReadingGroup group) {
		this.group = group;
	}

	protected void assertNoConflictWith(Reading other) {
		if (getLabel().equals(other.getLabel()))
			throw new KalibroException("Reading with label '" + getLabel() + "' already exists in the group.");
		if (getGrade().equals(other.getGrade()))
			throw new KalibroException("Reading with grade " + getGrade() + " already exists in the group.");
	}

	public void save() {
		if (group == null)
			throw new KalibroException("Reading is not in any group.");
		DaoFactory.getReadingDao().save(this);
	}

	public void delete() {
		DaoFactory.getReadingDao().delete(this);
		group.removeReading(this);
	}
}