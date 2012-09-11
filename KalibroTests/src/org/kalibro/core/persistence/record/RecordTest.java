package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.persistence.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.dto.ConcreteDtoTest;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.util.Identifier;
import org.kalibro.util.reflection.FieldReflector;

public abstract class RecordTest<ENTITY, RECORD extends DataTransferObject<ENTITY>> extends
	ConcreteDtoTest<ENTITY, RECORD> {

	private FieldReflector reflector;

	@Before
	public void createReflector() {
		reflector = new FieldReflector(dto);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkClassAnnotations() {
		String entityName = entity.getClass().getSimpleName();

		Entity entityAnnotation = dto.getClass().getAnnotation(Entity.class);
		assertNotNull(entityAnnotation);
		assertEquals(entityName, entityAnnotation.name());

		Table table = dto.getClass().getAnnotation(Table.class);
		assertNotNull(table);
		assertEquals(tableName(entityName), table.name());
	}

	private String tableName(String entityName) {
		return "\"" + Identifier.fromVariable(entityName).asConstant() + "\"";
	}

	protected void assertId() {
		assertNotNull(reflector.getFieldAnnotation("id", Id.class));
		assertNotNull(reflector.getFieldAnnotation("id", GeneratedValue.class));
		assertColumn("id", Long.class, false, false);
	}

	protected void assertColumn(String field, Class<?> type, boolean nullable, boolean unique) {
		assertEquals(type, reflector.getFieldType(field));
		Column column = reflector.getFieldAnnotation(field, Column.class);
		assertNotNull(column);
		assertEquals(columnName(field), column.name());
		assertEquals(nullable, column.nullable());
		assertEquals(unique, column.unique());
	}

	protected void assertOneToMany(String field, String mappedBy) {
		assertTrue(Collection.class.isAssignableFrom(reflector.getFieldType(field)));

		OneToMany oneToMany = reflector.getFieldAnnotation(field, OneToMany.class);
		assertNotNull(oneToMany);
		assertArrayEquals(new CascadeType[]{CascadeType.ALL}, oneToMany.cascade());
		assertEquals(FetchType.LAZY, oneToMany.fetch());
		assertEquals(mappedBy, oneToMany.mappedBy());
		assertTrue(oneToMany.orphanRemoval());
	}

	protected void assertManyToOne(String field, Class<?> type, boolean optional) {
		assertEquals(type, reflector.getFieldType(field));

		ManyToOne manyToOne = reflector.getFieldAnnotation(field, ManyToOne.class);
		assertNotNull(manyToOne);
		assertArrayEquals(new CascadeType[]{CascadeType.ALL}, manyToOne.cascade());
		assertEquals(FetchType.LAZY, manyToOne.fetch());
		assertEquals(optional, manyToOne.optional());

		JoinColumn column = reflector.getFieldAnnotation(field, JoinColumn.class);
		assertNotNull(column);
		assertEquals(columnName(field), column.name());
		assertEquals(optional, column.nullable());
		assertEquals("\"id\"", column.referencedColumnName());
	}

	private String columnName(String field) {
		return "\"" + Identifier.fromVariable(field).asConstant().toLowerCase() + "\"";
	}
}