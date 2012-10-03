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
	private Double beginning;

	private Double end;
	private Reading reading;
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

	void assertNoIntersectionWith(Range other) {
		assertNoIntersection(this, other);
	}

	private void validate(Double theBeginning, Double theEnd) {
		if (!(theBeginning < theEnd))
			throw new KalibroException("[" + theBeginning + ", " + theEnd + "[ is not a valid range");
		if (configuration != null)
			for (Range other : configuration.getRanges())
				if (other != this)
					assertNoIntersection(other, new Range(theBeginning, theEnd));
	}

	private void assertNoIntersection(Range range, Range other) {
		if (range.contains(other.beginning) || other.contains(range.beginning))
			throw new KalibroException("Range " + other + " would conflict with " + range);
	}

	public boolean isFinite() {
		return beginning != Double.NEGATIVE_INFINITY && end != Double.POSITIVE_INFINITY;
	}

	public boolean contains(Double value) {
		return beginning <= value && value < end;
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
		if (configuration == null)
			throw new KalibroException("Range is not in any configuration.");
		if (!configuration.hasId())
			throw new KalibroException("Configuration is not saved. Save configuration instead");
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