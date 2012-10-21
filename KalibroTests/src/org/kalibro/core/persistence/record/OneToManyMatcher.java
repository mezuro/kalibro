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

	OneToManyMatcher cascades(boolean shouldCascade) {
		if (shouldCascade) {
			assertArrayEquals("@OneToMany should cascade.", new CascadeType[]{CascadeType.ALL}, oneToMany.cascade());
			assertTrue("@OneToMany should remove orphans.", oneToMany.orphanRemoval());
		} else {
			assertArrayEquals("@OneToMany should NOT cascade", new CascadeType[]{}, oneToMany.cascade());
			assertFalse("@OneToMany should NOT remove orphans.", oneToMany.orphanRemoval());
		}
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