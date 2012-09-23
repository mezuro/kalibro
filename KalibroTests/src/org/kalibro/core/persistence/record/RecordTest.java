package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.persistence.*;

import org.junit.Test;
import org.kalibro.core.Identifier;
import org.kalibro.dto.ConcreteDtoTest;

public abstract class RecordTest<ENTITY> extends ConcreteDtoTest<ENTITY> {

	@Test
	public void checkClassAnnotations() throws ClassNotFoundException {
		Entity entityAnnotation = dtoClass().getAnnotation(Entity.class);
		assertNotNull(entityAnnotation);
		assertEquals(entityName(), entityAnnotation.name());

		Table table = dtoClass().getAnnotation(Table.class);
		assertNotNull(table);
		assertEquals(tableName(), table.name());
	}

	private String tableName() {
		return "\"" + Identifier.fromVariable(entityName()).asConstant() + "\"";
	}

	protected void assertId() {
		assertNotNull(dtoReflector.getFieldAnnotation("id", Id.class));
		assertNotNull(dtoReflector.getFieldAnnotation("id", GeneratedValue.class));
		assertColumn("id", Long.class, false, false);
	}

	protected void assertColumn(String field, Class<?> type, boolean nullable, boolean unique) {
		assertEquals(type, dtoReflector.getFieldType(field));
		Column column = dtoReflector.getFieldAnnotation(field, Column.class);
		assertNotNull(column);
		assertEquals(columnName(field), column.name());
		assertEquals(nullable, column.nullable());
		assertEquals(unique, column.unique());
	}

	protected void assertOneToMany(String field, String mappedBy) {
		assertTrue(Collection.class.isAssignableFrom(dtoReflector.getFieldType(field)));

		OneToMany oneToMany = dtoReflector.getFieldAnnotation(field, OneToMany.class);
		assertNotNull(oneToMany);
		assertArrayEquals(new CascadeType[]{CascadeType.ALL}, oneToMany.cascade());
		assertEquals(FetchType.LAZY, oneToMany.fetch());
		assertEquals(mappedBy, oneToMany.mappedBy());
		assertTrue(oneToMany.orphanRemoval());
	}

	protected void assertManyToOne(String field, Class<?> type, boolean optional) {
		assertEquals(type, dtoReflector.getFieldType(field));

		ManyToOne manyToOne = dtoReflector.getFieldAnnotation(field, ManyToOne.class);
		assertNotNull(manyToOne);
		assertEquals(0, manyToOne.cascade().length);
		assertEquals(FetchType.LAZY, manyToOne.fetch());
		assertEquals(optional, manyToOne.optional());

		JoinColumn column = dtoReflector.getFieldAnnotation(field, JoinColumn.class);
		assertNotNull(column);
		assertEquals(columnName(field), column.name());
		assertEquals(optional, column.nullable());
		assertEquals("\"id\"", column.referencedColumnName());
	}

	private String columnName(String field) {
		return "\"" + Identifier.fromVariable(field).asConstant().toLowerCase() + "\"";
	}
}