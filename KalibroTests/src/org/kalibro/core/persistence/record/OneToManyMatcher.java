package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertEquals;

import javax.persistence.OneToMany;

class OneToManyMatcher {

	private OneToMany oneToMany;

	OneToManyMatcher(OneToMany oneToMany) {
		this.oneToMany = oneToMany;
	}

	OneToManyMatcher mappedBy(String fieldName) {
		assertEquals(fieldName, oneToMany.mappedBy());
		return this;
	}
}