package org.kalibro.service.xml;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.kalibro.core.Identifier;
import org.kalibro.dto.ConcreteDtoTest;

public abstract class XmlTest extends ConcreteDtoTest {

	@Test
	public void elementNameShouldBeEntityNameAsVariable() throws ClassNotFoundException {
		XmlRootElement element = dtoClass().getAnnotation(XmlRootElement.class);
		assertNotNull(element);
		assertEquals(expectedElementName(), element.name());
	}

	private String expectedElementName() {
		return Identifier.fromVariable(entityName()).asVariable();
	}

	@Test
	public void shouldHaveFieldXmlAccess() throws ClassNotFoundException {
		XmlAccessorType accessorType = dtoClass().getAnnotation(XmlAccessorType.class);
		assertNotNull(accessorType);
		assertEquals(XmlAccessType.FIELD, accessorType.value());
	}

	protected void assertElement(String field, Class<?> type, boolean required) {
		assertEquals(type, dtoReflector.getFieldType(field));
		XmlElement element = dtoReflector.getFieldAnnotation(field, XmlElement.class);
		assertNotNull(element);
		assertEquals(required, element.required());
	}

	protected void assertCollection(String field, boolean required, String elementName) {
		assertEquals(Collection.class, dtoReflector.getFieldType(field));
		XmlElement element = dtoReflector.getFieldAnnotation(field, XmlElement.class);
		assertNotNull(element);
		assertEquals(required, element.required());
		assertEquals(elementName, element.name());
	}

	@Test
	public abstract void verifyElements();
}