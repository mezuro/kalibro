package org.kalibro;

import java.awt.Color;

import org.kalibro.core.abstractentity.*;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;

/**
 * Interpretation of a metric result. Examples: "Good", "Bad", "Complex", "Beautiful". Besides label, readings also have
 * color and grade for quick and comparable evaluation.
 * 
 * @author Carlos Morais
 */
@SortingFields("grade")
public class Reading extends AbstractEntity<Reading> {

	@Print(skip = true)
	private Long id;

	@IdentityField
	@Print(order = 1)
	private String label;

	@Print(order = 2)
	private Double grade;

	@Print(order = 3)
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

	public Long getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		if (group != null)
			for (Reading other : group.getReadings())
				if (other != this)
					assertNoLabelConflict(other, label);
		this.label = label;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		if (group != null)
			for (Reading other : group.getReadings())
				if (other != this)
					assertNoGradeConflict(other, grade);
		this.grade = grade;
	}

	void assertNoConflictWith(Reading other) {
		assertNoLabelConflict(other, label);
		assertNoGradeConflict(other, grade);
	}

	private void assertNoLabelConflict(Reading other, String theLabel) {
		throwExceptionIf(other.label.equals(theLabel),
			"Reading with label \"" + theLabel + "\" already exists in the group.");
	}

	private void assertNoGradeConflict(Reading other, Double theGrade) {
		throwExceptionIf(other.grade.equals(theGrade),
			"Reading with grade " + theGrade + " already exists in the group.");
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	void setGroup(ReadingGroup group) {
		this.group = group;
	}

	void assertSaved() {
		if (!hasId())
			save();
	}

	public void save() {
		throwExceptionIf(group == null, "Reading is not in any group.");
		group.assertSaved();
		id = dao().save(this, group.getId());
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		if (group != null)
			group.removeReading(this);
		deleted();
	}

	void deleted() {
		id = null;
	}

	private ReadingDao dao() {
		return DaoFactory.getReadingDao();
	}

	@Override
	public String toString() {
		return grade + " - " + label;
	}
}