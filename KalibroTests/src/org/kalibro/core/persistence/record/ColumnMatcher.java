package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import javax.persistence.Column;

class ColumnMatcher {

	private Column column;

	ColumnMatcher(Column column) {
		this.column = column;
	}

	ColumnMatcher isUnique() {
		assertTrue("Column " + column.name() + " should be unique.", column.unique());
		return this;
	}

	ColumnMatcher isNotUnique() {
		assertFalse("Column " + column.name() + " should NOT be unique.", column.unique());
		return this;
	}

	ColumnMatcher isRequired() {
		assertFalse("Column " + column.name() + " should NOT be nullable.", column.nullable());
		return this;
	}
}