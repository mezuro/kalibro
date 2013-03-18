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
		assertArrayEquals("@ManyToOne should NOT cascade", new CascadeType[]{}, manyToOne.cascade());
		return this;
	}

	ManyToOneMatcher isEager() {
		assertEquals(message("has wrong fetch type"), FetchType.EAGER, manyToOne.fetch());
		return this;
	}

	ManyToOneMatcher isLazy() {
		assertEquals(message("has wrong fetch type"), FetchType.LAZY, manyToOne.fetch());
		return this;
	}

	ManyToOneMatcher isRequired() {
		assertFalse(message("should NOT be optional"), manyToOne.optional());
		assertFalse(message("JoinColumn", "should NOT be nullable"), joinColumn.nullable());
		return this;
	}

	private String message(String message) {
		return message("ManyToOne", message);
	}

	private String message(String annotation, String message) {
		return "@" + annotation + " " + joinColumn.name() + " " + message + ".";
	}
}