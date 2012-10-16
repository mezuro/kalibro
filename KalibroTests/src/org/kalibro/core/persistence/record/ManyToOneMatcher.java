package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

class ManyToOneMatcher {

	private ManyToOne manyToOne;
	private JoinColumn joinColumn;

	ManyToOneMatcher(ManyToOne manyToOne, JoinColumn joinColumn) {
		this.manyToOne = manyToOne;
		this.joinColumn = joinColumn;
	}

	ManyToOneMatcher doesNotCascade() {
		assertArrayEquals(message(manyToOne, "should NOT cascade"), new CascadeType[]{}, manyToOne.cascade());
		return this;
	}

	ManyToOneMatcher isLazy() {
		assertEquals(message(manyToOne, "has wrong fetch type"), FetchType.LAZY, manyToOne.fetch());
		return this;
	}

	ManyToOneMatcher isOptional() {
		assertTrue(message(manyToOne, "should be optional"), manyToOne.optional());
		assertTrue(message(joinColumn, "should be nullable"), joinColumn.nullable());
		return this;
	}

	ManyToOneMatcher isRequired() {
		assertFalse(message(manyToOne, "should NOT be optional"), manyToOne.optional());
		assertFalse(message(joinColumn, "should NOT be nullable"), joinColumn.nullable());
		return this;
	}

	private String message(Object annotation, String message) {
		return "@" + annotation.getClass().getSimpleName() + " " + joinColumn.name() + " " + message + ".";
	}
}