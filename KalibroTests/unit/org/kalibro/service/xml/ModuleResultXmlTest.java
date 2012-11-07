package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;

public class ModuleResultXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class, true);
		assertElement("module", ModuleXml.class, true);
		assertElement("grade", Double.class);
		assertElement("parentId", Long.class);
	}

	@Test
	public void shouldRetrieveParentId() {
		ModuleResult moduleResult = (ModuleResult) entity;
		ModuleResultXml xml = (ModuleResultXml) dto;
		assertEquals(moduleResult.getParent().getId(), xml.parentId());
	}

	@Test
	public void checkNullParent() {
		assertNull(new ModuleResultXml(new ModuleResult(null, new Module(Granularity.CLASS))).parentId());
	}
}