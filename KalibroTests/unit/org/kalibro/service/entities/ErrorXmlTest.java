package org.kalibro.service.entities;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.KalibroException;
import org.kalibro.core.concurrent.Task;

public class ErrorXmlTest extends DtoTestCase<Throwable, ErrorXml> {

	@Override
	protected ErrorXml newDtoUsingDefaultConstructor() {
		return new ErrorXml();
	}

	@Override
	protected Collection<Throwable> entitiesForTestingConversion() {
		Throwable error = new Throwable("ErrorXmlTest");
		KalibroException exception = new KalibroException("ErrorXmlTest", error);
		return Arrays.asList(error, exception);
	}

	@Override
	protected ErrorXml createDto(Throwable error) {
		return new ErrorXml(error);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionForNotConvertibleError() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new ErrorXml(new Throwable() {
					// Anonymous class
				}).convert();
			}
		}, "Could not convert Error XML to Throwable", NullPointerException.class);
	}
}