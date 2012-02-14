package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.persistence.database.entities.ErrorRecord;

public class ErrorRecordTest extends DtoTestCase<Exception, ErrorRecord> {

	@Override
	protected ErrorRecord newDtoUsingDefaultConstructor() {
		return new ErrorRecord();
	}

	@Override
	protected Collection<Exception> entitiesForTestingConversion() {
		return Arrays.asList(new Exception("My error message"));
	}

	@Override
	protected ErrorRecord createDto(Exception error) {
		return new ErrorRecord(error, null);
	}

	@Override
	protected void assertCorrectConversion(Exception original, Exception converted) {
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