package org.kalibro;

import java.util.Collection;

import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.util.DataTransferObject;

public abstract class DtoTestCase<ENTITY, RECORD extends DataTransferObject<ENTITY>> extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultConstructorShouldDoNothing() {
		checkException(new Task() {

			@Override
			public void perform() {
				newDtoUsingDefaultConstructor().convert();
			}
		}, NullPointerException.class);
	}

	protected abstract RECORD newDtoUsingDefaultConstructor();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkCorrectConversion() {
		for (ENTITY entity : entitiesForTestingConversion())
			assertCorrectConversion(entity, createDto(entity).convert());
	}

	protected abstract Collection<ENTITY> entitiesForTestingConversion();

	protected abstract RECORD createDto(ENTITY entity);

	protected void assertCorrectConversion(ENTITY original, ENTITY converted) {
		assertDeepEquals((AbstractEntity<?>) original, (AbstractEntity<?>) converted);
	}
}