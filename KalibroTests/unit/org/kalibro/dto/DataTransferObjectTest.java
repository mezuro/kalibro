package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class DataTransferObjectTest extends UnitTest {

	@Test
	public void shouldConvertListOfDtos() {
		Collection<DataTransferObject<Integer>> dtos = asList(dto("6"), dto("28"), dto("42"));
		assertEquals(asList(6, 28, 42), DataTransferObject.convert(dtos));
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