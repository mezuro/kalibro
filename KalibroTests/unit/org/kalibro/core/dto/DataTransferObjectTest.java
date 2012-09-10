package org.kalibro.core.dto;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.TestCase;

public class DataTransferObjectTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertListOfDtos() {
		Collection<DataTransferObject<Integer>> dtos = Arrays.asList(dto("6"), dto("28"), dto("42"));
		assertEquals(Arrays.asList(6, 28, 42), DataTransferObject.convert(dtos));
	}

	private DataTransferObject<Integer> dto(final String value) {
		return new DataTransferObject<Integer>() {

			@Override
			public Integer convert() {
				return Integer.parseInt(value);
			}
		};
	}
}