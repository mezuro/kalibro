package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.persistence.DatePicker.*;

import org.junit.Test;
import org.kalibro.tests.EnumerationTest;

public class DatePickerTest extends EnumerationTest<DatePicker> {

	private static final String BASIC_CLAUSE = "processing.repository = :repositoryId";

	@Override
	protected Class<DatePicker> enumerationClass() {
		return DatePicker.class;
	}

	@Override
	protected String expectedText(DatePicker value) {
		return value.name();
	}

	@Test
	public void checkAliases() {
		assertSame(FIRST_AFTER, AFTER);
		assertSame(LAST_BEFORE, BEFORE);
	}

	@Test
	public void checkExistenteClause() {
		assertEquals("WHERE " + BASIC_CLAUSE, ANY.existenceClause());
		assertEquals("WHERE " + BASIC_CLAUSE + " AND processing.date > :date", AFTER.existenceClause());
		assertEquals("WHERE " + BASIC_CLAUSE + " AND processing.date < :date", BEFORE.existenceClause());
	}

	@Test
	public void checkProcessingClause() {
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT min(p.date) FROM Processing p " +
			"WHERE p.repository = :repositoryId)", FIRST.processingClause());
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT max(p.date) FROM Processing p " +
			"WHERE p.repository = :repositoryId)", LAST.processingClause());
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT min(p.date) FROM Processing p " +
			"WHERE p.repository = :repositoryId AND p.date > :date)", FIRST_AFTER.processingClause());
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT max(p.date) FROM Processing p " +
			"WHERE p.repository = :repositoryId AND p.date < :date)", LAST_BEFORE.processingClause());
	}

	@Test
	public void checkExtraCondition() {
		String extraCondition = "state = 'READY'";
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT min(p.date) FROM Processing p " +
			"WHERE p.repository = :repositoryId AND p.state = 'READY')", FIRST.processingClause(extraCondition));
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT max(p.date) FROM Processing p " +
			"WHERE p.repository = :repositoryId AND p.date < :date AND p.state = 'READY')",
			LAST_BEFORE.processingClause(extraCondition));
	}
}