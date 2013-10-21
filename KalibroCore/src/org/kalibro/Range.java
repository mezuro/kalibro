package org.kalibro;

import org.kalibro.core.abstractentity.*;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RangeDao;

/**
 * Evaluation range to be associated with a metric result. Contains {@link Reading} and comments.
 * 
 * @author Carlos Morais
 */
@SortingFields("beginning")
public class Range extends AbstractEntity<Range> {

	@Print(skip = true)
	private Long id;

	@IdentityField
	@Print(order = 1)
	private Double beginning;

	@Print(order = 2)
	private Double end;

	@Print(order = 3)
	private Reading reading;

	@Print(order = 4)
	private String comments;

	@Ignore
	private MetricConfiguration configuration;

	public Range() {
		this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	public Range(Double beginning, Double end) {
		validate(beginning, end);
		this.beginning = beginning;
		this.end = end;
		setReading(null);
		setComments("");
	}

	public Long getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
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

	public void set(Double newBeginning, Double newEnd) {
		validate(newBeginning, newEnd);
		beginning = newBeginning;
		end = newEnd;
	}

	void assertNoIntersectionWith(Range other) {
		throwExceptionIf(this.contains(other.beginning) || other.contains(this.beginning),
			"Range " + other + " would conflict with " + this);
	}

	private void validate(Double theBeginning, Double theEnd) {
		throwExceptionIf(! (theBeginning < theEnd), "[" + theBeginning + ", " + theEnd + "[ is not a valid range");
		if (configuration != null) {
			Range range = new Range(theBeginning, theEnd);
			for (Range other : configuration.getRanges())
				if (other != this)
					other.assertNoIntersectionWith(range);
		}
	}

	public boolean isFinite() {
		return ! (beginning.isInfinite() || end.isInfinite());
	}

	public boolean contains(Double value) {
		return beginning <= value && value < end;
	}

	public boolean hasReading() {
		return reading != null;
	}

	public Reading getReading() {
		return reading;
	}

	public void setReading(Reading reading) {
		this.reading = reading;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	void setConfiguration(MetricConfiguration configuration) {
		this.configuration = configuration;
	}

	public void save() {
		throwExceptionIf(configuration == null, "Range is not in any configuration.");
		configuration.assertSaved();
		if (hasReading())
			reading.assertSaved();
		id = dao().save(this, configuration.getId());
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		if (configuration != null)
			configuration.removeRange(this);
		deleted();
	}

	void deleted() {
		id = null;
	}

	private RangeDao dao() {
		return DaoFactory.getRangeDao();
	}

	@Override
	public String toString() {
		return "[" + beginning + ", " + end + "[";
	}
}