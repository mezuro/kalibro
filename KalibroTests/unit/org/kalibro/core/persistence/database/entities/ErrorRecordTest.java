package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;

public class ErrorRecordTest extends DtoTestCase<Throwable, ErrorRecord> {

	@Override
	protected ErrorRecord newDtoUsingDefaultConstructor() {
		return new ErrorRecord();
	}

	@Override
	protected Collection<Throwable> entitiesForTestingConversion() {
		return Arrays.asList(new Throwable("My error message"));
	}

	@Override
	protected ErrorRecord createDto(Throwable error) {
		return new ErrorRecord(error, null);
	}

	@Override
	protected void assertCorrectConversion(Throwable original, Throwable converted) {
		assertEquals(original.getMessage(), converted.getMessage());
		assertCorrectConversion(original.getStackTrace(), converted.getStackTrace());
	}

	private void assertCorrectConversion(StackTraceElement[] original, StackTraceElement[] converted) {
		assertEquals(original.length, converted.length);
		StackTraceElementRecordTest test = new StackTraceElementRecordTest();
		for (int i = 0; i < original.length; i++)
			test.assertCorrectConversion(original[i], converted[i]);
	}
}