package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Random;

import org.junit.Test;
import org.kalibro.ProcessState;
import org.kalibro.tests.UnitTest;

public class ProcessingTimeRecordTest extends UnitTest {

	@Test
	public void shouldHavePublicDefaultConstructor() throws Exception {
		Constructor<ProcessingTimeRecord> constructor = ProcessingTimeRecord.class.getConstructor();
		assertTrue(Modifier.isPublic(constructor.getModifiers()));
		constructor.newInstance();
	}

	@Test
	public void shouldConvertProperly() {
		ProcessState state = randomElementFrom(ProcessState.values());
		Long time = new Random().nextLong();
		ProcessingTimeRecord record = new ProcessingTimeRecord(state, time, null);

		assertEquals(state, record.state());
		assertEquals(time, record.time());
	}
}