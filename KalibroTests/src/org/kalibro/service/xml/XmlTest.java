package org.kalibro.service.xml;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.util.Identifier;
import org.kalibro.core.util.reflection.FieldReflector;
import org.kalibro.dto.ConcreteDtoTest;
import org.kalibro.dto.DataTransferObject;

public abstract class XmlTest<ENTITY, RECORD extends DataTransferObject<ENTITY>> extends
	ConcreteDtoTest<ENTITY, RECORD> {

	private FieldReflector reflector;

	@Before
	public void createReflector() {
		reflector = new FieldReflector(dto);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkClassAnnotations() {
		XmlRootElement element = dto.getClass().getAnnotation(XmlRootElement.class);
		assertNotNull(element);
		assertEquals(rootElementName(), element.name());

		XmlAccessorType accessorType = dto.getClass().getAnnotation(XmlAccessorType.class);
		assertNotNull(accessorType);
		assertEquals(XmlAccessType.FIELD, accessorType.value());
	}

	private String rootElementName() {
		String entityName = entity.getClass().getSimpleName();
		return Identifier.fromVariable(entityName).asVariable();
	}

	protected void assertElement(String field, Class<?> type, boolean required) {
		assertEquals(type, reflector.getFieldType(field));
		XmlElement element = reflector.getFieldAnnotation(field, XmlElement.class);
		assertNotNull(element);
		assertEquals(required, element.required());
	}

	protected void assertCollection(String field, boolean required, String elementName) {
		assertEquals(Collection.class, reflector.getFieldType(field));
		XmlElement element = reflector.getFieldAnnotation(field, XmlElement.class);
		assertNotNull(element);
		assertEquals(required, element.required());
		assertEquals(elementName, element.name());
	}
}