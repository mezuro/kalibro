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
				assertNoLabelConflict(other, label);
		this.label = label;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		if (group != null)
			for (Reading other : group.getReadings())
				assertNoGradeConflict(other, grade);
		this.grade = grade;
	}

	void assertNoConflictWith(Reading other) {
		assertNoLabelConflict(other, label);
		assertNoGradeConflict(other, grade);
	}

	private void assertNoLabelConflict(Reading other, String theLabel) {
		if (other.label.equals(theLabel))
			throw new KalibroException("Reading with label \"" + theLabel + "\" already exists in the group.");
	}

	private void assertNoGradeConflict(Reading other, Double theGrade) {
		if (other.grade.equals(theGrade))
			throw new KalibroException("Reading with grade " + theGrade + " already exists in the group.");
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Long getGroupId() {
		return group.getId();
	}

	void setGroup(ReadingGroup group) {
		this.group = group;
	}

	public void save() {
		if (group == null)
			throw new KalibroException("Reading is not in any group.");
		if (!group.hasId())
			throw new KalibroException("Group is not saved. Save group instead");
		id = dao().save(this);
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