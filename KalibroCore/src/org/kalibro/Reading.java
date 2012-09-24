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
		setId(null);
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

	public void setId(Long id) {
		this.id = id;
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

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	protected void assertNoConflictWith(Reading other) {
		if (getLabel().equals(other.getLabel()))
			throw new KalibroException("Reading with label \"" + getLabel() + "\" already exists in the group.");
		if (getGrade().equals(other.getGrade()))
			throw new KalibroException("Reading with grade " + getGrade() + " already exists in the group.");
	}

	public Long getGroupId() {
		return group.getId();
	}

	protected void setGroup(ReadingGroup group) {
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