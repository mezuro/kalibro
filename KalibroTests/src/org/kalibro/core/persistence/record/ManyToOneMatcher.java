package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

class ManyToOneMatcher {

	private ManyToOne manyToOne;
	private JoinColumn joinColumn;

	ManyToOneMatcher(ManyToOne manyToOne, JoinColumn joinColumn) {
		this.manyToOne = manyToOne;
		this.joinColumn = joinColumn;
	}

	ManyToOneMatcher isOptional() {
		assertTrue("ManyToOne " + joinColumn.name() + " should be optional.", manyToOne.optional());
		assertTrue("JoinColumn " + joinColumn.name() + " should be nullable.", joinColumn.nullable());
		return this;
	}

	ManyToOneMatcher isRequired() {
		assertFalse("ManyToOne " + joinColumn.name() + " should NOT be optional.", manyToOne.optional());
		assertFalse("JoinColumn " + joinColumn.name() + " should NOT be nullable.", joinColumn.nullable());
		return this;
	}
}