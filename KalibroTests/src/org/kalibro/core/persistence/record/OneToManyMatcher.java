package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

class OneToManyMatcher {

	private OneToMany oneToMany;

	OneToManyMatcher(OneToMany oneToMany) {
		this.oneToMany = oneToMany;
	}

	OneToManyMatcher cascades() {
		assertArrayEquals("@OneToMany should cascade", new CascadeType[]{CascadeType.ALL}, oneToMany.cascade());
		return this;
	}

	OneToManyMatcher isEager() {
		assertEquals("@OneToMany has wrong fetch type.", FetchType.EAGER, oneToMany.fetch());
		return this;
	}

	OneToManyMatcher isLazy() {
		assertEquals("@OneToMany has wrong fetch type.", FetchType.LAZY, oneToMany.fetch());
		return this;
	}

	OneToManyMatcher isMappedBy(String fieldName) {
		assertEquals("@OneToMany has wrong mapping.", fieldName, oneToMany.mappedBy());
		return this;
	}
}