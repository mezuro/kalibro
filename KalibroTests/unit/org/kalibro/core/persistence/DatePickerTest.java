package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.persistence.DatePicker.*;

import org.junit.Test;
import org.kalibro.tests.EnumerationTest;

public class DatePickerTest extends EnumerationTest<DatePicker> {

	private static final String BASIC_CLAUSE = "WHERE processing.repository.id = :repositoryId";

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
		assertEquals(BASIC_CLAUSE, ANY.existenceClause());
		assertEquals(BASIC_CLAUSE + " AND processing.date > :date", AFTER.existenceClause());
		assertEquals(BASIC_CLAUSE + " AND processing.date < :date", BEFORE.existenceClause());
	}

	@Test
	public void checkProcessingClause() {
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT min(p.date) FROM Processing p " +
			"WHERE p.repository.id = :repositoryId)", FIRST.processingClause());
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT max(p.date) FROM Processing p " +
			"WHERE p.repository.id = :repositoryId)", LAST.processingClause());
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT min(p.date) FROM Processing p " +
			"WHERE p.repository.id = :repositoryId AND p.date > :date)", FIRST_AFTER.processingClause());
		assertEquals(BASIC_CLAUSE + " AND processing.date = (SELECT max(p.date) FROM Processing p " +
			"WHERE p.repository.id = :repositoryId AND p.date < :date)", LAST_BEFORE.processingClause());
	}
}