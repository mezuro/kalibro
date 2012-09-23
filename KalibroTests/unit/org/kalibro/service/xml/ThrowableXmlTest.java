package org.kalibro.service.xml;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.KalibroException;
import org.kalibro.core.concurrent.VoidTask;

public class ThrowableXmlTest extends DtoTestCase<Throwable, ThrowableXml> {

	@Override
	protected ThrowableXml newDtoUsingDefaultConstructor() {
		return new ThrowableXml();
	}

	@Override
	protected Collection<Throwable> entitiesForTestingConversion() {
		Throwable error = new Throwable("ThrowableXmlTest");
		KalibroException exception = new KalibroException("ThrowableXmlTest", error);
		return Arrays.asList(error, exception);
	}

	@Override
	protected ThrowableXml createDto(Throwable error) {
		return new ThrowableXml(error);
	}

	@Test
	public void shouldThrowErrorForNotConvertibleError() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				new ThrowableXml(new Throwable() {
					// Anonymous class
				}).convert();
			}
		}).throwsError().withMessage("Could not convert Error XML to Throwable").withCause(NullPointerException.class);
	}
}