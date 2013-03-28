package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

class OneToOneMatcher {

	private OneToOne oneToOne;
	private JoinColumn joinColumn;

	OneToOneMatcher(OneToOne oneToMany, JoinColumn joinColumn) {
		this.oneToOne = oneToMany;
		this.joinColumn = joinColumn;
	}

	OneToOneMatcher cascades() {
		assertArrayEquals(message("should cascade"), new CascadeType[]{CascadeType.ALL}, oneToOne.cascade());
		return this;
	}

	OneToOneMatcher isEager() {
		assertEquals(message("has wrong fetch type"), FetchType.EAGER, oneToOne.fetch());
		return this;
	}

	OneToOneMatcher isLazy() {
		assertEquals(message("has wrong fetch type"), FetchType.LAZY, oneToOne.fetch());
		return this;
	}

	OneToOneMatcher isOptional() {
		assertTrue(message("should be optional"), oneToOne.optional());
		assertTrue(message("JoinColumn", "should be nullable"), joinColumn.nullable());
		return this;
	}

	OneToOneMatcher isRequired() {
		assertFalse(message("should NOT be optional"), oneToOne.optional());
		assertFalse(message("JoinColumn", "should NOT be nullable"), joinColumn.nullable());
		return this;
	}

	private String message(String message) {
		return message("OneToOne", message);
	}

	private String message(String annotation, String message) {
		return "@" + annotation + " " + joinColumn.name() + " " + message + ".";
	}
}