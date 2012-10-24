package org.kalibro.dto;

import static java.awt.Color.*;
import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class DataTransferObjectTest extends UnitTest {

	private static final Collection<Rgb> DTOS = list(new Rgb(RED), new Rgb(GREEN), new Rgb(BLUE));

	@Test
	public void shouldCreateDtos() {
		List<Rgb> dtos = DataTransferObject.createDtos(list(RED, GREEN, BLUE), Rgb.class);
		assertEquals(RED.getRGB(), dtos.get(0).rgb);
		assertEquals(GREEN.getRGB(), dtos.get(1).rgb);
		assertEquals(BLUE.getRGB(), dtos.get(2).rgb);
	}

	@Test
	public void shouldThrowErrorIfDtoCreationFails() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				DataTransferObject.createDtos(list(MAGENTA), ErrorDto.class);
			}
		}).throwsError().withMessage("Could not create DTO.").withCause(NoSuchMethodException.class);
	}

	@Test
	public void shouldConvertDtosToList() {
		assertEquals(list(RED, GREEN, BLUE), DataTransferObject.toList(DTOS));
	}

	@Test
	public void shouldConvertDtosToSet() {
		assertEquals(set(RED, GREEN, BLUE), DataTransferObject.toSet(DTOS));
	}

	@Test
	public void shouldConvertDtosToSortedSet() {
		assertTrue(DataTransferObject.toSortedSet(new ArrayList<DataTransferObject<String>>()).isEmpty());
	}

	@Test
	public void shouldSetEntityId() {
		final Long id = mock(Long.class);
		assertSame(id, new DataTransferObject<Reading>() {

			@Override
			public Reading convert() {
				Reading reading = new Reading();
				setId(reading, id);
				return reading;
			}
		}.convert().getId());
	}

	private class ErrorDto extends DataTransferObject<Color> {

		@Override
		public Color convert() {
			return null;
		}
	}
}