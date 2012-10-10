package org.kalibro.service.xml;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.kalibro.ProcessState;
import org.kalibro.core.reflection.FieldReflector;
import org.kalibro.tests.UnitTest;

public class StateTimeXmlTest extends UnitTest {

	@Test
	public void shouldHavePublicDefaultConstructor() throws Exception {
		Constructor<StateTimeXml> constructor = StateTimeXml.class.getConstructor();
		assertTrue(Modifier.isPublic(constructor.getModifiers()));
	}

	@Test
	public void shouldConvertProperly() {
		ProcessState[] states = ProcessState.values();
		ProcessState state = states[new Random().nextInt(states.length)];
		Long time = mock(Long.class);
		StateTimeXml xml = new StateTimeXml(state, time);
		assertSame(state, xml.state());
		assertSame(time, xml.time());
	}

	@Test
	public void shouldHaveClassAnnotations() {
		Class<StateTimeXml> xmlClass = StateTimeXml.class;
		assertEquals("stateTime", xmlClass.getAnnotation(XmlRootElement.class).name());
		assertEquals(XmlAccessType.FIELD, xmlClass.getAnnotation(XmlAccessorType.class).value());
	}

	@Test
	public void shouldHaveFieldAnnotations() {
		FieldReflector reflector = new FieldReflector(new StateTimeXml());
		assertNotNull(reflector.getFieldAnnotation("state", XmlElement.class));
		assertNotNull(reflector.getFieldAnnotation("time", XmlElement.class));
	}
}