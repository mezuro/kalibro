package org.kalibro;

import java.awt.Color;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

@SortingFields("beginning")
public class Range extends AbstractEntity<Range> {

	@IdentityField
	private Double beginning = Double.NEGATIVE_INFINITY;

	private Double end = Double.POSITIVE_INFINITY;

	private Reading reading;

	private String comments;

	public Range() {
		this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	public Range(Double beginning, Double end) {
		this(beginning, end, "", 0.0, Color.WHITE);
	}

	public Range(Double beginning, Double end, String label, Double grade, Color color) {
		validate(beginning, end);
		setBeginning(beginning);
		reading = new Reading(label, grade, color);
		setGrade(grade);
		setComments("");
	}

	@Override
	public String toString() {
		return "[" + beginning + ", " + end + "[";
	}

	public boolean isFinite() {
		return beginning != Double.NEGATIVE_INFINITY && end != Double.POSITIVE_INFINITY;
	}

	public boolean contains(Double value) {
		return beginning <= value && value < end;
	}

	public boolean intersectsWith(Range other) {
		return this.contains(other.beginning) || other.contains(this.beginning);
	}

	public Double getBeginning() {
		return beginning;
	}

	public void setBeginning(Double beginning) {
		validate(beginning, end);
		this.beginning = beginning;
	}

	public Double getEnd() {
		return end;
	}

	public void setEnd(Double end) {
		validate(beginning, end);
		this.end = end;
	}

	private void validate(Double leftPoint, Double rightPoint) {
		if (!(leftPoint < rightPoint))
			throw new KalibroException("[" + leftPoint + ", " + rightPoint + "[ is not a valid range");
	}

	public String getLabel() {
		return reading.getLabel();
	}

	public void setLabel(String label) {
		reading.setLabel(label);
	}

	public Double getGrade() {
		return reading.getGrade();
	}

	public void setGrade(Double grade) {
		reading.setGrade(grade);
	}

	public Color getColor() {
		return reading.getColor();
	}

	public void setColor(Color color) {
		reading.setColor(color);
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setConfiguration(MetricConfiguration metricConfiguration) {
		// TODO Auto-generated method stub

	}

	void deleted() {
		// TODO Auto-generated method stub

	}
}